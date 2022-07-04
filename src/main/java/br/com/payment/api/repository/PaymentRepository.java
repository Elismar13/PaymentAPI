package br.com.payment.api.repository;

import br.com.payment.api.model.entity.payment.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

  Optional<Payment> findPaymentById(Integer id);

  Optional<List<Payment>> findAllByWallet_IdAndCreationDateBetween(UUID wallet_id, LocalDateTime creationDate, LocalDateTime creationDate2);

}
