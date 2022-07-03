package br.com.payment.api.service;

import br.com.payment.api.exception.wallet.WalletAlreadyExistsException;
import br.com.payment.api.mapper.wallet.WalletMapper;
import br.com.payment.api.model.dto.wallet.request.WalletDTO;
import br.com.payment.api.model.dto.wallet.response.WalletResponseDTO;
import br.com.payment.api.model.entity.wallet.Wallet;
import br.com.payment.api.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

  private final WalletMapper walletMapper = WalletMapper.INSTANCE;
  @Autowired
  private WalletRepository walletRepository;

  public WalletResponseDTO createWallet(WalletDTO walletDTO) throws WalletAlreadyExistsException {
    verifyIfWalletAlreadyExists(walletDTO.getOwnerName());
    Wallet wallet = walletMapper.toWallet(walletDTO);
    Wallet savedWallet = walletRepository.save(wallet);
    return walletMapper.toDTO(savedWallet);
  }

  private void verifyIfWalletAlreadyExists(String ownerName) throws WalletAlreadyExistsException {
    Optional<Wallet> wallet = walletRepository.findByOwnerName(ownerName);

    if (wallet.isPresent()) {
      throw new WalletAlreadyExistsException("User wallet already exists!");
    }
  }

}
