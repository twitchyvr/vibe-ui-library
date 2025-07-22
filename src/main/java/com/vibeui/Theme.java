package com.vibeui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class Theme {
    private static ThemeStyle currentTheme = ThemeStyle.MODERN_DARK;
    private static final Map<String, Object> customProperties = new HashMap<>();
    
    // Core theme enumeration
    public enum ThemeStyle {
        MODERN_DARK,
        MODERN_LIGHT,
        GLASSMORPHIC,
        RETRO_90S,
        WINDOWS_XP,
        MAC_OS,
        CYBERPUNK,
        MINIMAL,
        GAMING_HUD
    }

    // Apply theme to entire application
    public static void apply(ThemeStyle theme) {
        currentTheme = theme;
        
        try {
            switch (theme) {
                case MODERN_DARK:
                    applyModernDark();
                    break;
                case MODERN_LIGHT:
                    applyModernLight();
                    break;
                case GLASSMORPHIC:
                    applyGlassmorphic();
                    break;
                case RETRO_90S:
                    applyRetro90s();
                    break;
                case WINDOWS_XP:
                    applyWindowsXP();
                    break;
                case MAC_OS:
                    applyMacOS();
                    break;
                case CYBERPUNK:
                    applyCyberpunk();
                    break;
                case MINIMAL:
                    applyMinimal();
                    break;
                case GAMING_HUD:
                    applyGamingHUD();
                    break;
            }
            
            // Refresh all open windows
            for (java.awt.Window window : java.awt.Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
            
        } catch (Exception e) {
            System.err.println("Failed to apply theme: " + e.getMessage());
        }
    }

    private static void applyModernDark() throws Exception {
        // Use system default look and feel
        
        // Dark theme colors
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("Button.background", new Color(70, 70, 70));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.border", new RoundedBorder(8, new Color(100, 100, 100)));
        UIManager.put("TextField.background", new Color(60, 60, 60));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.border", new RoundedBorder(6, new Color(80, 80, 80)));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Window.background", new Color(35, 35, 35));
        UIManager.put("TabbedPane.background", new Color(45, 45, 45));
        UIManager.put("TabbedPane.foreground", Color.WHITE);
        UIManager.put("Tree.background", new Color(50, 50, 50));
        UIManager.put("Tree.foreground", Color.WHITE);
        UIManager.put("Table.background", new Color(50, 50, 50));
        UIManager.put("Table.foreground", Color.WHITE);
        UIManager.put("Table.gridColor", new Color(70, 70, 70));
    }

    private static void applyModernLight() throws Exception {
        // Use system default look and feel
        
        // Light theme colors
        UIManager.put("Panel.background", new Color(250, 250, 250));
        UIManager.put("Button.background", new Color(240, 240, 240));
        UIManager.put("Button.foreground", new Color(40, 40, 40));
        UIManager.put("Button.border", new RoundedBorder(8, new Color(200, 200, 200)));
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", new Color(40, 40, 40));
        UIManager.put("TextField.border", new RoundedBorder(6, new Color(180, 180, 180)));
        UIManager.put("Label.foreground", new Color(40, 40, 40));
        UIManager.put("Window.background", Color.WHITE);
    }

    private static void applyGlassmorphic() throws Exception {
        // Use system default look and feel
        
        // Glassmorphic theme with transparency and blur effects
        UIManager.put("Panel.background", new Color(255, 255, 255, 180));
        UIManager.put("Button.background", new Color(255, 255, 255, 200));
        UIManager.put("Button.foreground", new Color(60, 60, 60));
        UIManager.put("Button.border", new GlassBorder());
        UIManager.put("TextField.background", new Color(255, 255, 255, 160));
        UIManager.put("TextField.foreground", new Color(40, 40, 40));
        UIManager.put("TextField.border", new GlassBorder());
        UIManager.put("Window.background", new Color(240, 245, 255, 220));
    }

    private static void applyRetro90s() throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        
        // 90s Windows style colors
        UIManager.put("Panel.background", new Color(192, 192, 192));
        UIManager.put("Button.background", new Color(192, 192, 192));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.border", new Retro90sBorder());
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("TextField.border", new Inset3DBorder());
        UIManager.put("Label.foreground", Color.BLACK);
        UIManager.put("Window.background", new Color(192, 192, 192));
        UIManager.put("TabbedPane.background", new Color(192, 192, 192));
        UIManager.put("MenuBar.background", new Color(192, 192, 192));
    }

    private static void applyWindowsXP() throws Exception {
        // Use system default look and feel
        
        // Windows XP Luna theme colors
        UIManager.put("Panel.background", new Color(236, 233, 216));
        UIManager.put("Button.background", new Color(245, 245, 245));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.border", new XPButtonBorder());
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("Window.background", new Color(236, 233, 216));
        UIManager.put("MenuBar.background", new Color(158, 190, 245));
        UIManager.put("TabbedPane.background", new Color(236, 233, 216));
    }

    private static void applyMacOS() throws Exception {
        // Try to use Mac look and feel if available
        try {
            UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
        } catch (Exception e) {
            // Use system default look and feel
        }
        
        // macOS style colors
        UIManager.put("Panel.background", new Color(246, 246, 246));
        UIManager.put("Button.background", new Color(255, 255, 255));
        UIManager.put("Button.foreground", new Color(50, 50, 50));
        UIManager.put("Button.border", new MacOSBorder());
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.border", new RoundedBorder(6, new Color(200, 200, 200)));
        UIManager.put("Window.background", new Color(246, 246, 246));
    }

    private static void applyCyberpunk() throws Exception {
        // Use system default look and feel
        
        // Cyberpunk neon theme
        UIManager.put("Panel.background", new Color(15, 15, 35));
        UIManager.put("Button.background", new Color(25, 25, 55));
        UIManager.put("Button.foreground", new Color(0, 255, 255));
        UIManager.put("Button.border", new NeonBorder(new Color(0, 255, 255)));
        UIManager.put("TextField.background", new Color(20, 20, 40));
        UIManager.put("TextField.foreground", new Color(0, 255, 255));
        UIManager.put("TextField.border", new NeonBorder(new Color(255, 0, 255)));
        UIManager.put("Label.foreground", new Color(0, 255, 255));
        UIManager.put("Window.background", new Color(10, 10, 25));
    }

    private static void applyMinimal() throws Exception {
        // Use system default look and feel
        
        // Minimal flat design
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", new Color(70, 70, 70));
        UIManager.put("Button.border", new FlatBorder(new Color(220, 220, 220)));
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", new Color(70, 70, 70));
        UIManager.put("TextField.border", new FlatBorder(new Color(200, 200, 200)));
        UIManager.put("Label.foreground", new Color(70, 70, 70));
        UIManager.put("Window.background", Color.WHITE);
    }

    private static void applyGamingHUD() throws Exception {
        // Use system default look and feel
        
        // Gaming HUD style
        UIManager.put("Panel.background", new Color(0, 0, 0, 180));
        UIManager.put("Button.background", new Color(100, 150, 0, 200));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.border", new HexagonBorder(new Color(150, 255, 0)));
        UIManager.put("TextField.background", new Color(20, 40, 0, 200));
        UIManager.put("TextField.foreground", new Color(150, 255, 0));
        UIManager.put("TextField.border", new HexagonBorder(new Color(100, 200, 0)));
        UIManager.put("Label.foreground", new Color(150, 255, 0));
        UIManager.put("Window.background", new Color(0, 0, 0, 220));
    }

    // Getters
    public static ThemeStyle getCurrentTheme() {
        return currentTheme;
    }

    public static Color getThemeColor(String property) {
        Object value = UIManager.get(property);
        return value instanceof Color ? (Color) value : Color.BLACK;
    }

    // Custom properties
    public static void setCustomProperty(String key, Object value) {
        customProperties.put(key, value);
    }

    public static Object getCustomProperty(String key) {
        return customProperties.get(key);
    }

    // Utility method to create themed components
    public static void applyThemeToComponent(JComponent component) {
        SwingUtilities.updateComponentTreeUI(component);
    }

    // Custom border classes
    public static class RoundedBorder implements Border {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class GlassBorder implements Border {
        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Glass effect with gradient
            GradientPaint gradient = new GradientPaint(
                x, y, new Color(255, 255, 255, 100),
                x, y + height, new Color(255, 255, 255, 50)
            );
            g2d.setPaint(gradient);
            g2d.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class NeonBorder implements Border {
        private final Color neonColor;

        public NeonBorder(Color neonColor) {
            this.neonColor = neonColor;
        }

        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Neon glow effect
            for (int i = 3; i > 0; i--) {
                g2d.setColor(new Color(neonColor.getRed(), neonColor.getGreen(), neonColor.getBlue(), 60 / i));
                g2d.setStroke(new BasicStroke(i * 2));
                g2d.drawRoundRect(x + i, y + i, width - 2 * i - 1, height - 2 * i - 1, 8, 8);
            }
            
            g2d.setColor(neonColor);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(x + 2, y + 2, width - 5, height - 5, 8, 8);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(5, 5, 5, 5);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class Retro90sBorder implements Border {
        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            // Classic 90s raised border
            g.setColor(Color.WHITE);
            g.drawLine(x, y, x + width - 1, y);
            g.drawLine(x, y, x, y + height - 1);
            
            g.setColor(new Color(128, 128, 128));
            g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class XPButtonBorder implements Border {
        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(0, 60, 116));
            g2d.drawRoundRect(x, y, width - 1, height - 1, 6, 6);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(3, 3, 3, 3);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class MacOSBorder implements Border {
        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(180, 180, 180));
            g2d.drawRoundRect(x, y, width - 1, height - 1, 8, 8);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(4, 4, 4, 4);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class FlatBorder implements Border {
        private final Color color;

        public FlatBorder(Color color) {
            this.color = color;
        }

        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawRect(x, y, width - 1, height - 1);
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(1, 1, 1, 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class HexagonBorder implements Border {
        private final Color color;

        public HexagonBorder(Color color) {
            this.color = color;
        }

        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2));
            
            // Draw hexagon-like border
            int[] xPoints = {x + 10, x + width - 10, x + width, x + width - 10, x + 10, x};
            int[] yPoints = {y, y, y + height / 2, y + height, y + height, y + height / 2};
            g2d.drawPolygon(xPoints, yPoints, 6);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(3, 10, 3, 10);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class Inset3DBorder implements Border {
        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            // 3D inset effect
            g.setColor(new Color(128, 128, 128));
            g.drawLine(x, y, x + width - 1, y);
            g.drawLine(x, y, x, y + height - 1);
            
            g.setColor(Color.WHITE);
            g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}