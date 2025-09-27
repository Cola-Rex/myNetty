package modern.core;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Simple virtual-thread powered server abstraction that accepts byte payloads and feeds them
 * through a {@link ChannelPipeline}.  The implementation is intentionally lightweight so it can
 * be benchmarked in isolation.
 */
public final class VirtualThreadServer implements AutoCloseable {

    private final ExecutorService executor;
    private final ChannelPipeline pipeline;

    public VirtualThreadServer(ChannelPipeline pipeline) {
        this.pipeline = Objects.requireNonNull(pipeline, "pipeline");
        this.executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    }

    public CompletableFuture<byte[]> handle(byte[] payload) {
        Objects.requireNonNull(payload, "payload");
        byte[] copy = Arrays.copyOf(payload, payload.length);
        return CompletableFuture.supplyAsync(() -> pipeline.process(copy), executor);
    }

    @Override
    public void close() {
        close(Duration.ofSeconds(10));
    }

    public void close(Duration timeout) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }
}
