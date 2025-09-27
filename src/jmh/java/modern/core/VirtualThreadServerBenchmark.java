package modern.core;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Microbenchmark that exercises {@link VirtualThreadServer} and {@link ChannelPipeline} under
 * a synthetic load representative of a message processing pipeline.  The benchmark performs a
 * decode-transform-encode cycle to simulate work done by the server for each request.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
public class VirtualThreadServerBenchmark {

    @Param({"128", "512", "2048"})
    public int payloadSize;

    private VirtualThreadServer server;
    private byte[] request;

    @Setup(Level.Trial)
    public void setUpBenchmark() {
        ChannelPipeline pipeline = ChannelPipeline.builder()
                .addLast(Base64.getDecoder()::decode)
                .addLast(new BusinessLogicHandler())
                .addLast(bytes -> Base64.getEncoder().encode(bytes))
                .build();
        server = new VirtualThreadServer(pipeline);
    }

    @Setup(Level.Iteration)
    public void prepareIteration() {
        byte[] payload = generatePayload(payloadSize);
        request = Base64.getEncoder().encode(payload);
    }

    @TearDown(Level.Trial)
    public void tearDownBenchmark() {
        if (server != null) {
            server.close();
        }
    }

    @Benchmark
    @Threads(32)
    public byte[] handleRequest() {
        return server.handle(request).join();
    }

    private static byte[] generatePayload(int size) {
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) ('a' + (i % 26));
        }
        return data;
    }

    private static final class BusinessLogicHandler implements ChannelPipeline.ChannelHandler {
        @Override
        public byte[] handle(byte[] payload) {
            CRC32 checksum = new CRC32();
            checksum.update(payload);
            long value = checksum.getValue();

            String message = new String(payload, StandardCharsets.UTF_8);
            String response = message.toUpperCase(Locale.ROOT) + ':' + Long.toHexString(value);

            byte[] encoded = response.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(encoded.length + Long.BYTES);
            buffer.put(encoded);
            buffer.putLong(value);
            return buffer.array();
        }
    }
}
