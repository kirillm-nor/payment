package io.kirmit.transfer.account;

import io.kirmit.transfer.account.service.SyncAccountService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 15, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 15, time = 1, timeUnit = TimeUnit.SECONDS)
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SyncServiceBenchmark {

    private final SyncAccountService service = new SyncAccountService();
    private final UUID id = UUID.randomUUID();

    @Setup
    public void setup() {
    }

    @Benchmark
    public void findService(Blackhole blackhole) {
        blackhole.consume(service.findAccount(id));
    }
}
