package com.example.application.progressreport;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class JobWithProgressReportTrigger {

    private final JobWithProgressReport job;
    private final TaskExecutor taskExecutor;

    public JobWithProgressReportTrigger(JobWithProgressReport job,
                                        TaskExecutor taskExecutor) {
        this.job = job;
        this.taskExecutor = taskExecutor;
    }

    @Async
    public CompletableFuture<String> runJob(Consumer<Double> updateProgressCallback) {
        return CompletableFuture.completedFuture(job.startLongRunningJob(updateProgressCallback));
    }

    public Output runJobAsMono() {
        Sinks.Many<Double> progress = Sinks.many().unicast().onBackpressureBuffer();
        var result = Mono.fromSupplier(() -> job.startLongRunningJob(progress::tryEmitNext))
                .subscribeOn(Schedulers.fromExecutor(taskExecutor));
        return new Output(result, progress.asFlux());
    }

    public record Output(Mono<String> result, Flux<Double> progress) {
    }
}
