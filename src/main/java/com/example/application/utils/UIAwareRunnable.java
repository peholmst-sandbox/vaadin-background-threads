package com.example.application.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.SerializableRunnable;

import static java.util.Objects.requireNonNull;

public final class UIAwareRunnable implements SerializableRunnable {

    private final UI ui;
    private final SerializableRunnable delegate;

    public UIAwareRunnable(UI ui, SerializableRunnable delegate) {
        this.ui = requireNonNull(ui);
        this.delegate = requireNonNull(delegate);
    }

    public UIAwareRunnable(SerializableRunnable delegate) {
        this(UI.getCurrent(), delegate);
    }

    @Override
    public void run() {
        ui.access(delegate::run);
    }
}
