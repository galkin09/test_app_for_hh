package com.test.wallet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Wallet {

    @Id
    @Column(name = "id")
    private UUID walletId;

    private BigDecimal balance = BigDecimal.ZERO;

    public Wallet() {
    }

    public Wallet(UUID walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
