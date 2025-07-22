package com.vibeui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TextBox extends Component<TextBox> {
    private String text;
    private String placeholder;
    private boolean editable = true;
    private boolean multiline = false;
    private List<Consumer<String>> changeHandlers = new ArrayList<>();
    private JScrollPane scrollPane;

    public TextBox() {
        this(false);
    }

    public TextBox(boolean multiline) {
        super(multiline ? new JTextArea() : new JTextField());
        this.multiline = multiline;
        
        if (multiline) {
            scrollPane = new JScrollPane((JTextArea) swingComponent);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        
        setupChangeListener();
    }

    public TextBox(String text) {
        this();
        text(text);
    }

    public TextBox(String text, boolean multiline) {
        this(multiline);
        text(text);
    }

    public TextBox text(String text) {
        this.text = text;
        if (multiline) {
            ((JTextArea) swingComponent).setText(text);
        } else {
            ((JTextField) swingComponent).setText(text);
        }
        return this;
    }

    public TextBox placeholder(String placeholder) {
        this.placeholder = placeholder;
        if (!multiline) {
            ((JTextField) swingComponent).setToolTipText(placeholder);
        } else {
            ((JTextArea) swingComponent).setToolTipText(placeholder);
        }
        return this;
    }

    public TextBox editable(boolean editable) {
        this.editable = editable;
        if (multiline) {
            ((JTextArea) swingComponent).setEditable(editable);
        } else {
            ((JTextField) swingComponent).setEditable(editable);
        }
        return this;
    }

    public TextBox rows(int rows) {
        if (multiline) {
            ((JTextArea) swingComponent).setRows(rows);
        }
        return this;
    }

    public TextBox columns(int columns) {
        if (multiline) {
            ((JTextArea) swingComponent).setColumns(columns);
        } else {
            ((JTextField) swingComponent).setColumns(columns);
        }
        return this;
    }

    public TextBox wordWrap(boolean wrap) {
        if (multiline) {
            ((JTextArea) swingComponent).setWrapStyleWord(wrap);
            ((JTextArea) swingComponent).setLineWrap(wrap);
        }
        return this;
    }

    public TextBox scrollBars(boolean vertical, boolean horizontal) {
        if (multiline && scrollPane != null) {
            scrollPane.setVerticalScrollBarPolicy(
                vertical ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER
            );
            scrollPane.setHorizontalScrollBarPolicy(
                horizontal ? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED : JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
        }
        return this;
    }

    public TextBox onChange(Consumer<String> handler) {
        changeHandlers.add(handler);
        return this;
    }

    public TextBox onFocus(FocusListener listener) {
        swingComponent.addFocusListener(listener);
        return this;
    }

    private void setupChangeListener() {
        DocumentListener docListener = new DocumentListener() {
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
        };

        if (multiline) {
            ((JTextArea) swingComponent).getDocument().addDocumentListener(docListener);
        } else {
            ((JTextField) swingComponent).getDocument().addDocumentListener(docListener);
        }
    }

    private void notifyChangeHandlers() {
        String currentText = getText();
        changeHandlers.forEach(handler -> handler.accept(currentText));
    }

    public String getText() {
        if (multiline) {
            return ((JTextArea) swingComponent).getText();
        } else {
            return ((JTextField) swingComponent).getText();
        }
    }

    @Override
    public void addTo(Container parent) {
        if (multiline && scrollPane != null) {
            parent.add(scrollPane);
        } else {
            super.addTo(parent);
        }
    }

    @Override
    public void addTo(Component<?> parent) {
        if (multiline && scrollPane != null) {
            parent.children.add(this);
            if (parent.getSwingComponent() instanceof Container) {
                ((Container) parent.getSwingComponent()).add(scrollPane);
            }
        } else {
            super.addTo(parent);
        }
    }

    @Override
    public JComponent getSwingComponent() {
        return multiline && scrollPane != null ? scrollPane : swingComponent;
    }

    @Override
    public TextBox build() {
        return this;
    }

    public static TextBox create() {
        return new TextBox();
    }

    public static TextBox create(String text) {
        return new TextBox(text);
    }

    public static TextBox createMultiline() {
        return new TextBox(true);
    }

    public static TextBox createMultiline(String text) {
        return new TextBox(text, true);
    }
}