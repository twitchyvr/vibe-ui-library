# Complete Application Examples

This document contains complete, working examples of applications built with Vibe UI, demonstrating real-world usage patterns and best practices.

## 📋 Table of Contents

1. [Text Editor Application](#text-editor-application)
2. [Contact Manager](#contact-manager)  
3. [File Explorer](#file-explorer)
4. [Data Visualization Dashboard](#data-visualization-dashboard)
5. [Settings Dialog](#settings-dialog)

## 📝 Text Editor Application

A complete text editor with file operations, formatting, and find/replace functionality.

```java
import com.vibeui.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextEditor {
    private Window mainWindow;
    private TextBox contentArea;
    private Label statusLabel;
    private File currentFile;
    private boolean hasUnsavedChanges = false;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditor().createUI());
    }
    
    private void createUI() {
        mainWindow = Window.create("Vibe Text Editor")
            .size(800, 600)
            .centerOnScreen()
            .layout(Layout.border());
        
        createMenuBar();
        createToolbar();
        createContentArea();
        createStatusBar();
        
        mainWindow.build().show();
        
        // Set up keyboard shortcuts
        setupKeyboardShortcuts();
    }
    
    private void createMenuBar() {
        // File menu
        Panel menuBar = Panel.create()
            .backgroundColor(Color.LIGHT_GRAY)
            .flowLayout()
            .padding(5);
        
        Button newBtn = Button.create("New")
            .onClick(this::newFile)
            .tooltip("Create new file (Ctrl+N)");
            
        Button openBtn = Button.create("Open")
            .onClick(this::openFile)
            .tooltip("Open file (Ctrl+O)");
            
        Button saveBtn = Button.create("Save")
            .onClick(this::saveFile)
            .tooltip("Save file (Ctrl+S)");
            
        Button saveAsBtn = Button.create("Save As")
            .onClick(this::saveAsFile)
            .tooltip("Save file as...");
        
        menuBar.add(newBtn).add(openBtn).add(saveBtn).add(saveAsBtn);
        
        mainWindow.add(menuBar, Layout.BorderConstraints.NORTH);
    }
    
    private void createToolbar() {
        Panel toolbar = Panel.create()
            .backgroundColor(new Color(240, 240, 240))
            .flowLayout()
            .padding(5);
        
        // Formatting buttons
        Button boldBtn = Button.create("B")
            .font("Arial", Font.BOLD, 12)
            .size(30, 25)
            .onClick(() -> applyFormatting(Font.BOLD))
            .tooltip("Bold");
        
        Button italicBtn = Button.create("I")
            .font("Arial", Font.ITALIC, 12)
            .size(30, 25)
            .onClick(() -> applyFormatting(Font.ITALIC))
            .tooltip("Italic");
        
        // Font size
        ComboBox fontSizeCombo = ComboBox.create("8", "10", "12", "14", "16", "18", "20", "24")
            .selectedItem("12")
            .onChange(size -> changeFontSize(Integer.parseInt(size)));
        
        // Find/Replace
        TextField findField = TextField.create("Find...")
            .size(150, 25)
            .onChange(this::highlightMatches);
        
        Button findNextBtn = Button.create("Next")
            .onClick(() -> findNext(findField.getText()))
            .tooltip("Find next (F3)");
        
        toolbar.add(boldBtn)
               .add(italicBtn)
               .add(Label.create("Size:"))
               .add(fontSizeCombo)
               .add(findField)
               .add(findNextBtn);
        
        Panel toolbarContainer = Panel.create()
            .layout(Layout.border())
            .add(toolbar, Layout.BorderConstraints.WEST);
        
        mainWindow.add(toolbarContainer, Layout.BorderConstraints.NORTH);
    }
    
    private void createContentArea() {
        contentArea = TextBox.createMultiline()
            .size(780, 450)
            .wordWrap(true)
            .font("Consolas", Font.PLAIN, 14)
            .onChange(text -> {
                hasUnsavedChanges = true;
                updateTitle();
                updateStatusBar();
            });
        
        Panel contentPanel = Panel.create()
            .layout(Layout.border())
            .padding(10)
            .add(contentArea, Layout.BorderConstraints.CENTER);
        
        mainWindow.add(contentPanel, Layout.BorderConstraints.CENTER);
    }
    
    private void createStatusBar() {
        Panel statusBar = Panel.create()
            .backgroundColor(Color.LIGHT_GRAY)
            .flowLayout()
            .padding(5);
        
        statusLabel = Label.create("Ready")
            .font("Arial", Font.PLAIN, 12);
        
        statusBar.add(statusLabel);
        mainWindow.add(statusBar, Layout.BorderConstraints.SOUTH);
        
        updateStatusBar();
    }
    
    private void setupKeyboardShortcuts() {
        // This would require additional Swing setup for key bindings
        // Simplified for example purposes
    }
    
    private void newFile() {
        if (hasUnsavedChanges && !confirmDiscardChanges()) {
            return;
        }
        
        contentArea.text("");
        currentFile = null;
        hasUnsavedChanges = false;
        updateTitle();
        updateStatusBar();
    }
    
    private void openFile() {
        if (hasUnsavedChanges && !confirmDiscardChanges()) {
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt", "java", "js", "html", "css"));
        
        int result = fileChooser.showOpenDialog(mainWindow.getSwingComponent());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadFile(selectedFile);
        }
    }
    
    private void loadFile(File file) {
        try {
            String content = Files.readString(Paths.get(file.getAbsolutePath()));
            contentArea.text(content);
            currentFile = file;
            hasUnsavedChanges = false;
            updateTitle();
            updateStatusBar();
            statusLabel.text("File loaded: " + file.getName());
        } catch (IOException e) {
            showError("Could not load file: " + e.getMessage());
        }
    }
    
    private void saveFile() {
        if (currentFile == null) {
            saveAsFile();
        } else {
            saveToFile(currentFile);
        }
    }
    
    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        
        int result = fileChooser.showSaveDialog(mainWindow.getSwingComponent());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().contains(".")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            saveToFile(selectedFile);
        }
    }
    
    private void saveToFile(File file) {
        try {
            Files.writeString(Paths.get(file.getAbsolutePath()), contentArea.getText());
            currentFile = file;
            hasUnsavedChanges = false;
            updateTitle();
            statusLabel.text("File saved: " + file.getName());
        } catch (IOException e) {
            showError("Could not save file: " + e.getMessage());
        }
    }
    
    private void applyFormatting(int fontStyle) {
        Font currentFont = contentArea.getSwingComponent().getFont();
        Font newFont = new Font(currentFont.getName(), 
                               currentFont.getStyle() ^ fontStyle, // Toggle style
                               currentFont.getSize());
        contentArea.font(newFont);
    }
    
    private void changeFontSize(int size) {
        Font currentFont = contentArea.getSwingComponent().getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), size);
        contentArea.font(newFont);
    }
    
    private void highlightMatches(String searchText) {
        // In a real implementation, you'd highlight matching text
        // This is simplified for the example
        if (searchText.length() > 2) {
            String content = contentArea.getText();
            int matches = content.split(searchText, -1).length - 1;
            statusLabel.text("Found " + matches + " matches for '" + searchText + "'");
        }
    }
    
    private void findNext(String searchText) {
        // Simplified find next implementation
        String content = contentArea.getText();
        int index = content.indexOf(searchText);
        if (index >= 0) {
            statusLabel.text("Found at position " + index);
        } else {
            statusLabel.text("Not found: " + searchText);
        }
    }
    
    private boolean confirmDiscardChanges() {
        int result = JOptionPane.showConfirmDialog(
            mainWindow.getSwingComponent(),
            "You have unsaved changes. Discard them?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    private void updateTitle() {
        String title = "Vibe Text Editor";
        if (currentFile != null) {
            title += " - " + currentFile.getName();
        }
        if (hasUnsavedChanges) {
            title += " *";
        }
        mainWindow.getSwingComponent().setTitle(title);
    }
    
    private void updateStatusBar() {
        String content = contentArea.getText();
        int lines = content.split("\n").length;
        int chars = content.length();
        int words = content.trim().isEmpty() ? 0 : content.trim().split("\\s+").length;
        
        statusLabel.text(String.format("Lines: %d | Words: %d | Characters: %d", lines, words, chars));
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            mainWindow.getSwingComponent(),
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
```

## 👥 Contact Manager

A complete contact management application with CRUD operations and search functionality.

```java
import com.vibeui.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ContactManager {
    private Window mainWindow;
    private ListBox contactsList;
    private TextField nameField, emailField, phoneField, searchField;
    private TextBox notesArea;
    private Button saveButton, deleteButton, newButton, editButton;
    private Label statusLabel;
    
    private final List<Contact> contacts = new ArrayList<>();
    private Contact currentContact;
    private boolean isEditing = false;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactManager().createUI());
    }
    
    private void createUI() {
        mainWindow = Window.create("Contact Manager")
            .size(900, 600)
            .centerOnScreen()
            .layout(Layout.border());
        
        createLeftPanel();
        createRightPanel();
        createStatusBar();
        
        // Add some sample data
        addSampleContacts();
        
        mainWindow.build().show();
        updateUI();
    }
    
    private void createLeftPanel() {
        Panel leftPanel = Panel.create()
            .layout(Layout.border())
            .size(300, 580)
            .border(Color.GRAY, 1);
        
        // Search area
        Panel searchPanel = Panel.create()
            .flowLayout()
            .padding(10);
        
        searchField = TextField.create("Search contacts...")
            .size(200, 30)
            .onChange(this::filterContacts);
        
        Button clearSearchBtn = Button.create("Clear")
            .onClick(() -> {
                searchField.text("");
                filterContacts("");
            });
        
        searchPanel.add(searchField).add(clearSearchBtn);
        
        // Contacts list
        contactsList = ListBox.create()
            .singleSelection()
            .visibleRowCount(15)
            .onSelection(this::selectContact);
        
        // Buttons
        Panel buttonPanel = Panel.create()
            .flowLayout()
            .padding(5);
        
        newButton = Button.create("New")
            .onClick(this::newContact)
            .backgroundColor(Color.GREEN)
            .foregroundColor(Color.WHITE);
        
        editButton = Button.create("Edit")
            .onClick(this::editContact)
            .enabled(false);
        
        deleteButton = Button.create("Delete")
            .onClick(this::deleteContact)
            .backgroundColor(Color.RED)
            .foregroundColor(Color.WHITE)
            .enabled(false);
        
        buttonPanel.add(newButton).add(editButton).add(deleteButton);
        
        leftPanel.add(searchPanel, Layout.BorderConstraints.NORTH)
                 .add(contactsList, Layout.BorderConstraints.CENTER)
                 .add(buttonPanel, Layout.BorderConstraints.SOUTH);
        
        mainWindow.add(leftPanel, Layout.BorderConstraints.WEST);
    }
    
    private void createRightPanel() {
        Panel rightPanel = Panel.create()
            .layout(Layout.border())
            .padding(20);
        
        // Form area
        Panel formPanel = Panel.create()
            .layout(Layout.gridBag())
            .padding(10);
        
        // Create form fields
        nameField = TextField.create("Full Name")
            .size(250, 30)
            .enabled(false);
        
        emailField = TextField.create("Email Address")
            .size(250, 30)
            .enabled(false);
        
        phoneField = TextField.create("Phone Number")
            .size(250, 30)
            .enabled(false);
        
        notesArea = TextBox.createMultiline("Notes...")
            .size(250, 100)
            .rows(4)
            .wordWrap(true)
            .enabled(false);
        
        // Add form fields with labels
        addFormRow(formPanel, "Name:", nameField);
        addFormRow(formPanel, "Email:", emailField);
        addFormRow(formPanel, "Phone:", phoneField);
        addFormRow(formPanel, "Notes:", notesArea);
        
        // Action buttons
        Panel actionPanel = Panel.create()
            .flowLayout()
            .padding(10);
        
        saveButton = Button.create("Save")
            .onClick(this::saveContact)
            .backgroundColor(Color.BLUE)
            .foregroundColor(Color.WHITE)
            .enabled(false);
        
        Button cancelButton = Button.create("Cancel")
            .onClick(this::cancelEdit);
        
        actionPanel.add(saveButton).add(cancelButton);
        
        rightPanel.add(formPanel, Layout.BorderConstraints.CENTER)
                  .add(actionPanel, Layout.BorderConstraints.SOUTH);
        
        mainWindow.add(rightPanel, Layout.BorderConstraints.CENTER);
    }
    
    private void addFormRow(Panel parent, String labelText, Component<?> field) {
        Panel row = Panel.create()
            .flowLayout()
            .padding(5);
        
        Label label = Label.create(labelText)
            .size(80, 30)
            .alignRight();
        
        row.add(label).add(field);
        parent.add(row);
    }
    
    private void createStatusBar() {
        Panel statusBar = Panel.create()
            .backgroundColor(Color.LIGHT_GRAY)
            .flowLayout()
            .padding(5);
        
        statusLabel = Label.create("Ready")
            .font("Arial", Font.PLAIN, 12);
        
        statusBar.add(statusLabel);
        mainWindow.add(statusBar, Layout.BorderConstraints.SOUTH);
    }
    
    private void addSampleContacts() {
        contacts.add(new Contact("John Doe", "john@example.com", "555-0101", "Manager at ABC Corp"));
        contacts.add(new Contact("Jane Smith", "jane@example.com", "555-0102", "Designer"));
        contacts.add(new Contact("Bob Wilson", "bob@example.com", "555-0103", "Developer"));
        contacts.add(new Contact("Alice Brown", "alice@example.com", "555-0104", "Sales Representative"));
        
        updateContactsList();
    }
    
    private void updateContactsList() {
        contactsList.removeAllItems();
        for (Contact contact : contacts) {
            contactsList.addItem(contact.getName());
        }
        updateStatusBar();
    }
    
    private void filterContacts(String searchText) {
        contactsList.removeAllItems();
        
        List<Contact> filtered = contacts.stream()
            .filter(contact -> contact.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                              contact.getEmail().toLowerCase().contains(searchText.toLowerCase()))
            .toList();
        
        for (Contact contact : filtered) {
            contactsList.addItem(contact.getName());
        }
        
        statusLabel.text("Showing " + filtered.size() + " of " + contacts.size() + " contacts");
    }
    
    private void selectContact(String contactName) {
        if (contactName != null) {
            currentContact = contacts.stream()
                .filter(c -> c.getName().equals(contactName))
                .findFirst()
                .orElse(null);
            
            if (currentContact != null) {
                displayContact(currentContact);
                editButton.enabled(true);
                deleteButton.enabled(true);
            }
        } else {
            currentContact = null;
            clearForm();
            editButton.enabled(false);
            deleteButton.enabled(false);
        }
    }
    
    private void displayContact(Contact contact) {
        nameField.text(contact.getName());
        emailField.text(contact.getEmail());
        phoneField.text(contact.getPhone());
        notesArea.text(contact.getNotes());
    }
    
    private void newContact() {
        currentContact = null;
        clearForm();
        setFormEditable(true);
        statusLabel.text("Creating new contact");
    }
    
    private void editContact() {
        if (currentContact != null) {
            setFormEditable(true);
            statusLabel.text("Editing contact: " + currentContact.getName());
        }
    }
    
    private void saveContact() {
        if (!validateForm()) {
            return;
        }
        
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String notes = notesArea.getText().trim();
        
        if (currentContact == null) {
            // New contact
            Contact newContact = new Contact(name, email, phone, notes);
            contacts.add(newContact);
            currentContact = newContact;
            statusLabel.text("Contact created: " + name);
        } else {
            // Update existing
            currentContact.setName(name);
            currentContact.setEmail(email);
            currentContact.setPhone(phone);
            currentContact.setNotes(notes);
            statusLabel.text("Contact updated: " + name);
        }
        
        updateContactsList();
        setFormEditable(false);
        
        // Reselect the contact in the list
        contactsList.selectedItem(currentContact.getName());
    }
    
    private void cancelEdit() {
        if (currentContact != null) {
            displayContact(currentContact);
        } else {
            clearForm();
        }
        setFormEditable(false);
        statusLabel.text("Edit cancelled");
    }
    
    private void deleteContact() {
        if (currentContact != null) {
            int result = JOptionPane.showConfirmDialog(
                mainWindow.getSwingComponent(),
                "Delete contact: " + currentContact.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            
            if (result == JOptionPane.YES_OPTION) {
                contacts.remove(currentContact);
                updateContactsList();
                currentContact = null;
                clearForm();
                editButton.enabled(false);
                deleteButton.enabled(false);
                statusLabel.text("Contact deleted");
            }
        }
    }
    
    private void clearForm() {
        nameField.text("");
        emailField.text("");
        phoneField.text("");
        notesArea.text("");
    }
    
    private void setFormEditable(boolean editable) {
        nameField.enabled(editable);
        emailField.enabled(editable);
        phoneField.enabled(editable);
        notesArea.enabled(editable);
        saveButton.enabled(editable);
        isEditing = editable;
    }
    
    private boolean validateForm() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        
        if (name.isEmpty()) {
            showError("Name is required");
            return false;
        }
        
        if (!email.isEmpty() && !isValidEmail(email)) {
            showError("Invalid email format");
            return false;
        }
        
        // Check for duplicate names (except current contact)
        boolean isDuplicate = contacts.stream()
            .filter(c -> c != currentContact)
            .anyMatch(c -> c.getName().equalsIgnoreCase(name));
        
        if (isDuplicate) {
            showError("Contact name already exists");
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void updateUI() {
        updateStatusBar();
    }
    
    private void updateStatusBar() {
        if (contacts.isEmpty()) {
            statusLabel.text("No contacts");
        } else {
            statusLabel.text(contacts.size() + " contact" + (contacts.size() == 1 ? "" : "s"));
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            mainWindow.getSwingComponent(),
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    // Contact data class
    private static class Contact {
        private String name, email, phone, notes;
        
        public Contact(String name, String email, String phone, String notes) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.notes = notes;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
```

## 📁 File Explorer

A simple file browser with navigation and file operations.

```java
import com.vibeui.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class FileExplorer {
    private Window mainWindow;
    private ListBox filesList;
    private TextField pathField;
    private Label statusLabel;
    private File currentDirectory;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileExplorer().createUI());
    }
    
    private void createUI() {
        mainWindow = Window.create("File Explorer")
            .size(700, 500)
            .centerOnScreen()
            .layout(Layout.border());
        
        createToolbar();
        createMainArea();
        createStatusBar();
        
        // Start in user home directory
        currentDirectory = new File(System.getProperty("user.home"));
        refreshFilesList();
        
        mainWindow.build().show();
    }
    
    private void createToolbar() {
        Panel toolbar = Panel.create()
            .backgroundColor(Color.LIGHT_GRAY)
            .flowLayout()
            .padding(5);
        
        Button backButton = Button.create("Back")
            .onClick(this::goBack)
            .tooltip("Go to parent directory");
        
        Button homeButton = Button.create("Home")
            .onClick(this::goHome)
            .tooltip("Go to home directory");
        
        Button refreshButton = Button.create("Refresh")
            .onClick(this::refreshFilesList)
            .tooltip("Refresh current directory");
        
        pathField = TextField.create()
            .size(400, 30)
            .onChange(this::navigateToPath);
        
        toolbar.add(backButton)
               .add(homeButton)
               .add(refreshButton)
               .add(Label.create("Path:"))
               .add(pathField);
        
        mainWindow.add(toolbar, Layout.BorderConstraints.NORTH);
    }
    
    private void createMainArea() {
        Panel mainPanel = Panel.create()
            .layout(Layout.border())
            .padding(10);
        
        filesList = ListBox.create()
            .singleSelection()
            .visibleRowCount(20)
            .onSelection(this::selectFile)
            .fixedCellHeight(20);
        
        // Add context menu functionality
        setupContextMenu();
        
        mainPanel.add(filesList, Layout.BorderConstraints.CENTER);
        mainWindow.add(mainPanel, Layout.BorderConstraints.CENTER);
    }
    
    private void setupContextMenu() {
        // In a real implementation, you'd add right-click context menu
        // This is simplified for the example
    }
    
    private void createStatusBar() {
        Panel statusBar = Panel.create()
            .backgroundColor(Color.LIGHT_GRAY)
            .flowLayout()
            .padding(5);
        
        statusLabel = Label.create("Ready")
            .font("Arial", Font.PLAIN, 12);
        
        statusBar.add(statusLabel);
        mainWindow.add(statusBar, Layout.BorderConstraints.SOUTH);
    }
    
    private void refreshFilesList() {
        filesList.removeAllItems();
        
        if (currentDirectory != null && currentDirectory.exists()) {
            pathField.text(currentDirectory.getAbsolutePath());
            
            File[] files = currentDirectory.listFiles();
            if (files != null) {
                // Sort files: directories first, then files
                Arrays.sort(files, (f1, f2) -> {
                    if (f1.isDirectory() && !f2.isDirectory()) return -1;
                    if (!f1.isDirectory() && f2.isDirectory()) return 1;
                    return f1.getName().compareToIgnoreCase(f2.getName());
                });
                
                // Add parent directory entry if not at root
                if (currentDirectory.getParent() != null) {
                    filesList.addItem(".. (Parent Directory)");
                }
                
                for (File file : files) {
                    String displayName = formatFileEntry(file);
                    filesList.addItem(displayName);
                }
                
                updateStatusBar(files);
            }
        }
    }
    
    private String formatFileEntry(File file) {
        StringBuilder sb = new StringBuilder();
        
        // Icon representation
        sb.append(file.isDirectory() ? "[DIR] " : "[FILE] ");
        
        // Name
        sb.append(file.getName());
        
        // Size and date for files
        if (file.isFile()) {
            long size = file.length();
            String sizeStr = formatFileSize(size);
            
            Date lastModified = new Date(file.lastModified());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            String dateStr = dateFormat.format(lastModified);
            
            // Pad to align columns (simplified)
            sb.append(String.format(" (%s, %s)", sizeStr, dateStr));
        }
        
        return sb.toString();
    }
    
    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }
    
    private void selectFile(String selectedItem) {
        if (selectedItem == null) return;
        
        if (selectedItem.equals(".. (Parent Directory)")) {
            goBack();
            return;
        }
        
        // Extract actual filename from formatted display
        String fileName = extractFileName(selectedItem);
        File selectedFile = new File(currentDirectory, fileName);
        
        if (selectedFile.isDirectory()) {
            currentDirectory = selectedFile;
            refreshFilesList();
        } else {
            showFileInfo(selectedFile);
        }
    }
    
    private String extractFileName(String displayItem) {
        // Remove the [DIR] or [FILE] prefix and size/date info
        String name = displayItem.substring(displayItem.indexOf("] ") + 2);
        int parenIndex = name.lastIndexOf(" (");
        if (parenIndex > 0) {
            name = name.substring(0, parenIndex);
        }
        return name;
    }
    
    private void showFileInfo(File file) {
        StringBuilder info = new StringBuilder();
        info.append("File: ").append(file.getName()).append("\n");
        info.append("Size: ").append(formatFileSize(file.length())).append("\n");
        info.append("Modified: ").append(new Date(file.lastModified())).append("\n");
        info.append("Path: ").append(file.getAbsolutePath());
        
        JOptionPane.showMessageDialog(
            mainWindow.getSwingComponent(),
            info.toString(),
            "File Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void goBack() {
        if (currentDirectory != null && currentDirectory.getParent() != null) {
            currentDirectory = currentDirectory.getParentFile();
            refreshFilesList();
        }
    }
    
    private void goHome() {
        currentDirectory = new File(System.getProperty("user.home"));
        refreshFilesList();
    }
    
    private void navigateToPath(String path) {
        if (path != null && !path.trim().isEmpty()) {
            File newDir = new File(path.trim());
            if (newDir.exists() && newDir.isDirectory()) {
                currentDirectory = newDir;
                refreshFilesList();
            } else {
                statusLabel.text("Invalid path: " + path);
            }
        }
    }
    
    private void updateStatusBar(File[] files) {
        int fileCount = 0;
        int dirCount = 0;
        long totalSize = 0;
        
        for (File file : files) {
            if (file.isDirectory()) {
                dirCount++;
            } else {
                fileCount++;
                totalSize += file.length();
            }
        }
        
        String status = String.format("%d directories, %d files (%s)", 
            dirCount, fileCount, formatFileSize(totalSize));
        statusLabel.text(status);
    }
}
```

## ⚙️ Settings Dialog

A comprehensive settings dialog with multiple tabs and validation.

```java
import com.vibeui.*;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class SettingsDialog {
    private Window settingsWindow;
    private ComboBox themeCombo, languageCombo, fontSizeCombo;
    private CheckBox autoSaveCheck, soundsCheck, notificationsCheck;
    private Spinner maxBackupsSpinner, autoSaveIntervalSpinner;
    private TextField nameField, emailField;
    private Properties settings = new Properties();
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SettingsDialog().show());
    }
    
    public void show() {
        createSettingsWindow();
        loadSettings();
        settingsWindow.show();
    }
    
    private void createSettingsWindow() {
        settingsWindow = Window.create("Settings")
            .size(500, 400)
            .centerOnScreen()
            .resizable(false)
            .layout(Layout.border());
        
        createHeader();
        createTabbedContent();
        createButtonPanel();
        
        settingsWindow.build();
    }
    
    private void createHeader() {
        Panel header = Panel.create()
            .backgroundColor(new Color(70, 130, 180))
            .padding(15);
        
        Label titleLabel = Label.create("Application Settings")
            .font("Arial", Font.BOLD, 16)
            .foregroundColor(Color.WHITE)
            .alignCenter();
        
        header.add(titleLabel);
        settingsWindow.add(header, Layout.BorderConstraints.NORTH);
    }
    
    private void createTabbedContent() {
        // Create tab selector
        ComboBox tabSelector = ComboBox.create("General", "Appearance", "Advanced", "User Profile")
            .selectedItem("General")
            .onChange(this::switchTab);
        
        Panel tabHeader = Panel.create()
            .flowLayout()
            .padding(10)
            .add(Label.create("Category: "))
            .add(tabSelector);
        
        // Create content panels for each tab
        Panel contentArea = Panel.create()
            .layout(new CardLayout())
            .padding(20);
        
        contentArea.add(createGeneralTab(), "General");
        contentArea.add(createAppearanceTab(), "Appearance");
        contentArea.add(createAdvancedTab(), "Advanced");
        contentArea.add(createUserProfileTab(), "User Profile");
        
        Panel mainContent = Panel.create()
            .layout(Layout.border())
            .add(tabHeader, Layout.BorderConstraints.NORTH)
            .add(contentArea, Layout.BorderConstraints.CENTER);
        
        settingsWindow.add(mainContent, Layout.BorderConstraints.CENTER);
    }
    
    private Panel createGeneralTab() {
        Panel panel = Panel.create()
            .layout(Layout.flow())
            .padding(10);
        
        // Auto-save settings
        Panel autoSavePanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label autoSaveTitle = Label.create("Auto-Save")
            .font("Arial", Font.BOLD, 14);
        
        autoSaveCheck = CheckBox.create("Enable auto-save", true)
            .onChange(enabled -> autoSaveIntervalSpinner.enabled(enabled));
        
        Panel intervalPanel = Panel.create()
            .flowLayout()
            .add(Label.create("Interval (minutes):"))
            .add(autoSaveIntervalSpinner = Spinner.createNumber(5, 1, 60, 1)
                .size(80, 25)
                .numberEditor("#0"));
        
        autoSavePanel.add(autoSaveTitle)
                    .add(autoSaveCheck)
                    .add(intervalPanel);
        
        // Sound settings
        Panel soundPanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label soundTitle = Label.create("Audio")
            .font("Arial", Font.BOLD, 14);
        
        soundsCheck = CheckBox.create("Enable sounds", true);
        notificationsCheck = CheckBox.create("Enable notifications", true);
        
        soundPanel.add(soundTitle)
                  .add(soundsCheck)
                  .add(notificationsCheck);
        
        panel.add(autoSavePanel).add(soundPanel);
        return panel;
    }
    
    private Panel createAppearanceTab() {
        Panel panel = Panel.create()
            .layout(Layout.flow())
            .padding(10);
        
        // Theme settings
        Panel themePanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label themeTitle = Label.create("Theme")
            .font("Arial", Font.BOLD, 14);
        
        Panel themeRow = Panel.create()
            .flowLayout()
            .add(Label.create("Theme:"))
            .add(themeCombo = ComboBox.create("Light", "Dark", "System")
                .selectedItem("Light"));
        
        themePanel.add(themeTitle).add(themeRow);
        
        // Font settings  
        Panel fontPanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label fontTitle = Label.create("Font")
            .font("Arial", Font.BOLD, 14);
        
        Panel fontRow = Panel.create()
            .flowLayout()
            .add(Label.create("Size:"))
            .add(fontSizeCombo = ComboBox.create("Small", "Medium", "Large")
                .selectedItem("Medium"));
        
        fontPanel.add(fontTitle).add(fontRow);
        
        // Language settings
        Panel langPanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label langTitle = Label.create("Language")
            .font("Arial", Font.BOLD, 14);
        
        Panel langRow = Panel.create()
            .flowLayout()
            .add(Label.create("Language:"))
            .add(languageCombo = ComboBox.create("English", "Spanish", "French", "German")
                .selectedItem("English"));
        
        langPanel.add(langTitle).add(langRow);
        
        panel.add(themePanel).add(fontPanel).add(langPanel);
        return panel;
    }
    
    private Panel createAdvancedTab() {
        Panel panel = Panel.create()
            .layout(Layout.flow())
            .padding(10);
        
        // Backup settings
        Panel backupPanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label backupTitle = Label.create("Backup Settings")
            .font("Arial", Font.BOLD, 14);
        
        Panel maxBackupsRow = Panel.create()
            .flowLayout()
            .add(Label.create("Max backups to keep:"))
            .add(maxBackupsSpinner = Spinner.createNumber(10, 1, 100, 1)
                .size(80, 25)
                .numberEditor("#0"));
        
        backupPanel.add(backupTitle).add(maxBackupsRow);
        
        // Debug settings
        Panel debugPanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label debugTitle = Label.create("Debug")
            .font("Arial", Font.BOLD, 14);
        
        CheckBox verboseLogging = CheckBox.create("Enable verbose logging", false);
        CheckBox debugMode = CheckBox.create("Enable debug mode", false);
        
        debugPanel.add(debugTitle)
                  .add(verboseLogging)
                  .add(debugMode);
        
        panel.add(backupPanel).add(debugPanel);
        return panel;
    }
    
    private Panel createUserProfileTab() {
        Panel panel = Panel.create()
            .layout(Layout.flow())
            .padding(10);
        
        Panel profilePanel = Panel.create()
            .border(Color.GRAY, 1)
            .padding(10)
            .verticalLayout();
        
        Label profileTitle = Label.create("User Information")
            .font("Arial", Font.BOLD, 14);
        
        Panel nameRow = Panel.create()
            .flowLayout()
            .add(Label.create("Name:"))
            .add(nameField = TextField.create("Enter your name")
                .size(200, 25));
        
        Panel emailRow = Panel.create()
            .flowLayout()
            .add(Label.create("Email:"))
            .add(emailField = TextField.create("Enter your email")
                .size(200, 25)
                .onChange(this::validateEmail));
        
        profilePanel.add(profileTitle)
                   .add(nameRow)
                   .add(emailRow);
        
        panel.add(profilePanel);
        return panel;
    }
    
    private void createButtonPanel() {
        Panel buttonPanel = Panel.create()
            .backgroundColor(Color.LIGHT_GRAY)
            .flowLayout()
            .padding(10);
        
        Button okButton = Button.create("OK")
            .size(80, 30)
            .backgroundColor(Color.BLUE)
            .foregroundColor(Color.WHITE)
            .onClick(this::saveAndClose);
        
        Button cancelButton = Button.create("Cancel")
            .size(80, 30)
            .onClick(() -> settingsWindow.hide());
        
        Button applyButton = Button.create("Apply")
            .size(80, 30)
            .onClick(this::saveSettings);
        
        Button resetButton = Button.create("Reset")
            .size(80, 30)
            .onClick(this::resetToDefaults);
        
        buttonPanel.add(okButton)
                   .add(cancelButton)
                   .add(applyButton)
                   .add(resetButton);
        
        settingsWindow.add(buttonPanel, Layout.BorderConstraints.SOUTH);
    }
    
    private void switchTab(String tabName) {
        CardLayout cardLayout = (CardLayout) settingsWindow.getSwingComponent()
            .getContentPane().getComponent(1).getLayout();
        cardLayout.show((Container) settingsWindow.getSwingComponent()
            .getContentPane().getComponent(1), tabName);
    }
    
    private void validateEmail(String email) {
        if (email.isEmpty() || email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            emailField.foregroundColor(Color.BLACK);
        } else {
            emailField.foregroundColor(Color.RED);
        }
    }
    
    private void loadSettings() {
        // Load from properties file or use defaults
        settings.setProperty("theme", "Light");
        settings.setProperty("fontSize", "Medium");
        settings.setProperty("language", "English");
        settings.setProperty("autoSave", "true");
        settings.setProperty("autoSaveInterval", "5");
        settings.setProperty("sounds", "true");
        settings.setProperty("notifications", "true");
        settings.setProperty("maxBackups", "10");
        settings.setProperty("userName", "");
        settings.setProperty("userEmail", "");
        
        // Apply loaded settings to controls
        themeCombo.selectedItem(settings.getProperty("theme"));
        fontSizeCombo.selectedItem(settings.getProperty("fontSize"));
        languageCombo.selectedItem(settings.getProperty("language"));
        autoSaveCheck.checked(Boolean.parseBoolean(settings.getProperty("autoSave")));
        autoSaveIntervalSpinner.value(Integer.parseInt(settings.getProperty("autoSaveInterval")));
        soundsCheck.checked(Boolean.parseBoolean(settings.getProperty("sounds")));
        notificationsCheck.checked(Boolean.parseBoolean(settings.getProperty("notifications")));
        maxBackupsSpinner.value(Integer.parseInt(settings.getProperty("maxBackups")));
        nameField.text(settings.getProperty("userName"));
        emailField.text(settings.getProperty("userEmail"));
    }
    
    private void saveSettings() {
        // Validate first
        if (!validateSettings()) {
            return;
        }
        
        // Save to properties
        settings.setProperty("theme", themeCombo.getSelectedItem());
        settings.setProperty("fontSize", fontSizeCombo.getSelectedItem());
        settings.setProperty("language", languageCombo.getSelectedItem());
        settings.setProperty("autoSave", String.valueOf(autoSaveCheck.isChecked()));
        settings.setProperty("autoSaveInterval", String.valueOf(autoSaveIntervalSpinner.getIntValue()));
        settings.setProperty("sounds", String.valueOf(soundsCheck.isChecked()));
        settings.setProperty("notifications", String.valueOf(notificationsCheck.isChecked()));
        settings.setProperty("maxBackups", String.valueOf(maxBackupsSpinner.getIntValue()));
        settings.setProperty("userName", nameField.getText());
        settings.setProperty("userEmail", emailField.getText());
        
        // In a real application, save to file
        JOptionPane.showMessageDialog(
            settingsWindow.getSwingComponent(),
            "Settings saved successfully",
            "Settings",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private boolean validateSettings() {
        String email = emailField.getText();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(
                settingsWindow.getSwingComponent(),
                "Please enter a valid email address",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }
    
    private void saveAndClose() {
        if (validateSettings()) {
            saveSettings();
            settingsWindow.hide();
        }
    }
    
    private void resetToDefaults() {
        int result = JOptionPane.showConfirmDialog(
            settingsWindow.getSwingComponent(),
            "Reset all settings to defaults?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            themeCombo.selectedItem("Light");
            fontSizeCombo.selectedItem("Medium");
            languageCombo.selectedItem("English");
            autoSaveCheck.checked(true);
            autoSaveIntervalSpinner.value(5);
            soundsCheck.checked(true);
            notificationsCheck.checked(true);
            maxBackupsSpinner.value(10);
            nameField.text("");
            emailField.text("");
        }
    }
}
```

These complete examples demonstrate real-world usage patterns of Vibe UI components, including:

- **Complex layouts** with multiple panels and containers
- **Event handling** for user interactions and data validation
- **Data management** with CRUD operations
- **Error handling** and user feedback
- **Professional UI patterns** like forms, lists, and dialogs
- **Best practices** for code organization and maintainability

Each example can be compiled and run independently to see Vibe UI in action!