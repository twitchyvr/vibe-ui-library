# Best Practices for Vibe UI Development

This guide covers best practices for building professional desktop applications with Vibe UI.

## 🏗️ Application Architecture

### 1. Separate UI from Business Logic

Keep your UI code separate from business logic using the MVC or MVP pattern:

```java
// ❌ Bad: Business logic mixed with UI
public class OrderForm {
    private TextField customerField;
    private TextField amountField;
    
    private void saveOrder() {
        String customer = customerField.getText();
        double amount = Double.parseDouble(amountField.getText());
        
        // Business logic mixed in UI class
        try {
            Connection db = DriverManager.getConnection(...);
            PreparedStatement stmt = db.prepareStatement("INSERT INTO orders...");
            // ... database code
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }
}

// ✅ Good: Separated concerns
public class OrderForm {
    private final OrderService orderService = new OrderService();
    private TextField customerField;
    private TextField amountField;
    
    private void saveOrder() {
        String customer = customerField.getText();
        String amountText = amountField.getText();
        
        try {
            Order order = new Order(customer, Double.parseDouble(amountText));
            orderService.saveOrder(order);
            showSuccess("Order saved successfully");
            clearForm();
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        } catch (OrderServiceException e) {
            showError("Could not save order: " + e.getMessage());
        }
    }
}

public class OrderService {
    private final OrderRepository repository = new OrderRepository();
    
    public void saveOrder(Order order) throws OrderServiceException {
        validateOrder(order);
        repository.save(order);
    }
    
    private void validateOrder(Order order) throws OrderServiceException {
        if (order.getCustomer() == null || order.getCustomer().trim().isEmpty()) {
            throw new OrderServiceException("Customer name is required");
        }
        if (order.getAmount() <= 0) {
            throw new OrderServiceException("Amount must be positive");
        }
    }
}
```

### 2. Use Builder Pattern for Complex Components

For complex UI setup, use builder classes:

```java
public class DialogBuilder {
    private String title = "Dialog";
    private int width = 300;
    private int height = 200;
    private List<Component<?>> components = new ArrayList<>();
    private Runnable onOk;
    private Runnable onCancel;
    
    public static DialogBuilder create() {
        return new DialogBuilder();
    }
    
    public DialogBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    public DialogBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }
    
    public DialogBuilder addComponent(Component<?> component) {
        this.components.add(component);
        return this;
    }
    
    public DialogBuilder onOk(Runnable action) {
        this.onOk = action;
        return this;
    }
    
    public DialogBuilder onCancel(Runnable action) {
        this.onCancel = action;
        return this;
    }
    
    public Window build() {
        Window dialog = Window.create(title)
            .size(width, height)
            .centerOnScreen()
            .layout(Layout.border());
        
        Panel contentPanel = Panel.create()
            .layout(Layout.flow())
            .padding(10);
        
        components.forEach(contentPanel::add);
        
        Panel buttonPanel = Panel.create()
            .flowLayout()
            .add(Button.create("OK").onClick(onOk != null ? onOk : () -> {}))
            .add(Button.create("Cancel").onClick(onCancel != null ? onCancel : () -> {}));
        
        dialog.add(contentPanel, Layout.BorderConstraints.CENTER)
              .add(buttonPanel, Layout.BorderConstraints.SOUTH);
        
        return dialog;
    }
}

// Usage
DialogBuilder.create()
    .title("Confirm Action")
    .size(400, 200)
    .addComponent(Label.create("Are you sure you want to delete this item?"))
    .onOk(() -> deleteItem())
    .onCancel(() -> dialog.hide())
    .build()
    .show();
```

## 🎯 Event Handling Best Practices

### 1. Use Method References When Possible

```java
// ❌ Less readable
Button.create("Save").onClick(() -> this.save());
Button.create("Load").onClick(() -> this.load());
Button.create("Exit").onClick(() -> this.exit());

// ✅ More readable
Button.create("Save").onClick(this::save);
Button.create("Load").onClick(this::load);
Button.create("Exit").onClick(this::exit);
```

