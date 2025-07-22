package com.vibeui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TabPane extends com.vibeui.Component<TabPane> {
    private JTabbedPane tabbedPane;
    private Consumer<Integer> tabChangeHandler;
    private Consumer<String> tabSelectedHandler;
    private List<Tab> tabs = new ArrayList<>();

    private TabPane() {
        super(new JTabbedPane());
        this.tabbedPane = (JTabbedPane) swingComponent;
        setupEventHandlers();
    }

    public static TabPane create() {
        return new TabPane();
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    private void setupEventHandlers() {
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                
                if (tabChangeHandler != null) {
                    tabChangeHandler.accept(selectedIndex);
                }
                
                if (tabSelectedHandler != null && selectedIndex >= 0 && selectedIndex < tabs.size()) {
                    Tab selectedTab = tabs.get(selectedIndex);
                    tabSelectedHandler.accept(selectedTab.getTitle());
                }
            }
        });
    }

    public TabPane onTabChange(Consumer<Integer> handler) {
        this.tabChangeHandler = handler;
        return this;
    }

    public TabPane onTabSelected(Consumer<String> handler) {
        this.tabSelectedHandler = handler;
        return this;
    }

    public TabPane addTab(String title, com.vibeui.Component<?> content) {
        return addTab(title, content, null);
    }

    public TabPane addTab(String title, com.vibeui.Component<?> content, String tooltip) {
        Tab tab = new Tab(title, content, tooltip);
        tabs.add(tab);
        
        tabbedPane.addTab(title, content.getSwingComponent());
        
        if (tooltip != null) {
            tabbedPane.setToolTipTextAt(tabs.size() - 1, tooltip);
        }
        
        return this;
    }

    public TabPane addTab(Tab tab) {
        return addTab(tab.getTitle(), tab.getContent(), tab.getTooltip());
    }

    public TabPane removeTab(int index) {
        if (index >= 0 && index < tabs.size()) {
            tabs.remove(index);
            tabbedPane.removeTabAt(index);
        }
        return this;
    }

    public TabPane removeTab(String title) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getTitle().equals(title)) {
                return removeTab(i);
            }
        }
        return this;
    }

    public TabPane removeAllTabs() {
        tabs.clear();
        tabbedPane.removeAll();
        return this;
    }

    public TabPane selectedIndex(int index) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(index);
        }
        return this;
    }

    public TabPane selectedTab(String title) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getTitle().equals(title)) {
                return selectedIndex(i);
            }
        }
        return this;
    }

    public TabPane tabPlacement(int placement) {
        tabbedPane.setTabPlacement(placement);
        return this;
    }

    public TabPane tabsOnTop() {
        return tabPlacement(JTabbedPane.TOP);
    }

    public TabPane tabsOnBottom() {
        return tabPlacement(JTabbedPane.BOTTOM);
    }

    public TabPane tabsOnLeft() {
        return tabPlacement(JTabbedPane.LEFT);
    }

    public TabPane tabsOnRight() {
        return tabPlacement(JTabbedPane.RIGHT);
    }

    public TabPane tabLayoutPolicy(int policy) {
        tabbedPane.setTabLayoutPolicy(policy);
        return this;
    }

    public TabPane scrollingTabs() {
        return tabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public TabPane wrappingTabs() {
        return tabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    }

    public TabPane setTabEnabled(int index, boolean enabled) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setEnabledAt(index, enabled);
        }
        return this;
    }

    public TabPane setTabEnabled(String title, boolean enabled) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getTitle().equals(title)) {
                return setTabEnabled(i, enabled);
            }
        }
        return this;
    }

    public TabPane setTabIcon(int index, Icon icon) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setIconAt(index, icon);
        }
        return this;
    }

    public TabPane setTabIcon(String title, Icon icon) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getTitle().equals(title)) {
                return setTabIcon(i, icon);
            }
        }
        return this;
    }

    public TabPane setTabTooltip(int index, String tooltip) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setToolTipTextAt(index, tooltip);
            tabs.get(index).setTooltip(tooltip);
        }
        return this;
    }

    public TabPane setTabTooltip(String title, String tooltip) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getTitle().equals(title)) {
                return setTabTooltip(i, tooltip);
            }
        }
        return this;
    }

    public TabPane setTabBackground(int index, Color color) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setBackgroundAt(index, color);
        }
        return this;
    }

    public TabPane setTabForeground(int index, Color color) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setForegroundAt(index, color);
        }
        return this;
    }

    // Getters
    public int getSelectedIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public String getSelectedTabTitle() {
        int index = getSelectedIndex();
        return index >= 0 && index < tabs.size() ? tabs.get(index).getTitle() : null;
    }

    public Tab getSelectedTab() {
        int index = getSelectedIndex();
        return index >= 0 && index < tabs.size() ? tabs.get(index) : null;
    }

    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    public Tab getTab(int index) {
        return index >= 0 && index < tabs.size() ? tabs.get(index) : null;
    }

    public Tab getTab(String title) {
        return tabs.stream()
                .filter(tab -> tab.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public List<Tab> getAllTabs() {
        return new ArrayList<>(tabs);
    }

    public int getTabIndex(String title) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getTitle().equals(title)) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasTab(String title) {
        return getTabIndex(title) >= 0;
    }

    @Override
    public TabPane build() {
        return this;
    }

    // Tab data class
    public static class Tab {
        private String title;
        private com.vibeui.Component<?> content;
        private String tooltip;
        private Icon icon;
        private boolean enabled = true;

        public Tab(String title, com.vibeui.Component<?> content) {
            this(title, content, null);
        }

        public Tab(String title, com.vibeui.Component<?> content, String tooltip) {
            this.title = title;
            this.content = content;
            this.tooltip = tooltip;
        }

        public static Tab create(String title, com.vibeui.Component<?> content) {
            return new Tab(title, content);
        }

        public static Tab create(String title, com.vibeui.Component<?> content, String tooltip) {
            return new Tab(title, content, tooltip);
        }

        // Getters and setters
        public String getTitle() { return title; }
        public Tab setTitle(String title) { this.title = title; return this; }

        public com.vibeui.Component<?> getContent() { return content; }
        public Tab setContent(com.vibeui.Component<?> content) { this.content = content; return this; }

        public String getTooltip() { return tooltip; }
        public Tab setTooltip(String tooltip) { this.tooltip = tooltip; return this; }

        public Icon getIcon() { return icon; }
        public Tab setIcon(Icon icon) { this.icon = icon; return this; }

        public boolean isEnabled() { return enabled; }
        public Tab setEnabled(boolean enabled) { this.enabled = enabled; return this; }
    }
}