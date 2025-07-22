package com.vibeui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Table extends Component<Table> {
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private Consumer<Integer> rowSelectionHandler;
    private Consumer<List<Integer>> multiRowSelectionHandler;
    private Consumer<TableData> cellClickHandler;
    private Consumer<TableData> cellDoubleClickHandler;
    private List<String> columnNames = new ArrayList<>();

    private Table(String[] columnNames) {
        super(createScrollableTable(columnNames));
        this.columnNames.addAll(Arrays.asList(columnNames));
        initializeComponents();
        setupEventHandlers();
    }

    private static JScrollPane createScrollableTable(String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        return new JScrollPane(table);
    }

    private void initializeComponents() {
        this.scrollPane = (JScrollPane) swingComponent;
        this.table = (JTable) scrollPane.getViewport().getView();
        this.tableModel = (DefaultTableModel) table.getModel();
    }

    public static Table create(String... columnNames) {
        return new Table(columnNames);
    }

    public static Table createEmpty() {
        return new Table(new String[0]);
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    private void setupEventHandlers() {
        // Row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (rowSelectionHandler != null) {
                        int selectedRow = table.getSelectedRow();
                        rowSelectionHandler.accept(selectedRow);
                    }
                    
                    if (multiRowSelectionHandler != null) {
                        int[] selectedRows = table.getSelectedRows();
                        List<Integer> rows = new ArrayList<>();
                        for (int row : selectedRows) {
                            rows.add(row);
                        }
                        multiRowSelectionHandler.accept(rows);
                    }
                }
            }
        });

        // Mouse listener for cell clicks
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                
                if (row >= 0 && column >= 0) {
                    Object value = table.getValueAt(row, column);
                    String columnName = table.getColumnName(column);
                    TableData cellData = new TableData(row, column, columnName, value);
                    
                    if (e.getClickCount() == 1 && cellClickHandler != null) {
                        cellClickHandler.accept(cellData);
                    } else if (e.getClickCount() == 2 && cellDoubleClickHandler != null) {
                        cellDoubleClickHandler.accept(cellData);
                    }
                }
            }
        });
    }

    public Table onRowSelection(Consumer<Integer> handler) {
        this.rowSelectionHandler = handler;
        return this;
    }

    public Table onMultiRowSelection(Consumer<List<Integer>> handler) {
        this.multiRowSelectionHandler = handler;
        return this;
    }

    public Table onCellClick(Consumer<TableData> handler) {
        this.cellClickHandler = handler;
        return this;
    }

    public Table onCellDoubleClick(Consumer<TableData> handler) {
        this.cellDoubleClickHandler = handler;
        return this;
    }

    // Column management
    public Table addColumn(String columnName) {
        columnNames.add(columnName);
        tableModel.addColumn(columnName);
        return this;
    }

    public Table removeColumn(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < columnNames.size()) {
            columnNames.remove(columnIndex);
            TableColumn column = table.getColumnModel().getColumn(columnIndex);
            table.getColumnModel().removeColumn(column);
        }
        return this;
    }

    public Table removeColumn(String columnName) {
        int index = columnNames.indexOf(columnName);
        if (index >= 0) {
            removeColumn(index);
        }
        return this;
    }

    public Table setColumnWidth(int columnIndex, int width) {
        if (columnIndex >= 0 && columnIndex < table.getColumnCount()) {
            TableColumn column = table.getColumnModel().getColumn(columnIndex);
            column.setPreferredWidth(width);
            column.setWidth(width);
        }
        return this;
    }

    public Table setColumnWidth(String columnName, int width) {
        int index = getColumnIndex(columnName);
        if (index >= 0) {
            setColumnWidth(index, width);
        }
        return this;
    }

    public Table setColumnResizable(int columnIndex, boolean resizable) {
        if (columnIndex >= 0 && columnIndex < table.getColumnCount()) {
            TableColumn column = table.getColumnModel().getColumn(columnIndex);
            column.setResizable(resizable);
        }
        return this;
    }

    // Row management
    public Table addRow(Object... values) {
        tableModel.addRow(values);
        return this;
    }

    public Table addRows(Object[][] rows) {
        for (Object[] row : rows) {
            addRow(row);
        }
        return this;
    }

    public Table insertRow(int index, Object... values) {
        tableModel.insertRow(index, values);
        return this;
    }

    public Table removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < tableModel.getRowCount()) {
            tableModel.removeRow(rowIndex);
        }
        return this;
    }

    public Table removeAllRows() {
        tableModel.setRowCount(0);
        return this;
    }

    public Table moveRow(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < tableModel.getRowCount() && 
            toIndex >= 0 && toIndex < tableModel.getRowCount()) {
            tableModel.moveRow(fromIndex, fromIndex, toIndex);
        }
        return this;
    }

    // Cell value management
    public Table setValue(int row, int column, Object value) {
        if (row >= 0 && row < tableModel.getRowCount() && 
            column >= 0 && column < tableModel.getColumnCount()) {
            tableModel.setValueAt(value, row, column);
        }
        return this;
    }

    public Table setValue(int row, String columnName, Object value) {
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex >= 0) {
            setValue(row, columnIndex, value);
        }
        return this;
    }

    // Selection management
    public Table selectionMode(int mode) {
        table.setSelectionMode(mode);
        return this;
    }

    public Table singleSelection() {
        return selectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public Table multipleSelection() {
        return selectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public Table selectRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < table.getRowCount()) {
            table.setRowSelectionInterval(rowIndex, rowIndex);
        }
        return this;
    }

    public Table selectRows(int... rowIndices) {
        table.clearSelection();
        for (int row : rowIndices) {
            if (row >= 0 && row < table.getRowCount()) {
                table.addRowSelectionInterval(row, row);
            }
        }
        return this;
    }

    public Table clearSelection() {
        table.clearSelection();
        return this;
    }

    // Appearance
    public Table headerVisible(boolean visible) {
        scrollPane.setColumnHeaderView(visible ? table.getTableHeader() : null);
        return this;
    }

    public Table gridVisible(boolean visible) {
        table.setShowGrid(visible);
        return this;
    }

    public Table gridColor(Color color) {
        table.setGridColor(color);
        return this;
    }

    public Table alternatingRowColors(Color color1, Color color2) {
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer(color1, color2));
        return this;
    }

    public Table rowHeight(int height) {
        table.setRowHeight(height);
        return this;
    }

    public Table autoResizeMode(int mode) {
        table.setAutoResizeMode(mode);
        return this;
    }

    public Table autoResizeOff() {
        return autoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public Table autoResizeAll() {
        return autoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public Table editable(boolean editable) {
        table.setDefaultEditor(Object.class, editable ? new DefaultCellEditor(new JTextField()) : null);
        return this;
    }

    public Table sortable(boolean sortable) {
        table.setAutoCreateRowSorter(sortable);
        return this;
    }

    // Scroll pane configuration
    public Table horizontalScrollPolicy(int policy) {
        scrollPane.setHorizontalScrollBarPolicy(policy);
        return this;
    }

    public Table verticalScrollPolicy(int policy) {
        scrollPane.setVerticalScrollBarPolicy(policy);
        return this;
    }

    public Table autoScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public Table autoScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    // Data retrieval
    public Object getValue(int row, int column) {
        if (row >= 0 && row < tableModel.getRowCount() && 
            column >= 0 && column < tableModel.getColumnCount()) {
            return tableModel.getValueAt(row, column);
        }
        return null;
    }

    public Object getValue(int row, String columnName) {
        int columnIndex = getColumnIndex(columnName);
        return columnIndex >= 0 ? getValue(row, columnIndex) : null;
    }

    public Object[] getRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < tableModel.getRowCount()) {
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int i = 0; i < row.length; i++) {
                row[i] = tableModel.getValueAt(rowIndex, i);
            }
            return row;
        }
        return null;
    }

    public Object[] getColumn(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < tableModel.getColumnCount()) {
            Object[] column = new Object[tableModel.getRowCount()];
            for (int i = 0; i < column.length; i++) {
                column[i] = tableModel.getValueAt(i, columnIndex);
            }
            return column;
        }
        return null;
    }

    public Object[] getColumn(String columnName) {
        int columnIndex = getColumnIndex(columnName);
        return columnIndex >= 0 ? getColumn(columnIndex) : null;
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public int[] getSelectedRows() {
        return table.getSelectedRows();
    }

    public int getRowCount() {
        return tableModel.getRowCount();
    }

    public int getColumnCount() {
        return tableModel.getColumnCount();
    }

    public String getColumnName(int columnIndex) {
        return columnIndex >= 0 && columnIndex < columnNames.size() ? 
               columnNames.get(columnIndex) : null;
    }

    public List<String> getColumnNames() {
        return new ArrayList<>(columnNames);
    }

    public int getColumnIndex(String columnName) {
        return columnNames.indexOf(columnName);
    }

    public boolean hasColumn(String columnName) {
        return columnNames.contains(columnName);
    }

    public Object[][] getAllData() {
        int rows = tableModel.getRowCount();
        int cols = tableModel.getColumnCount();
        Object[][] data = new Object[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = tableModel.getValueAt(i, j);
            }
        }
        
        return data;
    }

    @Override
    public Table build() {
        return this;
    }

    // Helper classes
    public static class TableData {
        private final int row;
        private final int column;
        private final String columnName;
        private final Object value;

        public TableData(int row, int column, String columnName, Object value) {
            this.row = row;
            this.column = column;
            this.columnName = columnName;
            this.value = value;
        }

        public int getRow() { return row; }
        public int getColumn() { return column; }
        public String getColumnName() { return columnName; }
        public Object getValue() { return value; }

        @Override
        public String toString() {
            return String.format("TableData{row=%d, column=%d, columnName='%s', value=%s}", 
                               row, column, columnName, value);
        }
    }

    // Custom cell renderer for alternating row colors
    private static class AlternatingRowRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private final Color color1;
        private final Color color2;

        public AlternatingRowRenderer(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            java.awt.Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                component.setBackground(row % 2 == 0 ? color1 : color2);
            }
            
            return component;
        }
    }
}