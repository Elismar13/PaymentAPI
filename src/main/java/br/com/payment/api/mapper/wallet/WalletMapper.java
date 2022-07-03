package br.com.payment.api.mapper.wallet;

import br.com.payment.api.model.dto.wallet.request.WalletDTO;
import br.com.payment.api.model.dto.wallet.response.WalletResponseDTO;
import br.com.payment.api.model.entity.wallet.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {

  WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

  Wallet toWallet(WalletDTO walletDTO);

  WalletResponseDTO toDTO(Wallet wallet);

}
