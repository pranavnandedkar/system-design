import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public final class FixedWindowRateLimiter {
    private final long limit;
    private final long windowMillis;
    private final ConcurrentHashMap<String, Counter> counters = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(long limit, Duration window) {
        this.limit = limit;
        this.windowMillis = window.toMillis();
    }

    public boolean allow(String key) {
        long now = System.currentTimeMillis();
        long window = now / windowMillis;
        Counter c = counters.compute(key, (k, old) -> {
            if (old == null || old.window != window) return new Counter(window, 1);
            return new Counter(old.window, old.count + 1);
        });
        return c.count <= limit;
    }

    private record Counter(long window, long count) {}
}
