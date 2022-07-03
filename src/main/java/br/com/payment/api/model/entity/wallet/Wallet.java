package br.com.payment.api.model.entity.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallets")
public class Wallet {

  String ownerName;
  LocalDateTime registrationDate;
  @Id
  @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
  @GeneratedValue(generator = "UUIDGenerator")
  private UUID id;

}