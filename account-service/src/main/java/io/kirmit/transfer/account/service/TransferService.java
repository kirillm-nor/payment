package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.repository.AccountRepository;

public class TransferService {

    private final transient Object lock = new Object();
    private final AccountRepository repository;

    public TransferService(AccountRepository repository) {
        this.repository = repository;
    }
}
