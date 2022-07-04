package br.com.payment.api.model.dto.payment.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

  @JsonIgnore
  private Integer id;

  @JsonIgnore
  private Number amount;

  @JsonIgnore
  private String ownerName;

  @JsonIgnore
  private LocalDateTime creationDate;
  @JsonIgnore
  private String ownerId;

}