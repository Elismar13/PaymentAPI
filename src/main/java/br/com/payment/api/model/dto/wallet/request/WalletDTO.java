package br.com.payment.api.model.dto.wallet.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {

  String ownerName;

}