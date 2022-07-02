package br.com.payment.api.mapper;

import br.com.payment.api.model.dto.payment.request.CreateWalletDTO;
import br.com.payment.api.model.dto.payment.response.CreateWalletResponseDTO;
import br.com.payment.api.model.entity.wallet.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {

  WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

  Wallet toWallet(CreateWalletDTO walletDTO);

  CreateWalletResponseDTO toDTO(Wallet wallet);

}
