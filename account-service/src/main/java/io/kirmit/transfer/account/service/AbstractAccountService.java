package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.HasId;
import io.kirmit.transfer.account.repository.AbstractAccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractAccountService<T extends HasId<UUID>> {

    public static final String ACCOUNT_ID_IS_NULL = "Account id is null";
    final AbstractAccountRepository<T> repository;

    AbstractAccountService(AbstractAccountRepository<T> repository) {
        this.repository = repository;
    }

    public Optional<T> findAccount(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException(ACCOUNT_ID_IS_NULL);
        }
        return repository.getAccount(id);
    }

    public void addAccount(T account) {
        if (account == null) {
            throw new IllegalArgumentException("Account is null");
        }
        repository.addAccount(account);
    }

    public void transferMoney(UUID from, UUID to, BigDecimal amount) {
        if (from == null || to == null || amount.signum() < 0) {
            throw new IllegalArgumentException(ACCOUNT_ID_IS_NULL);
        }
        Optional<T> fromAccount = repository.getAccount(from);
        Optional<T> toAccount = repository.getAccount(to);
        fromAccount.ifPresent(f -> toAccount.ifPresent(t -> transfer(f, t, amount)));
    }

    protected abstract void transfer(T from, T to, BigDecimal amount);
}
