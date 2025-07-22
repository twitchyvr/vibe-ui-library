package com.vibeui.demo;

import com.vibeui.*;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;

public class DemoApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createDemoWindow();
        });
    }

    private static void createDemoWindow() {
        Window window = Window.create("Vibe UI Demo")
                .size(600, 400)
                .centerOnScreen()
                .layout(Layout.border());

        Panel headerPanel = Panel.create()
                .backgroundColor(new Color(240, 240, 240))
                .padding(10);

        Label titleLabel = Label.create("Welcome to Vibe UI!")
                .font("Arial", Font.BOLD, 18)
                .alignCenter();

        headerPanel.add(titleLabel);

        Panel mainPanel = Panel.create()
                .layout(Layout.flow())
                .padding(20);

        Button helloButton = Button.create("Click Me!")
                .size(120, 30)
                .onClick(() -> {
                    System.out.println("Hello from Vibe UI!");
                    titleLabel.text("Button Clicked!");
                });

        TextField textField = TextField.create("Type something...")
                .size(200, 30)
                .onChange(text -> {
                    System.out.println("Text changed: " + text);
                });

        Label statusLabel = Label.create("Status: Ready")
                .foregroundColor(Color.BLUE);

        Button changeStatusButton = Button.create("Change Status")
                .size(120, 30)
                .onClick(() -> {
                    String currentText = textField.getText();
                    statusLabel.text("Status: " + (currentText.isEmpty() ? "Empty" : currentText));
                });

        mainPanel.add(helloButton)
                .add(textField)
                .add(changeStatusButton)
                .add(statusLabel);

        Panel footerPanel = Panel.create()
                .backgroundColor(new Color(220, 220, 220))
                .padding(5);

        Label footerLabel = Label.create("Vibe UI - Easy Java GUI Development")
                .font("Arial", Font.ITALIC, 12)
                .alignCenter();

        footerPanel.add(footerLabel);

        window.add(headerPanel, Layout.BorderConstraints.NORTH)
                .add(mainPanel, Layout.BorderConstraints.CENTER)
                .add(footerPanel, Layout.BorderConstraints.SOUTH)
                .build()
                .show();

        System.out.println("Demo application started successfully on platform: " + 
                          System.getProperty("os.name") + " " + System.getProperty("os.arch"));
    }
}