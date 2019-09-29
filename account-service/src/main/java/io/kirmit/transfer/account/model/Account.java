package io.kirmit.transfer.account.model;

import java.math.BigDecimal;
import java.util.UUID;

public final class Account implements HasId<UUID> {
    private final UUID id;
    private volatile BigDecimal balance;

    public Account(UUID accountId) {
        this.id = accountId;
        this.balance = BigDecimal.ZERO;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
