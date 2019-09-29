package io.kirmit.transfer.account.service;

import io.kirmit.transfer.account.model.FinalAccount;
import io.kirmit.transfer.account.model.exception.InsufficientFundsException;
import io.kirmit.transfer.account.repository.FinalAccountRepository;

import java.lang.invoke.VarHandle;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class MergeAccountService extends AbstractAccountService<FinalAccount> {

    private final FinalAccountRepository finalRepository;

    public MergeAccountService() {
        super(new FinalAccountRepository());
        finalRepository = (FinalAccountRepository) repository;
    }

    @Override
    protected void transfer(UUID from, UUID to, BigDecimal amount) {
        Optional<FinalAccount> fromAccount = finalRepository.getAccount(from);
        Optional<FinalAccount> toAccount = finalRepository.getAccount(to);
        fromAccount.ifPresent(f -> toAccount.ifPresent(t -> {
            BigDecimal fb = f.getBalance().subtract(amount);
            if (fb.signum() >= 0) {
                finalRepository.merge(new FinalAccount(from, fb), amount.negate());
                VarHandle.releaseFence();
                finalRepository.merge(new FinalAccount(to, t.getBalance().add(amount)), amount);
            } else {
                throw new InsufficientFundsException("Not enough");
            }
        }));
    }
}
