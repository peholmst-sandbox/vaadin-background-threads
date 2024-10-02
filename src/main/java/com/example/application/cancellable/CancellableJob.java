package com.example.application.cancellable;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.function.Supplier;

@Component
public class CancellableJob {

    private final Random rnd = new Random();

    public void startLongRunningJob(Supplier<Boolean> isCancelled) {
        System.out.println(getClass().getSimpleName() + " running in thread " + Thread.currentThread());
        for (int i = 1; i <= 10; ++i) {
            if (isCancelled.get()) {
                System.out.println(getClass().getSimpleName() + " got cancelled");
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (rnd.nextInt(20) == 0) {
                throw new RuntimeException("Randomly failed, just for fun");
            }
        }
    }
}
