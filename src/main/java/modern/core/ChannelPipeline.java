package modern.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A very small pipeline abstraction that mirrors the behaviour of Netty's channel pipeline
 * in a way that is suitable for microbenchmarking.  Each handler transforms the inbound
 * byte payload and hands it to the next handler.
 */
public final class ChannelPipeline {

    private final List<ChannelHandler> handlers;

    private ChannelPipeline(List<ChannelHandler> handlers) {
        this.handlers = handlers;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Processes the provided payload through all handlers in the pipeline.
     *
     * @param payload data to process
     * @return the transformed payload produced by the last handler
     */
    public byte[] process(byte[] payload) {
        Objects.requireNonNull(payload, "payload");
        byte[] current = payload;
        for (ChannelHandler handler : handlers) {
            current = Objects.requireNonNull(handler.handle(current),
                    () -> handler.getClass().getSimpleName() + " returned null");
        }
        return current;
    }

    public interface ChannelHandler {
        byte[] handle(byte[] payload);
    }

    public static final class Builder {
        private final List<ChannelHandler> handlers = new ArrayList<>();

        private Builder() {
        }

        public Builder addLast(ChannelHandler handler) {
            handlers.add(Objects.requireNonNull(handler, "handler"));
            return this;
        }

        public ChannelPipeline build() {
            if (handlers.isEmpty()) {
                throw new IllegalStateException("Pipeline requires at least one handler");
            }
            return new ChannelPipeline(Collections.unmodifiableList(new ArrayList<>(handlers)));
        }
    }
}
