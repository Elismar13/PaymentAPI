package br.com.payment.api.utils;

import br.com.payment.api.enums.PaymentIntervalsEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PaymentUtils {

  public static Boolean isWeekend(LocalDate localDate) {
    DayOfWeek day = localDate.getDayOfWeek();
    return DayOfWeek.SATURDAY.equals(day) || DayOfWeek.SUNDAY.equals(day);
  }

  public static PaymentIntervalsEnum checkHourLimit(LocalDateTime currentTime) {
    LocalDate currentDate = currentTime.toLocalDate();

    if (isWeekend(currentDate)) {
      return PaymentIntervalsEnum.WEEKEND;
    }

    if (currentTime.isAfter(LocalDateTime.of(currentDate, LocalTime.of(6, 0, 0))) &&
        currentTime.isBefore(LocalDateTime.of(currentDate, LocalTime.of(18, 0)))) {
      return PaymentIntervalsEnum.DAYLIGHT;
    } else {
      return PaymentIntervalsEnum.NIGHTLY;
    }
  }

}
