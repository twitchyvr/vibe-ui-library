package com.vibeui.demo;

import com.vibeui.*;
import javax.swing.SwingUtilities;
import javax.swing.ButtonGroup;
import java.awt.Color;
import java.awt.Font;

public class ComprehensiveDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createComprehensiveDemo();
        });
    }

    private static void createComprehensiveDemo() {
        Window mainWindow = Window.create("Vibe UI - Complete Component Demo")
                .size(800, 600)
                .centerOnScreen()
                .resizable(true)
                .layout(Layout.border());

        Panel headerPanel = Panel.create()
                .backgroundColor(new Color(70, 130, 180))
                .padding(15);

        Label titleLabel = Label.create("Vibe UI Component Showcase")
                .font("Arial", Font.BOLD, 20)
                .foregroundColor(Color.WHITE)
                .alignCenter();

        headerPanel.add(titleLabel);

        Panel mainPanel = Panel.create()
                .layout(Layout.flow())
                .padding(20);

        Panel buttonsPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .flowLayout();

        Button standardButton = Button.create("Standard Button")
                .size(150, 30)
                .onClick(() -> System.out.println("Standard button clicked!"));

        ToggleButton toggleButton = ToggleButton.create("Toggle Me")
                .size(150, 30)
                .onChange(selected -> System.out.println("Toggle: " + selected));

        buttonsPanel.add(standardButton).add(toggleButton);

        Panel inputPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .flowLayout();

        TextField singleLineText = TextField.create("Single line input")
                .size(200, 30)
                .onChange(text -> System.out.println("Text changed: " + text));

        TextBox multiLineText = TextBox.createMultiline("Multi-line text area")
                .size(200, 80)
                .rows(3)
                .columns(20)
                .wordWrap(true);

        inputPanel.add(singleLineText).add(multiLineText);

        Panel sliderPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .flowLayout();

        Label sliderLabel = Label.create("Volume: 50");

        Slider volumeSlider = Slider.create(0, 100, 50)
                .size(200, 50)
                .showTicks(true)
                .majorTickSpacing(25)
                .minorTickSpacing(5)
                .showLabels(true)
                .onChange(value -> {
                    sliderLabel.text("Volume: " + value);
                    System.out.println("Slider value: " + value);
                });

        sliderPanel.add(sliderLabel).add(volumeSlider);

        Panel checkboxPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .verticalLayout();

        CheckBox enableFeature = CheckBox.create("Enable Advanced Features")
                .onChange(checked -> System.out.println("Feature enabled: " + checked));

        CheckBox sendNotifications = CheckBox.create("Send Notifications", true)
                .onChange(checked -> System.out.println("Notifications: " + checked));

        checkboxPanel.add(enableFeature).add(sendNotifications);

        Panel radioPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .verticalLayout();

        ButtonGroup themeGroup = RadioButton.createGroup();

        RadioButton lightTheme = RadioButton.create("Light Theme", true)
                .group(themeGroup)
                .onChange(selected -> {
                    if (selected) System.out.println("Light theme selected");
                });

        RadioButton darkTheme = RadioButton.create("Dark Theme")
                .group(themeGroup)
                .onChange(selected -> {
                    if (selected) System.out.println("Dark theme selected");
                });

        RadioButton autoTheme = RadioButton.create("Auto Theme")
                .group(themeGroup)
                .onChange(selected -> {
                    if (selected) System.out.println("Auto theme selected");
                });

        radioPanel.add(lightTheme).add(darkTheme).add(autoTheme);

        Panel controlsPanel = Panel.create()
                .border(Color.GRAY, 1)
                .padding(10)
                .flowLayout();

        Button minimizeBtn = Button.create("Minimize")
                .onClick(() -> mainWindow.minimize());

        Button maximizeBtn = Button.create("Maximize")
                .onClick(() -> mainWindow.maximize());

        Button restoreBtn = Button.create("Restore")
                .onClick(() -> mainWindow.restore());

        controlsPanel.add(minimizeBtn).add(maximizeBtn).add(restoreBtn);

        mainPanel.add(buttonsPanel)
                .add(inputPanel)
                .add(sliderPanel)
                .add(checkboxPanel)
                .add(radioPanel)
                .add(controlsPanel);

        Panel footerPanel = Panel.create()
                .backgroundColor(new Color(240, 240, 240))
                .padding(10);

        Label statusLabel = Label.create("Ready - All components loaded successfully")
                .font("Arial", Font.ITALIC, 12)
                .alignCenter();

        footerPanel.add(statusLabel);

        mainWindow.add(headerPanel, Layout.BorderConstraints.NORTH)
                .add(mainPanel, Layout.BorderConstraints.CENTER)
                .add(footerPanel, Layout.BorderConstraints.SOUTH)
                .build()
                .show();

        System.out.println("Comprehensive demo application started successfully!");
        System.out.println("Platform: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        System.out.println("Java Version: " + System.getProperty("java.version"));
    }
}