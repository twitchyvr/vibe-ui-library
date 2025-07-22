package com.vibeui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ComboBox extends Component<ComboBox> {
    private JComboBox<String> comboBox;
    private Consumer<String> changeHandler;
    private Consumer<String> selectionHandler;
    private List<String> items = new ArrayList<>();

    private ComboBox(String[] items) {
        super(new JComboBox<>(items));
        this.comboBox = (JComboBox<String>) swingComponent;
        this.items.addAll(Arrays.asList(items));
        setupEventHandlers();
    }

    private ComboBox(List<String> items) {
        super(new JComboBox<>(items.toArray(new String[0])));
        this.comboBox = (JComboBox<String>) swingComponent;
        this.items.addAll(items);
        setupEventHandlers();
    }

    public static ComboBox create(String... items) {
        return new ComboBox(items);
    }

    public static ComboBox create(List<String> items) {
        return new ComboBox(items);
    }

    public static ComboBox createEmpty() {
        return new ComboBox(new String[0]);
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    private void setupEventHandlers() {
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) comboBox.getSelectedItem();
                if (changeHandler != null) {
                    changeHandler.accept(selectedItem);
                }
                if (selectionHandler != null) {
                    selectionHandler.accept(selectedItem);
                }
            }
        });
    }

    public ComboBox onChange(Consumer<String> handler) {
        this.changeHandler = handler;
        return this;
    }

    public ComboBox onSelection(Consumer<String> handler) {
        this.selectionHandler = handler;
        return this;
    }

    public ComboBox addItem(String item) {
        items.add(item);
        comboBox.addItem(item);
        return this;
    }

    public ComboBox removeItem(String item) {
        items.remove(item);
        comboBox.removeItem(item);
        return this;
    }

    public ComboBox removeAllItems() {
        items.clear();
        comboBox.removeAllItems();
        return this;
    }

    public ComboBox selectedItem(String item) {
        comboBox.setSelectedItem(item);
        return this;
    }

    public ComboBox selectedIndex(int index) {
        if (index >= 0 && index < comboBox.getItemCount()) {
            comboBox.setSelectedIndex(index);
        }
        return this;
    }

    public ComboBox editable(boolean editable) {
        comboBox.setEditable(editable);
        return this;
    }

    public ComboBox maxRowCount(int rows) {
        comboBox.setMaximumRowCount(rows);
        return this;
    }

    public String getSelectedItem() {
        return (String) comboBox.getSelectedItem();
    }

    public int getSelectedIndex() {
        return comboBox.getSelectedIndex();
    }

    public int getItemCount() {
        return comboBox.getItemCount();
    }

    public String getItemAt(int index) {
        return comboBox.getItemAt(index);
    }

    public List<String> getAllItems() {
        return new ArrayList<>(items);
    }

    @Override
    public ComboBox build() {
        return this;
    }
}