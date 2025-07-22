package com.vibeui;

import javax.swing.*;

public class Label extends Component<Label> {
    private String text;
    private int horizontalAlignment = SwingConstants.LEFT;

    public Label() {
        super(new JLabel());
    }

    public Label(String text) {
        this();
        text(text);
    }

    public Label text(String text) {
        this.text = text;
        ((JLabel) swingComponent).setText(text);
        return this;
    }

    public Label align(int alignment) {
        this.horizontalAlignment = alignment;
        ((JLabel) swingComponent).setHorizontalAlignment(alignment);
        return this;
    }

    public Label alignLeft() {
        return align(SwingConstants.LEFT);
    }

    public Label alignCenter() {
        return align(SwingConstants.CENTER);
    }

    public Label alignRight() {
        return align(SwingConstants.RIGHT);
    }

    public String getText() {
        return text;
    }

    @Override
    public Label build() {
        return this;
    }

    public static Label create() {
        return new Label();
    }

    public static Label create(String text) {
        return new Label(text);
    }
}