### 2. Handle Exceptions Gracefully

```java
// ❌ Unhandled exceptions can crash the UI
Button.create("Load File").onClick(() -> {
    FileInputStream file = new FileInputStream("data.txt"); // May throw IOException
    processFile(file); // May throw various exceptions
});

// ✅ Proper exception handling
Button.create("Load File").onClick(() -> {
    try {
        FileInputStream file = new FileInputStream("data.txt");
        processFile(file);
        showSuccess("File loaded successfully");
    } catch (FileNotFoundException e) {
        showError("File not found: data.txt");
    } catch (IOException e) {
        showError("Error reading file: " + e.getMessage());
    } catch (ProcessingException e) {
        showError("Error processing file: " + e.getMessage());
    } catch (Exception e) {
        showError("Unexpected error: " + e.getMessage());
        logger.error("Unexpected error in file loading", e);
    }
});
```

### 3. Debounce Frequent Events

For events that fire frequently (like text changes), use debouncing:

```java
public class DebounceHelper {
    private Timer timer;
    
    public void debounce(Runnable action, int delayMs) {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(delayMs, e -> action.run());
        timer.setRepeats(false);
        timer.start();
    }
}

// Usage
DebounceHelper debouncer = new DebounceHelper();

TextField searchField = TextField.create("Search...")
    .onChange(text -> {
        // Debounce search to avoid excessive API calls
        debouncer.debounce(() -> performSearch(text), 300);
    });
```

## 💅 UI Design Best Practices

### 1. Consistent Spacing and Sizing

Define constants for consistent spacing:

```java
public class UIConstants {
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;
    
    public static final int BUTTON_WIDTH = 100;
    public static final int BUTTON_HEIGHT = 30;
    
    public static final int FIELD_HEIGHT = 25;
    
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color ERROR_COLOR = new Color(220, 53, 69);
}

// Usage
Panel form = Panel.create()
    .padding(UIConstants.PADDING_MEDIUM)
    .add(Button.create("Submit")
        .size(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT)
        .backgroundColor(UIConstants.PRIMARY_COLOR));
```

### 2. Create Reusable UI Components

```java
public class FormFieldBuilder {
    public static Panel createLabeledField(String labelText, TextField field) {
        Panel fieldPanel = Panel.create()
            .layout(Layout.flow(FlowLayout.LEFT))
            .add(Label.create(labelText)
                .size(100, UIConstants.FIELD_HEIGHT)
                .alignRight())
            .add(field);
        
        return fieldPanel;
    }
    
    public static Panel createButtonPanel(Button... buttons) {
        Panel buttonPanel = Panel.create()
            .flowLayout()
            .padding(UIConstants.PADDING_MEDIUM);
        
        for (Button button : buttons) {
            button.size(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT);
            buttonPanel.add(button);
        }
        
        return buttonPanel;
    }
}

// Usage
Panel form = Panel.create()
    .verticalLayout()
    .add(FormFieldBuilder.createLabeledField("Name:", 
        TextField.create().onChange(this::validateName)))
    .add(FormFieldBuilder.createLabeledField("Email:", 
        TextField.create().onChange(this::validateEmail)))
    .add(FormFieldBuilder.createButtonPanel(
        Button.create("Save").onClick(this::save),
        Button.create("Cancel").onClick(this::cancel)
    ));
```

### 3. Responsive Design

Make your UI responsive to different screen sizes:

