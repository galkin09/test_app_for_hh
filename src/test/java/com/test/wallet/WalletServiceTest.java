package com.test.wallet;

import com.test.wallet.models.Wallet;
import com.test.wallet.repositories.WalletRepository;
import com.test.wallet.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class WalletServiceTest {

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        // Можно добавить дополнительную настройку, если необходимо
    }

    @Test
    void testGetWalletBalance() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, BigDecimal.valueOf(1000));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getBalance(walletId);

        assertEquals(walletId, result.getWalletId());
        assertEquals(BigDecimal.valueOf(1000), result.getBalance());

        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testUpdateWalletBalance_Deposit() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, BigDecimal.valueOf(1000));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.updateBalance(walletId, "DEPOSIT", BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(1500), wallet.getBalance());

        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testUpdateWalletBalance_Withdraw() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, BigDecimal.valueOf(1000));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.updateBalance(walletId, "WITHDRAW", BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(500), wallet.getBalance());

        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testUpdateWalletBalance_InvalidOperationType() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, BigDecimal.valueOf(1000));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(IllegalArgumentException.class, () -> {
            walletService.updateBalance(walletId, "INVALID", BigDecimal.valueOf(500));
        });

        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testUpdateWalletBalance_InsufficientFunds() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, BigDecimal.valueOf(1000));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(RuntimeException.class, () -> {
            walletService.updateBalance(walletId, "WITHDRAW", BigDecimal.valueOf(1500));
        });

        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, never()).save(any());
    }
}