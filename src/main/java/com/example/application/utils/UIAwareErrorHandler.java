package com.example.application.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;

import static java.util.Objects.requireNonNull;

public final class UIAwareErrorHandler implements SerializableFunction<Throwable, Void> {

    private final UI ui;
    private final SerializableConsumer<Throwable> delegate;

    public UIAwareErrorHandler(UI ui, SerializableConsumer<Throwable> delegate) {
        this.ui = requireNonNull(ui);
        this.delegate = requireNonNull(delegate);
    }

    public UIAwareErrorHandler(SerializableConsumer<Throwable> delegate) {
        this(UI.getCurrent(), delegate);
    }

    @Override
    public Void apply(Throwable throwable) {
        ui.access(() -> delegate.accept(throwable));
        return null;
    }
}
