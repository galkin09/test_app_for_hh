package com.test.wallet.controllers;

import com.test.wallet.models.Wallet;
import com.test.wallet.models.WalletRequest;
import com.test.wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping()
public class WalletController {

    private final WalletService walletService;
    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/api/v1/wallet")
    public String updateBalance(@RequestBody WalletRequest walletRequest){
            walletService.updateBalance(walletRequest.getWalletId(), walletRequest.getOperationType(), walletRequest.getAmount());
            return "Success";
        }

    @GetMapping("/api/v1/wallets/{walletId}")
    public Wallet getBalance(@PathVariable("walletId") UUID walletId){
            return walletService.getBalance(walletId);
    }
}
