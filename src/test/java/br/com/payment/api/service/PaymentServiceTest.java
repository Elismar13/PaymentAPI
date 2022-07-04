package br.com.payment.api.service;

import br.com.payment.api.exception.payment.InsufficientPaymentLimitException;
import br.com.payment.api.mapper.payment.PaymentMapper;
import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.PaymentResponseDTO;
import br.com.payment.api.model.dto.payment.response.RemainingLimitResponseDTO;
import br.com.payment.api.model.entity.payment.Payment;
import br.com.payment.api.model.entity.wallet.Wallet;
import br.com.payment.api.repository.PaymentRepository;
import br.com.payment.api.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceTest {

  public static final UUID WALLETID = UUID.fromString("7a05eb6c-3a07-4839-8c62-b4b93521bd27");
  public static final LocalDateTime CREATIONDATE_WEEKEND = LocalDateTime.parse("2022-07-03T15:21:32.144631");
  public static final LocalDateTime CREATIONDATE_DAILY = LocalDateTime.parse("2022-07-04T15:00:00.144631");
  public static final LocalDateTime CREATIONDATE_NIGHTTIME = LocalDateTime.parse("2022-07-04T22:00:00.144631");
  public static final String OWNERNAME = "Thushima Dante";

  @Mock
  PaymentRepository paymentRepository;

  @InjectMocks
  PaymentService paymentService;
  PaymentMapper paymentMapper = PaymentMapper.INSTANCE;
  @Mock
  private WalletRepository walletRepository;

  @Test
  public void whenDaylightPaymentIsMadeItShouldReturnNothing() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));

    // then
    paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_DAILY);
  }

  @Test
  public void whenPaymentIsMadeOnDaylightItShouldReturnPaymentIfHasLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();
    List<Payment> paymentList = validDaylightList();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_DAILY);
  }

  @Test
  public void whenPaymentIsMadeOnDaylightItShouldReturnAExceptionIfHasNoLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();
    List<Payment> paymentList = invalidDaylightList();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    Throwable exception = assertThrows(InsufficientPaymentLimitException.class, () -> paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_DAILY));
    assertThat(exception.getMessage(), is(equalTo("The amount 4005 has exceed the remaining payment limit")));
  }

  @Test
  public void whenNightTimePaymentIsMadeItShouldReturnNothing() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));

    // then
    paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_NIGHTTIME);
  }

  @Test
  public void whenPaymentIsMadeOnNightTimeItShouldReturnPaymentIfHasLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();
    List<Payment> paymentList = validNightTimeList();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_NIGHTTIME);
  }

  @Test
  public void whenPaymentIsMadeOnNightTimeItShouldReturnAExceptionIfHasNoLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();
    List<Payment> paymentList = invalidNightTimeList();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    Throwable exception = assertThrows(InsufficientPaymentLimitException.class, () -> paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_NIGHTTIME));
    assertThat(exception.getMessage(), is(equalTo("The amount 1005 has exceed the remaining payment limit")));
  }
  @Test
  public void whenWeekendPaymentIsMadeItShouldReturnNothing() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));

    // then
    paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_WEEKEND);

  }

  @Test
  public void whenPaymentIsMadeOnWeekendItShouldReturnPaymentIfHasLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = validWeekendList();
    Wallet wallet = createWallet();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_WEEKEND);
  }

  @Test
  public void whenPaymentIsMadeOnWeekendItShouldReturnAExceptionIfHasNoLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();
    List<Payment> paymentList = invalidWeekendList();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    Throwable exception = assertThrows(InsufficientPaymentLimitException.class, () -> paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_WEEKEND));
    assertThat(exception.getMessage(), is(equalTo("The amount 1005 has exceed the remaining payment limit")));
  }

  @Test
  public void checkIfHasValidDailyTimeLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();
    List<Payment> paymentList = validDaylightList();
    RemainingLimitResponseDTO paymentResponseExpected = RemainingLimitResponseDTO.builder().value(50).build();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    RemainingLimitResponseDTO paymentResponse = paymentService.checkRemainingLimit(WALLETID, CREATIONDATE_DAILY);

    assertThat(paymentResponse.getValue().intValue(), is(paymentResponseExpected.getValue().intValue()));
  }

  @Test
  public void checkIfHasValidNightTimeLimit() {
    // given
    Payment payment = createValidPayment();
    Wallet wallet = createWallet();
    List<Payment> paymentList = validNightTimeList();
    RemainingLimitResponseDTO paymentResponseExpected = RemainingLimitResponseDTO.builder().value(50).build();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    when(walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(wallet));
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByWallet_IdAndCreationDateBetween(Mockito.any(UUID.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    RemainingLimitResponseDTO paymentResponse = paymentService.checkRemainingLimit(WALLETID, CREATIONDATE_NIGHTTIME);

    assertThat(paymentResponseExpected.getValue().intValue(), equalTo(paymentResponse.getValue().intValue()));
  }

  private Wallet createWallet() {
    return Wallet.builder()
        .ownerName(OWNERNAME)
        .id(WALLETID)
        .build();
  }

  public Payment createValidPayment() {
    return Payment.builder()
        .wallet(createWallet())
        .amount(50)
        .build();
  }

  public PaymentDTO createValidPaymentDTO() {
    return PaymentDTO.builder()
        .amount(50)
        .build();
  }

  public PaymentDTO createInvalidPayment() {
    return PaymentDTO.builder()
        .amount(55)
        .build();
  }

  public List<Payment> validDaylightList() {
    return Arrays.asList(Payment.builder()
            .wallet(createWallet())
            .amount(3000)
            .creationDate(CREATIONDATE_DAILY)
            .build(),
        Payment.builder()
            .wallet(createWallet())
            .amount(950)
            .creationDate(CREATIONDATE_DAILY)
            .build()
    );
  }

  public List<Payment> invalidDaylightList() {
    return Arrays.asList(Payment.builder()
            .wallet(createWallet())
            .amount(2500)
            .creationDate(CREATIONDATE_DAILY)
            .build(),
        Payment.builder()
            .wallet(createWallet())
            .amount(1455)
            .creationDate(CREATIONDATE_DAILY)
            .build()
    );
  }

  public List<Payment> validNightTimeList() {
    return Arrays.asList(Payment.builder()
            .wallet(createWallet())
            .amount(800)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build(),
        Payment.builder()
            .wallet(createWallet())
            .amount(150)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build()
    );
  }

  public List<Payment> invalidNightTimeList() {
    return Arrays.asList(Payment.builder()
            .wallet(createWallet())
            .amount(800)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build(),
        Payment.builder()
            .wallet(createWallet())
            .amount(155)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build()
    );
  }

  public List<Payment> validWeekendList() {
    return Arrays.asList(Payment.builder()
            .wallet(createWallet())
            .amount(800)
            .creationDate(CREATIONDATE_WEEKEND)
            .build(),
        Payment.builder()
            .wallet(createWallet())
            .amount(150)
            .creationDate(CREATIONDATE_WEEKEND)
            .build()
    );
  }

  public List<Payment> invalidWeekendList() {
    return Arrays.asList(Payment.builder()
            .wallet(createWallet())
            .amount(800)
            .creationDate(CREATIONDATE_WEEKEND)
            .build(),
        Payment.builder()
            .wallet(createWallet())
            .amount(155)
            .creationDate(CREATIONDATE_WEEKEND)
            .build()
    );
  }


}
