package com.example.application.resultonly;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class JobWithResultTrigger {

    private final JobWithResult job;

    public JobWithResultTrigger(JobWithResult job) {
        this.job = job;
    }

    @Async
    public CompletableFuture<String> runJob() {
        return CompletableFuture.completedFuture(job.startLongRunningJob());
    }
}
