package io.kirmit.transfer.account.model;

import java.math.BigDecimal;
import java.util.UUID;

public final class FinalAccount implements HasId<UUID> {
    private final UUID id;
    private final BigDecimal balance;

    public FinalAccount(UUID id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