```java
public class ResponsiveHelper {
    public static Dimension getPreferredWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Use 80% of screen size, but with reasonable min/max
        int width = Math.min(Math.max((int)(screenSize.width * 0.8), 600), 1200);
        int height = Math.min(Math.max((int)(screenSize.height * 0.8), 400), 800);
        
        return new Dimension(width, height);
    }
    
    public static boolean isSmallScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.width < 1024 || screenSize.height < 768;
    }
}

// Usage
Dimension windowSize = ResponsiveHelper.getPreferredWindowSize();
boolean isSmall = ResponsiveHelper.isSmallScreen();

Window window = Window.create("My App")
    .size(windowSize.width, windowSize.height)
    .centerOnScreen()
    .layout(isSmall ? Layout.flow() : Layout.border());
```

## 🔧 Performance Best Practices

### 1. Lazy Loading of Components

For applications with many components, use lazy loading:

```java
public class TabbedInterface {
    private Panel tab1Panel;
    private Panel tab2Panel;
    private Panel tab3Panel;
    
    private boolean tab1Loaded = false;
    private boolean tab2Loaded = false;
    private boolean tab3Loaded = false;
    
    private void createTabs() {
        // Only create tab content when needed
        ComboBox tabSelector = ComboBox.create("Tab 1", "Tab 2", "Tab 3")
            .onChange(this::switchTab);
        
        // Load first tab immediately
        loadTab1();
    }
    
    private void switchTab(String tabName) {
        switch (tabName) {
            case "Tab 1":
                if (!tab1Loaded) loadTab1();
                showPanel(tab1Panel);
                break;
            case "Tab 2":
                if (!tab2Loaded) loadTab2();
                showPanel(tab2Panel);
                break;
            case "Tab 3":
                if (!tab3Loaded) loadTab3();
                showPanel(tab3Panel);
                break;
        }
    }
    
    private void loadTab1() {
        if (!tab1Loaded) {
            tab1Panel = createTab1Content();
            tab1Loaded = true;
        }
    }
}
```

### 2. Efficient Event Handling

Avoid creating unnecessary objects in event handlers:

```java
// ❌ Creates new objects on every event
TextField searchField = TextField.create()
    .onChange(text -> {
        List<String> results = new ArrayList<>(); // Created every time
        String lowerText = text.toLowerCase();    // Created every time
        performSearch(results, lowerText);
    });

// ✅ Reuse objects where possible
public class SearchHandler {
    private final List<String> searchResults = new ArrayList<>();
    
    public void handleSearch(String text) {
        searchResults.clear(); // Reuse existing list
        String lowerText = text.toLowerCase();
        performSearch(searchResults, lowerText);
    }
}

SearchHandler searchHandler = new SearchHandler();
TextField searchField = TextField.create()
    .onChange(searchHandler::handleSearch);
```

### 3. Background Processing

Use background threads for long-running operations:

```java
public class BackgroundTaskHelper {
    public static void runInBackground(Runnable task, Consumer<Exception> errorHandler) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                task.run();
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                } catch (Exception e) {
                    if (errorHandler != null) {
                        errorHandler.accept(e);
                    }
                }
            }
        };
        worker.execute();
    }
}

// Usage
Button processButton = Button.create("Process Large File")
    .onClick(() -> {
        processButton.enabled(false);
        progressBar.indeterminate(true);
        
        BackgroundTaskHelper.runInBackground(
            () -> processLargeFile(), // Background task
            exception -> {             // Error handler (runs on EDT)
                processButton.enabled(true);
                progressBar.indeterminate(false);
                showError("Processing failed: " + exception.getMessage());
            }
        );
    });
```

## 🧪 Testing Best Practices

### 1. Testable UI Code

Make your UI code testable by extracting logic:

```java
// ❌ Hard to test - UI and logic coupled
public class CalculatorUI {
    private TextField displayField;
    
    private void addButtonPressed() {
        String current = displayField.getText();
        double value = Double.parseDouble(current);
        double result = value + 10; // Business logic mixed in
        displayField.text(String.valueOf(result));
    }
}

// ✅ Easy to test - logic separated
public class Calculator {
    public double add(double a, double b) {
        return a + b;
    }
    
    public boolean isValidNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

public class CalculatorUI {
    private final Calculator calculator = new Calculator();
    private TextField displayField;
    
    private void addButtonPressed() {
        String current = displayField.getText();
        
        if (!calculator.isValidNumber(current)) {
            showError("Invalid number");
            return;
        }
        
        double currentValue = Double.parseDouble(current);
        double result = calculator.add(currentValue, 10);
        displayField.text(String.valueOf(result));
    }
}

// Test the logic separately
@Test
public void testCalculatorAdd() {
    Calculator calc = new Calculator();
    assertEquals(15.0, calc.add(10, 5), 0.001);
}
```

