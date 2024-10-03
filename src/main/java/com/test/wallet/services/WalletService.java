package com.test.wallet.services;

import com.test.wallet.exceptions.WalletException;
import com.test.wallet.models.Wallet;
import com.test.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void updateBalance(UUID walletId, String operationType, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));

        synchronized (wallet) {
            if ("DEPOSIT".equalsIgnoreCase(operationType)) {
                wallet.setBalance(wallet.getBalance().add(amount));
            } else if ("WITHDRAW".equalsIgnoreCase(operationType)) {
                if (wallet.getBalance().compareTo(amount) < 0) {
                    throw new RuntimeException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
            } else {
                throw new IllegalArgumentException("Invalid operation type");
            }
            walletRepository.save(wallet);
        }
    }

    @Transactional(readOnly = true)
    public Wallet getBalance(UUID walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }
}
