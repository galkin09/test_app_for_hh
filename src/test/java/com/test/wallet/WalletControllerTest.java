package com.test.wallet;

import com.test.wallet.models.Wallet;
import com.test.wallet.services.WalletService;
import com.test.wallet.controllers.WalletController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;


    @Test
    void testGetBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, BigDecimal.valueOf(1000));

        when(walletService.getBalance(walletId)).thenReturn(wallet);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(1000));

        verify(walletService, times(1)).getBalance(walletId);
    }

    @Test
    void testUpdateBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        String operationType = "DEPOSIT";
        //String operationType = "WITHDRAW";
        BigDecimal amount = BigDecimal.valueOf(-1000);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\",\"operationType\":\"" + operationType + "\",\"amount\":" + amount + "}"))
                .andExpect(status().isOk());

        verify(walletService, times(1)).updateBalance(walletId, operationType, amount);
    }
}
