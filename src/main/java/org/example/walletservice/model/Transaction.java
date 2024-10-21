package org.example.walletservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

    @Entity
    public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        private UUID walletId;
        private String operationType;
        private BigDecimal amount;
        private LocalDateTime timestamp;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public UUID getWalletId() {
            return walletId;
        }

        public void setWalletId(UUID walletId) {
            this.walletId = walletId;
        }

        public String getOperationType() {
            return operationType;
        }

        public void setOperationType(String operationType) {
            this.operationType = operationType;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
