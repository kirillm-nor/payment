package io.kirmit.transfer.account.repository;

import io.kirmit.transfer.account.model.FinalAccount;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class FinalAccountRepository extends AbstractAccountRepository<FinalAccount> {
    public FinalAccountRepository() {
        super(new ConcurrentHashMap<>());
    }

    public void merge(FinalAccount account, final BigDecimal diff) {
        memoryRepository.merge(account.getId(), account,
                (o, n) -> new FinalAccount(n.getId(), o.getBalance().add(diff)));
    }
}
