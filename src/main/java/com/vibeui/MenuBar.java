package com.vibeui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MenuBar extends Component<MenuBar> {
    private JMenuBar menuBar;
    private List<Menu> menus = new ArrayList<>();

    private MenuBar() {
        super(new JMenuBar());
        this.menuBar = (JMenuBar) swingComponent;
    }

    public static MenuBar create() {
        return new MenuBar();
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    public MenuBar addMenu(Menu menu) {
        menus.add(menu);
        menuBar.add(menu.getJMenu());
        return this;
    }

    public MenuBar addMenu(String title) {
        Menu menu = Menu.create(title);
        return addMenu(menu);
    }

    public MenuBar removeMenu(Menu menu) {
        if (menus.remove(menu)) {
            menuBar.remove(menu.getJMenu());
        }
        return this;
    }

    public MenuBar removeMenu(String title) {
        Menu menu = getMenu(title);
        if (menu != null) {
            removeMenu(menu);
        }
        return this;
    }

    public MenuBar removeAllMenus() {
        menus.clear();
        menuBar.removeAll();
        return this;
    }

    public Menu getMenu(String title) {
        return menus.stream()
                .filter(menu -> menu.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public List<Menu> getAllMenus() {
        return new ArrayList<>(menus);
    }

    public int getMenuCount() {
        return menus.size();
    }

    @Override
    public MenuBar build() {
        return this;
    }

    // Menu class
    public static class Menu {
        private JMenu jMenu;
        private String title;
        private List<MenuItem> items = new ArrayList<>();

        private Menu(String title) {
            this.title = title;
            this.jMenu = new JMenu(title);
        }

        public static Menu create(String title) {
            return new Menu(title);
        }

        public Menu addItem(MenuItem item) {
            items.add(item);
            
            if (item instanceof MenuSeparator) {
                jMenu.addSeparator();
            } else {
                jMenu.add(item.getJMenuItem());
            }
            
            return this;
        }

        public Menu addItem(String title, Runnable action) {
            MenuItem item = MenuItem.create(title, action);
            return addItem(item);
        }

        public Menu addSeparator() {
            return addItem(new MenuSeparator());
        }

        public Menu addSubMenu(String title) {
            SubMenu subMenu = SubMenu.create(title);
            items.add(subMenu);
            jMenu.add(subMenu.getJMenu());
            return this;
        }

        public Menu removeItem(MenuItem item) {
            if (items.remove(item)) {
                jMenu.remove(item.getJMenuItem());
            }
            return this;
        }

        public Menu removeAllItems() {
            items.clear();
            jMenu.removeAll();
            return this;
        }

        public MenuItem getItem(String title) {
            return items.stream()
                    .filter(item -> item.getTitle().equals(title))
                    .findFirst()
                    .orElse(null);
        }

        public Menu mnemonic(int keyCode) {
            jMenu.setMnemonic(keyCode);
            return this;
        }

        public Menu enabled(boolean enabled) {
            jMenu.setEnabled(enabled);
            return this;
        }

        public Menu tooltip(String tooltip) {
            jMenu.setToolTipText(tooltip);
            return this;
        }

        // Getters
        public String getTitle() { return title; }
        public JMenu getJMenu() { return jMenu; }
        public List<MenuItem> getItems() { return new ArrayList<>(items); }
    }

    // MenuItem class
    public static class MenuItem {
        protected JMenuItem jMenuItem;
        protected String title;
        protected Runnable action;
        protected KeyStroke accelerator;
        protected Icon icon;

        protected MenuItem(String title) {
            this.title = title;
            this.jMenuItem = new JMenuItem(title);
        }

        protected MenuItem(String title, Runnable action) {
            this(title);
            this.action = action;
            setupActionListener();
        }

        public static MenuItem create(String title) {
            return new MenuItem(title);
        }

        public static MenuItem create(String title, Runnable action) {
            return new MenuItem(title, action);
        }

        private void setupActionListener() {
            if (action != null) {
                jMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            action.run();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }

        public MenuItem onClick(Runnable action) {
            this.action = action;
            setupActionListener();
            return this;
        }

        public MenuItem accelerator(KeyStroke keyStroke) {
            this.accelerator = keyStroke;
            jMenuItem.setAccelerator(keyStroke);
            return this;
        }

        public MenuItem accelerator(int keyCode, int modifiers) {
            return accelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        }

        public MenuItem accelerator(String keyStroke) {
            return accelerator(KeyStroke.getKeyStroke(keyStroke));
        }

        public MenuItem mnemonic(int keyCode) {
            jMenuItem.setMnemonic(keyCode);
            return this;
        }

        public MenuItem icon(Icon icon) {
            this.icon = icon;
            jMenuItem.setIcon(icon);
            return this;
        }

        public MenuItem enabled(boolean enabled) {
            jMenuItem.setEnabled(enabled);
            return this;
        }

        public MenuItem tooltip(String tooltip) {
            jMenuItem.setToolTipText(tooltip);
            return this;
        }

        public MenuItem selected(boolean selected) {
            jMenuItem.setSelected(selected);
            return this;
        }

        // Getters
        public String getTitle() { return title; }
        public JMenuItem getJMenuItem() { return jMenuItem; }
        public boolean isEnabled() { return jMenuItem.isEnabled(); }
        public boolean isSelected() { return jMenuItem.isSelected(); }
    }

    // CheckboxMenuItem class
    public static class CheckboxMenuItem extends MenuItem {
        private JCheckBoxMenuItem checkboxItem;

        private CheckboxMenuItem(String title, boolean selected) {
            super(title);
            this.checkboxItem = new JCheckBoxMenuItem(title, selected);
            this.jMenuItem = checkboxItem;
        }

        public static CheckboxMenuItem create(String title) {
            return new CheckboxMenuItem(title, false);
        }

        public static CheckboxMenuItem create(String title, boolean selected) {
            return new CheckboxMenuItem(title, selected);
        }

        public CheckboxMenuItem onToggle(java.util.function.Consumer<Boolean> handler) {
            checkboxItem.addActionListener(e -> {
                if (handler != null) {
                    handler.accept(checkboxItem.isSelected());
                }
            });
            return this;
        }

        @Override
        public CheckboxMenuItem selected(boolean selected) {
            checkboxItem.setSelected(selected);
            return this;
        }

        public boolean isChecked() {
            return checkboxItem.isSelected();
        }
    }

    // RadioMenuItem class  
    public static class RadioMenuItem extends MenuItem {
        private JRadioButtonMenuItem radioItem;
        private static ButtonGroup currentGroup;

        private RadioMenuItem(String title, boolean selected) {
            super(title);
            this.radioItem = new JRadioButtonMenuItem(title, selected);
            this.jMenuItem = radioItem;
        }

        public static RadioMenuItem create(String title) {
            return new RadioMenuItem(title, false);
        }

        public static RadioMenuItem create(String title, boolean selected) {
            return new RadioMenuItem(title, selected);
        }

        public static ButtonGroup createGroup() {
            currentGroup = new ButtonGroup();
            return currentGroup;
        }

        public RadioMenuItem group(ButtonGroup group) {
            group.add(radioItem);
            return this;
        }

        public RadioMenuItem onSelect(java.util.function.Consumer<Boolean> handler) {
            radioItem.addActionListener(e -> {
                if (handler != null) {
                    handler.accept(radioItem.isSelected());
                }
            });
            return this;
        }

        @Override
        public RadioMenuItem selected(boolean selected) {
            radioItem.setSelected(selected);
            return this;
        }
    }

    // SubMenu class
    public static class SubMenu extends MenuItem {
        private JMenu subMenu;
        private List<MenuItem> subItems = new ArrayList<>();

        private SubMenu(String title) {
            super(title);
            this.subMenu = new JMenu(title);
            this.jMenuItem = subMenu;
        }

        public static SubMenu create(String title) {
            return new SubMenu(title);
        }

        public SubMenu addItem(MenuItem item) {
            subItems.add(item);
            
            if (item instanceof MenuSeparator) {
                subMenu.addSeparator();
            } else {
                subMenu.add(item.getJMenuItem());
            }
            
            return this;
        }

        public SubMenu addItem(String title, Runnable action) {
            MenuItem item = MenuItem.create(title, action);
            return addItem(item);
        }

        public SubMenu addSeparator() {
            return addItem(new MenuSeparator());
        }

        public SubMenu removeItem(MenuItem item) {
            if (subItems.remove(item)) {
                subMenu.remove(item.getJMenuItem());
            }
            return this;
        }

        public JMenu getJMenu() {
            return subMenu;
        }

        public List<MenuItem> getSubItems() {
            return new ArrayList<>(subItems);
        }
    }

    // MenuSeparator class
    public static class MenuSeparator extends MenuItem {
        private MenuSeparator() {
            super("---");
        }

        public static MenuSeparator create() {
            return new MenuSeparator();
        }
    }
}