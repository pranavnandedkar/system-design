import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

public final class SlidingWindowLogRateLimiter {
    private final int limit;
    private final long windowMillis;
    private final ConcurrentHashMap<String, Deque<Long>> logs = new ConcurrentHashMap<>();

    public SlidingWindowLogRateLimiter(int limit, Duration window) {
        this.limit = limit;
        this.windowMillis = window.toMillis();
    }

    public boolean allow(String key) {
        long now = System.currentTimeMillis();
        Deque<Long> q = logs.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (q) {
            long cutoff = now - windowMillis;
            while (!q.isEmpty() && q.peekFirst() <= cutoff) q.removeFirst();
            if (q.size() >= limit) return false;
            q.addLast(now);
            return true;
        }
    }
}
