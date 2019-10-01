package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.model.exception.InsufficientFundsException;
import io.kirmit.transfer.account.repository.SimpleAccountRepository;
import java.math.BigDecimal;

/**
 * Plain sync locking
 */
public class SyncAccountService extends AbstractAccountService<Account> {

    private final transient Object lock = new Object();

    public SyncAccountService() {
        super(new SimpleAccountRepository());
    }

    @Override
    protected void transfer(Account from, Account to, BigDecimal amount) {
        synchronized (lock) {
            BigDecimal fb = from.getBalance().subtract(amount);
            if (fb.signum() >= 0) {
                BigDecimal tb = to.getBalance();
                from.setBalance(fb);
                to.setBalance(tb.add(amount));
            } else {
                throw new InsufficientFundsException("Not enough");
            }
        }
    }
}
