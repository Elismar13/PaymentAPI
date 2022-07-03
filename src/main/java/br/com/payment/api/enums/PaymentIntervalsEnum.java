package br.com.payment.api.enums;

import br.com.payment.api.constants.PaymentConstants;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;

public enum PaymentIntervalsEnum {


  DAYLIGHT(1, Money.of(new BigDecimal(4000), "BRL")),
  NIGHTLY(2, Money.of(new BigDecimal(1000), "BRL")),
  WEEKEND(3, Money.of(new BigDecimal(1000), "BRL"));

  private final int id;
  private final Money amount;

  PaymentIntervalsEnum(int id, Money amount) {
    this.id = id;
    this.amount = amount;
  }

  public int getId() {
    return id;
  }

  public Money getAmount() {
    return amount;
  }

}
