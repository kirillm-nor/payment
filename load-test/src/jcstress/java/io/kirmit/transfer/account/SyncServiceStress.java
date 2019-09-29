package io.kirmit.transfer.account;

import io.kirmit.transfer.account.service.SyncAccountService;
import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

@JCStressTest
@Outcome(id = "1", expect = ACCEPTABLE_INTERESTING, desc = "One update lost.")
@Outcome(id = "2", expect = ACCEPTABLE,  desc = "Both updates.")
public class SyncServiceStress {

    @State
    public static class ServiceState {
        private volatile int value;
        private final SyncAccountService service = new SyncAccountService() {{
        }};
    }

    @Actor
    public void actor1(ServiceState state) {
        state.value++;
    }

    @Actor
    public void actor2(ServiceState state) {
        state.value++;
    }

    @Arbiter
    public void arbiter(ServiceState state, I_Result r) {
        r.r1 = state.value;
    }
}
