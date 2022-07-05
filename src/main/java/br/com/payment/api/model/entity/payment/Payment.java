package br.com.payment.api.model.entity.payment;

import br.com.payment.api.model.entity.wallet.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payments")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private BigDecimal amount;

  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private Wallet wallet;

}
