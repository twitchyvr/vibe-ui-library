package com.vibeui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TextField extends Component<TextField> {
    private String text;
    private String placeholder;
    private boolean editable = true;
    private List<Consumer<String>> changeHandlers = new ArrayList<>();

    public TextField() {
        super(new JTextField());
        setupChangeListener();
    }

    public TextField(String text) {
        this();
        text(text);
    }

    public TextField text(String text) {
        this.text = text;
        ((JTextField) swingComponent).setText(text);
        return this;
    }

    public TextField placeholder(String placeholder) {
        this.placeholder = placeholder;
        ((JTextField) swingComponent).setToolTipText(placeholder);
        return this;
    }

    public TextField editable(boolean editable) {
        this.editable = editable;
        ((JTextField) swingComponent).setEditable(editable);
        return this;
    }

    public TextField columns(int columns) {
        ((JTextField) swingComponent).setColumns(columns);
        return this;
    }

    public TextField onChange(Consumer<String> handler) {
        changeHandlers.add(handler);
        return this;
    }

    public TextField onFocus(FocusListener listener) {
        ((JTextField) swingComponent).addFocusListener(listener);
        return this;
    }

    private void setupChangeListener() {
        ((JTextField) swingComponent).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                notifyChangeHandlers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                notifyChangeHandlers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                notifyChangeHandlers();
            }
        });
    }

    private void notifyChangeHandlers() {
        String currentText = ((JTextField) swingComponent).getText();
        changeHandlers.forEach(handler -> handler.accept(currentText));
    }

    public String getText() {
        return ((JTextField) swingComponent).getText();
    }

    @Override
    public TextField build() {
        return this;
    }

    public static TextField create() {
        return new TextField();
    }

    public static TextField create(String text) {
        return new TextField(text);
    }
}