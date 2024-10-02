package com.example.application.resultonly;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Random;

@Component
public class JobWithResult {

    private final Random rnd = new Random();

    public String startLongRunningJob() {
        System.out.println(getClass().getSimpleName() + " running in thread " + Thread.currentThread());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (rnd.nextInt(5) == 0) {
            throw new RuntimeException("Randomly failed, just for fun");
        }
        return "I'm finished and the current time is " + ZonedDateTime.now();
    }
}
