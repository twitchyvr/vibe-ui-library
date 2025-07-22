package com.vibeui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public abstract class Component<T extends Component<T>> {
    protected JComponent swingComponent;
    protected Container parent;
    protected List<Component<?>> children = new ArrayList<>();
    protected Dimension size;
    protected Point position;
    protected Color backgroundColor;
    protected Color foregroundColor;
    protected Font font;
    protected boolean visible = true;
    protected boolean enabled = true;

    public Component(JComponent swingComponent) {
        this.swingComponent = swingComponent;
        configureDefaults();
    }

    protected void configureDefaults() {
        swingComponent.setOpaque(true);
    }

    @SuppressWarnings("unchecked")
    public T size(int width, int height) {
        this.size = new Dimension(width, height);
        swingComponent.setPreferredSize(size);
        swingComponent.setSize(size);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T position(int x, int y) {
        this.position = new Point(x, y);
        swingComponent.setLocation(position);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T backgroundColor(Color color) {
        this.backgroundColor = color;
        swingComponent.setBackground(color);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T foregroundColor(Color color) {
        this.foregroundColor = color;
        swingComponent.setForeground(color);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T font(Font font) {
        this.font = font;
        swingComponent.setFont(font);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T font(String name, int style, int size) {
        return font(new Font(name, style, size));
    }

    @SuppressWarnings("unchecked")
    public T visible(boolean visible) {
        this.visible = visible;
        swingComponent.setVisible(visible);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T enabled(boolean enabled) {
        this.enabled = enabled;
        swingComponent.setEnabled(enabled);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T tooltip(String text) {
        swingComponent.setToolTipText(text);
        return (T) this;
    }

    public JComponent getSwingComponent() {
        return swingComponent;
    }

    public void addTo(Container parent) {
        this.parent = parent;
        parent.add(swingComponent);
    }

    public void addTo(Component<?> parent) {
        this.parent = parent.getSwingComponent();
        parent.children.add(this);
        if (parent.getSwingComponent() instanceof Container) {
            ((Container) parent.getSwingComponent()).add(swingComponent);
        }
    }

    public void remove() {
        if (parent != null) {
            parent.remove(swingComponent);
            parent = null;
        }
    }

    public abstract T build();
}