package br.com.payment.api.exception.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientPaymentLimitException extends RuntimeException {

  public InsufficientPaymentLimitException(Number value) {
    super(String.format("The amount %d has exceed the remaining payment limit", value.intValue()));
  }

}
