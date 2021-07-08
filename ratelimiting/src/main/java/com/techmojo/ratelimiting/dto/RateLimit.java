package com.techmojo.ratelimiting.dto;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RateLimit {
	private Semaphore semaphore;
    private int maxPermits;
    private TimeUnit timePeriod;
    private ScheduledExecutorService scheduler;

    public static RateLimit create(int permits, TimeUnit timePeriod) {
        RateLimit limiter = new RateLimit(permits, timePeriod);
        limiter.schedulePermitReplenishment();
        return limiter;
    }

    private RateLimit(int permits, TimeUnit timePeriod) {
        this.semaphore = new Semaphore(permits);
        this.maxPermits = permits;
        this.timePeriod = timePeriod;
    }

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    public void schedulePermitReplenishment() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            semaphore.release(maxPermits - semaphore.availablePermits());
        }, 1, timePeriod);

    }
}
