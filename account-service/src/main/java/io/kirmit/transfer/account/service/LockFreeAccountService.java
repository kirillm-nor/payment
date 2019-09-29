package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.AtomicAccount;
import io.kirmit.transfer.account.model.exception.InsufficientFundsException;
import io.kirmit.transfer.account.repository.AtomicAccountRepository;

import java.lang.invoke.VarHandle;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class LockFreeAccountService extends AbstractAccountService<AtomicAccount> {

    LockFreeAccountService() {
        super(new AtomicAccountRepository());
    }

    @Override
    protected void transfer(UUID from, UUID to, BigDecimal amount) {
        Optional<AtomicAccount> fromAccount = repository.getAccount(from);
        Optional<AtomicAccount> toAccount = repository.getAccount(to);
        fromAccount.ifPresent(f -> toAccount.ifPresent(t -> {
            BigDecimal fb = f.getBalance();
            BigDecimal fbn = fb.subtract(amount);
            if (fb.signum() >= 0) {
                while (!f.setBalance(fb, fbn)) {
                    fb = f.getBalance();
                    fbn = fb.subtract(amount);
                    if (fb.signum() < 0) {
                        throw new InsufficientFundsException("Not enough");
                    }
                }
                VarHandle.releaseFence();
                BigDecimal tb = t.getBalance();
                BigDecimal tbn = tb.add(amount);
                while (!t.setBalance(tb, tbn)) {
                    tb = t.getBalance();
                    tbn = tb.add(amount);
                }
            } else {
                throw new InsufficientFundsException("Not enough");
            }
        }));
    }
}
