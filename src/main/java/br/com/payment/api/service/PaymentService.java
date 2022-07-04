package br.com.payment.api.service;

import br.com.payment.api.enums.PaymentIntervalsEnum;
import br.com.payment.api.exception.payment.InsufficientPaymentLimitException;
import br.com.payment.api.exception.wallet.WalletAlreadyExistsException;
import br.com.payment.api.exception.wallet.WalletNotFountException;
import br.com.payment.api.mapper.payment.PaymentMapper;
import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.PaymentResponseDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.payment.api.utils.PaymentUtils.isMoneyOutOfLimit;

@Service
public class PaymentService {

  private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;
  @Autowired
  private PaymentRepository paymentRepository;
  @Autowired
  private WalletRepository walletRepository;

  public PaymentResponseDTO makePayment(UUID walletId, PaymentDTO paymentDTO, LocalDateTime dateTime) throws InsufficientPaymentLimitException, WalletAlreadyExistsException {
    Optional<Wallet> wallet = walletRepository.findById(walletId);

    if (wallet.isPresent()) {
      Wallet savedWallet = wallet.get();
      validatePayment(walletId, paymentDTO, dateTime);
      Payment payment = paymentMapper.toPayment(paymentDTO);
      payment.setWallet(savedWallet);
      savedWallet.getPayments().add(payment);
      walletRepository.save(savedWallet);
      return paymentMapper.toDTO(payment);
    }

    throw new WalletNotFountException();
  }

  private Money getTotalPaid(UUID walletId, LocalDateTime dateTime, Money amount) {
    LocalDateTime firstDate = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(0, 0));
    LocalDateTime lastDate = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(23, 59, 59));
    Money zero = Money.of(0, "BRL");

    Optional<List<Payment>> dailyPayments = paymentRepository.findAllByWallet_IdAndCreationDateBetween(walletId, firstDate, lastDate);

    if (dailyPayments.isPresent()) {
      Optional<Money> totalSpent = dailyPayments.get().stream()
          .map(payment -> Money.of(payment.getAmount(), "BRL"))
          .reduce(Money::add);

      return totalSpent.orElse(zero).add(amount);
    } else {
      return zero;
    }
  }

  public RemainingLimitResponseDTO checkRemainingLimit(UUID walletId, LocalDateTime dateTime) {
    Money remainingLimit = getRemainingLimit(walletId, dateTime);
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

  private Money getRemainingLimit(UUID walletId, LocalDateTime dateTime) {
    Money totalPaid = getTotalPaid(walletId, dateTime, Money.of(0, "BRL"));
    PaymentIntervalsEnum currentValidLimit = PaymentUtils.checkHourLimit(dateTime);
    return currentValidLimit.getAmount().subtract(totalPaid);
  }

}