### 2. UI Integration Testing

Create helper methods for UI testing:

```java
public class UITestHelper {
    public static void clickButton(Button button) {
        // Simulate button click
        button.getSwingComponent().doClick();
    }
    
    public static void setText(TextField field, String text) {
        field.text(text);
        // Simulate text change event
        field.getSwingComponent().firePropertyChange("text", "", text);
    }
    
    public static void waitForUI() throws InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            // Wait for EDT to complete
        });
    }
}
```

## 🔒 Security Best Practices

### 1. Input Validation

Always validate user input:

```java
public class ValidationHelper {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }
    
    public static boolean isValidAge(String ageText) {
        try {
            int age = Integer.parseInt(ageText);
            return age >= 0 && age <= 150;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"'&]", "")
                   .trim()
                   .substring(0, Math.min(input.length(), 1000));
    }
}

// Usage
TextField emailField = TextField.create("Email")
    .onChange(email -> {
        if (ValidationHelper.isValidEmail(email)) {
            emailField.foregroundColor(Color.BLACK);
            submitButton.enabled(true);
        } else {
            emailField.foregroundColor(Color.RED);
            submitButton.enabled(false);
        }
    });
```

### 2. Secure File Operations

```java
public class FileHelper {
    private static final Set<String> ALLOWED_EXTENSIONS = 
        Set.of(".txt", ".pdf", ".doc", ".docx", ".jpg", ".png");
    
    public static boolean isSafeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        // Check for directory traversal
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            return false;
        }
        
        // Check extension
        String lowerName = fileName.toLowerCase();
        return ALLOWED_EXTENSIONS.stream().anyMatch(lowerName::endsWith);
    }
    
    public static File createSafeFile(String directory, String fileName) {
        if (!isSafeFileName(fileName)) {
            throw new IllegalArgumentException("Unsafe file name: " + fileName);
        }
        
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        return new File(dir, fileName);
    }
}
```

## 📊 Logging and Monitoring

### 1. Structured Logging

```java
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserInterface {
    private static final Logger logger = Logger.getLogger(UserInterface.class.getName());
    
    private void saveData() {
        logger.info("Starting data save operation");
        
        try {
            dataService.save(getData());
            logger.info("Data saved successfully");
            showSuccess("Data saved");
        } catch (DataServiceException e) {
            logger.log(Level.SEVERE, "Failed to save data", e);
            showError("Save failed: " + e.getMessage());
        }
    }
}
```

### 2. User Action Tracking

```java
public class ActionTracker {
    private static final Logger actionLogger = Logger.getLogger("UserActions");
    
    public static void trackAction(String action, String component) {
        actionLogger.info(String.format("Action: %s, Component: %s, Time: %d", 
            action, component, System.currentTimeMillis()));
    }
}

// Usage
Button saveButton = Button.create("Save")
    .onClick(() -> {
        ActionTracker.trackAction("click", "saveButton");
        save();
    });
```

## 🎯 Summary

Following these best practices will help you build:

- **Maintainable** applications with clean separation of concerns
- **Responsive** UIs that work well on different screen sizes  
- **Performant** applications that handle resources efficiently
- **Testable** code with proper abstraction layers
- **Secure** applications that validate input and handle errors gracefully
- **Professional** applications with consistent styling and behavior

Remember: Good UI code is not just about making things work—it's about making them work reliably, efficiently, and maintainably over time.