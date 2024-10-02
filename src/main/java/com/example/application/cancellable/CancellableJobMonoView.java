package com.example.application.cancellable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import reactor.core.Disposable;

@Route("cancellable-job-mono")
@SuppressWarnings("DuplicatedCode")
public class CancellableJobMonoView extends VerticalLayout {

    private final CancellableJobTrigger trigger;
    private final Button start;
    private final ProgressBar progressBar;
    private final Button cancel;

    private Disposable job;

    public CancellableJobMonoView(CancellableJobTrigger trigger) {
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
        job = trigger.runJobAsMono()
                .doOnError(UI.getCurrent().accessLater(this::onJobFailed, null))
                .doOnSuccess(UI.getCurrent().accessLater(this::onJobCompleted, null)) // Also called if the job is cancelled
                .subscribe();
        progressBar.setVisible(true);
        cancel.setVisible(true);
        cancel.setEnabled(true);
    }

    private void cancelJob() {
        if (job != null) {
            job.dispose();
        }
    }

    private void onJobCompleted(Void ignore) {
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
