package io.kirmit.transfer.account.model;

import java.math.BigDecimal;
import java.util.UUID;

public final class Account {
    private final UUID accountId;
    private volatile BigDecimal balance;

    public Account(UUID accountId) {
        this.accountId = accountId;
        this.balance = BigDecimal.ZERO;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
