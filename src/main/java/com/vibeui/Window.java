package com.vibeui;

import javax.swing.*;
import java.awt.*;

public class Window extends Component<Window> {
    private JFrame frame;
    private String title;
    private boolean resizable = true;
    private int defaultCloseOperation = JFrame.EXIT_ON_CLOSE;

    public Window() {
        super(new JPanel());
        this.frame = new JFrame();
        frame.setContentPane((JPanel) swingComponent);
        frame.setDefaultCloseOperation(defaultCloseOperation);
        frame.setLayout(null);
        ((JPanel) swingComponent).setLayout(null);
        configureWindowFeatures();
    }

    private void configureWindowFeatures() {
        frame.setResizable(resizable);
    }

    public Window(String title) {
        this();
        title(title);
    }

    public Window title(String title) {
        this.title = title;
        frame.setTitle(title);
        return this;
    }

    public Window resizable(boolean resizable) {
        this.resizable = resizable;
        frame.setResizable(resizable);
        return this;
    }

    public Window minimizable(boolean minimizable) {
        if (!minimizable) {
            frame.setExtendedState(frame.getExtendedState() & ~JFrame.ICONIFIED);
        }
        return this;
    }

    public Window maximizable(boolean maximizable) {
        if (!maximizable) {
            frame.setExtendedState(frame.getExtendedState() & ~JFrame.MAXIMIZED_BOTH);
        }
        return this;
    }

    public Window minimize() {
        frame.setExtendedState(frame.getExtendedState() | JFrame.ICONIFIED);
        return this;
    }

    public Window maximize() {
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        return this;
    }

    public Window restore() {
        frame.setExtendedState(JFrame.NORMAL);
        return this;
    }

    public Window alwaysOnTop(boolean onTop) {
        frame.setAlwaysOnTop(onTop);
        return this;
    }

    public Window undecorated(boolean undecorated) {
        frame.setUndecorated(undecorated);
        return this;
    }

    public Window closeOperation(int operation) {
        this.defaultCloseOperation = operation;
        frame.setDefaultCloseOperation(operation);
        return this;
    }

    public Window centerOnScreen() {
        frame.setLocationRelativeTo(null);
        return this;
    }

    public Window layout(LayoutManager layout) {
        ((JPanel) swingComponent).setLayout(layout);
        return this;
    }

    public Window add(Component<?> component, Object constraints) {
        ((JPanel) swingComponent).add(component.getSwingComponent(), constraints);
        children.add(component);
        return this;
    }

    @Override
    public Window size(int width, int height) {
        super.size(width, height);
        frame.setSize(width, height);
        return this;
    }

    public Window show() {
        frame.setVisible(true);
        return this;
    }

    public Window hide() {
        frame.setVisible(false);
        return this;
    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public Window build() {
        frame.pack();
        return this;
    }

    public static Window create() {
        return new Window();
    }

    public static Window create(String title) {
        return new Window(title);
    }
}