package br.com.payment.api.controller;

import br.com.payment.api.exception.wallet.WalletAlreadyExistsException;
import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.PaymentResponseDTO;
import br.com.payment.api.model.dto.payment.response.RemainingLimitResponseDTO;
import br.com.payment.api.model.dto.wallet.request.WalletDTO;
import br.com.payment.api.model.dto.wallet.response.WalletResponseDTO;
import br.com.payment.api.model.entity.wallet.Wallet;
import br.com.payment.api.service.PaymentService;
import br.com.payment.api.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  @Autowired
  private WalletService walletService;

  @Autowired
  private PaymentService paymentService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<WalletResponseDTO> createNewUser(@RequestBody @Valid WalletDTO walletDTO) throws WalletAlreadyExistsException {
    WalletResponseDTO createdUser = walletService.createWallet(walletDTO);

    return ResponseEntity.ok(createdUser);
  }


  @GetMapping(value = "/{walletId}/limits", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RemainingLimitResponseDTO>  getAvailableCashLimit(@PathVariable("walletId") String walletId) {
    RemainingLimitResponseDTO checkRemainingLimit = paymentService.checkRemainingLimit(walletId);

    return ResponseEntity.ok(checkRemainingLimit);
  }

  @PostMapping(value = "/{walletId}/payments", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaymentResponseDTO> makePayment(@PathVariable String walletId, @RequestBody @Valid PaymentDTO walletDTO) throws Exception {
    PaymentResponseDTO createdPayment = paymentService.makePayment(walletId, walletDTO);

    return ResponseEntity.ok(createdPayment);
  }

}
