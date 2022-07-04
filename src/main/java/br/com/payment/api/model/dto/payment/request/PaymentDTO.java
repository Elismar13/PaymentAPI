package br.com.payment.api.model.dto.payment.request;

import br.com.payment.api.constants.RegexConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

  private Integer id;

  @Min(0)
  @Max(value = 1000, message = "{payment.maximum.limit}")
  private Number amount;

  private LocalDateTime Date;

}