package com.example.application.progressreport;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.function.Consumer;

@Component
public class JobWithProgressReport {

    private final Random rnd = new Random();

    public String startLongRunningJob(Consumer<Double> updateProgressCallback) {
        System.out.println(getClass().getSimpleName() + " running in thread " + Thread.currentThread());
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 10; ++i) {
            try {
                sb.append(i).append("...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (rnd.nextInt(20) == 0) {
                throw new RuntimeException("Randomly failed, just for fun");
            }
            updateProgressCallback.accept(i / 10d);
        }
        return sb.toString();
    }
}
