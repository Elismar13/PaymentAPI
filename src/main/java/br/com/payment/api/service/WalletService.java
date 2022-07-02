package br.com.payment.api.service;

import br.com.payment.api.model.dto.payment.request.CreateWalletDTO;
import br.com.payment.api.model.dto.payment.response.CreateWalletResponseDTO;
import br.com.payment.api.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  @Autowired
  private WalletRepository walletRepository;

  public CreateWalletResponseDTO createWallet(CreateWalletDTO walletDTO) {
    return null;
  }

}
