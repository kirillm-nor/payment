package io.kirmit.transfer.account;

import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.service.SyncAccountService;
import java.math.BigDecimal;
import java.util.UUID;
import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.I_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

@JCStressTest
@Outcome(id = "1, 2", expect = ACCEPTABLE,  desc = "Both updates.")
public class SyncServiceStress {

    @State
    public static class ServiceState {
        private final UUID firstId = UUID.randomUUID();
        private final UUID secondId = UUID.randomUUID();
        private final SyncAccountService service = new SyncAccountService() {{
            addAccount(new Account(firstId, BigDecimal.valueOf(2)));
            addAccount(new Account(secondId, BigDecimal.valueOf(1)));
        }};
    }

    @Actor
    public void actor1(ServiceState state) {
        state.service.transferMoney(state.firstId, state.secondId, BigDecimal.ONE);
        state.service.transferMoney(state.firstId, state.secondId, BigDecimal.ONE);
    }

    @Actor
    public void actor2(ServiceState state) {
        state.service.transferMoney(state.secondId, state.firstId, BigDecimal.ONE);
    }

    @Arbiter
    public void arbiter(ServiceState state, II_Result r) {
        r.r1 = state.service.findAccount(state.firstId).get().getBalance().intValue();
        r.r2 = state.service.findAccount(state.secondId).get().getBalance().intValue();
    }
}
