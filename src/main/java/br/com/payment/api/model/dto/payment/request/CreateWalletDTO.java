package br.com.payment.api.model.dto.payment.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletDTO {

  String ownerName;

}