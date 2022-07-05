package br.com.payment.api.controller;

import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.wallet.request.WalletDTO;
import br.com.payment.api.model.dto.wallet.response.WalletResponseDTO;
import br.com.payment.api.model.entity.payment.Payment;
import br.com.payment.api.model.entity.wallet.Wallet;
import br.com.payment.api.repository.PaymentRepository;
import br.com.payment.api.repository.WalletRepository;
import br.com.payment.api.service.PaymentService;
import br.com.payment.api.service.WalletService;
import br.com.payment.api.utils.JsonConvertionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WalletControllerTest {

  private MockMvc mockMvc;

  @Mock
  private WalletService walletService;

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private PaymentService paymentService;

  @Mock
  private WalletRepository walletRepository;

  @InjectMocks
  private WalletController walletController;


  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(walletController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
        .build();
  }

  @Test
  public void whenPOSTIsCalledThenAWalletIsCreated() throws Exception {
    // given
    WalletResponseDTO walletDTO = createWalletResponseDTO();

    // when
    doReturn(walletDTO).when(walletService).createWallet(any(WalletDTO.class), any(LocalDateTime.class));

    mockMvc.perform(post("/wallets")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(JsonConvertionUtils.asJsonString(walletDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.ownerName", is(walletDTO.getOwnerName())));
  }

  @Test
  void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
    // given
    WalletDTO walletDTO = createWalletDTO();
    walletDTO.setOwnerName(null);

    // then
    mockMvc.perform(post("/wallets")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(JsonConvertionUtils.asJsonString(walletDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void whenPaymentPOSTIsCalledWithWrongRequiredFieldThenAnErrorIsReturned() throws Exception {
    // given
    WalletDTO walletDTO = createWalletDTO();
    walletDTO.setOwnerName("      ");

    // then
    mockMvc.perform(post("/wallets")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(JsonConvertionUtils.asJsonString(walletDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void whenPOSTIsCalledThenAPaymentIsCreated() throws Exception {
    // given
    WalletDTO walletDTO = createWalletDTO();
    Wallet wallet = createWallet();
    UUID uuid = UUID.randomUUID();

    // when
    doReturn(Optional.of(wallet)).when(walletRepository).findById(any(UUID.class));

    mockMvc.perform(post(String.format("/wallets/%s/payments", uuid))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertionUtils.asJsonString(walletDTO)))
        .andExpect(status().isOk());
  }

  @Test
  public void whenPOSTIsCalledWithInvalidWalletIDThenAnErrorIsReturned() throws Exception {
    // given
    PaymentDTO paymentDTO = createPaymentDTO();
    Wallet wallet = createWallet();

    // when
    doReturn(Optional.of(wallet)).when(walletRepository).findById("213");

    mockMvc.perform(post(String.format("/wallets/%s/payments", UUID.randomUUID()))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertionUtils.asJsonString(paymentDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void whenGETIsCalledThenWalletLimitIsReturned() throws Exception {
    // given
    Payment payment = createPayment();
    Wallet wallet = createWallet();
    UUID uuid = UUID.randomUUID();

    // when
    doReturn(Optional.of(wallet)).when(walletRepository).findById(any(UUID.class));
    doReturn(Optional.of(Arrays.asList(payment))).when(paymentRepository).findAllByWallet_IdAndCreationDateBetween(
        any(UUID.class),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
    );

    mockMvc.perform(get(String.format("/wallets/%s/limits", uuid))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  private Wallet createWallet() {
    return Wallet.builder()
        .ownerName("Thushima Dante")
        .build();
  }

  private WalletDTO createWalletDTO() {
    return WalletDTO.builder()
        .ownerName("Thushima Dante")
        .build();
  }

  private PaymentDTO createPaymentDTO() {
    return PaymentDTO.builder()
        .amount(BigDecimal.valueOf(2500))
        .Date(LocalDateTime.parse("2022-07-03T16:47:59.009"))
        .build();
  }

  private WalletResponseDTO createWalletResponseDTO() {
    return WalletResponseDTO.builder()
        .ownerName("Thushima Dante")
        .build();
  }

  private Payment createPayment() {
    return Payment.builder()
        .wallet(createWallet())
        .build();
  }

}
