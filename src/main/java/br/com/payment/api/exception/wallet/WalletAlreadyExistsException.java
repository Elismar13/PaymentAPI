package br.com.payment.api.exception.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WalletAlreadyExistsException extends RuntimeException {

  public WalletAlreadyExistsException(String ownerName) {
    super(String.format("Wallet with name %s already registered in the system.", ownerName));
  }

}
