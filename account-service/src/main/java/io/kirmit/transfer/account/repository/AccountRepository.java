package io.kirmit.transfer.account.repository;

import io.kirmit.transfer.account.model.Account;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepository {

    private final Map<UUID, Account> memoryRepository = new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        memoryRepository.put(account.getAccountId(), account);
    }

    public Optional<Account> getAccount(UUID id) {
        Account value = memoryRepository.get(id);
        return Optional.ofNullable(value);
    }
}
