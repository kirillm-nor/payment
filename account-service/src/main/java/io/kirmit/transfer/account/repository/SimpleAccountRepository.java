package io.kirmit.transfer.account.repository;

import io.kirmit.transfer.account.model.Account;

import java.util.concurrent.ConcurrentHashMap;

public class SimpleAccountRepository extends AbstractAccountRepository<Account> {
    public SimpleAccountRepository() { super(new ConcurrentHashMap<>());}
}
