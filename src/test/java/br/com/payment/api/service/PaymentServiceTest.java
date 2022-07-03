package br.com.payment.api.service;

import br.com.payment.api.exception.payment.InsufficientPaymentLimitException;
import br.com.payment.api.mapper.payment.PaymentMapper;
import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.PaymentResponseDTO;
import br.com.payment.api.model.dto.payment.response.RemainingLimitResponseDTO;
import br.com.payment.api.model.entity.payment.Payment;
import br.com.payment.api.repository.PaymentRepository;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceTest {

  public static final String WALLETID = "7a05eb6c-3a07-4839-8c62-b4b93521bd27";
  public static final LocalDateTime CREATIONDATE_WEEKEND = LocalDateTime.parse("2022-07-03T15:21:32.144631");
  public static final LocalDateTime CREATIONDATE_DAILY = LocalDateTime.parse("2022-07-04T15:00:00.144631");
  public static final LocalDateTime CREATIONDATE_NIGHTTIME = LocalDateTime.parse("2022-07-04T22:00:00.144631");
  public static final String OWNERNAME = "Thushima Dante";

  @Mock
  PaymentRepository paymentRepository;

  @InjectMocks
  PaymentService paymentService;

  PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

  @Test
  public void whenDaylightPaymentIsMadeItShouldReturnPaymentInformation() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);

    PaymentResponseDTO paymentResponseDTO = paymentMapper.toDTO(payment);
    // then
    PaymentResponseDTO createdPaymentResponseDTO = paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_DAILY);

    assertThat(50, is(createdPaymentResponseDTO.getAmount()));
    assertThat(WALLETID, is(createdPaymentResponseDTO.getOwnerId()));
    assertThat(OWNERNAME, is(createdPaymentResponseDTO.getOwnerName()));
  }

  @Test
  public void whenPaymentIsMadeOnDaylightItShouldReturnPaymentIfHasLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = validDaylightList();
    PaymentResponseDTO paymentResponseExpected = paymentMapper.toDTO(payment);

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    PaymentResponseDTO paymentResponse = paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_DAILY);

    assertThat(paymentResponseExpected.getAmount(), is(paymentResponse.getAmount()));
    assertThat(paymentResponseExpected.getOwnerId(), is(paymentResponse.getOwnerId()));
    assertThat(paymentResponseExpected.getOwnerName(), is(paymentResponse.getOwnerName()));
  }

  @Test
  public void whenPaymentIsMadeOnDaylightItShouldReturnAExceptionIfHasNoLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = invalidDaylightList();
    PaymentResponseDTO paymentResponseExpected = paymentMapper.toDTO(payment);

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    Throwable exception = assertThrows(InsufficientPaymentLimitException.class, () -> paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_DAILY));
    assertThat(exception.getMessage(), is(equalTo("The amount 4005 has exceed the remaining payment limit")));
  }

  @Test
  public void whenNightTimePaymentIsMadeItShouldReturnPaymentInformation() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);

    PaymentResponseDTO paymentResponseDTO = paymentMapper.toDTO(payment);
    // then
    PaymentResponseDTO createdPaymentResponseDTO = paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_NIGHTTIME);

    assertThat(50, is(createdPaymentResponseDTO.getAmount()));
    assertThat(WALLETID, is(createdPaymentResponseDTO.getOwnerId()));
    assertThat(OWNERNAME, is(createdPaymentResponseDTO.getOwnerName()));
  }

  @Test
  public void whenPaymentIsMadeOnNightTimeItShouldReturnPaymentIfHasLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = validNightTimeList();
    PaymentResponseDTO paymentResponseExpected = paymentMapper.toDTO(payment);

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    PaymentResponseDTO paymentResponse = paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_NIGHTTIME);

    assertThat(paymentResponseExpected.getAmount(), is(paymentResponse.getAmount()));
    assertThat(paymentResponseExpected.getOwnerId(), is(paymentResponse.getOwnerId()));
    assertThat(paymentResponseExpected.getOwnerName(), is(paymentResponse.getOwnerName()));
  }

  @Test
  public void whenPaymentIsMadeOnNightTimeItShouldReturnAExceptionIfHasNoLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = invalidNightTimeList();
    PaymentResponseDTO paymentResponseExpected = paymentMapper.toDTO(payment);

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    Throwable exception = assertThrows(InsufficientPaymentLimitException.class, () -> paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_NIGHTTIME));
    assertThat(exception.getMessage(), is(equalTo("The amount 1005 has exceed the remaining payment limit")));
  }

  @Test
  public void whenWeekendPaymentIsMadeItShouldReturnPaymentInformation() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);

    PaymentResponseDTO paymentResponseDTO = paymentMapper.toDTO(payment);
    // then
    PaymentResponseDTO createdPaymentResponseDTO = paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_WEEKEND);

    assertThat(50, is(createdPaymentResponseDTO.getAmount()));
    assertThat(WALLETID, is(createdPaymentResponseDTO.getOwnerId()));
    assertThat(OWNERNAME, is(createdPaymentResponseDTO.getOwnerName()));
  }

  @Test
  public void whenPaymentIsMadeOnWeekendItShouldReturnPaymentIfHasLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = validWeekendList();
    PaymentResponseDTO paymentResponseExpected = paymentMapper.toDTO(payment);

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    PaymentResponseDTO paymentResponse = paymentService.makePayment(WALLETID, paymentDTO, CREATIONDATE_WEEKEND);

    assertThat(paymentResponseExpected.getAmount(), is(paymentResponse.getAmount()));
    assertThat(paymentResponseExpected.getOwnerId(), is(paymentResponse.getOwnerId()));
    assertThat(paymentResponseExpected.getOwnerName(), is(paymentResponse.getOwnerName()));
  }

  @Test
  public void whenPaymentIsMadeOnWeekendItShouldReturnAExceptionIfHasNoLimit() {
    // given
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = invalidWeekendList();
    PaymentResponseDTO paymentResponseExpected = paymentMapper.toDTO(payment);

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
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
    List<Payment> paymentList = validDaylightList();
    RemainingLimitResponseDTO paymentResponseExpected = RemainingLimitResponseDTO.builder().value(50).build();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
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
    PaymentDTO paymentDTO = createValidPaymentDTO();
    Payment payment = createValidPayment();
    List<Payment> paymentList = validNightTimeList();
    RemainingLimitResponseDTO paymentResponseExpected = RemainingLimitResponseDTO.builder().value(50).build();

    // when
    when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
    doReturn(Optional.of(paymentList)).when(paymentRepository)
        .findAllByOwnerIdAndCreationDateBetween(Mockito.any(String.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class)
        );

    // then
    RemainingLimitResponseDTO paymentResponse = paymentService.checkRemainingLimit(WALLETID, CREATIONDATE_NIGHTTIME);

    assertThat(paymentResponseExpected.getValue().intValue(), equalTo(paymentResponse.getValue().intValue()));
  }

  public Payment createValidPayment() {
    return Payment.builder()
        .ownerName(OWNERNAME)
        .ownerId(WALLETID)
        .amount(50)
        .build();
  }

  public PaymentDTO createValidPaymentDTO() {
    return PaymentDTO.builder()
        .ownerName(OWNERNAME)
        .ownerId(WALLETID)
        .amount(50)
        .build();
  }

  public PaymentDTO createInvalidPayment() {
    return PaymentDTO.builder()
        .ownerName(OWNERNAME)
        .ownerId(WALLETID)
        .amount(55)
        .build();
  }

  public List<Payment> validDaylightList() {
    return Arrays.asList(Payment.builder()
            .ownerId(WALLETID)
            .amount(3000)
            .creationDate(CREATIONDATE_DAILY)
            .build(),
        Payment.builder()
            .ownerId(WALLETID)
            .amount(950)
            .creationDate(CREATIONDATE_DAILY)
            .build()
    );
  }

  public List<Payment> invalidDaylightList() {
    return Arrays.asList(Payment.builder()
            .ownerId(WALLETID)
            .amount(2500)
            .creationDate(CREATIONDATE_DAILY)
            .build(),
        Payment.builder()
            .ownerId(WALLETID)
            .amount(1455)
            .creationDate(CREATIONDATE_DAILY)
            .build()
    );
  }

  public List<Payment> validNightTimeList() {
    return Arrays.asList(Payment.builder()
            .ownerId(WALLETID)
            .amount(800)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build(),
        Payment.builder()
            .ownerId(WALLETID)
            .amount(150)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build()
    );
  }

  public List<Payment> invalidNightTimeList() {
    return Arrays.asList(Payment.builder()
            .ownerId(WALLETID)
            .amount(800)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build(),
        Payment.builder()
            .ownerId(WALLETID)
            .amount(155)
            .creationDate(CREATIONDATE_NIGHTTIME)
            .build()
    );
  }

  public List<Payment> validWeekendList() {
    return Arrays.asList(Payment.builder()
            .ownerId(WALLETID)
            .amount(800)
            .creationDate(CREATIONDATE_WEEKEND)
            .build(),
        Payment.builder()
            .ownerId(WALLETID)
            .amount(150)
            .creationDate(CREATIONDATE_WEEKEND)
            .build()
    );
  }

  public List<Payment> invalidWeekendList() {
    return Arrays.asList(Payment.builder()
            .ownerId(WALLETID)
            .amount(800)
            .creationDate(CREATIONDATE_WEEKEND)
            .build(),
        Payment.builder()
            .ownerId(WALLETID)
            .amount(155)
            .creationDate(CREATIONDATE_WEEKEND)
            .build()
    );
  }


}
