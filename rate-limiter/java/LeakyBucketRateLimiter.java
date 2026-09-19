import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;

public final class LeakyBucketRateLimiter {
    private final int queueCapacity;
    private final long intervalMillis;
    private final Queue<Long> queue = new ArrayDeque<>();
    private long nextLeakAt;

    public LeakyBucketRateLimiter(int queueCapacity, Duration outputInterval) {
        this.queueCapacity = queueCapacity;
        this.intervalMillis = outputInterval.toMillis();
    }

    public synchronized boolean submit(long requestId) {
        drain(System.currentTimeMillis());
        if (queue.size() >= queueCapacity) return false;
        queue.add(requestId);
        return true;
    }

    public synchronized Long pollReady() {
        drain(System.currentTimeMillis());
        return queue.poll();
    }

    private void drain(long now) {
        if (queue.isEmpty()) {
            nextLeakAt = now;
            return;
        }
        if (now >= nextLeakAt) nextLeakAt = now + intervalMillis;
    }
}
