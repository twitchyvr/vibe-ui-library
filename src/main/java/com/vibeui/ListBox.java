package com.vibeui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ListBox extends Component<ListBox> {
    private JList<String> listBox;
    private DefaultListModel<String> listModel;
    private JScrollPane scrollPane;
    private Consumer<String> selectionHandler;
    private Consumer<List<String>> multiSelectionHandler;
    private Consumer<Integer> indexSelectionHandler;

    private ListBox(String[] items) {
        super(createScrollableList(items));
        initializeComponents();
        setupEventHandlers();
    }

    private ListBox(List<String> items) {
        this(items.toArray(new String[0]));
    }

    private static JScrollPane createScrollableList(String[] items) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String item : items) {
            model.addElement(item);
        }
        JList<String> list = new JList<>(model);
        return new JScrollPane(list);
    }

    private void initializeComponents() {
        this.scrollPane = (JScrollPane) swingComponent;
        this.listBox = (JList<String>) scrollPane.getViewport().getView();
        this.listModel = (DefaultListModel<String>) listBox.getModel();
    }

    public static ListBox create(String... items) {
        return new ListBox(items);
    }

    public static ListBox create(List<String> items) {
        return new ListBox(items);
    }

    public static ListBox createEmpty() {
        return new ListBox(new String[0]);
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    private void setupEventHandlers() {
        listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        listBox.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (selectionHandler != null) {
                        String selected = listBox.getSelectedValue();
                        selectionHandler.accept(selected);
                    }
                    
                    if (multiSelectionHandler != null) {
                        List<String> selected = listBox.getSelectedValuesList();
                        multiSelectionHandler.accept(selected);
                    }
                    
                    if (indexSelectionHandler != null) {
                        int index = listBox.getSelectedIndex();
                        indexSelectionHandler.accept(index);
                    }
                }
            }
        });
    }

    public ListBox onSelection(Consumer<String> handler) {
        this.selectionHandler = handler;
        return this;
    }

    public ListBox onMultiSelection(Consumer<List<String>> handler) {
        this.multiSelectionHandler = handler;
        return this;
    }

    public ListBox onIndexSelection(Consumer<Integer> handler) {
        this.indexSelectionHandler = handler;
        return this;
    }

    public ListBox selectionMode(int mode) {
        listBox.setSelectionMode(mode);
        return this;
    }

    public ListBox singleSelection() {
        return selectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public ListBox multipleSelection() {
        return selectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public ListBox contiguousSelection() {
        return selectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    }

    public ListBox addItem(String item) {
        listModel.addElement(item);
        return this;
    }

    public ListBox addItems(String... items) {
        for (String item : items) {
            listModel.addElement(item);
        }
        return this;
    }

    public ListBox addItems(List<String> items) {
        for (String item : items) {
            listModel.addElement(item);
        }
        return this;
    }

    public ListBox removeItem(String item) {
        listModel.removeElement(item);
        return this;
    }

    public ListBox removeItemAt(int index) {
        if (index >= 0 && index < listModel.size()) {
            listModel.remove(index);
        }
        return this;
    }

    public ListBox removeAllItems() {
        listModel.clear();
        return this;
    }

    public ListBox selectedIndex(int index) {
        if (index >= 0 && index < listModel.size()) {
            listBox.setSelectedIndex(index);
        }
        return this;
    }

    public ListBox selectedIndices(int... indices) {
        listBox.setSelectedIndices(indices);
        return this;
    }

    public ListBox selectedItem(String item) {
        listBox.setSelectedValue(item, true);
        return this;
    }

    public ListBox visibleRowCount(int rows) {
        listBox.setVisibleRowCount(rows);
        return this;
    }

    public ListBox fixedCellHeight(int height) {
        listBox.setFixedCellHeight(height);
        return this;
    }

    public ListBox fixedCellWidth(int width) {
        listBox.setFixedCellWidth(width);
        return this;
    }

    public ListBox horizontalScrollPolicy(int policy) {
        scrollPane.setHorizontalScrollBarPolicy(policy);
        return this;
    }

    public ListBox verticalScrollPolicy(int policy) {
        scrollPane.setVerticalScrollBarPolicy(policy);
        return this;
    }

    public ListBox autoScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public ListBox autoScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public ListBox alwaysScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    public ListBox alwaysScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public ListBox neverScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public ListBox neverScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }

    public String getSelectedItem() {
        return listBox.getSelectedValue();
    }

    public List<String> getSelectedItems() {
        return listBox.getSelectedValuesList();
    }

    public int getSelectedIndex() {
        return listBox.getSelectedIndex();
    }

    public int[] getSelectedIndices() {
        return listBox.getSelectedIndices();
    }

    public int getItemCount() {
        return listModel.size();
    }

    public String getItemAt(int index) {
        if (index >= 0 && index < listModel.size()) {
            return listModel.getElementAt(index);
        }
        return null;
    }

    public List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            items.add(listModel.getElementAt(i));
        }
        return items;
    }

    @Override
    public ListBox build() {
        return this;
    }
}