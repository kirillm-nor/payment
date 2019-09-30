package io.kirmit.transfer.account;

import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.model.FinalAccount;
import io.kirmit.transfer.account.service.MergeAccountService;
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

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
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
public class MergeServiceBenchmark {

    private MergeAccountService service;
    private final UUID firstId = UUID.randomUUID();
    private final UUID secondId = UUID.randomUUID();

    @Setup(Level.Iteration)
    public void setup() {
        service = new MergeAccountService();
        service.addAccount(new FinalAccount(firstId, BigDecimal.valueOf(1000000000)));
        service.addAccount(new FinalAccount(secondId, BigDecimal.valueOf(1000000000)));
    }

    @Benchmark
    public void transfer() {
        service.transferMoney(firstId, secondId, BigDecimal.ONE);
    }


}
