# Contributing to Vibe UI

Thank you for your interest in contributing to Vibe UI! We welcome contributions from developers of all skill levels. This document provides guidelines and information for contributing to the project.

## 🎯 Ways to Contribute

- 🐛 **Bug Reports**: Help us identify and fix issues
- 💡 **Feature Requests**: Suggest new features or improvements
- 📝 **Documentation**: Improve our docs and examples
- 🔧 **Code Contributions**: Fix bugs or implement new features
- 🧪 **Testing**: Help us improve test coverage
- 🎨 **Design**: Improve UI/UX and visual design

## 🚀 Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

### Setting Up Development Environment

1. **Fork the repository**
   ```bash
   # Fork on GitHub, then clone your fork
   git clone https://github.com/yourusername/vibe-ui.git
   cd vibe-ui
   ```

2. **Set up upstream remote**
   ```bash
   git remote add upstream https://github.com/originalowner/vibe-ui.git
   ```

3. **Build the project**
   ```bash
   mvn clean compile
   ```

4. **Run tests**
   ```bash
   mvn test
   ```

5. **Run the demo**
   ```bash
   java -cp target/classes com.vibeui.demo.ComprehensiveDemo
   ```

## 📋 Development Workflow

### 1. Create a Feature Branch

```bash
git checkout -b feature/your-feature-name
# or
git checkout -b bugfix/issue-number-description
```

### 2. Make Your Changes

- Follow our coding standards (see below)
- Add tests for new functionality
- Update documentation as needed
- Ensure all tests pass

### 3. Commit Your Changes

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```bash
git commit -m "feat: add new slider component"
git commit -m "fix: resolve button click event issue"
git commit -m "docs: update README with new examples"
```

**Commit Types:**
- `feat`: New features
- `fix`: Bug fixes
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### 4. Push and Create Pull Request

```bash
git push origin feature/your-feature-name
```

Then create a Pull Request on GitHub with:
- Clear title and description
- Reference any related issues
- Include screenshots for UI changes
- List any breaking changes

## 🎨 Coding Standards

### Java Code Style

```java
// Use descriptive names
public class ButtonComponent extends Component<ButtonComponent> {
    
    // Constants in UPPER_SNAKE_CASE
    private static final int DEFAULT_HEIGHT = 30;
    
    // Fields in camelCase
    private String buttonText;
    private ActionListener clickHandler;
    
    // Methods with clear intent
    public ButtonComponent text(String text) {
        this.buttonText = text;
        updateDisplay();
        return this;
    }
    
    // Helper methods are private
    private void updateDisplay() {
        // Implementation
    }
}
```

### Key Principles

1. **Fluent API Design**: All setter methods return `this` for chaining
2. **Type Safety**: Use generics properly: `Component<T extends Component<T>>`
3. **Null Safety**: Check for null values and provide defaults
4. **Documentation**: All public methods must have JavaDoc
5. **Immutability**: Prefer immutable objects where possible
6. **Builder Pattern**: Use static factory methods: `Button.create()`

### Code Formatting

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: 120 characters maximum
- **Braces**: Opening brace on same line
- **Imports**: Group and sort imports

```java
// Good
public class Example {
    public void method() {
        if (condition) {
            doSomething();
        }
    }
}
```

## 📝 Documentation Standards

### JavaDoc Requirements

All public classes and methods must have comprehensive JavaDoc:

```java
/**
 * A fluent button component that supports click events and styling.
 * 
 * <p>Example usage:
 * <pre>{@code
 * Button saveButton = Button.create("Save")
 *     .size(100, 30)
 *     .onClick(() -> saveDocument());
 * }</pre>
 * 
 * @author Your Name
 * @since 1.0.0
 */
public class Button extends Component<Button> {
    
    /**
     * Sets the button text and returns this button for method chaining.
     * 
     * @param text the text to display on the button, must not be null
     * @return this button instance for method chaining
     * @throws IllegalArgumentException if text is null
     */
    public Button text(String text) {
        // Implementation
    }
}
```

### README Updates

When adding new features, update the README.md with:
- New component examples
- Updated API documentation
- Additional use cases

## 🧪 Testing Guidelines

### Test Structure

```java
public class ButtonTest {
    private Button button;
    
    @BeforeEach
    void setUp() {
        button = Button.create("Test Button");
    }
    
    @Test
    void shouldSetTextCorrectly() {
        // Given
        String expectedText = "New Text";
        
        // When
        button.text(expectedText);
        
        // Then
        assertEquals(expectedText, button.getText());
    }
    
    @Test
    void shouldThrowExceptionForNullText() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> button.text(null));
    }
}
```

### Test Coverage

- **Unit Tests**: Test individual component behavior
- **Integration Tests**: Test component interactions
- **UI Tests**: Test visual behavior (when applicable)
- **Performance Tests**: Test memory usage and responsiveness

Aim for 80%+ code coverage on new features.

## 🐛 Bug Reports

When reporting bugs, please include:

### Required Information

```markdown
**Bug Description**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Create a Button with '...'
2. Call method '...'
3. See error

**Expected Behavior**
What you expected to happen.

**Actual Behavior**
What actually happened.

**Environment**
- OS: [e.g. macOS 13.0, Windows 11]
- Java Version: [e.g. 11.0.2]
- Vibe UI Version: [e.g. 1.0.0]

**Code Sample**
```java
// Minimal code that reproduces the issue
Button button = Button.create("Test")
    .size(100, 30);
```

**Screenshots**
If applicable, add screenshots.
```

## 💡 Feature Requests

For new features, please provide:

1. **Use Case**: Why is this feature needed?
2. **Proposed API**: How should it work?
3. **Examples**: Code examples of usage
4. **Alternatives**: Other ways to achieve the same goal

### Example Feature Request

```markdown
**Feature**: Add tooltip support to all components

**Use Case**: Users need contextual help text when hovering over components.

**Proposed API**:
```java
Button.create("Save")
    .tooltip("Save the current document")
    .onClick(() -> save());
```

**Implementation Notes**: Should use Swing's built-in tooltip mechanism.
```

## 🔄 Release Process

### Version Numbering

We follow [Semantic Versioning](https://semver.org/):
- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes (backward compatible)

### Release Checklist

- [ ] All tests passing
- [ ] Documentation updated
- [ ] CHANGELOG.md updated
- [ ] Version bumped in pom.xml
- [ ] Tagged release in Git
- [ ] JAR published to Maven Central

## 🎖️ Recognition

Contributors will be:
- Listed in the README.md
- Mentioned in release notes
- Invited to the core team (for significant contributions)

## 📞 Getting Help

- **GitHub Discussions**: For questions and general discussion
- **GitHub Issues**: For bug reports and feature requests
- **Discord**: Real-time chat with the community
- **Email**: maintainers@vibeui.com

## 📜 Code of Conduct

This project follows the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating, you agree to uphold this code.

## 🙏 Thank You

Thank you for contributing to Vibe UI! Your efforts help make this library better for everyone.

---

*Happy coding! 🎉*