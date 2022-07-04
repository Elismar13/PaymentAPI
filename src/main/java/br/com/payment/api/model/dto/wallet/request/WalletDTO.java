package br.com.payment.api.model.dto.wallet.request;

import br.com.payment.api.constants.RegexConstants;
import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {

  @NotBlank(message = "{wallet.validate.username.blank}")
  @NotNull(message = "{wallet.validate.username.blank}")
  @Size(min = 1, max = 255)
  @Pattern(regexp = RegexConstants.REGEX_VALIDATE_LETTERS, message = "{wallet.validate.username}")
  private String ownerName;

}