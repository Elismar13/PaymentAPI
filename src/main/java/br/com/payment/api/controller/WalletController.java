package br.com.payment.api.controller;

import br.com.payment.api.model.dto.payment.response.CreateWalletResponseDTO;
import br.com.payment.api.model.dto.payment.request.CreateWalletDTO;
import br.com.payment.api.model.entity.wallet.Wallet;
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

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CreateWalletResponseDTO> createNewUser(@RequestBody @Valid CreateWalletDTO walletDTO) throws Exception {
    CreateWalletResponseDTO createdUser = walletService.createWallet(walletDTO);

    return ResponseEntity.ok(createdUser);
  }


  @GetMapping(value = "/{walletId}/limits", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Wallet> getAvailableCashLimit(@PathVariable("walletId") String walletId) {
    return null;
  }

  @PostMapping(value = "/{walletId}/payments", produces = MediaType.APPLICATION_JSON_VALUE)
  public Wallet makePayment(@PathVariable("walletId") String walletId) {
    return null;
  }

}
