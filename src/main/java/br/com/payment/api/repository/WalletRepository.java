package br.com.payment.api.repository;

import br.com.payment.api.model.entity.wallet.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {

}
