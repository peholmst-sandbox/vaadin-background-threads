package com.example.application.resultonly;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;

@Route("job-with-result-mono")
@SuppressWarnings("DuplicatedCode")
public class JobWithResultMonoView extends VerticalLayout {

    private final Button start;
    private final ProgressBar progressBar;

    public JobWithResultMonoView(JobWithResultTrigger trigger) {
        setWidth("400px");
        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        start = new Button("Start job", event -> {
            progressBar.setVisible(true);
            trigger.runJobAsMono()
                    .doOnError(UI.getCurrent().accessLater(this::onJobFailed, null))
                    .subscribe(UI.getCurrent().accessLater(this::onJobCompleted, null));
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
