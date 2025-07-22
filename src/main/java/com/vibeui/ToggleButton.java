package com.vibeui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ToggleButton extends Component<ToggleButton> {
    private String text;
    private boolean selected = false;
    private List<Consumer<Boolean>> changeHandlers = new ArrayList<>();

    public ToggleButton() {
        super(new JToggleButton());
        setupChangeListener();
    }

    public ToggleButton(String text) {
        this();
        text(text);
    }

    public ToggleButton(String text, boolean selected) {
        this(text);
        selected(selected);
    }

    public ToggleButton text(String text) {
        this.text = text;
        ((JToggleButton) swingComponent).setText(text);
        return this;
    }

    public ToggleButton selected(boolean selected) {
        this.selected = selected;
        ((JToggleButton) swingComponent).setSelected(selected);
        return this;
    }

    public ToggleButton onChange(Consumer<Boolean> handler) {
        changeHandlers.add(handler);
        return this;
    }

    public ToggleButton onClick(ActionListener listener) {
        ((JToggleButton) swingComponent).addActionListener(listener);
        return this;
    }

    public ToggleButton onClick(Runnable action) {
        return onClick(e -> action.run());
    }

    private void setupChangeListener() {
        ((JToggleButton) swingComponent).addActionListener(e -> {
            boolean currentState = ((JToggleButton) swingComponent).isSelected();
            changeHandlers.forEach(handler -> handler.accept(currentState));
        });
    }

    public boolean isSelected() {
        return ((JToggleButton) swingComponent).isSelected();
    }

    public String getText() {
        return text;
    }

    @Override
    public ToggleButton build() {
        return this;
    }

    public static ToggleButton create() {
        return new ToggleButton();
    }

    public static ToggleButton create(String text) {
        return new ToggleButton(text);
    }

    public static ToggleButton create(String text, boolean selected) {
        return new ToggleButton(text, selected);
    }
}