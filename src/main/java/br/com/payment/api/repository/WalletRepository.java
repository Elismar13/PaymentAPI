package br.com.payment.api.repository;

import br.com.payment.api.model.entity.wallet.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {

  Optional<Wallet> findByOwnerName(String ownerName);

  Optional<Wallet> findById(UUID id);

}
