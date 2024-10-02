package com.example.application.utils;

import com.vaadin.flow.component.UI;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class UIAwareErrorHandler implements Function<Throwable, Void> {

    private final UI ui;
    private final Consumer<Throwable> delegate;

    public UIAwareErrorHandler(UI ui, Consumer<Throwable> delegate) {
        this.ui = requireNonNull(ui);
        this.delegate = requireNonNull(delegate);
    }

    public  UIAwareErrorHandler(Consumer<Throwable> delegate) {
        this(UI.getCurrent(), delegate);
    }

    @Override
    public Void apply(Throwable throwable) {
        ui.access(() -> delegate.accept(throwable));
        return null;
    }
}
