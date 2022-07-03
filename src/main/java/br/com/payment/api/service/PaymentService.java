package br.com.payment.api.service;

import br.com.payment.api.enums.PaymentIntervalsEnum;
import br.com.payment.api.exception.payment.InsufficientPaymentLimitException;
import br.com.payment.api.mapper.payment.PaymentMapper;
import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.PaymentResponseDTO;
import br.com.payment.api.model.dto.payment.response.RemainingLimitResponseDTO;
import br.com.payment.api.model.entity.payment.Payment;
import br.com.payment.api.repository.PaymentRepository;
import br.com.payment.api.utils.PaymentUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

  public PaymentResponseDTO makePayment(String walletId, PaymentDTO paymentDTO) throws InsufficientPaymentLimitException {
    paymentDTO.setOwnerId(walletId);
    validatePayment(walletId, paymentDTO);
    Payment payment = paymentMapper.toPayment(paymentDTO);
    Payment savedPayment = paymentRepository.save(payment);
    return paymentMapper.toDTO(savedPayment);
  }

  public RemainingLimitResponseDTO checkRemainingLimit(String walletId) {
    LocalDateTime dateTime = LocalDateTime.now();
    Money remainingLimit = getRemainingLimit(walletId);
    return RemainingLimitResponseDTO.builder()
        .value(remainingLimit.getNumber())
        .build();
  }

  private Money getTotalPaid(String walletId, LocalDateTime dateTime, Money amount) {
    LocalDateTime firstDate = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(0, 0));
    LocalDateTime lastDate = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(23, 59, 59));
    Money zero = Money.of(0, "BRL");

    Optional<List<Payment>> dailyPayments = paymentRepository.findAllByOwnerIdAndCreationDateBetween(walletId, firstDate, lastDate);

    if(dailyPayments.isPresent()) {
       Optional<Money> totalSpent = dailyPayments.get().stream()
          .map(payment -> Money.of(payment.getAmount(), "BRL"))
          .reduce(Money::add);

       return totalSpent.orElse(zero).add(amount);
    } else {
      return zero;
    }
  }

  private void isMoneyOutOfLimit(LocalDateTime dateTime, Money amount) throws InsufficientPaymentLimitException {
    PaymentIntervalsEnum currentValidLimit = PaymentUtils.checkHourLimit(dateTime);
    if(currentValidLimit.getAmount().subtract(amount).isNegative()) {
      throw new InsufficientPaymentLimitException(amount.getNumber());
    }
  }

  private void validatePayment(String walletId, PaymentDTO paymentDTO) throws InsufficientPaymentLimitException {
    LocalDateTime dateTime = LocalDateTime.now();
    paymentDTO.setDate(dateTime);
    Money amount = Money.of(paymentDTO.getAmount(), "BRL");

    isMoneyOutOfLimit(dateTime, amount);
    Money totalPaid = getTotalPaid(walletId, dateTime, amount);
    isMoneyOutOfLimit(dateTime, totalPaid);
  }

  private Money getRemainingLimit(String walletId) {
    LocalDateTime dateTime = LocalDateTime.now();
    Money totalPaid = getTotalPaid(walletId, dateTime, Money.of(0, "BRL"));
    PaymentIntervalsEnum currentValidLimit = PaymentUtils.checkHourLimit(dateTime);
    return currentValidLimit.getAmount().subtract(totalPaid);
  }

}
