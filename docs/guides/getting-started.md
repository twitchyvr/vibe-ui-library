# Getting Started with Vibe UI

Welcome to Vibe UI! This guide will help you create your first desktop application using our fluent Java UI library.

## 📋 Prerequisites

Before you begin, make sure you have:

- **Java 11 or higher** installed
- **Maven 3.6+** (if using Maven)
- **IDE** of your choice (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 🚀 Quick Setup

### 1. Add Vibe UI to Your Project

#### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.vibeui</groupId>
    <artifactId>vibe-ui</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle

Add this to your `build.gradle`:

```gradle
dependencies {
    implementation 'com.vibeui:vibe-ui:1.0.0'
}
```

#### Manual JAR

1. Download the JAR from releases
2. Add it to your classpath: `java -cp vibe-ui-1.0.0.jar:. YourApp`

### 2. Your First Application

Create a simple "Hello World" application:

```java
import com.vibeui.*;
import javax.swing.SwingUtilities;
import java.awt.Font;

public class HelloWorld {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window.create("Hello Vibe UI")
                .size(400, 200)
                .centerOnScreen()
                .layout(Layout.flow())
                .add(Label.create("Welcome to Vibe UI!")
                    .font("Arial", Font.BOLD, 20))
                .add(Button.create("Click Me!")
                    .onClick(() -> System.out.println("Hello World!")))
                .build()
                .show();
        });
    }
}
```

### 3. Run Your Application

```bash
javac -cp vibe-ui-1.0.0.jar HelloWorld.java
java -cp vibe-ui-1.0.0.jar:. HelloWorld
```

## 🎯 Core Concepts

### The Fluent API

Vibe UI uses a fluent API pattern that allows you to chain method calls:

```java
Button saveButton = Button.create("Save")
    .size(100, 30)
    .backgroundColor(Color.BLUE)
    .foregroundColor(Color.WHITE)
    .onClick(() -> saveFile())
    .tooltip("Save the current file");
```

### Component Lifecycle

Every component follows this pattern:

1. **Create**: Use static factory methods like `Button.create()`
2. **Configure**: Chain methods to set properties
3. **Build**: Call `.build()` if needed (optional for most components)
4. **Add**: Add to a container using `.add()`

```java
// Create and configure
TextField nameField = TextField.create("Enter name")
    .size(200, 30)
    .onChange(text -> validateName(text));

// Add to container
Panel form = Panel.create()
    .add(Label.create("Name:"))
    .add(nameField);
```

### Event Handling

Use lambda expressions for clean event handling:

```java
// Simple action
Button.create("Print").onClick(() -> print());

// With validation
TextField.create().onChange(text -> {
    if (text.length() < 3) {
        showError("Name too short");
    } else {
        clearError();
    }
});
```

## 🏗️ Building Your First Real Application

Let's create a simple calculator application:

### Step 1: Set Up the Main Window

```java
public class Calculator {
    private TextField displayField;
    private double firstNumber = 0;
    private String operation = "";
    private boolean startNewNumber = true;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator().createUI());
    }
    
    private void createUI() {
        Window window = Window.create("Calculator")
            .size(300, 400)
            .centerOnScreen()
            .resizable(false)
            .layout(Layout.border());
        
        createDisplay();
        createButtons();
        
        window.add(displayPanel, Layout.BorderConstraints.NORTH)
              .add(buttonPanel, Layout.BorderConstraints.CENTER)
              .build()
              .show();
    }
}
```

### Step 2: Create the Display

```java
private Panel displayPanel;

private void createDisplay() {
    displayPanel = Panel.create()
        .backgroundColor(Color.BLACK)
        .padding(10);
    
    displayField = TextField.create("0")
        .size(280, 50)
        .font("Arial", Font.BOLD, 24)
        .backgroundColor(Color.BLACK)
        .foregroundColor(Color.WHITE)
        .editable(false)
        .alignRight();
    
    displayPanel.add(displayField);
}
```

### Step 3: Create the Button Grid

```java
private Panel buttonPanel;

private void createButtons() {
    buttonPanel = Panel.create()
        .layout(Layout.grid(5, 4, 2, 2))
        .padding(10);
    
    // Number buttons
    for (int i = 0; i <= 9; i++) {
        final int number = i;
        Button numButton = Button.create(String.valueOf(i))
            .size(60, 60)
            .font("Arial", Font.BOLD, 18)
            .onClick(() -> numberPressed(number));
        buttonPanel.add(numButton);
    }
    
    // Operation buttons
    Button addButton = Button.create("+")
        .size(60, 60)
        .backgroundColor(Color.ORANGE)
        .onClick(() -> operationPressed("+"));
    
    Button equalsButton = Button.create("=")
        .size(60, 60)
        .backgroundColor(Color.ORANGE)
        .onClick(() -> calculateResult());
    
    Button clearButton = Button.create("C")
        .size(60, 60)
        .backgroundColor(Color.RED)
        .foregroundColor(Color.WHITE)
        .onClick(() -> clear());
    
    // Add all buttons to panel
    buttonPanel.add(clearButton)
              .add(addButton)
              .add(equalsButton);
}
```

### Step 4: Implement Calculator Logic

```java
private void numberPressed(int number) {
    if (startNewNumber) {
        displayField.text(String.valueOf(number));
        startNewNumber = false;
    } else {
        String current = displayField.getText();
        if (!current.equals("0")) {
            displayField.text(current + number);
        } else {
            displayField.text(String.valueOf(number));
        }
    }
}

private void operationPressed(String op) {
    firstNumber = Double.parseDouble(displayField.getText());
    operation = op;
    startNewNumber = true;
}

private void calculateResult() {
    if (!operation.isEmpty()) {
        double secondNumber = Double.parseDouble(displayField.getText());
        double result = 0;
        
        switch (operation) {
            case "+": result = firstNumber + secondNumber; break;
            case "-": result = firstNumber - secondNumber; break;
            case "*": result = firstNumber * secondNumber; break;
            case "/": result = firstNumber / secondNumber; break;
        }
        
        displayField.text(String.valueOf(result));
        operation = "";
        startNewNumber = true;
    }
}

private void clear() {
    displayField.text("0");
    firstNumber = 0;
    operation = "";
    startNewNumber = true;
}
```

## 🎨 Styling and Theming

### Colors and Fonts

```java
// Use predefined colors
button.backgroundColor(Color.BLUE)
      .foregroundColor(Color.WHITE);

// Use custom colors
Color customBlue = new Color(70, 130, 180);
panel.backgroundColor(customBlue);

// Fonts
label.font("Arial", Font.BOLD, 16);
title.font(new Font("Times New Roman", Font.ITALIC, 24));
```

### Borders and Spacing

```java
// Add borders
panel.border(Color.GRAY, 2);
panel.border(BorderFactory.createRaisedBorder());

// Add padding
panel.padding(10);                    // All sides
panel.padding(5, 10, 5, 10);         // Top, right, bottom, left
```

### Layout Tips

```java
// Flow layout for toolbars
Panel toolbar = Panel.create()
    .flowLayout()
    .add(saveButton)
    .add(loadButton)
    .add(exitButton);

// Border layout for main windows
Window.create("App")
    .layout(Layout.border())
    .add(toolbar, Layout.BorderConstraints.NORTH)
    .add(contentPanel, Layout.BorderConstraints.CENTER)
    .add(statusBar, Layout.BorderConstraints.SOUTH);

// Grid layout for forms
Panel form = Panel.create()
    .layout(Layout.grid(3, 2, 5, 5)) // 3 rows, 2 cols, 5px gaps
    .add(Label.create("Name:")).add(nameField)
    .add(Label.create("Email:")).add(emailField)
    .add(Label.create("Phone:")).add(phoneField);
```

## 📱 Responsive Design

### Handling Different Screen Sizes

```java
// Get screen dimensions
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
int screenWidth = screenSize.width;
int screenHeight = screenSize.height;

// Size window as percentage of screen
int windowWidth = (int)(screenWidth * 0.8);
int windowHeight = (int)(screenHeight * 0.8);

Window.create("Responsive App")
    .size(windowWidth, windowHeight)
    .centerOnScreen();
```

### Flexible Layouts

```java
// Use preferred sizes instead of fixed sizes
TextField flexibleField = TextField.create("Flexible")
    .preferredSize(200, 30);  // Preferred, but can resize

// Let panels grow with content
Panel flexPanel = Panel.create()
    .layout(new BorderLayout())
    .add(scrollableContent, BorderLayout.CENTER);
```

## 🔍 Debugging and Testing

### Debug Event Handling

```java
Button debugButton = Button.create("Debug")
    .onClick(() -> {
        System.out.println("Button clicked at: " + System.currentTimeMillis());
        performAction();
    });
```

### Component Inspection

```java
// Log component state
TextField field = TextField.create()
    .onChange(text -> {
        System.out.println("Field value: '" + text + "'");
        System.out.println("Field size: " + field.getSize());
        System.out.println("Field enabled: " + field.isEnabled());
    });
```

### Error Handling

```java
Button riskyButton = Button.create("Process")
    .onClick(() -> {
        try {
            performRiskyOperation();
            showSuccess("Operation completed successfully");
        } catch (Exception e) {
            showError("Operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    });
```

## 🚀 Next Steps

Now that you've mastered the basics:

1. **Explore the [API Documentation](../api/README.md)** for complete component reference
2. **Try the [Examples](../examples/)** for more complex applications
3. **Read the [Best Practices Guide](best-practices.md)** for professional development
4. **Check out [Advanced Topics](advanced-topics.md)** for power user features

## 💡 Tips for Success

### 1. Use SwingUtilities.invokeLater()

Always wrap UI creation in `SwingUtilities.invokeLater()`:

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        // UI creation code here
    });
}
```

### 2. Separate UI from Logic

Keep your business logic separate from UI code:

```java
public class UserInterface {
    private BusinessLogic logic = new BusinessLogic();
    
    private void createSaveButton() {
        Button.create("Save")
            .onClick(() -> logic.saveData(getData()));
    }
}
```

### 3. Use Method References

Clean up lambda expressions with method references:

```java
// Instead of this:
Button.create("Save").onClick(() -> this.save());

// Use this:
Button.create("Save").onClick(this::save);
```

### 4. Handle Exceptions Gracefully

Always handle potential exceptions in event handlers:

```java
Button.create("Load File")
    .onClick(() -> {
        try {
            loadFile();
        } catch (IOException e) {
            showErrorDialog("Could not load file: " + e.getMessage());
        }
    });
```

## 🎉 Congratulations!

You've now learned the fundamentals of Vibe UI. You can create windows, add components, handle events, and style your applications. The fluent API makes it easy to build professional desktop applications with clean, readable code.

Happy coding! 🚀