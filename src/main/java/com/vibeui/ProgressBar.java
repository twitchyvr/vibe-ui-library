package com.vibeui;

import javax.swing.*;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

public class ProgressBar extends Component<ProgressBar> {
    private JProgressBar progressBar;
    private Consumer<Integer> changeHandler;
    private SwingWorker<Void, Integer> worker;

    private ProgressBar(int min, int max, int value) {
        super(new JProgressBar(min, max));
        this.progressBar = (JProgressBar) swingComponent;
        progressBar.setValue(value);
        setupEventHandlers();
    }

    public static ProgressBar create() {
        return new ProgressBar(0, 100, 0);
    }

    public static ProgressBar create(int max) {
        return new ProgressBar(0, max, 0);
    }

    public static ProgressBar create(int min, int max) {
        return new ProgressBar(min, max, min);
    }

    public static ProgressBar create(int min, int max, int value) {
        return new ProgressBar(min, max, value);
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    private void setupEventHandlers() {
        progressBar.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (changeHandler != null) {
                    changeHandler.accept((Integer) evt.getNewValue());
                }
            }
        });
    }

    public ProgressBar onChange(Consumer<Integer> handler) {
        this.changeHandler = handler;
        return this;
    }

    public ProgressBar value(int value) {
        progressBar.setValue(value);
        return this;
    }

    public ProgressBar minimum(int min) {
        progressBar.setMinimum(min);
        return this;
    }

    public ProgressBar maximum(int max) {
        progressBar.setMaximum(max);
        return this;
    }

    public ProgressBar range(int min, int max) {
        progressBar.setMinimum(min);
        progressBar.setMaximum(max);
        return this;
    }

    public ProgressBar indeterminate(boolean indeterminate) {
        progressBar.setIndeterminate(indeterminate);
        return this;
    }

    public ProgressBar indeterminate() {
        return indeterminate(true);
    }

    public ProgressBar determinate() {
        return indeterminate(false);
    }

    public ProgressBar showString(boolean show) {
        progressBar.setStringPainted(show);
        return this;
    }

    public ProgressBar showString() {
        return showString(true);
    }

    public ProgressBar hideString() {
        return showString(false);
    }

    public ProgressBar text(String text) {
        progressBar.setString(text);
        progressBar.setStringPainted(true);
        return this;
    }

    public ProgressBar orientation(int orientation) {
        progressBar.setOrientation(orientation);
        return this;
    }

    public ProgressBar horizontal() {
        return orientation(SwingConstants.HORIZONTAL);
    }

    public ProgressBar vertical() {
        return orientation(SwingConstants.VERTICAL);
    }

    public ProgressBar borderPainted(boolean painted) {
        progressBar.setBorderPainted(painted);
        return this;
    }

    public ProgressBar showBorder() {
        return borderPainted(true);
    }

    public ProgressBar hideBorder() {
        return borderPainted(false);
    }

    public ProgressBar progressColor(Color color) {
        progressBar.setForeground(color);
        return this;
    }

    public ProgressBar increment() {
        progressBar.setValue(progressBar.getValue() + 1);
        return this;
    }

    public ProgressBar increment(int amount) {
        progressBar.setValue(progressBar.getValue() + amount);
        return this;
    }

    public ProgressBar decrement() {
        progressBar.setValue(progressBar.getValue() - 1);
        return this;
    }

    public ProgressBar decrement(int amount) {
        progressBar.setValue(progressBar.getValue() - amount);
        return this;
    }

    public ProgressBar setToMinimum() {
        progressBar.setValue(progressBar.getMinimum());
        return this;
    }

    public ProgressBar setToMaximum() {
        progressBar.setValue(progressBar.getMaximum());
        return this;
    }

    public ProgressBar animateTo(int targetValue, int durationMillis) {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }

        int startValue = progressBar.getValue();
        int steps = Math.abs(targetValue - startValue);
        int stepDelay = Math.max(1, durationMillis / Math.max(1, steps));

        worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                int currentValue = startValue;
                int direction = targetValue > startValue ? 1 : -1;

                while (currentValue != targetValue && !isCancelled()) {
                    Thread.sleep(stepDelay);
                    currentValue += direction;
                    publish(currentValue);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                if (!chunks.isEmpty()) {
                    int latestValue = chunks.get(chunks.size() - 1);
                    progressBar.setValue(latestValue);
                }
            }
        };

        worker.execute();
        return this;
    }

    public ProgressBar stopAnimation() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        return this;
    }

    public int getValue() {
        return progressBar.getValue();
    }

    public int getMinimum() {
        return progressBar.getMinimum();
    }

    public int getMaximum() {
        return progressBar.getMaximum();
    }

    public double getPercentComplete() {
        return progressBar.getPercentComplete();
    }

    public String getString() {
        return progressBar.getString();
    }

    public boolean isIndeterminate() {
        return progressBar.isIndeterminate();
    }

    @Override
    public ProgressBar build() {
        return this;
    }
}