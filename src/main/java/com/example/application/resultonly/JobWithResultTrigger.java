package com.example.application.resultonly;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;

@Service
public class JobWithResultTrigger {

    private final JobWithResult job;
    private final TaskExecutor taskExecutor;

    public JobWithResultTrigger(JobWithResult job, TaskExecutor taskExecutor) {
        this.job = job;
        this.taskExecutor = taskExecutor;
    }

    @Async
    public CompletableFuture<String> runJob() {
        return CompletableFuture.completedFuture(job.startLongRunningJob());
    }

    public Mono<String> runJobAsMono() {
        return Mono.fromSupplier(job::startLongRunningJob).subscribeOn(Schedulers.fromExecutor(taskExecutor));
    }
}
