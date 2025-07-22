package com.vibeui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Slider extends Component<Slider> {
    private int minimum = 0;
    private int maximum = 100;
    private int value = 50;
    private List<Consumer<Integer>> changeHandlers = new ArrayList<>();

    public Slider() {
        super(new JSlider());
        setupChangeListener();
    }

    public Slider(int min, int max) {
        this();
        range(min, max);
    }

    public Slider(int min, int max, int value) {
        this(min, max);
        value(value);
    }

    public Slider range(int min, int max) {
        this.minimum = min;
        this.maximum = max;
        JSlider slider = (JSlider) swingComponent;
        slider.setMinimum(min);
        slider.setMaximum(max);
        return this;
    }

    public Slider value(int value) {
        this.value = value;
        ((JSlider) swingComponent).setValue(value);
        return this;
    }

    public Slider majorTickSpacing(int spacing) {
        ((JSlider) swingComponent).setMajorTickSpacing(spacing);
        return this;
    }

    public Slider minorTickSpacing(int spacing) {
        ((JSlider) swingComponent).setMinorTickSpacing(spacing);
        return this;
    }

    public Slider showTicks(boolean show) {
        ((JSlider) swingComponent).setPaintTicks(show);
        return this;
    }

    public Slider showLabels(boolean show) {
        ((JSlider) swingComponent).setPaintLabels(show);
        return this;
    }

    public Slider orientation(int orientation) {
        ((JSlider) swingComponent).setOrientation(orientation);
        return this;
    }

    public Slider vertical() {
        return orientation(SwingConstants.VERTICAL);
    }

    public Slider horizontal() {
        return orientation(SwingConstants.HORIZONTAL);
    }

    public Slider onChange(Consumer<Integer> handler) {
        changeHandlers.add(handler);
        return this;
    }

    private void setupChangeListener() {
        ((JSlider) swingComponent).addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = ((JSlider) swingComponent).getValue();
                changeHandlers.forEach(handler -> handler.accept(currentValue));
            }
        });
    }

    public int getValue() {
        return ((JSlider) swingComponent).getValue();
    }

    @Override
    public Slider build() {
        return this;
    }

    public static Slider create() {
        return new Slider();
    }

    public static Slider create(int min, int max) {
        return new Slider(min, max);
    }

    public static Slider create(int min, int max, int value) {
        return new Slider(min, max, value);
    }
}