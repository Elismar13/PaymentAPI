package br.com.payment.api.service;

import br.com.payment.api.exception.wallet.WalletAlreadyExistsException;
import br.com.payment.api.mapper.wallet.WalletMapper;
import br.com.payment.api.model.dto.wallet.request.WalletDTO;
import br.com.payment.api.model.dto.wallet.response.WalletResponseDTO;
import br.com.payment.api.model.entity.wallet.Wallet;
import br.com.payment.api.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

  @Mock
  WalletRepository walletRepository;

  @InjectMocks
  WalletService walletService;

  private WalletMapper walletMapper = WalletMapper.INSTANCE;

  private WalletDTO createWallet() {
    return WalletDTO.builder()
        .ownerName("Thushima Dante")
        .build();
  }

  @Test
  public void whenWalletIsCreatedItShouldReturnWallet() {
    // given
    WalletDTO walletDTO = createWallet();
    Wallet wallet = walletMapper.toWallet(walletDTO);
    WalletResponseDTO walletResponseDTO = walletMapper.toDTO(wallet);

    // when
    when(walletRepository.save(wallet)).thenReturn(wallet);

    // then
    WalletResponseDTO createdWalletResponseDTO = walletService.createWallet(walletDTO);

    assertThat(walletResponseDTO.getOwnerName(), is(equalTo(createdWalletResponseDTO.getOwnerName())));
  }

  @Test
  public void whenWalletIsCreatedItShouldReturnAExceptionIfWalletAlreadyExists() {
    // given
    WalletDTO walletDTO = createWallet();
    Wallet wallet = walletMapper.toWallet(walletDTO);
    WalletResponseDTO walletResponseDTO = walletMapper.toDTO(wallet);

    // when
    when(walletRepository.findByOwnerName(wallet.getOwnerName())).thenReturn(Optional.of(wallet));

    // then
    assertThrows(WalletAlreadyExistsException.class, () -> walletService.createWallet(walletDTO));
  }

}
