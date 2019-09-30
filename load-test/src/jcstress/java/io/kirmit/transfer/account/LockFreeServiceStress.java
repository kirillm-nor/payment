package io.kirmit.transfer.account;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;

import io.kirmit.transfer.account.model.AtomicAccount;
import io.kirmit.transfer.account.service.LockFreeAccountService;
import java.math.BigDecimal;
import java.util.UUID;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

@JCStressTest
@Outcome(id = "1, 2", expect = ACCEPTABLE,  desc = "Both updates.")
public class LockFreeServiceStress {

    @State
    public static class ServiceState {
        private final UUID firstId = UUID.randomUUID();
        private final UUID secondId = UUID.randomUUID();
        private final LockFreeAccountService service = new LockFreeAccountService() {{
            addAccount(new AtomicAccount(firstId, BigDecimal.valueOf(2)));
            addAccount(new AtomicAccount(secondId, BigDecimal.valueOf(1)));
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
