# myNetty

Experimental playground for Netty-inspired networking experiments and microservices sketches.

## Building

The project uses Maven. A standard build can be triggered with:

```bash
mvn clean install
```

## Performance

A JMH microbenchmark is available to exercise the `modern.core.VirtualThreadServer` and
`ChannelPipeline` under synthetic load. Run the benchmark with the dedicated Maven profile:

```bash
mvn -Pjmh clean install
```

Benchmark configuration:

- Java 21.0.2 (virtual-thread support) with JMH 1.37.
- Host JVM execution (`-f 0`) to accommodate the container environment; treat the results as
  indicative rather than absolute.
- 32 worker threads to emulate a busy request fan-in.
- Per-iteration payload regenerated at sizes 128, 512, and 2048 bytes.

Hardware snapshot (`lscpu`): Intel(R) Xeon(R) Platinum 8370C CPU @ 2.80GHz, 5 vCPUs on the test VM.

### Throughput results (ops/s)

| Payload (bytes) | Mean throughput | 99.9% CI | Notes |
|-----------------|-----------------|----------|-------|
| 128             | 319,676         | ±64,236  | Warm payload transformation mix; host VM run |
| 512             | 287,765         | ±84,277  | Same configuration |
| 2048            | 169,301         | ±24,705  | Same configuration |

Command output with the full JMH log is captured in the repository history for reference.
