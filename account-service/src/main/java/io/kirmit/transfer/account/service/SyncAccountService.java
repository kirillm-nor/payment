package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.model.exception.InsufficientFundsException;
import io.kirmit.transfer.account.repository.SimpleAccountRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SyncAccountService extends AbstractAccountService<Account> {

    private final transient Object lock = new Object();

    public SyncAccountService() {
        super(new SimpleAccountRepository());
    }

    @Override
    protected void transfer(UUID from, UUID to, BigDecimal amount) {
        Optional<Account> fromAccount = repository.getAccount(from);
        Optional<Account> toAccount = repository.getAccount(to);
        fromAccount.ifPresent(f -> toAccount.ifPresent(t -> {
            synchronized (lock) {
                BigDecimal fb = f.getBalance().subtract(amount);
                if (fb.signum() >= 0) {
                    BigDecimal tb = t.getBalance();
                    f.setBalance(fb);
                    t.setBalance(tb.add(amount));
                } else {
                    throw new InsufficientFundsException("Not enough");
                }
            }
        }));
    }
}
