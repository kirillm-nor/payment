package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.AtomicAccount;
import io.kirmit.transfer.account.model.exception.InsufficientFundsException;
import io.kirmit.transfer.account.repository.AtomicAccountRepository;
import java.lang.invoke.VarHandle;
import java.math.BigDecimal;

/**
 * Lockfree balance update, based on Atomic*FieldsUpdater
 */
public class LockFreeAccountService extends AbstractAccountService<AtomicAccount> {

    public LockFreeAccountService() {
        super(new AtomicAccountRepository());
    }

    @Override
    protected void transfer(AtomicAccount from, AtomicAccount to, BigDecimal amount) {
        BigDecimal fb = from.getBalance();
        BigDecimal fbn = fb.subtract(amount);
        if (fb.signum() >= 0) {
            while (!from.setBalance(fb, fbn)) {
                fb = from.getBalance();
                fbn = fb.subtract(amount);
                if (fb.signum() < 0) {
                    throw new InsufficientFundsException("Not enough");
                }
            }
            BigDecimal tb = to.getBalance();
            BigDecimal tbn = tb.add(amount);
            while (!to.setBalance(tb, tbn)) {
                tb = to.getBalance();
                tbn = tb.add(amount);
            }
        } else {
            throw new InsufficientFundsException("Not enough");
        }
    }
}
