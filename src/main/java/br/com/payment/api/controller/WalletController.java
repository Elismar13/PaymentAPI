package br.com.payment.api.controller;

import br.com.payment.api.constants.RegexConstants;
import br.com.payment.api.exception.wallet.WalletAlreadyExistsException;
import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.RemainingLimitResponseDTO;
import br.com.payment.api.model.dto.wallet.request.WalletDTO;
import br.com.payment.api.model.dto.wallet.response.WalletResponseDTO;
import br.com.payment.api.service.PaymentService;
import br.com.payment.api.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  @Autowired
  private WalletService walletService;

  @Autowired
  private PaymentService paymentService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<WalletResponseDTO> createNewUser(@RequestBody @Valid WalletDTO walletDTO) throws WalletAlreadyExistsException {
    WalletResponseDTO createdUser = walletService.createWallet(walletDTO, LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }


  @GetMapping(value = "/{walletId}/limits", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RemainingLimitResponseDTO> getAvailableCashLimit(@PathVariable("walletId") @NotNull UUID walletId) {
    RemainingLimitResponseDTO checkRemainingLimit = paymentService.checkRemainingLimit(walletId, LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.OK).body(checkRemainingLimit);
  }

  @PostMapping(value = "/{walletId}/payments", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity makePayment(@PathVariable @NotNull
                                    @Pattern(regexp = RegexConstants.REGEX_VALIDATE_UUID, message = "{payment.wallet.uuid}")
                                        UUID walletId,
                                    @RequestBody @Valid PaymentDTO walletDTO) {
    paymentService.makePayment(walletId, walletDTO, LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
