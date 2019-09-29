package io.kirmit.transfer.account.repository;

import io.kirmit.transfer.account.model.HasId;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractAccountRepository<T extends HasId<UUID>> {
    protected final Map<UUID, T> memoryRepository;

    protected AbstractAccountRepository(Map<UUID, T> memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    public void addAccount(T account) {
        memoryRepository.put(account.getId(), account);
    }

    public Optional<T> getAccount(UUID id) {
        T value = memoryRepository.get(id);
        return Optional.ofNullable(value);
    }
}
