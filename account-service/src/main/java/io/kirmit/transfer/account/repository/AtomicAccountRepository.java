package io.kirmit.transfer.account.repository;

import io.kirmit.transfer.account.model.AtomicAccount;

import java.util.concurrent.ConcurrentHashMap;

public class AtomicAccountRepository extends AbstractAccountRepository<AtomicAccount> {
    public AtomicAccountRepository() {
        super(new ConcurrentHashMap<>());
    }
}
