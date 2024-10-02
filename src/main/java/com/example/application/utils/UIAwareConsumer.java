package com.example.application.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.SerializableConsumer;

import static java.util.Objects.requireNonNull;

public final class UIAwareConsumer<T> implements SerializableConsumer<T> {

    private final UI ui;
    private final SerializableConsumer<T> delegate;

    public UIAwareConsumer(UI ui, SerializableConsumer<T> delegate) {
        this.ui = requireNonNull(ui);
        this.delegate = requireNonNull(delegate);
    }

    public UIAwareConsumer(SerializableConsumer<T> delegate) {
        this(UI.getCurrent(), delegate);
    }

    @Override
    public void accept(T t) {
        ui.access(() -> delegate.accept(t));
    }
}
