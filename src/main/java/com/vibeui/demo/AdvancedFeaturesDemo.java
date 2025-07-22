package com.vibeui.demo;

import com.vibeui.*;
import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.util.Random;

public class AdvancedFeaturesDemo {
    private Window mainWindow;
    private TabPane mainTabPane;
    private Viewport hudViewport;
    private Table dataTable;
    private Tree fileTree;
    private Random random = new Random();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdvancedFeaturesDemo().createDemo());
    }

    private void createDemo() {
        // Apply initial theme
        Theme.apply(Theme.ThemeStyle.MODERN_DARK);
        
        mainWindow = Window.create("Vibe UI - Advanced Features Showcase")
            .size(1200, 800)
            .centerOnScreen()
            .resizable(true)
            .layout(Layout.border());

        createMenuBar();
        createMainContent();
        createStatusBar();

        mainWindow.build().show();
        
        System.out.println("Advanced Features Demo started!");
        System.out.println("Try different themes from the View menu");
        System.out.println("Explore all the new components in the tabs");
    }

    private void createMenuBar() {
        MenuBar menuBar = MenuBar.create()
            .addMenu(Menu.create("File")
                .addItem("New", () -> showNewDialog())
                .addItem("Open", () -> Dialog.showMessage("Open file clicked"))
                .addSeparator()
                .addItem("Exit", () -> System.exit(0))
                .mnemonic(KeyEvent.VK_F))
            .addMenu(Menu.create("View")
                .addItem("Modern Dark", () -> Theme.apply(Theme.ThemeStyle.MODERN_DARK))
                .addItem("Modern Light", () -> Theme.apply(Theme.ThemeStyle.MODERN_LIGHT))
                .addItem("Glassmorphic", () -> Theme.apply(Theme.ThemeStyle.GLASSMORPHIC))
                .addItem("Retro 90s", () -> Theme.apply(Theme.ThemeStyle.RETRO_90S))
                .addItem("Windows XP", () -> Theme.apply(Theme.ThemeStyle.WINDOWS_XP))
                .addItem("macOS", () -> Theme.apply(Theme.ThemeStyle.MAC_OS))
                .addItem("Cyberpunk", () -> Theme.apply(Theme.ThemeStyle.CYBERPUNK))
                .addItem("Minimal", () -> Theme.apply(Theme.ThemeStyle.MINIMAL))
                .addItem("Gaming HUD", () -> Theme.apply(Theme.ThemeStyle.GAMING_HUD))
                .mnemonic(KeyEvent.VK_V))
            .addMenu(Menu.create("Tools")
                .addItem("Show Table Demo", () -> mainTabPane.selectedTab("Data Table"))
                .addItem("Show HUD Demo", () -> mainTabPane.selectedTab("HUD Viewport"))
                .addItem("Show Tree Demo", () -> mainTabPane.selectedTab("File Tree"))
                .addSeparator()
                .addItem("Test Dialog", () -> showTestDialog())
                .addItem("Test Confirm", () -> showConfirmDialog())
                .mnemonic(KeyEvent.VK_T))
            .addMenu(Menu.create("Help")
                .addItem("About", () -> showAboutDialog())
                .addItem("Keyboard Shortcuts", () -> showShortcutsDialog())
                .mnemonic(KeyEvent.VK_H));

        mainWindow.add(menuBar, Layout.BorderConstraints.NORTH);
    }

    private void createMainContent() {
        mainTabPane = TabPane.create()
            .tabsOnTop()
            .scrollingTabs()
            .onTabSelected(tab -> System.out.println("Selected tab: " + tab));

        // Tab 1: Data Table Demo
        createDataTableTab();
        
        // Tab 2: HUD Viewport Demo
        createHUDViewportTab();
        
        // Tab 3: File Tree Demo
        createFileTreeTab();
        
        // Tab 4: Component Showcase
        createComponentShowcaseTab();
        
        // Tab 5: Layout Examples
        createLayoutExamplesTab();

        mainWindow.add(mainTabPane, Layout.BorderConstraints.CENTER);
    }

    private void createDataTableTab() {
        Panel tablePanel = Panel.create()
            .layout(Layout.border())
            .padding(10);

        // Create table with sample data
        dataTable = Table.create("ID", "Name", "Department", "Salary", "Status")
            .sortable(true)
            .alternatingRowColors(new Color(240, 240, 240), Color.WHITE)
            .onCellDoubleClick(cell -> {
                String message = String.format("Double-clicked cell: %s = %s", 
                    cell.getColumnName(), cell.getValue());
                Dialog.showMessage("Cell Double-Click", message, Dialog.DialogType.INFORMATION);
            })
            .onRowSelection(row -> {
                if (row >= 0) {
                    Object[] rowData = dataTable.getRow(row);
                    System.out.println("Selected employee: " + rowData[1]);
                }
            });

        // Add sample data
        addSampleTableData();

        // Table controls
        Panel tableControls = Panel.create()
            .flowLayout()
            .padding(5);

        Button addRowBtn = Button.create("Add Employee")
            .onClick(this::addRandomEmployee)
            .backgroundColor(new Color(0, 150, 0))
            .foregroundColor(Color.WHITE);

        Button removeRowBtn = Button.create("Remove Selected")
            .onClick(this::removeSelectedEmployee)
            .backgroundColor(new Color(200, 0, 0))
            .foregroundColor(Color.WHITE);

        Button clearAllBtn = Button.create("Clear All")
            .onClick(() -> dataTable.removeAllRows());

        tableControls.add(addRowBtn).add(removeRowBtn).add(clearAllBtn);

        tablePanel.add(tableControls, Layout.BorderConstraints.NORTH)
                  .add(dataTable, Layout.BorderConstraints.CENTER);

        mainTabPane.addTab("Data Table", tablePanel, "Interactive data table with sorting and selection");
    }

    private void createHUDViewportTab() {
        Panel hudPanel = Panel.create()
            .layout(Layout.border());

        // Create viewport for HUD
        hudViewport = Viewport.create(600, 400)
            .clearColor(new Color(20, 30, 60))
            .antiAlias(true)
            .onRender(this::renderHUD)
            .onMouseClick(e -> System.out.println("HUD clicked at: " + e.getX() + ", " + e.getY()));

        // HUD Controls
        Panel hudControls = Panel.create()
            .flowLayout()
            .padding(5);

        Button startAnimationBtn = Button.create("Start Animation")
            .onClick(() -> hudViewport.startAnimation(60));

        Button stopAnimationBtn = Button.create("Stop Animation")
            .onClick(() -> hudViewport.stopAnimation());

        Button screenshotBtn = Button.create("Screenshot")
            .onClick(() -> {
                hudViewport.saveScreenshot("hud_screenshot.png");
                Dialog.showMessage("Screenshot saved as hud_screenshot.png");
            });

        hudControls.add(startAnimationBtn).add(stopAnimationBtn).add(screenshotBtn);

        // Add HUD overlays
        createHUDOverlays();

        hudPanel.add(hudControls, Layout.BorderConstraints.NORTH)
                .add(hudViewport, Layout.BorderConstraints.CENTER);

        mainTabPane.addTab("HUD Viewport", hudPanel, "Custom rendering viewport with HUD overlays");
    }

    private void createFileTreeTab() {
        Panel treePanel = Panel.create()
            .layout(Layout.border())
            .padding(10);

        // Create file tree structure
        Tree.TreeNode root = Tree.TreeNode.create("File System")
            .addChild(Tree.TreeNode.create("Documents")
                .addChild("report.pdf")
                .addChild("notes.txt")
                .addChild(Tree.TreeNode.create("Projects")
                    .addChild("vibe-ui")
                    .addChild("my-app")))
            .addChild(Tree.TreeNode.create("Pictures")
                .addChild("vacation.jpg")
                .addChild("family.png"))
            .addChild(Tree.TreeNode.create("Downloads")
                .addChild("installer.exe")
                .addChild("update.zip"));

        fileTree = Tree.create(root)
            .showRootHandles(true)
            .rootVisible(true)
            .onSelection(node -> System.out.println("Selected: " + node.getPath()))
            .onDoubleClick(node -> {
                if (node.isLeaf()) {
                    Dialog.showMessage("Open File", "Opening: " + node.getTitle(), Dialog.DialogType.INFORMATION);
                }
            })
            .onExpand(node -> System.out.println("Expanded: " + node.getTitle()));

        // Tree controls
        Panel treeControls = Panel.create()
            .flowLayout()
            .padding(5);

        Button expandAllBtn = Button.create("Expand All")
            .onClick(() -> fileTree.expandAll());

        Button collapseAllBtn = Button.create("Collapse All")
            .onClick(() -> fileTree.collapseAll());

        Button addNodeBtn = Button.create("Add File")
            .onClick(() -> addRandomFile());

        treeControls.add(expandAllBtn).add(collapseAllBtn).add(addNodeBtn);

        treePanel.add(treeControls, Layout.BorderConstraints.NORTH)
                 .add(fileTree, Layout.BorderConstraints.CENTER);

        mainTabPane.addTab("File Tree", treePanel, "Hierarchical tree view with file system simulation");
    }

    private void createComponentShowcaseTab() {
        Panel showcasePanel = Panel.create()
            .layout(Layout.border())
            .padding(10);

        TabPane componentTabs = TabPane.create()
            .tabsOnLeft();

        // Input Components Tab
        Panel inputPanel = Panel.create()
            .layout(Layout.flow())
            .padding(10);

        // Form with various inputs
        Panel formPanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(15)
            .verticalLayout();

        Label formTitle = Label.create("Input Components Demo")
            .font("Arial", Font.BOLD, 16);

        ComboBox countryCombo = ComboBox.create("USA", "Canada", "UK", "Germany", "France")
            .selectedItem("USA")
            .onChange(country -> System.out.println("Country: " + country));

        Spinner ageSpinner = Spinner.createNumber(25, 18, 100, 1)
            .onNumberChange(age -> System.out.println("Age: " + age));

        ProgressBar progressBar = ProgressBar.create(100)
            .value(0)
            .showString()
            .text("Progress: 0%");

        Button animateProgress = Button.create("Animate Progress")
            .onClick(() -> {
                progressBar.setToMinimum();
                progressBar.animateTo(100, 3000);
            });

        formPanel.add(formTitle)
                 .add(Label.create("Country:")).add(countryCombo)
                 .add(Label.create("Age:")).add(ageSpinner)
                 .add(Label.create("Progress:")).add(progressBar)
                 .add(animateProgress);

        inputPanel.add(formPanel);
        componentTabs.addTab("Inputs", inputPanel);

        // Lists Tab
        Panel listsPanel = Panel.create()
            .layout(Layout.flow())
            .padding(10);

        ListBox multiList = ListBox.create("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
            .multipleSelection()
            .visibleRowCount(6)
            .onMultiSelection(items -> System.out.println("Selected items: " + items));

        listsPanel.add(multiList);
        componentTabs.addTab("Lists", listsPanel);

        showcasePanel.add(componentTabs, Layout.BorderConstraints.CENTER);
        mainTabPane.addTab("Components", showcasePanel, "Showcase of all available components");
    }

    private void createLayoutExamplesTab() {
        Panel layoutPanel = Panel.create()
            .layout(Layout.border());

        TabPane layoutTabs = TabPane.create();

        // Border Layout Demo
        Panel borderDemo = createBorderLayoutDemo();
        layoutTabs.addTab("Border", borderDemo);

        // Flow Layout Demo  
        Panel flowDemo = createFlowLayoutDemo();
        layoutTabs.addTab("Flow", flowDemo);

        // Grid Layout Demo
        Panel gridDemo = createGridLayoutDemo();
        layoutTabs.addTab("Grid", gridDemo);

        layoutPanel.add(layoutTabs, Layout.BorderConstraints.CENTER);
        mainTabPane.addTab("Layouts", layoutPanel, "Examples of different layout managers");
    }

    private Panel createBorderLayoutDemo() {
        Panel demo = Panel.create()
            .layout(Layout.border())
            .padding(10);

        Panel north = Panel.create()
            .backgroundColor(Color.RED)
            .add(Label.create("NORTH").foregroundColor(Color.WHITE).alignCenter());

        Panel south = Panel.create()
            .backgroundColor(Color.BLUE)
            .add(Label.create("SOUTH").foregroundColor(Color.WHITE).alignCenter());

        Panel west = Panel.create()
            .backgroundColor(Color.GREEN)
            .add(Label.create("WEST").foregroundColor(Color.WHITE).alignCenter());

        Panel east = Panel.create()
            .backgroundColor(Color.ORANGE)
            .add(Label.create("EAST").alignCenter());

        Panel center = Panel.create()
            .backgroundColor(Color.YELLOW)
            .add(Label.create("CENTER").alignCenter());

        demo.add(north, Layout.BorderConstraints.NORTH)
            .add(south, Layout.BorderConstraints.SOUTH)
            .add(west, Layout.BorderConstraints.WEST)
            .add(east, Layout.BorderConstraints.EAST)
            .add(center, Layout.BorderConstraints.CENTER);

        return demo;
    }

    private Panel createFlowLayoutDemo() {
        Panel demo = Panel.create()
            .flowLayout()
            .padding(10);

        for (int i = 1; i <= 10; i++) {
            Button btn = Button.create("Button " + i)
                .onClick(() -> Dialog.showMessage("Button clicked!"));
            demo.add(btn);
        }

        return demo;
    }

    private Panel createGridLayoutDemo() {
        Panel demo = Panel.create()
            .gridLayout(3, 3)
            .padding(10);

        for (int i = 1; i <= 9; i++) {
            Panel cell = Panel.create()
                .backgroundColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)))
                .add(Label.create("Cell " + i).alignCenter());
            demo.add(cell);
        }

        return demo;
    }

    private void createStatusBar() {
        Panel statusBar = Panel.create()
            .backgroundColor(Color.LIGHT_GRAY)
            .flowLayout()
            .padding(5);

        Label statusLabel = Label.create("Ready - Advanced Features Demo Loaded")
            .font("Arial", Font.PLAIN, 12);

        Label themeLabel = Label.create("Theme: " + Theme.getCurrentTheme())
            .font("Arial", Font.PLAIN, 12);

        statusBar.add(statusLabel).add(Label.create(" | ")).add(themeLabel);
        mainWindow.add(statusBar, Layout.BorderConstraints.SOUTH);
    }

    private void renderHUD(Graphics2D g2d) {
        // Render animated HUD elements
        long time = System.currentTimeMillis();
        
        // Animated crosshair
        int centerX = hudViewport.getViewportWidth() / 2;
        int centerY = hudViewport.getViewportHeight() / 2;
        
        g2d.setColor(new Color(0, 255, 0, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(centerX - 20, centerY, centerX + 20, centerY);
        g2d.drawLine(centerX, centerY - 20, centerX, centerY + 20);
        g2d.drawOval(centerX - 30, centerY - 30, 60, 60);

        // Animated radar sweep
        double angle = (time / 50.0) % (2 * Math.PI);
        int radarX = centerX + (int)(100 * Math.cos(angle));
        int radarY = centerY + (int)(100 * Math.sin(angle));
        
        g2d.setColor(new Color(0, 255, 255, 100));
        g2d.drawLine(centerX, centerY, radarX, radarY);

        // Grid pattern
        g2d.setColor(new Color(255, 255, 255, 30));
        for (int x = 0; x < hudViewport.getViewportWidth(); x += 50) {
            g2d.drawLine(x, 0, x, hudViewport.getViewportHeight());
        }
        for (int y = 0; y < hudViewport.getViewportHeight(); y += 50) {
            g2d.drawLine(0, y, hudViewport.getViewportWidth(), y);
        }

        // Animated data streams
        g2d.setColor(new Color(0, 255, 0, 200));
        g2d.setFont(new Font("Courier", Font.BOLD, 12));
        for (int i = 0; i < 5; i++) {
            int y = (int)((time / 10 + i * 50) % hudViewport.getViewportHeight());
            g2d.drawString("DATA_STREAM_" + i + "_ACTIVE", 10, y);
        }
    }

    private void createHUDOverlays() {
        // Health bar overlay
        Panel healthPanel = Panel.create()
            .backgroundColor(new Color(0, 0, 0, 150))
            .padding(5);
        
        ProgressBar healthBar = ProgressBar.create(100)
            .value(75)
            .progressColor(Color.GREEN)
            .size(150, 20);
        
        healthPanel.add(Label.create("Health:").foregroundColor(Color.WHITE))
                   .add(healthBar);

        hudViewport.addOverlay(healthPanel, Viewport.HUDPosition.TOP_LEFT, 10, 10);

        // Mini-map overlay
        Panel miniMapPanel = Panel.create()
            .backgroundColor(new Color(0, 0, 0, 180))
            .size(100, 100)
            .border(Color.WHITE, 1);

        hudViewport.addOverlay(miniMapPanel, Viewport.HUDPosition.TOP_RIGHT, 10, 10);
    }

    // Event handlers and utility methods
    private void addSampleTableData() {
        dataTable.addRow(1, "John Doe", "Engineering", "$75,000", "Active")
                .addRow(2, "Jane Smith", "Marketing", "$65,000", "Active") 
                .addRow(3, "Bob Wilson", "Sales", "$55,000", "Active")
                .addRow(4, "Alice Brown", "HR", "$60,000", "Inactive")
                .addRow(5, "Charlie Davis", "Engineering", "$80,000", "Active");
    }

    private void addRandomEmployee() {
        String[] names = {"Mike Johnson", "Sarah Wilson", "Tom Brown", "Lisa Davis", "Kevin Lee"};
        String[] departments = {"Engineering", "Marketing", "Sales", "HR", "Finance"};
        String[] statuses = {"Active", "Inactive"};
        
        int id = dataTable.getRowCount() + 1;
        String name = names[random.nextInt(names.length)];
        String dept = departments[random.nextInt(departments.length)];
        String salary = "$" + (50000 + random.nextInt(50000));
        String status = statuses[random.nextInt(statuses.length)];
        
        dataTable.addRow(id, name, dept, salary, status);
    }

    private void removeSelectedEmployee() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow >= 0) {
            dataTable.removeRow(selectedRow);
        }
    }

    private void addRandomFile() {
        String[] fileNames = {"document.txt", "image.png", "video.mp4", "archive.zip", "config.json"};
        String fileName = fileNames[random.nextInt(fileNames.length)];
        
        // Add to Downloads folder
        Tree.TreeNode root = fileTree.getRootNode();
        Tree.TreeNode downloads = root.findChild("Downloads");
        if (downloads != null) {
            downloads.addChild(fileName);
        }
    }

    private void showNewDialog() {
        Dialog.createMessageDialog("This would create a new file in a real application.")
              .show();
    }

    private void showTestDialog() {
        Dialog dialog = Dialog.create("Custom Dialog Test")
            .size(400, 200)
            .centerOnParent();

        Panel content = Panel.create()
            .layout(Layout.border())
            .padding(20);

        content.add(Label.create("This is a custom dialog with multiple buttons").alignCenter(), 
                   Layout.BorderConstraints.CENTER);

        dialog.content(content)
              .buttons(
                  Dialog.DialogButton.ok(),
                  Dialog.DialogButton.cancel(),
                  Dialog.DialogButton.create("Custom", Dialog.DialogResult.OK)
              )
              .onResult(result -> System.out.println("Dialog result: " + result))
              .show();
    }

    private void showConfirmDialog() {
        Dialog.createConfirmDialog("Do you want to continue with this operation?", confirmed -> {
            String message = confirmed ? "Operation confirmed!" : "Operation cancelled.";
            Dialog.showMessage(message);
        }).show();
    }

    private void showAboutDialog() {
        Dialog dialog = Dialog.create("About Vibe UI")
            .size(500, 300)
            .centerOnParent();

        Panel content = Panel.create()
            .layout(Layout.border())
            .padding(20);

        String aboutText = """
            Vibe UI Library v2.0
            
            A comprehensive, modern Java UI library featuring:
            • Advanced theming with 9+ built-in themes
            • HUD viewport with custom rendering
            • Professional data components (Table, Tree)
            • Tabbed interfaces and dialog system
            • Complete menu system
            • Multiple layout managers
            
            Built with fluent API design for maximum productivity.
            """;

        Label aboutLabel = Label.create(aboutText)
            .font("Arial", Font.PLAIN, 12)
            .alignCenter();

        content.add(aboutLabel, Layout.BorderConstraints.CENTER);

        dialog.content(content)
              .buttons(Dialog.DialogButton.ok())
              .show();
    }

    private void showShortcutsDialog() {
        Dialog dialog = Dialog.create("Keyboard Shortcuts")
            .size(400, 250)
            .centerOnParent();

        Panel content = Panel.create()
            .layout(Layout.border())
            .padding(20);

        String shortcuts = """
            Alt+F - File Menu
            Alt+V - View Menu (Themes)
            Alt+T - Tools Menu
            Alt+H - Help Menu
            
            Tab Navigation:
            Ctrl+Tab - Next Tab
            Ctrl+Shift+Tab - Previous Tab
            """;

        Label shortcutsLabel = Label.create(shortcuts)
            .font("Courier", Font.PLAIN, 12);

        content.add(shortcutsLabel, Layout.BorderConstraints.CENTER);

        dialog.content(content)
              .buttons(Dialog.DialogButton.ok())
              .show();
    }
}