package br.com.payment.api.model.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

  private Integer id;
  private BigDecimal amount;
  private String ownerName;
  private LocalDateTime Date;
  private String ownerId;

}