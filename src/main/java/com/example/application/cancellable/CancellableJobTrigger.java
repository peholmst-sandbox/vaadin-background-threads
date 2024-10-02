package com.example.application.cancellable;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CancellableJobTrigger {

    private final CancellableJob job;
    private final TaskExecutor taskExecutor;

    public CancellableJobTrigger(CancellableJob job, TaskExecutor taskExecutor) {
        this.job = job;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<Void> runJob() {
        var future = new CompletableFuture<Void>();
        taskExecutor.execute(() -> {
            try {
                job.startLongRunningJob(future::isCancelled);
                future.complete(null);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public Mono<Void> runJobAsMono() {
        var cancelled = new AtomicBoolean(false);
        return Mono.fromRunnable(() -> job.startLongRunningJob(cancelled::get))
                .doOnCancel(() -> cancelled.set(true))
                .subscribeOn(Schedulers.fromExecutor(taskExecutor))
                .then();
    }
}
