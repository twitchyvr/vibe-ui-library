package com.vibeui;

import javax.swing.*;
import java.awt.*;

public class ScrollPane extends Component<ScrollPane> {
    private JScrollPane scrollPane;
    private Component<?> viewComponent;

    private ScrollPane(Component<?> viewComponent) {
        super(new JScrollPane(viewComponent.getSwingComponent()));
        this.scrollPane = (JScrollPane) swingComponent;
        this.viewComponent = viewComponent;
    }

    private ScrollPane() {
        super(new JScrollPane());
        this.scrollPane = (JScrollPane) swingComponent;
    }

    public static ScrollPane create(Component<?> viewComponent) {
        return new ScrollPane(viewComponent);
    }

    public static ScrollPane createEmpty() {
        return new ScrollPane();
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    public ScrollPane setViewComponent(Component<?> component) {
        this.viewComponent = component;
        scrollPane.setViewportView(component.getSwingComponent());
        return this;
    }

    public ScrollPane horizontalScrollPolicy(int policy) {
        scrollPane.setHorizontalScrollBarPolicy(policy);
        return this;
    }

    public ScrollPane verticalScrollPolicy(int policy) {
        scrollPane.setVerticalScrollBarPolicy(policy);
        return this;
    }

    public ScrollPane autoScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public ScrollPane autoScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public ScrollPane alwaysScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    public ScrollPane alwaysScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public ScrollPane neverScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public ScrollPane neverScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }

    public ScrollPane wheelScrollingEnabled(boolean enabled) {
        scrollPane.setWheelScrollingEnabled(enabled);
        return this;
    }

    public ScrollPane unitIncrement(int increment) {
        scrollPane.getHorizontalScrollBar().setUnitIncrement(increment);
        scrollPane.getVerticalScrollBar().setUnitIncrement(increment);
        return this;
    }

    public ScrollPane blockIncrement(int increment) {
        scrollPane.getHorizontalScrollBar().setBlockIncrement(increment);
        scrollPane.getVerticalScrollBar().setBlockIncrement(increment);
        return this;
    }

    public Component<?> getViewComponent() {
        return viewComponent;
    }

    @Override
    public ScrollPane build() {
        return this;
    }
}