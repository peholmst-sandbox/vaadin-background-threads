package com.example.application.resultonly;

import com.example.application.utils.UIAwareConsumer;
import com.example.application.utils.UIAwareErrorHandler;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;

@Route("job-with-result")
@SuppressWarnings("DuplicatedCode")
public class JobWithResultView extends VerticalLayout {

    private final Button start;
    private final ProgressBar progressBar;

    public JobWithResultView(JobWithResultTrigger trigger) {
        setWidth("400px");
        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        start = new Button("Start job", event -> {
            progressBar.setVisible(true);
            trigger.runJob()
                    .thenAccept(new UIAwareConsumer<>(this::onJobCompleted))
                    .exceptionally(new UIAwareErrorHandler(this::onJobFailed));
        });
        start.setDisableOnClick(true);
        add(progressBar, start);
    }

    private void onJobCompleted(String output) {
        Notification.show("Job completed: " + output, 3000, Notification.Position.MIDDLE);
        start.setEnabled(true);
        progressBar.setVisible(false);
    }

    private void onJobFailed(Throwable error) {
        var notification = new Notification("Job failed with error: " + error.getMessage());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(3000);
        notification.open();
        start.setEnabled(true);
        progressBar.setVisible(false);
    }
}
