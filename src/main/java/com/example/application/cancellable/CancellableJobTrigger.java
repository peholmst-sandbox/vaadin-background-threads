package com.example.application.cancellable;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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
}
