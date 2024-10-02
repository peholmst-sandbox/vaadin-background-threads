package com.example.application.progressreport;

import com.example.application.utils.UIAwareConsumer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;

@Route("job-with-progress-report-mono")
@SuppressWarnings("DuplicatedCode")
public class JobWithProgressReportMonoView extends VerticalLayout {

    private final Button start;

    public JobWithProgressReportMonoView(JobWithProgressReportTrigger trigger) {
        setWidth("400px");
        var progressBar = new ProgressBar();
        start = new Button("Start job", event -> {
            progressBar.setValue(0);
            var output = trigger.runJobAsMono();
            output.progress().subscribe(new UIAwareConsumer<>(progressBar::setValue));
            output.result().doOnError(new UIAwareConsumer<>(this::onJobFailed))
                    .subscribe(new UIAwareConsumer<>(this::onJobCompleted));
        });
        start.setDisableOnClick(true);
        add(progressBar, start);
    }

    private void onJobCompleted(String output) {
        Notification.show("Job completed: " + output, 3000, Notification.Position.MIDDLE);
        start.setEnabled(true);
    }

    private void onJobFailed(Throwable error) {
        var notification = new Notification("Job failed with error: " + error.getMessage());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(3000);
        notification.open();
        start.setEnabled(true);
    }
}
