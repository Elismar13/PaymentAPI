package br.com.payment.api.model.entity.payment;

import lombok.*;
import org.mapstruct.Mapping;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private Number amount;
  private String ownerName;

  private LocalDateTime creationDate;
  private String ownerId;

}
