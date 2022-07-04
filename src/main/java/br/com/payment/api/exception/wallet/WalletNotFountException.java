package br.com.payment.api.exception.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WalletNotFountException extends RuntimeException {

  public WalletNotFountException() {
    super("Wallet does not exists");
  }

}
