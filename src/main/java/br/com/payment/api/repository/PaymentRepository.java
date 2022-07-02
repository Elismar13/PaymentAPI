package br.com.payment.api.repository;

import br.com.payment.api.model.entity.payment.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

  Optional<Payment> findPaymentById(Integer id);

}
