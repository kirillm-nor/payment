package io.kirmit.transfer.account;

import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.model.AtomicAccount;
import io.kirmit.transfer.account.service.SyncAccountService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(
        value = 1,
        jvmArgs = {
                "-server",
                "-XX:+UnlockExperimentalVMOptions",
                "-XX:+TrustFinalNonStaticFields",
                "-XX:-UseBiasedLocking",
                "-XX:+UnlockDiagnosticVMOptions",
                "-XX:+PrintAssembly"
        }
)
@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SyncServiceBenchmark {

    private SyncAccountService service;
    private final int amount = 10000;
    private final Random random = new Random();
    private final UUID firstId = UUID.randomUUID();
    private final UUID secondId = UUID.randomUUID();
    private final List<UUID> ids = new ArrayList<>(10000);

    @Setup(Level.Iteration)
    public void setup() {
        service = new SyncAccountService();
        service.addAccount(new Account(firstId, BigDecimal.valueOf(1000000000)));
        service.addAccount(new Account(secondId, BigDecimal.valueOf(1000000000)));
        for (int i = 0; i < amount; i++) {
            UUID id = UUID.randomUUID();
            service.addAccount(new Account(id, BigDecimal.valueOf(1000000000)));
            ids.add(id);
        }
    }

    @Benchmark
    public void transfer() {
        service.transferMoney(firstId, secondId, BigDecimal.ONE);
    }

    @Benchmark
    public void transferMulty() {
        int i = random.nextInt(amount);
        service.transferMoney(ids.get(i), ids.get(amount - i - 1), BigDecimal.ONE);
    }
}
