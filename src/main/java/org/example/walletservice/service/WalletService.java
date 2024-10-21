package org.example.walletservice.service;


import org.example.walletservice.model.Transaction;
import org.example.walletservice.model.Wallet;
import org.example.walletservice.repository.TransactionRepository;
import org.example.walletservice.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
    public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void modifyWallet(UUID walletId, String operationType, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if ("WITHDRAW".equalsIgnoreCase(operationType) && wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        if ("DEPOSIT".equalsIgnoreCase(operationType)) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else if ("WITHDRAW".equalsIgnoreCase(operationType)) {
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }

        walletRepository.save(wallet);
        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setOperationType(operationType);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        return wallet.getBalance();
    }
}
