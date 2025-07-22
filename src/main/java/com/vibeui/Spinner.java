package com.vibeui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.text.DecimalFormat;
import java.util.function.Consumer;

public class Spinner extends Component<Spinner> {
    private JSpinner spinner;
    private SpinnerModel model;
    private Consumer<Object> changeHandler;
    private Consumer<Number> numberChangeHandler;

    private Spinner(SpinnerModel model) {
        super(new JSpinner(model));
        this.spinner = (JSpinner) swingComponent;
        this.model = model;
        setupEventHandlers();
    }

    public static Spinner create() {
        return new Spinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
    }

    public static Spinner createNumber(int value) {
        return new Spinner(new SpinnerNumberModel(value, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
    }

    public static Spinner createNumber(int value, int min, int max, int step) {
        return new Spinner(new SpinnerNumberModel(value, min, max, step));
    }

    public static Spinner createNumber(double value, double min, double max, double step) {
        return new Spinner(new SpinnerNumberModel(value, min, max, step));
    }

    public static Spinner createDate() {
        return new Spinner(new SpinnerDateModel());
    }

    public static Spinner createDate(java.util.Date value) {
        return new Spinner(new SpinnerDateModel(value, null, null, java.util.Calendar.DAY_OF_MONTH));
    }

    public static Spinner createList(Object... values) {
        return new Spinner(new SpinnerListModel(values));
    }

    public static Spinner createList(java.util.List<?> values) {
        return new Spinner(new SpinnerListModel(values));
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    private void setupEventHandlers() {
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Object value = spinner.getValue();
                
                if (changeHandler != null) {
                    changeHandler.accept(value);
                }
                
                if (numberChangeHandler != null && value instanceof Number) {
                    numberChangeHandler.accept((Number) value);
                }
            }
        });
    }

    public Spinner onChange(Consumer<Object> handler) {
        this.changeHandler = handler;
        return this;
    }

    public Spinner onNumberChange(Consumer<Number> handler) {
        this.numberChangeHandler = handler;
        return this;
    }

    public Spinner value(Object value) {
        spinner.setValue(value);
        return this;
    }

    public Spinner editor(JSpinner.DefaultEditor editor) {
        spinner.setEditor(editor);
        return this;
    }

    public Spinner numberEditor() {
        if (model instanceof SpinnerNumberModel) {
            spinner.setEditor(new JSpinner.NumberEditor(spinner));
        }
        return this;
    }

    public Spinner numberEditor(String format) {
        if (model instanceof SpinnerNumberModel) {
            spinner.setEditor(new JSpinner.NumberEditor(spinner, format));
        }
        return this;
    }

    public Spinner dateEditor() {
        if (model instanceof SpinnerDateModel) {
            spinner.setEditor(new JSpinner.DateEditor(spinner));
        }
        return this;
    }

    public Spinner dateEditor(String format) {
        if (model instanceof SpinnerDateModel) {
            spinner.setEditor(new JSpinner.DateEditor(spinner, format));
        }
        return this;
    }

    public Spinner minimum(Comparable<?> minimum) {
        if (model instanceof SpinnerNumberModel) {
            ((SpinnerNumberModel) model).setMinimum(minimum);
        } else if (model instanceof SpinnerDateModel && minimum instanceof java.util.Date) {
            ((SpinnerDateModel) model).setStart((java.util.Date) minimum);
        }
        return this;
    }

    public Spinner maximum(Comparable<?> maximum) {
        if (model instanceof SpinnerNumberModel) {
            ((SpinnerNumberModel) model).setMaximum(maximum);
        } else if (model instanceof SpinnerDateModel && maximum instanceof java.util.Date) {
            ((SpinnerDateModel) model).setEnd((java.util.Date) maximum);
        }
        return this;
    }

    public Spinner range(Comparable<?> min, Comparable<?> max) {
        minimum(min);
        maximum(max);
        return this;
    }

    public Spinner step(Number stepSize) {
        if (model instanceof SpinnerNumberModel) {
            ((SpinnerNumberModel) model).setStepSize(stepSize);
        }
        return this;
    }

    public Spinner calendarField(int field) {
        if (model instanceof SpinnerDateModel) {
            ((SpinnerDateModel) model).setCalendarField(field);
        }
        return this;
    }

    public Spinner dayField() {
        return calendarField(java.util.Calendar.DAY_OF_MONTH);
    }

    public Spinner monthField() {
        return calendarField(java.util.Calendar.MONTH);
    }

    public Spinner yearField() {
        return calendarField(java.util.Calendar.YEAR);
    }

    public Spinner hourField() {
        return calendarField(java.util.Calendar.HOUR_OF_DAY);
    }

    public Spinner minuteField() {
        return calendarField(java.util.Calendar.MINUTE);
    }

    public Spinner nextValue() {
        Object next = spinner.getNextValue();
        if (next != null) {
            spinner.setValue(next);
        }
        return this;
    }

    public Spinner previousValue() {
        Object previous = spinner.getPreviousValue();
        if (previous != null) {
            spinner.setValue(previous);
        }
        return this;
    }

    public Spinner commitEdit() {
        try {
            spinner.commitEdit();
        } catch (java.text.ParseException e) {
        }
        return this;
    }

    public Object getValue() {
        return spinner.getValue();
    }

    public Number getNumberValue() {
        Object value = spinner.getValue();
        return value instanceof Number ? (Number) value : null;
    }

    public int getIntValue() {
        Number number = getNumberValue();
        return number != null ? number.intValue() : 0;
    }

    public double getDoubleValue() {
        Number number = getNumberValue();
        return number != null ? number.doubleValue() : 0.0;
    }

    public java.util.Date getDateValue() {
        Object value = spinner.getValue();
        return value instanceof java.util.Date ? (java.util.Date) value : null;
    }

    public Object getNextValue() {
        return spinner.getNextValue();
    }

    public Object getPreviousValue() {
        return spinner.getPreviousValue();
    }

    public SpinnerModel getModel() {
        return spinner.getModel();
    }

    @Override
    public Spinner build() {
        return this;
    }
}