package io.kirmit.transfer.account;

import io.kirmit.transfer.account.model.AtomicAccount;
import io.kirmit.transfer.account.service.LockFreeAccountService;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
@Warmup(iterations = 10, time = 1, batchSize = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, batchSize = 2, timeUnit = TimeUnit.SECONDS)
@Fork(
    value = 1,
    jvmArgs = {
        "-server",
        "-Xms2g",
        "-Xmx2g",
        "-XX:NewSize=1g",
        "-XX:MaxNewSize=1g",
        "-XX:InitialCodeCacheSize=512m",
        "-XX:ReservedCodeCacheSize=512m",
        "-XX:+UseParallelGC",
        "-XX:+UnlockExperimentalVMOptions",
        "-XX:+TrustFinalNonStaticFields",
        "-XX:-UseBiasedLocking"
    }
)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class LockFreeServiceBenchmark {

    private LockFreeAccountService service;
    private final UUID firstId = UUID.randomUUID();
    private final UUID secondId = UUID.randomUUID();

    @Setup(Level.Iteration)
    public void setup() {
        service = new LockFreeAccountService();
        service.addAccount(new AtomicAccount(firstId, BigDecimal.valueOf(1000000000)));
        service.addAccount(new AtomicAccount(secondId, BigDecimal.valueOf(1000000000)));
    }

    @Benchmark
    public void transfer() {
        service.transferMoney(firstId, secondId, BigDecimal.ONE);
    }
}
