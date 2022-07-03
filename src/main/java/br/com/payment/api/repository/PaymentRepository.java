package br.com.payment.api.repository;

import br.com.payment.api.model.entity.payment.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

  Optional<Payment> findPaymentById(Integer id);

  Optional<List<Payment>> findAllByOwnerIdAndCreationDateBetween(String ownerId, LocalDateTime startDate, LocalDateTime finalDate);

}
