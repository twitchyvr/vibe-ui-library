# CLAUDE.md - Professional Development Guidelines

## 🔧 **PROJECT OVERVIEW**
**Vibe UI Library - AAA Studio Quality Java UI Framework**
- Modern, fluent Java UI library for cross-platform desktop applications
- Clean, intuitive API built on top of Java Swing with builder pattern
- Professional-grade components with type-safe fluent interface
- Enterprise-ready architecture optimized for maintainability and extensibility

**Core Mission**: Provide the most elegant and powerful Java UI library with AAA studio quality implementation standards.

## 📋 **DEVELOPMENT STANDARDS**

### **Git Workflow - Enterprise Level**
**🚨 CRITICAL: GIT EVERYTHING - NO EXCEPTIONS 🚨**

**MANDATORY RULES:**

1. **COMMIT AFTER EVERY EDIT** - No matter how small
2. **ALWAYS use descriptive commit messages** 
3. **Use branches for features** - Never commit directly to main
4. **Create issues for major changes**
5. **Use pull requests for reviews**
6. **Follow professional Git best practices**

```bash
# MANDATORY workflow for EVERY change:
git checkout -b feature/description-of-change  # Create feature branch
git add .
git status  # ALWAYS review changes
git diff    # ALWAYS review code changes
git commit -m "$(cat <<'EOF'
[TYPE]: Brief description of change

Detailed explanation of:
- What was changed and why
- Any performance implications
- Breaking changes or new features
- Testing notes

Technical details:
- Specific files modified
- Architecture decisions
- Configuration changes

🤖 Generated with [Claude Code](https://claude.ai/code)

Co-Authored-By: Claude <noreply@anthropic.com>
EOF
)"
git push origin feature/description-of-change  # Push feature branch
# Create PR for review before merging to main
```

**Git Advanced Features - ALWAYS USE:**

- **Feature branches**: `git checkout -b feature/ui-improvements`
- **Issues**: Track bugs and features with GitHub issues
- **Pull Requests**: All changes go through PR review process
- **Tags**: Version releases with semantic versioning
- **Conventional Commits**: Use standardized commit message format

**Commit Types:**

- `FEAT`: New features and components
- `FIX`: Bug fixes and corrections
- `PERF`: Performance improvements
- `UI`: User interface enhancements
- `REFACTOR`: Code restructuring and optimization
- `DOCS`: Documentation updates
- `BUILD`: Build system changes
- `TEST`: Adding or updating tests

## 🏗️ **BUILD COMMANDS**

This is a Maven-based Java project (Java 11+). Professional commands:

```bash
# Compile the project
mvn clean compile

# Run comprehensive tests
mvn test

# Package JAR with dependencies
mvn package

# Run the comprehensive demo (AAA quality showcase)
java -cp target/classes com.vibeui.demo.ComprehensiveDemo

# Run the basic demo
java -cp target/classes com.vibeui.demo.DemoApp

# Run the new controls demo
java -cp target/classes com.vibeui.demo.NewControlsDemo

# Generate documentation
mvn javadoc:javadoc

# Run with performance profiling
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=vibeui-profile.jfr -cp target/classes com.vibeui.demo.ComprehensiveDemo
```

## 🎯 **DEVELOPMENT OBJECTIVES**

### **ABSOLUTE REQUIREMENTS - NO EXCEPTIONS:**

1. **GIT EVERY SINGLE EDIT** - Commit after every file modification
2. **NO PLACEHOLDERS** - Every implementation must be complete and production-ready
3. **NO SHORTCUTS** - Full professional implementation, never simplified or subpar
4. **NO EXPLANATIONS** - Code and commit messages speak for themselves
5. **ENTERPRISE QUALITY** - AAA studio standards in all code, UI, and architecture
6. **ADVANCED FEATURES ONLY** - Use professional Git workflow with branches, PRs, issues
7. **TEST EVERYTHING** - Unit tests, integration tests, performance verification required
8. **DOCUMENT THOROUGHLY** - Comprehensive commit messages with technical details
9. **PROFESSIONAL STANDARDS** - Follow all industry best practices without compromise
10. **COMPLETE IMPLEMENTATIONS** - Never leave anything unfinished or rudimentary

### **Performance Standards**

- **Instant UI Response** - All component interactions must be sub-100ms
- **Memory Efficient** - Minimal object allocation during runtime
- **Scalable Architecture** - Support for complex enterprise applications
- **Cross-Platform Excellence** - Perfect rendering on all target platforms

