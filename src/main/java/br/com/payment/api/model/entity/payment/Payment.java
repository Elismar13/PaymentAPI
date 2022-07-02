package br.com.payment.api.model.entity.payment;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payments")
public class Payment {

  @Id
  private Integer id;

  private String amount;
  private String ownerName;
  private LocalDateTime creationDate;
  private String ownerId;

}
