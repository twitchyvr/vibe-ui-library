# Vibe UI Documentation Hub

Welcome to the complete documentation for Vibe UI - the modern, fluent Java UI library for desktop applications.

## 📚 Documentation Overview

| Category | Description | Best For |
|----------|-------------|----------|
| **[🚀 Getting Started](guides/getting-started.md)** | Step-by-step tutorials, first application, core concepts | New users, first-time setup |
| **[📋 API Reference](api/README.md)** | Complete method documentation for all components | Looking up specific methods |
| **[📖 Complete Examples](examples/complete-applications.md)** | Full working applications with source code | Learning patterns, copy-paste solutions |
| **[💡 Best Practices](guides/best-practices.md)** | Professional development patterns and conventions | Production applications |

## 🎯 Quick Navigation

### 👶 **New to Vibe UI?**
Start with the **[Getting Started Guide](guides/getting-started.md)** to build your first application in minutes.

### 🔍 **Need a Specific Method?**
Check the **[API Documentation](api/README.md)** for complete reference with examples.

### 🏗️ **Want to See Complete Apps?**
Browse **[Example Applications](examples/complete-applications.md)** for full working code.

### ⚙️ **Building for Production?**
Follow **[Best Practices](guides/best-practices.md)** for professional development.

## 📋 Component Quick Reference

### Core Components
- **[Window](api/README.md#window)** - Application window management
- **[Panel](api/README.md#panel)** - Container component with layouts
- **[Layout](api/README.md#layout-management)** - Professional layout managers

### Input Components  
- **[TextField](api/README.md#textfield)** - Single-line text input
- **[TextBox](api/README.md#textbox)** - Multi-line text areas
- **[ComboBox](api/README.md#combobox)** - Dropdown selection
- **[Spinner](api/README.md#spinner)** - Number, date, and list input

### Selection Components
- **[Button](api/README.md#button)** - Clickable buttons  
- **[CheckBox](api/README.md#checkbox)** - Boolean selection
- **[RadioButton](api/README.md#radiobutton)** - Exclusive selection
- **[ListBox](api/README.md#listbox)** - Single/multi-selection lists
- **[Slider](api/README.md#slider)** - Value adjustment

### Display Components
- **[Label](api/README.md#label)** - Text display
- **[ProgressBar](api/README.md#progressbar)** - Progress indication

## 🚀 Getting Started in 5 Minutes

### 1. Add Dependency

**Maven:**
```xml
<dependency>
    <groupId>com.vibeui</groupId>
    <artifactId>vibe-ui</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Create Your First App

```java
import com.vibeui.*;
import javax.swing.SwingUtilities;

public class MyFirstApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window.create("My First Vibe UI App")
                .size(400, 300)
                .centerOnScreen()
                .layout(Layout.flow())
                .add(Label.create("Hello, Vibe UI!"))
                .add(Button.create("Click Me!")
                    .onClick(() -> System.out.println("Button clicked!")))
                .build()
                .show();
        });
    }
}
```

### 3. Run and Explore

```bash
javac -cp vibe-ui-1.0.0.jar MyFirstApp.java
java -cp vibe-ui-1.0.0.jar:. MyFirstApp
```

**🎉 Congratulations!** You've created your first Vibe UI application.

**➡️ Next Steps:** [Complete Getting Started Guide](guides/getting-started.md)

## 🎨 Key Features Showcase

### Fluent API Design
```java
Button.create("Professional Button")
    .size(150, 40)
    .backgroundColor(new Color(70, 130, 180))
    .foregroundColor(Color.WHITE)
    .font("Arial", Font.BOLD, 14)
    .onClick(() -> processAction())
    .tooltip("Click to process");
```

### Modern Event Handling
```java
TextField emailField = TextField.create("Enter email")
    .onChange(email -> validateEmail(email))
    .onFocus(() -> highlightField())
    .onBlur(() -> finalizeValidation());
```

### Professional Layouts
```java
Window.create("Professional Application")
    .layout(Layout.border())
    .add(createMenuBar(), Layout.BorderConstraints.NORTH)
    .add(createSidebar(), Layout.BorderConstraints.WEST)
    .add(createMainContent(), Layout.BorderConstraints.CENTER)
    .add(createStatusBar(), Layout.BorderConstraints.SOUTH);
```

### Advanced Components
```java
// Progress tracking with animation
ProgressBar.create(100)
    .value(0)
    .showString()
    .animateTo(100, 3000);

// Rich list selection
ListBox.create("Option 1", "Option 2", "Option 3")
    .multipleSelection()
    .onMultiSelection(items -> processSelection(items));

// Smart value input
Spinner.createNumber(50, 0, 100, 5)
    .numberEditor("#0")
    .onNumberChange(value -> updateDisplay(value));
```

## 📖 Documentation Standards

All our documentation follows these principles:

- **🎯 Purpose-Driven**: Each document serves a specific user need
- **📝 Code-Heavy**: More examples, less text
- **🔗 Cross-Referenced**: Easy navigation between related topics
- **✅ Tested**: All code examples are verified working
- **🆕 Up-to-Date**: Maintained with every release

## 🤝 Contributing to Documentation

Found an error? Want to improve an example? See our **[Contributing Guide](../CONTRIBUTING.md)**.

Common improvements needed:
- Additional code examples
- Clearer explanations
- More real-world use cases
- Performance tips
- Platform-specific notes

## 📞 Getting Help

- **📚 Check this documentation first** - Most questions are answered here
- **🐛 Bug Reports**: [GitHub Issues](https://github.com/yourusername/vibe-ui/issues)  
- **💡 Feature Requests**: [GitHub Discussions](https://github.com/yourusername/vibe-ui/discussions)
- **💬 Community**: [Discord Server](https://discord.gg/vibeui)

---

## 📋 Document Versions

| Document | Last Updated | Version |
|----------|--------------|---------|
| Getting Started | Latest | 1.0.0 |
| API Reference | Latest | 1.0.0 |
| Complete Examples | Latest | 1.0.0 |
| Best Practices | Latest | 1.0.0 |

---

<div align="center">
  <p><strong>Ready to build amazing desktop applications?</strong></p>
  <p>
    <a href="guides/getting-started.md">🚀 Get Started</a> •
    <a href="api/README.md">📋 API Docs</a> •
    <a href="examples/complete-applications.md">🏗️ Examples</a>
  </p>
</div>