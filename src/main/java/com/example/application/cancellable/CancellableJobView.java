package com.example.application.cancellable;

import com.example.application.utils.UIAwareErrorHandler;
import com.example.application.utils.UIAwareRunnable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;

import java.util.concurrent.CompletableFuture;

@Route("cancellable-job")
@SuppressWarnings("DuplicatedCode")
public class CancellableJobView extends VerticalLayout {

    private final CancellableJobTrigger trigger;
    private final Button start;
    private final ProgressBar progressBar;
    private final Button cancel;

    private CompletableFuture<Void> job;

    public CancellableJobView(CancellableJobTrigger trigger) {
        this.trigger = trigger;

        setWidth("400px");
        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        start = new Button("Start job", event -> startJob());
        start.setDisableOnClick(true);
        cancel = new Button("Cancel job", event -> cancelJob());
        cancel.setDisableOnClick(true);
        cancel.setVisible(false);
        add(progressBar, start, cancel);
    }

    private void startJob() {
        job = trigger.runJob();
        progressBar.setVisible(true);
        cancel.setVisible(true);
        cancel.setEnabled(true);
        job.thenRun(new UIAwareRunnable(this::onJobCompleted))
                .exceptionally(new UIAwareErrorHandler(this::onJobFailed));
    }

    private void cancelJob() {
        if (job != null) {
            job.cancel(true);
        }
    }

    private void onJobCompleted() {
        Notification.show("Job completed", 3000, Notification.Position.MIDDLE);
        start.setEnabled(true);
        cancel.setVisible(false);
        progressBar.setVisible(false);
    }

    private void onJobFailed(Throwable error) {
        var notification = new Notification("Job failed with error: " + error.getMessage());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(3000);
        notification.open();
        start.setEnabled(true);
        cancel.setVisible(false);
        progressBar.setVisible(false);
    }
}
