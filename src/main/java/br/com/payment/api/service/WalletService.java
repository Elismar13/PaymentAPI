package br.com.payment.api.service;

import br.com.payment.api.mapper.WalletMapper;
import br.com.payment.api.model.dto.payment.response.CreateWalletResponseDTO;
import br.com.payment.api.model.dto.payment.request.CreateWalletDTO;
import br.com.payment.api.model.entity.wallet.Wallet;
import br.com.payment.api.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

  @Autowired
  private WalletRepository walletRepository;

  private final WalletMapper walletMapper = WalletMapper.INSTANCE;

  public CreateWalletResponseDTO createWallet(CreateWalletDTO walletDTO) throws Exception {
    verifyIfWalletAlreadyExists(walletDTO.getOwnerName());
    Wallet wallet = walletMapper.toWallet(walletDTO);
    Wallet savedWallet = walletRepository.save(wallet);
    return walletMapper.toDTO(savedWallet);
  }

  private void verifyIfWalletAlreadyExists(String ownerName) throws Exception {
    Optional<Wallet> wallet = walletRepository.findByOwnerName(ownerName);

    if(wallet.isPresent()) {
      throw new Exception("User wallet already exists!");
    }
  }

}
