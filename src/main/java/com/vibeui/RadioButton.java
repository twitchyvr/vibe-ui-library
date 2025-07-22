package com.vibeui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RadioButton extends Component<RadioButton> {
    private String text;
    private boolean selected = false;
    private ButtonGroup buttonGroup;
    private List<Consumer<Boolean>> changeHandlers = new ArrayList<>();

    public RadioButton() {
        super(new JRadioButton());
        setupChangeListener();
    }

    public RadioButton(String text) {
        this();
        text(text);
    }

    public RadioButton(String text, boolean selected) {
        this(text);
        selected(selected);
    }

    public RadioButton text(String text) {
        this.text = text;
        ((JRadioButton) swingComponent).setText(text);
        return this;
    }

    public RadioButton selected(boolean selected) {
        this.selected = selected;
        ((JRadioButton) swingComponent).setSelected(selected);
        return this;
    }

    public RadioButton group(ButtonGroup group) {
        this.buttonGroup = group;
        group.add((JRadioButton) swingComponent);
        return this;
    }

    public RadioButton onChange(Consumer<Boolean> handler) {
        changeHandlers.add(handler);
        return this;
    }

    public RadioButton onClick(ActionListener listener) {
        ((JRadioButton) swingComponent).addActionListener(listener);
        return this;
    }

    public RadioButton onClick(Runnable action) {
        return onClick(e -> action.run());
    }

    private void setupChangeListener() {
        ((JRadioButton) swingComponent).addActionListener(e -> {
            boolean currentState = ((JRadioButton) swingComponent).isSelected();
            changeHandlers.forEach(handler -> handler.accept(currentState));
        });
    }

    public boolean isSelected() {
        return ((JRadioButton) swingComponent).isSelected();
    }

    public String getText() {
        return text;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    @Override
    public RadioButton build() {
        return this;
    }

    public static RadioButton create() {
        return new RadioButton();
    }

    public static RadioButton create(String text) {
        return new RadioButton(text);
    }

    public static RadioButton create(String text, boolean selected) {
        return new RadioButton(text, selected);
    }

    public static ButtonGroup createGroup() {
        return new ButtonGroup();
    }
}