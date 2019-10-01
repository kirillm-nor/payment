package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.FinalAccount;
import io.kirmit.transfer.account.model.exception.InsufficientFundsException;
import io.kirmit.transfer.account.repository.FinalAccountRepository;
import java.lang.invoke.VarHandle;
import java.math.BigDecimal;

/**
 * Balance update is using final account structure and merge sync in repository
 */
public class MergeAccountService extends AbstractAccountService<FinalAccount> {

    private final FinalAccountRepository finalRepository;

    public MergeAccountService() {
        super(new FinalAccountRepository());
        finalRepository = (FinalAccountRepository) repository;
    }

    @Override
    protected void transfer(FinalAccount from, FinalAccount to, BigDecimal amount) {
        BigDecimal fb = from.getBalance().subtract(amount);
        if (fb.signum() >= 0) {
            finalRepository.merge(new FinalAccount(from.getId(), fb), amount.negate());
            finalRepository.merge(new FinalAccount(to.getId(), to.getBalance().add(amount)), amount);
        } else {
            throw new InsufficientFundsException("Not enough");
        }
    }
}
