package br.com.payment.api.model.entity.wallet;

import br.com.payment.api.model.entity.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallets")
public class Wallet {

  @Id
  @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
  @GeneratedValue(generator = "UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  private String ownerName;
  private LocalDateTime registrationDate;

  @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
  private List<Payment> payments;

}