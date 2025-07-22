package com.vibeui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class Dialog {
    private JDialog dialog;
    private Component<?> parent;
    private Consumer<DialogResult> resultHandler;

    public enum DialogType {
        INFORMATION,
        WARNING,
        ERROR,
        QUESTION,
        CUSTOM
    }

    public enum DialogResult {
        OK,
        CANCEL,
        YES,
        NO,
        CLOSE
    }

    private Dialog(Component<?> parent, String title) {
        JFrame parentFrame = null;
        if (parent != null) {
            Container container = parent.getSwingComponent().getTopLevelAncestor();
            if (container instanceof JFrame) {
                parentFrame = (JFrame) container;
            }
        }
        
        this.dialog = new JDialog(parentFrame, title, true);
        this.parent = parent;
        configureDefaults();
    }

    public static Dialog create(String title) {
        return new Dialog(null, title);
    }

    public static Dialog create(Component<?> parent, String title) {
        return new Dialog(parent, title);
    }

    private void configureDefaults() {
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (resultHandler != null) {
                    resultHandler.accept(DialogResult.CLOSE);
                }
            }
        });
    }

    public Dialog size(int width, int height) {
        dialog.setSize(width, height);
        return this;
    }

    public Dialog preferredSize(int width, int height) {
        dialog.setPreferredSize(new Dimension(width, height));
        return this;
    }

    public Dialog centerOnParent() {
        if (parent != null) {
            dialog.setLocationRelativeTo(parent.getSwingComponent());
        } else {
            dialog.setLocationRelativeTo(null);
        }
        return this;
    }

    public Dialog centerOnScreen() {
        dialog.setLocationRelativeTo(null);
        return this;
    }

    public Dialog resizable(boolean resizable) {
        dialog.setResizable(resizable);
        return this;
    }

    public Dialog modal(boolean modal) {
        dialog.setModal(modal);
        return this;
    }

    public Dialog alwaysOnTop(boolean onTop) {
        dialog.setAlwaysOnTop(onTop);
        return this;
    }

    public Dialog content(Component<?> content) {
        dialog.add(content.getSwingComponent(), BorderLayout.CENTER);
        return this;
    }

    public Dialog buttons(DialogButton... buttons) {
        Panel buttonPanel = Panel.create()
            .flowLayout()
            .padding(10);
        
        for (DialogButton button : buttons) {
            Button vibeButton = Button.create(button.getText())
                .onClick(() -> {
                    if (resultHandler != null) {
                        resultHandler.accept(button.getResult());
                    }
                    if (button.isClosesDialog()) {
                        dialog.dispose();
                    }
                });
            
            buttonPanel.add(vibeButton);
        }
        
        dialog.add(buttonPanel.getSwingComponent(), BorderLayout.SOUTH);
        return this;
    }

    public Dialog onResult(Consumer<DialogResult> handler) {
        this.resultHandler = handler;
        return this;
    }

    public Dialog show() {
        dialog.pack();
        dialog.setVisible(true);
        return this;
    }

    public Dialog hide() {
        dialog.setVisible(false);
        return this;
    }

    public Dialog dispose() {
        dialog.dispose();
        return this;
    }

    // Static convenience methods for common dialogs
    public static void showMessage(String message) {
        showMessage(null, "Information", message, DialogType.INFORMATION);
    }

    public static void showMessage(Component<?> parent, String title, String message, DialogType type) {
        int messageType;
        switch (type) {
            case WARNING: messageType = JOptionPane.WARNING_MESSAGE; break;
            case ERROR: messageType = JOptionPane.ERROR_MESSAGE; break;
            case QUESTION: messageType = JOptionPane.QUESTION_MESSAGE; break;
            default: messageType = JOptionPane.INFORMATION_MESSAGE; break;
        }
        
        JFrame parentFrame = null;
        if (parent != null) {
            Container container = parent.getSwingComponent().getTopLevelAncestor();
            if (container instanceof JFrame) {
                parentFrame = (JFrame) container;
            }
        }
        
        JOptionPane.showMessageDialog(parentFrame, message, title, messageType);
    }

    public static boolean showConfirm(String message) {
        return showConfirm(null, "Confirm", message);
    }

    public static boolean showConfirm(Component<?> parent, String title, String message) {
        JFrame parentFrame = null;
        if (parent != null) {
            Container container = parent.getSwingComponent().getTopLevelAncestor();
            if (container instanceof JFrame) {
                parentFrame = (JFrame) container;
            }
        }
        
        int result = JOptionPane.showConfirmDialog(
            parentFrame, message, title, JOptionPane.YES_NO_OPTION
        );
        
        return result == JOptionPane.YES_OPTION;
    }

    public static String showInput(String message) {
        return showInput(null, "Input", message, "");
    }

    public static String showInput(Component<?> parent, String title, String message, String defaultValue) {
        JFrame parentFrame = null;
        if (parent != null) {
            Container container = parent.getSwingComponent().getTopLevelAncestor();
            if (container instanceof JFrame) {
                parentFrame = (JFrame) container;
            }
        }
        
        return (String) JOptionPane.showInputDialog(
            parentFrame, message, title, JOptionPane.QUESTION_MESSAGE, null, null, defaultValue
        );
    }

    // Custom dialog builders
    public static Dialog createMessageDialog(String message) {
        Dialog dialog = create("Message")
            .size(300, 150)
            .centerOnScreen()
            .resizable(false);
        
        Panel content = Panel.create()
            .layout(Layout.border())
            .padding(20);
        
        Label messageLabel = Label.create(message)
            .alignCenter();
        
        content.add(messageLabel, Layout.BorderConstraints.CENTER);
        
        dialog.content(content)
               .buttons(DialogButton.ok());
        
        return dialog;
    }

    public static Dialog createConfirmDialog(String message, Consumer<Boolean> callback) {
        Dialog dialog = create("Confirm")
            .size(350, 150)
            .centerOnScreen()
            .resizable(false);
        
        Panel content = Panel.create()
            .layout(Layout.border())
            .padding(20);
        
        Label messageLabel = Label.create(message)
            .alignCenter();
        
        content.add(messageLabel, Layout.BorderConstraints.CENTER);
        
        dialog.content(content)
               .buttons(
                   DialogButton.yes(),
                   DialogButton.no()
               )
               .onResult(result -> {
                   if (callback != null) {
                       callback.accept(result == DialogResult.YES);
                   }
               });
        
        return dialog;
    }

    // Helper class for dialog buttons
    public static class DialogButton {
        private String text;
        private DialogResult result;
        private boolean closesDialog = true;

        private DialogButton(String text, DialogResult result) {
            this.text = text;
            this.result = result;
        }

        public static DialogButton create(String text, DialogResult result) {
            return new DialogButton(text, result);
        }

        public static DialogButton ok() {
            return new DialogButton("OK", DialogResult.OK);
        }

        public static DialogButton cancel() {
            return new DialogButton("Cancel", DialogResult.CANCEL);
        }

        public static DialogButton yes() {
            return new DialogButton("Yes", DialogResult.YES);
        }

        public static DialogButton no() {
            return new DialogButton("No", DialogResult.NO);
        }

        public static DialogButton close() {
            return new DialogButton("Close", DialogResult.CLOSE);
        }

        public DialogButton closesDialog(boolean closes) {
            this.closesDialog = closes;
            return this;
        }

        // Getters
        public String getText() { return text; }
        public DialogResult getResult() { return result; }
        public boolean isClosesDialog() { return closesDialog; }
    }
}