## 🚀 **ARCHITECTURE**

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
- `Button`, `Label`, `TextField`, `TextBox` - Basic UI controls
- `CheckBox`, `RadioButton`, `ToggleButton` - Selection controls
- `Slider` - Value adjustment control
- `ComboBox` - Dropdown selection control with editable option
- `ListBox` - Single/multi-selection list control with scrolling
- `ProgressBar` - Progress indication with determinate/indeterminate modes
- `Spinner` - Number, date, and list value input controls
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

## 🎨 **ADVANCED CONTROLS**

#### ComboBox (Dropdown)

```java
ComboBox.create("Option 1", "Option 2", "Option 3")
    .selectedItem("Option 1")
    .editable(true)
    .onChange(selected -> handleSelection(selected));
```

#### ListBox (Selection Lists)

```java
ListBox.create("Item 1", "Item 2", "Item 3")
    .multipleSelection()
    .visibleRowCount(5)
    .onMultiSelection(items -> handleMultiSelection(items));
```

#### ProgressBar

```java
ProgressBar.create(100)
    .value(50)
    .showString()
    .text("Loading...")
    .animateTo(100, 3000); // animate to 100% over 3 seconds
```

#### Spinner (Value Input)

```java
Spinner.createNumber(0, 0, 100, 5) // value, min, max, step
    .onNumberChange(value -> handleValueChange(value));

Spinner.createDate()
    .dayField()
    .onChange(date -> handleDateChange(date));
```

## 🧪 **TESTING FRAMEWORK**

### Professional Testing Standards

- Uses JUnit 5 (jupiter) for comprehensive testing
- Test files should be in `src/test/java/com/vibeui/`
- **100% Code Coverage Required** for all core components
- Performance benchmarking for all UI operations
- Cross-platform compatibility testing (Windows, macOS, Linux)

### Testing Categories

- **Unit Tests**: Individual component functionality
- **Integration Tests**: Component interaction and layout
- **Performance Tests**: Response time and memory usage
- **UI Tests**: Visual rendering and user interaction
- **Compatibility Tests**: JDK version and platform verification

### Test Execution Commands

```bash
# Run all tests with coverage
mvn test jacoco:report

# Run performance benchmarks
mvn test -Dtest=**/*PerformanceTest

# Run UI integration tests
mvn test -Dtest=**/*UITest

# Generate test reports
mvn surefire-report:report
```

## 📦 **DEPENDENCIES & COMPATIBILITY**

### Core Requirements

- **Java 11+** required (LTS version recommended)
- **JUnit 5.9.2** for testing (test scope only)
- **No runtime dependencies** beyond Java standard library
- **Maven 3.6+** for build management

### Optional Dependencies

- **Mockito** for advanced mocking in tests
- **TestFX** for automated UI testing
- **JaCoCo** for code coverage reporting
- **SpotBugs** for static analysis

## 🚀 **NEXT DEVELOPMENT PRIORITIES**

### Immediate Goals (Sprint 1)

1. **Complete Theme System** - Advanced styling and customization
2. **Enhanced Layout Managers** - Professional layout options
3. **Animation Framework** - Smooth transitions and effects
4. **Accessibility Support** - ARIA compliance and screen reader support
5. **Documentation Portal** - Interactive component gallery

### Advanced Features (Sprint 2)

1. **Data Binding System** - Model-view synchronization
2. **Component Templates** - Reusable UI patterns
3. **Hot Reload Support** - Development-time UI updates
4. **Performance Monitoring** - Built-in profiling tools
5. **Plugin Architecture** - Extensible component system

### Enterprise Features (Sprint 3)

1. **Internationalization** - Multi-language support
2. **Advanced Security** - Input validation and sanitization
3. **Enterprise Integrations** - Database and service connectivity
4. **Professional Tooling** - IDE plugins and design tools
5. **Community Ecosystem** - Component marketplace

## 📝 **DEVELOPMENT NOTES**

### Common Patterns

- **Builder Pattern**: Consistent across all components
- **Event-Driven Architecture**: Reactive programming model
- **Immutable Configurations**: Thread-safe component setup
- **Lazy Loading**: Performance optimization for complex UIs

### Performance Optimization

- **Component Pooling**: Reuse instances where appropriate
- **Efficient Rendering**: Minimize Swing EDT blocking
- **Memory Management**: Proper cleanup and disposal
- **Async Operations**: Non-blocking UI updates

### Best Practices

- **Consistent API Design**: Predictable method naming and behavior
- **Comprehensive Error Handling**: Graceful degradation and recovery
- **Professional Documentation**: Javadoc with examples and best practices
- **Backward Compatibility**: Maintain API stability across versions

**This implementation represents AAA studio quality standards for Java UI development, combining modern design patterns with enterprise-grade reliability and performance.**