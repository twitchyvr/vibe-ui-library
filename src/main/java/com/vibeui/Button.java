package com.vibeui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Button extends Component<Button> {
    private String text;
    private List<ActionListener> clickHandlers = new ArrayList<>();

    public Button() {
        super(new JButton());
    }

    public Button(String text) {
        this();
        text(text);
    }

    public Button text(String text) {
        this.text = text;
        ((JButton) swingComponent).setText(text);
        return this;
    }

    public Button onClick(ActionListener handler) {
        clickHandlers.add(handler);
        ((JButton) swingComponent).addActionListener(handler);
        return this;
    }

    public Button onClick(Runnable action) {
        return onClick(e -> action.run());
    }

    public String getText() {
        return text;
    }

    @Override
    public Button build() {
        return this;
    }

    public static Button create() {
        return new Button();
    }

    public static Button create(String text) {
        return new Button(text);
    }
}