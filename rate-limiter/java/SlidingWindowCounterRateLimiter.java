public final class SlidingWindowCounterRateLimiter {
    private final long limit;
    private final long windowMillis;

    private long currentWindow = -1;
    private long currentCount;
    private long previousCount;

    public SlidingWindowCounterRateLimiter(long limit, long windowMillis) {
        this.limit = limit;
        this.windowMillis = windowMillis;
    }

    public synchronized boolean allow() {
        long now = System.currentTimeMillis();
        long nowWindow = now / windowMillis;

        if (currentWindow == -1) currentWindow = nowWindow;
        else if (nowWindow != currentWindow) {
            previousCount = currentCount;
            currentCount = 0;
            currentWindow = nowWindow;
        }

        double elapsedFraction = (now % windowMillis) / (double) windowMillis;
        double estimated = previousCount * (1.0 - elapsedFraction) + currentCount;

        if (estimated >= limit) return false;
        currentCount++;
        return true;
    }
}
