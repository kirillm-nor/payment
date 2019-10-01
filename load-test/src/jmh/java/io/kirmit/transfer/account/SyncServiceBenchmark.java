package io.kirmit.transfer.account;

import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.service.SyncAccountService;
import java.math.BigDecimal;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
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
    private final UUID firstId = UUID.randomUUID();
    private final UUID secondId = UUID.randomUUID();

    @Setup(Level.Iteration)
    public void setup() {
        service = new SyncAccountService();
        service.addAccount(new Account(firstId, BigDecimal.valueOf(1000000000)));
        service.addAccount(new Account(secondId, BigDecimal.valueOf(1000000000)));
    }

    @Benchmark
    public void transfer() {
        service.transferMoney(firstId, secondId, BigDecimal.ONE);
    }
}
