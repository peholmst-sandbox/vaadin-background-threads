package com.example.application.utils;

import com.vaadin.flow.component.UI;

import static java.util.Objects.requireNonNull;

public final class UIAwareRunnable implements Runnable {

    private final UI ui;
    private final Runnable delegate;

    public UIAwareRunnable(UI ui, Runnable delegate) {
        this.ui = requireNonNull(ui);
        this.delegate = requireNonNull(delegate);
    }

    public UIAwareRunnable(Runnable delegate) {
        this(UI.getCurrent(), delegate);
    }

    @Override
    public void run() {
        ui.access(delegate::run);
    }
}
