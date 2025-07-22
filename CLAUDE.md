# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Vibe UI is a modern, fluent Java UI library for building cross-platform desktop applications. It provides a clean, intuitive API built on top of Java Swing with a builder pattern for component creation.

## Build Commands

This is a Maven-based Java project (Java 11+). Common commands:

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Package JAR
mvn package

# Run the comprehensive demo
java -cp target/classes com.vibeui.demo.ComprehensiveDemo

# Run the basic demo
java -cp target/classes com.vibeui.demo.DemoApp
```

## Architecture

### Core Design Pattern
- **Fluent API**: All components use method chaining with a builder pattern
- **Type-safe builders**: Generic base class `Component<T>` ensures type safety
- **Swing-based**: Built on top of Java Swing for compatibility

### Package Structure
- `com.vibeui` - Core components and base classes
- `com.vibeui.demo` - Demo applications showing component usage

### Key Classes
- `Component<T>` - Abstract base class for all UI components with fluent API
- `Window` - Main application window management
- `Panel` - Container component with layout management
- `Button`, `Label`, `TextField`, `TextBox` - UI controls
- `CheckBox`, `RadioButton`, `ToggleButton` - Selection controls
- `Slider` - Value adjustment control
- `Layout` - Layout manager utilities
- `Events` - Event handling utilities

### Component Creation Pattern
All components follow this pattern:
```java
ComponentType.create(parameters)
    .fluentMethod1(value)
    .fluentMethod2(value)
    .build()  // optional
    .show();  // for windows
```

### Event Handling
Uses lambda expressions for modern event handling:
```java
Button.create("Click Me")
    .onClick(() -> handleClick())
    .onChange(value -> processChange(value))
```

### Layout Management
- Flow Layout: `Layout.flow()`
- Border Layout: `Layout.border()` with constraints
- Grid Layout: `Layout.gridBag()` for complex layouts

## Testing
- Uses JUnit 5 (jupiter) for testing
- Test files should be in `src/test/java/com/vibeui/`
- No test files currently exist in the project

## Dependencies
- Java 11+ required
- JUnit 5.9.2 for testing (test scope only)
- No runtime dependencies beyond Java standard library