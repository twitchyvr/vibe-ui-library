package com.vibeui;

import javax.swing.*;
import java.awt.*;

public class Panel extends Component<Panel> {
    
    public Panel() {
        super(new JPanel());
        ((JPanel) swingComponent).setLayout(null);
    }

    public Panel layout(LayoutManager layout) {
        ((JPanel) swingComponent).setLayout(layout);
        return this;
    }

    public Panel flowLayout() {
        return layout(new FlowLayout());
    }

    public Panel borderLayout() {
        return layout(new BorderLayout());
    }

    public Panel gridLayout(int rows, int cols) {
        return layout(new GridLayout(rows, cols));
    }

    public Panel boxLayout(int axis) {
        return layout(new BoxLayout((JPanel) swingComponent, axis));
    }

    public Panel verticalLayout() {
        return boxLayout(BoxLayout.Y_AXIS);
    }

    public Panel horizontalLayout() {
        return boxLayout(BoxLayout.X_AXIS);
    }

    public Panel add(Component<?> component) {
        component.addTo(this);
        return this;
    }

    public Panel add(Component<?> component, Object constraints) {
        ((JPanel) swingComponent).add(component.getSwingComponent(), constraints);
        children.add(component);
        return this;
    }

    public Panel border(int thickness) {
        ((JPanel) swingComponent).setBorder(BorderFactory.createLineBorder(Color.GRAY, thickness));
        return this;
    }

    public Panel border(Color color, int thickness) {
        ((JPanel) swingComponent).setBorder(BorderFactory.createLineBorder(color, thickness));
        return this;
    }

    public Panel padding(int padding) {
        ((JPanel) swingComponent).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        return this;
    }

    public Panel padding(int top, int left, int bottom, int right) {
        ((JPanel) swingComponent).setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return this;
    }

    @Override
    public Panel build() {
        return this;
    }

    public static Panel create() {
        return new Panel();
    }
}