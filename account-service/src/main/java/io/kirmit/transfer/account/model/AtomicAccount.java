package io.kirmit.transfer.account.model;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicAccount implements HasId<UUID> {
    private static final AtomicReferenceFieldUpdater<AtomicAccount, BigDecimal> BALANCE_UPDATE =
            AtomicReferenceFieldUpdater.newUpdater(AtomicAccount.class, BigDecimal.class, "balance");
    private final UUID id;
    private volatile BigDecimal balance;

    public AtomicAccount(UUID id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
    }

    public boolean setBalance(BigDecimal expected, BigDecimal balance) {
        return BALANCE_UPDATE.compareAndSet(this, expected, balance);
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return BALANCE_UPDATE.get(this);
    }
}
