package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.HasId;
import io.kirmit.transfer.account.repository.AbstractAccountRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractAccountService<T extends HasId<UUID>> {

    final AbstractAccountRepository<T> repository;

    AbstractAccountService(AbstractAccountRepository<T> repository) {
        this.repository = repository;
    }

    public Optional<T> findAccount(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Account id is null");
        }
        return repository.getAccount(id);
    }

    public void transferMoney(UUID from, UUID to, BigDecimal amount) {
        if (from == null || to == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Account id is null");
        }
        transfer(from, to, amount);
    }

    protected abstract void transfer(UUID from, UUID to, BigDecimal amount);
}
