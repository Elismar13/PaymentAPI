package br.com.payment.api.model.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

  private Integer id;
  private String amount;
  private String ownerName;
  private LocalDateTime creationDate;
  private String ownerId;

}