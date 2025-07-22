package com.vibeui.demo;

import com.vibeui.*;
import javax.swing.SwingUtilities;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class NewControlsDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createNewControlsDemo();
        });
    }

    private static void createNewControlsDemo() {
        Window mainWindow = Window.create("Vibe UI - New Controls Demo")
                .size(900, 700)
                .centerOnScreen()
                .resizable(true)
                .layout(Layout.border());

        Panel headerPanel = Panel.create()
                .backgroundColor(new Color(60, 120, 180))
                .padding(15);

        Label titleLabel = Label.create("New UI Controls Showcase")
                .font("Arial", Font.BOLD, 18)
                .foregroundColor(Color.WHITE)
                .alignCenter();

        headerPanel.add(titleLabel);

        Panel mainPanel = Panel.create()
                .layout(Layout.flow())
                .padding(20);

        Panel comboBoxPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .verticalLayout();

        Label comboLabel = Label.create("ComboBox Demo")
                .font("Arial", Font.BOLD, 14);

        ComboBox languageCombo = ComboBox.create("Java", "Python", "JavaScript", "C++", "Go")
                .selectedItem("Java")
                .size(150, 30)
                .onChange(selected -> System.out.println("Language selected: " + selected));

        ComboBox editableCombo = ComboBox.createEmpty()
                .addItem("Option 1")
                .addItem("Option 2")
                .addItem("Option 3")
                .editable(true)
                .size(150, 30)
                .onChange(value -> System.out.println("Editable combo value: " + value));

        comboBoxPanel.add(comboLabel).add(languageCombo).add(editableCombo);

        Panel listBoxPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .verticalLayout();

        Label listLabel = Label.create("ListBox Demo")
                .font("Arial", Font.BOLD, 14);

        ListBox singleSelectionList = ListBox.create("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
                .size(200, 120)
                .visibleRowCount(4)
                .singleSelection()
                .onSelection(selected -> System.out.println("Single selection: " + selected));

        ListBox multiSelectionList = ListBox.create()
                .addItems(Arrays.asList("Apple", "Banana", "Cherry", "Date", "Elderberry"))
                .size(200, 120)
                .visibleRowCount(4)
                .multipleSelection()
                .onMultiSelection(selected -> System.out.println("Multi selection: " + selected));

        listBoxPanel.add(listLabel).add(singleSelectionList).add(multiSelectionList);

        Panel progressPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .verticalLayout();

        Label progressLabel = Label.create("ProgressBar Demo")
                .font("Arial", Font.BOLD, 14);

        ProgressBar determinate = ProgressBar.create(100)
                .value(30)
                .size(200, 25)
                .showString()
                .text("Loading...")
                .progressColor(new Color(0, 150, 0))
                .onChange(value -> System.out.println("Progress: " + value + "%"));

        ProgressBar indeterminate = ProgressBar.create()
                .indeterminate()
                .size(200, 25)
                .text("Processing...")
                .showString();

        Button animateBtn = Button.create("Animate Progress")
                .onClick(() -> {
                    determinate.animateTo(100, 3000);
                    System.out.println("Animating progress to 100%");
                });

        Button resetBtn = Button.create("Reset")
                .onClick(() -> {
                    determinate.setToMinimum();
                    System.out.println("Progress reset to minimum");
                });

        progressPanel.add(progressLabel).add(determinate).add(indeterminate)
                     .add(animateBtn).add(resetBtn);

        Panel spinnerPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .verticalLayout();

        Label spinnerLabel = Label.create("Spinner Demo")
                .font("Arial", Font.BOLD, 14);

        Spinner numberSpinner = Spinner.createNumber(50, 0, 100, 5)
                .size(100, 30)
                .numberEditor("#0")
                .onNumberChange(value -> System.out.println("Number: " + value));

        Spinner doubleSpinner = Spinner.createNumber(3.14, 0.0, 10.0, 0.1)
                .size(100, 30)
                .numberEditor("#0.00")
                .onNumberChange(value -> System.out.println("Double: " + value.doubleValue()));

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        
        Spinner dateSpinner = Spinner.createDate(today)
                .size(150, 30)
                .dayField()
                .dateEditor("MM/dd/yyyy")
                .onChange(value -> System.out.println("Date: " + value));

        Spinner listSpinner = Spinner.createList("Small", "Medium", "Large", "Extra Large")
                .size(100, 30)
                .onChange(value -> System.out.println("Size: " + value));

        Button incrementBtn = Button.create("+")
                .onClick(() -> numberSpinner.nextValue());

        Button decrementBtn = Button.create("-")
                .onClick(() -> numberSpinner.previousValue());

        spinnerPanel.add(spinnerLabel)
                   .add(numberSpinner)
                   .add(doubleSpinner)
                   .add(dateSpinner)
                   .add(listSpinner)
                   .add(incrementBtn)
                   .add(decrementBtn);

        Panel testPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .verticalLayout();

        Label testLabel = Label.create("Integration Test")
                .font("Arial", Font.BOLD, 14);

        Button testAllBtn = Button.create("Test All Controls")
                .size(200, 35)
                .backgroundColor(new Color(70, 130, 180))
                .foregroundColor(Color.WHITE)
                .onClick(() -> {
                    System.out.println("=== Testing All New Controls ===");
                    
                    System.out.println("ComboBox: " + languageCombo.getSelectedItem());
                    System.out.println("Editable ComboBox items: " + editableCombo.getAllItems());
                    
                    System.out.println("Single ListBox: " + singleSelectionList.getSelectedItem());
                    System.out.println("Multi ListBox: " + multiSelectionList.getSelectedItems());
                    
                    System.out.println("Progress: " + determinate.getValue() + "/" + determinate.getMaximum() + 
                                     " (" + (determinate.getPercentComplete() * 100) + "%)");
                    
                    System.out.println("Number Spinner: " + numberSpinner.getNumberValue());
                    System.out.println("Double Spinner: " + doubleSpinner.getDoubleValue());
                    System.out.println("Date Spinner: " + dateSpinner.getDateValue());
                    System.out.println("List Spinner: " + listSpinner.getValue());
                    
                    System.out.println("=== Test Complete ===");
                });

        testPanel.add(testLabel).add(testAllBtn);

        mainPanel.add(comboBoxPanel)
                .add(listBoxPanel)
                .add(progressPanel)
                .add(spinnerPanel)
                .add(testPanel);

        Panel footerPanel = Panel.create()
                .backgroundColor(new Color(240, 240, 240))
                .padding(10);

        Label statusLabel = Label.create("New controls demo loaded - ComboBox, ListBox, ProgressBar, Spinner")
                .font("Arial", Font.ITALIC, 12)
                .alignCenter();

        footerPanel.add(statusLabel);

        mainWindow.add(headerPanel, Layout.BorderConstraints.NORTH)
                .add(mainPanel, Layout.BorderConstraints.CENTER)
                .add(footerPanel, Layout.BorderConstraints.SOUTH)
                .build()
                .show();

        System.out.println("New controls demo application started!");
        System.out.println("Available controls: ComboBox, ListBox, ProgressBar, Spinner");
        System.out.println("Try interacting with each control to see the console output.");
    }
}