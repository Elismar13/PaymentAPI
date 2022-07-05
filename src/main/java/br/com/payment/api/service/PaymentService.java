package br.com.payment.api.service;

import br.com.payment.api.enums.PaymentIntervalsEnum;
import br.com.payment.api.exception.payment.InsufficientPaymentLimitException;
import br.com.payment.api.exception.wallet.WalletAlreadyExistsException;
import br.com.payment.api.exception.wallet.WalletNotFountException;
import br.com.payment.api.mapper.payment.PaymentMapper;
import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.RemainingLimitResponseDTO;
import br.com.payment.api.model.entity.payment.Payment;
import br.com.payment.api.model.entity.wallet.Wallet;
import br.com.payment.api.repository.PaymentRepository;
import br.com.payment.api.repository.WalletRepository;
import br.com.payment.api.utils.PaymentUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static br.com.payment.api.utils.PaymentUtils.isMoneyOutOfLimit;

@Service
public class PaymentService {

  private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private WalletRepository walletRepository;

  public void makePayment(UUID walletId, PaymentDTO paymentDTO, LocalDateTime dateTime) throws InsufficientPaymentLimitException, WalletAlreadyExistsException {
    Optional<Wallet> wallet = walletRepository.findById(walletId);
    Payment payment = paymentMapper.toPayment(paymentDTO);

    if (wallet.isPresent()) {
      Wallet savedWallet = wallet.get();
      validatePayment(walletId, paymentDTO, dateTime);
      payment.setWallet(savedWallet);

      if (Objects.isNull(savedWallet.getPayments())) {
        savedWallet.setPayments(new ArrayList<>());
      }

      savedWallet.getPayments().add(payment);
      walletRepository.save(savedWallet);
      return;
    }

    throw new WalletNotFountException();
  }

  private Money getTotalPaid(UUID walletId, LocalDateTime dateTime, Money amount) {
    LocalDateTime firstDate = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(0, 0));
    LocalDateTime lastDate = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(23, 59, 59));
    Money zero = Money.of(0, "BRL");

    Optional<Wallet> walletSaved = walletRepository.findById(walletId);

    Optional<List<Payment>> dailyPayments = walletSaved.map(Wallet::getPayments);

    if (dailyPayments.isPresent()) {
      Optional<Money> totalSpent = dailyPayments.get().stream()
          .map(payment -> Money.of(payment.getAmount(), "BRL"))
          .reduce(Money::add);

      return totalSpent.orElse(zero).add(amount);
    } else {
      return zero;
    }
  }

  public RemainingLimitResponseDTO checkRemainingLimit(UUID wallet, LocalDateTime dateTime) {
    Money remainingLimit = getRemainingLimit(wallet, dateTime);
    return RemainingLimitResponseDTO.builder()
        .value(remainingLimit.getNumber())
        .build();
  }

  private void validatePayment(UUID walletId, PaymentDTO paymentDTO, LocalDateTime dateTime) throws InsufficientPaymentLimitException {
    paymentDTO.setDate(dateTime);
    Money amount = Money.of(paymentDTO.getAmount(), "BRL");

    isMoneyOutOfLimit(dateTime, amount);
    Money totalPaid = getTotalPaid(walletId, dateTime, amount);
    isMoneyOutOfLimit(dateTime, totalPaid);
  }

  private Money getRemainingLimit(UUID wallet, LocalDateTime dateTime) {
    Money totalPaid = getTotalPaid(wallet, dateTime, Money.of(0, "BRL"));
    PaymentIntervalsEnum currentValidLimit = PaymentUtils.checkHourLimit(dateTime);
    return currentValidLimit.getAmount().subtract(totalPaid);
  }

}
