package com.example.application.utils;

import com.vaadin.flow.component.UI;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public final class UIAwareConsumer<T> implements Consumer<T> {

    private final UI ui;
    private final Consumer<T> delegate;

    public UIAwareConsumer(UI ui, Consumer<T> delegate) {
        this.ui = requireNonNull(ui);
        this.delegate = requireNonNull(delegate);
    }

    public UIAwareConsumer(Consumer<T> delegate) {
        this(UI.getCurrent(), delegate);
    }

    @Override
    public void accept(T t) {
        ui.access(() -> delegate.accept(t));
    }
}
