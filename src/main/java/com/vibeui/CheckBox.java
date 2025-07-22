package com.vibeui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CheckBox extends Component<CheckBox> {
    private String text;
    private boolean selected = false;
    private List<Consumer<Boolean>> changeHandlers = new ArrayList<>();

    public CheckBox() {
        super(new JCheckBox());
        setupChangeListener();
    }

    public CheckBox(String text) {
        this();
        text(text);
    }

    public CheckBox(String text, boolean selected) {
        this(text);
        selected(selected);
    }

    public CheckBox text(String text) {
        this.text = text;
        ((JCheckBox) swingComponent).setText(text);
        return this;
    }

    public CheckBox selected(boolean selected) {
        this.selected = selected;
        ((JCheckBox) swingComponent).setSelected(selected);
        return this;
    }

    public CheckBox onChange(Consumer<Boolean> handler) {
        changeHandlers.add(handler);
        return this;
    }

    public CheckBox onClick(ActionListener listener) {
        ((JCheckBox) swingComponent).addActionListener(listener);
        return this;
    }

    public CheckBox onClick(Runnable action) {
        return onClick(e -> action.run());
    }

    private void setupChangeListener() {
        ((JCheckBox) swingComponent).addActionListener(e -> {
            boolean currentState = ((JCheckBox) swingComponent).isSelected();
            changeHandlers.forEach(handler -> handler.accept(currentState));
        });
    }

    public boolean isSelected() {
        return ((JCheckBox) swingComponent).isSelected();
    }

    public String getText() {
        return text;
    }

    @Override
    public CheckBox build() {
        return this;
    }

    public static CheckBox create() {
        return new CheckBox();
    }

    public static CheckBox create(String text) {
        return new CheckBox(text);
    }

    public static CheckBox create(String text, boolean selected) {
        return new CheckBox(text, selected);
    }
}