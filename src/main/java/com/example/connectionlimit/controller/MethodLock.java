package com.example.connectionlimit.controller;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MethodLock {
    private final Semaphore entrance = new Semaphore(0);
    private final Semaphore continuation = new Semaphore(0);

    public void enter() {
        entrance.release();
        try {
            continuation.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void proceed() {
        continuation.release();
    }

    public boolean waitUntilEntered(final long interval) throws InterruptedException {
        return entrance.tryAcquire(interval, TimeUnit.SECONDS);
    }
}
