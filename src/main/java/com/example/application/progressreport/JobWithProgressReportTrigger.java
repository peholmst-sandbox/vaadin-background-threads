package com.example.application.progressreport;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class JobWithProgressReportTrigger {

    private final JobWithProgressReport job;

    public JobWithProgressReportTrigger(JobWithProgressReport job) {
        this.job = job;
    }

    @Async
    public CompletableFuture<String> runJob(Consumer<Double> updateProgressCallback) {
        return CompletableFuture.completedFuture(job.startLongRunningJob(updateProgressCallback));
    }
}
