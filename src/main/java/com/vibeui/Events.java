package com.vibeui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class Events {
    
    @FunctionalInterface
    public interface ClickHandler {
        void onClick();
    }
    
    @FunctionalInterface
    public interface MouseHandler {
        void onMouse(MouseEvent event);
    }
    
    @FunctionalInterface
    public interface KeyHandler {
        void onKey(KeyEvent event);
    }
    
    @FunctionalInterface
    public interface TextChangeHandler {
        void onChange(String text);
    }

    public static ActionListener click(ClickHandler handler) {
        return e -> handler.onClick();
    }

    public static ActionListener click(Runnable action) {
        return e -> action.run();
    }

    public static MouseListener mouse() {
        return new MouseAdapter();
    }

    public static MouseListener mouseClick(MouseHandler handler) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handler.onMouse(e);
            }
        };
    }

    public static MouseListener mouseEnter(MouseHandler handler) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                handler.onMouse(e);
            }
        };
    }

    public static MouseListener mouseExit(MouseHandler handler) {
        return new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                handler.onMouse(e);
            }
        };
    }

    public static KeyListener key() {
        return new KeyAdapter();
    }

    public static KeyListener keyPressed(KeyHandler handler) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handler.onKey(e);
            }
        };
    }

    public static KeyListener keyReleased(KeyHandler handler) {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                handler.onKey(e);
            }
        };
    }

    public static Consumer<String> textChange(TextChangeHandler handler) {
        return handler::onChange;
    }

    private static class MouseAdapter implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    private static class KeyAdapter implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }
}