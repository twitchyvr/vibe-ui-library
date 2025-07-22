package com.vibeui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class Viewport extends Component<Viewport> {
    private JPanel viewport;
    private Graphics2D g2d;
    private Consumer<Graphics2D> renderHandler;
    private Consumer<MouseEvent> mouseClickHandler;
    private Consumer<MouseEvent> mouseMotionHandler;
    private Consumer<MouseEvent> mouseEnterHandler;
    private Consumer<MouseEvent> mouseExitHandler;
    private Timer animationTimer;
    private boolean doubleBuffered = true;
    private Color clearColor = Color.BLACK;
    private boolean antiAlias = true;

    private Viewport(int width, int height) {
        super(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                if (antiAlias) {
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                }
                
                // Clear with background color
                g2d.setColor(clearColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Call custom render handler
                if (renderHandler != null) {
                    renderHandler.accept(g2d);
                }
                
                g2d.dispose();
            }
        });
        
        this.viewport = (JPanel) swingComponent;
        this.viewport.setPreferredSize(new Dimension(width, height));
        this.viewport.setDoubleBuffered(doubleBuffered);
        setupEventHandlers();
    }

    public static Viewport create(int width, int height) {
        return new Viewport(width, height);
    }

    public static Viewport createFullscreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Viewport(screenSize.width, screenSize.height);
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
        viewport.setBackground(clearColor);
        viewport.setFocusable(true);
    }

    private void setupEventHandlers() {
        viewport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mouseClickHandler != null) {
                    mouseClickHandler.accept(e);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (mouseEnterHandler != null) {
                    mouseEnterHandler.accept(e);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (mouseExitHandler != null) {
                    mouseExitHandler.accept(e);
                }
            }
        });

        viewport.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (mouseMotionHandler != null) {
                    mouseMotionHandler.accept(e);
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseMotionHandler != null) {
                    mouseMotionHandler.accept(e);
                }
            }
        });
    }

    // Event handlers
    public Viewport onRender(Consumer<Graphics2D> handler) {
        this.renderHandler = handler;
        return this;
    }

    public Viewport onMouseClick(Consumer<MouseEvent> handler) {
        this.mouseClickHandler = handler;
        return this;
    }

    public Viewport onMouseMotion(Consumer<MouseEvent> handler) {
        this.mouseMotionHandler = handler;
        return this;
    }

    public Viewport onMouseEnter(Consumer<MouseEvent> handler) {
        this.mouseEnterHandler = handler;
        return this;
    }

    public Viewport onMouseExit(Consumer<MouseEvent> handler) {
        this.mouseExitHandler = handler;
        return this;
    }

    // Rendering configuration
    public Viewport clearColor(Color color) {
        this.clearColor = color;
        viewport.setBackground(color);
        return this;
    }

    public Viewport antiAlias(boolean enabled) {
        this.antiAlias = enabled;
        return this;
    }

    public Viewport doubleBuffered(boolean enabled) {
        this.doubleBuffered = enabled;
        viewport.setDoubleBuffered(enabled);
        return this;
    }

    // Animation support
    public Viewport startAnimation(int frameRate) {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        
        int delay = 1000 / frameRate;
        animationTimer = new Timer(delay, e -> viewport.repaint());
        animationTimer.start();
        return this;
    }

    public Viewport stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
        return this;
    }

    public Viewport setFrameRate(int fps) {
        if (animationTimer != null) {
            animationTimer.setDelay(1000 / fps);
        }
        return this;
    }

    // Manual refresh
    public Viewport refresh() {
        viewport.repaint();
        return this;
    }

    public Viewport refreshAsync() {
        SwingUtilities.invokeLater(() -> viewport.repaint());
        return this;
    }

    // Coordinate system helpers
    public Point screenToViewport(Point screenPoint) {
        Point viewportLocation = viewport.getLocationOnScreen();
        return new Point(
            screenPoint.x - viewportLocation.x,
            screenPoint.y - viewportLocation.y
        );
    }

    public Point viewportToScreen(Point viewportPoint) {
        Point viewportLocation = viewport.getLocationOnScreen();
        return new Point(
            viewportPoint.x + viewportLocation.x,
            viewportPoint.y + viewportLocation.y
        );
    }

    // Viewport properties
    public Dimension getViewportSize() {
        return viewport.getSize();
    }

    public int getViewportWidth() {
        return viewport.getWidth();
    }

    public int getViewportHeight() {
        return viewport.getHeight();
    }

    public Rectangle getViewportBounds() {
        return viewport.getBounds();
    }

    // Focus and interaction
    public Viewport requestFocus() {
        viewport.requestFocusInWindow();
        return this;
    }

    public boolean hasFocus() {
        return viewport.hasFocus();
    }

    public Viewport focusable(boolean focusable) {
        viewport.setFocusable(focusable);
        return this;
    }

    // HUD overlay support
    public Viewport addOverlay(Component<?> overlay, HUDPosition position) {
        return addOverlay(overlay, position, 10, 10);
    }

    public Viewport addOverlay(Component<?> overlay, HUDPosition position, int offsetX, int offsetY) {
        viewport.setLayout(null); // Use absolute positioning
        
        JComponent overlayComponent = overlay.getSwingComponent();
        Dimension overlaySize = overlayComponent.getPreferredSize();
        
        Point location = calculateOverlayPosition(position, overlaySize, offsetX, offsetY);
        overlayComponent.setBounds(location.x, location.y, overlaySize.width, overlaySize.height);
        
        viewport.add(overlayComponent);
        viewport.setComponentZOrder(overlayComponent, 0); // Bring to front
        
        return this;
    }

    private Point calculateOverlayPosition(HUDPosition position, Dimension overlaySize, int offsetX, int offsetY) {
        int viewportWidth = getViewportWidth();
        int viewportHeight = getViewportHeight();
        
        switch (position) {
            case TOP_LEFT:
                return new Point(offsetX, offsetY);
            case TOP_CENTER:
                return new Point((viewportWidth - overlaySize.width) / 2, offsetY);
            case TOP_RIGHT:
                return new Point(viewportWidth - overlaySize.width - offsetX, offsetY);
            case CENTER_LEFT:
                return new Point(offsetX, (viewportHeight - overlaySize.height) / 2);
            case CENTER:
                return new Point((viewportWidth - overlaySize.width) / 2, (viewportHeight - overlaySize.height) / 2);
            case CENTER_RIGHT:
                return new Point(viewportWidth - overlaySize.width - offsetX, (viewportHeight - overlaySize.height) / 2);
            case BOTTOM_LEFT:
                return new Point(offsetX, viewportHeight - overlaySize.height - offsetY);
            case BOTTOM_CENTER:
                return new Point((viewportWidth - overlaySize.width) / 2, viewportHeight - overlaySize.height - offsetY);
            case BOTTOM_RIGHT:
                return new Point(viewportWidth - overlaySize.width - offsetX, viewportHeight - overlaySize.height - offsetY);
            default:
                return new Point(offsetX, offsetY);
        }
    }

    public Viewport removeAllOverlays() {
        viewport.removeAll();
        viewport.repaint();
        return this;
    }

    // Screenshot and export
    public BufferedImage screenshot() {
        BufferedImage image = new BufferedImage(
            getViewportWidth(), 
            getViewportHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        
        Graphics2D g2d = image.createGraphics();
        viewport.print(g2d);
        g2d.dispose();
        
        return image;
    }

    public Viewport saveScreenshot(String filename) {
        try {
            BufferedImage screenshot = screenshot();
            javax.imageio.ImageIO.write(screenshot, "PNG", new java.io.File(filename));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save screenshot: " + e.getMessage(), e);
        }
        return this;
    }

    @Override
    public Viewport build() {
        return this;
    }

    // HUD position enumeration
    public enum HUDPosition {
        TOP_LEFT,
        TOP_CENTER, 
        TOP_RIGHT,
        CENTER_LEFT,
        CENTER,
        CENTER_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    // Utility classes for HUD elements
    public static class HUDElement {
        private final Component<?> component;
        private final HUDPosition position;
        private final Point offset;
        private boolean visible = true;
        private float opacity = 1.0f;

        public HUDElement(Component<?> component, HUDPosition position) {
            this(component, position, new Point(10, 10));
        }

        public HUDElement(Component<?> component, HUDPosition position, Point offset) {
            this.component = component;
            this.position = position;
            this.offset = offset;
        }

        // Getters and setters
        public Component<?> getComponent() { return component; }
        public HUDPosition getPosition() { return position; }
        public Point getOffset() { return offset; }
        public boolean isVisible() { return visible; }
        public HUDElement setVisible(boolean visible) { this.visible = visible; return this; }
        public float getOpacity() { return opacity; }
        public HUDElement setOpacity(float opacity) { this.opacity = Math.max(0, Math.min(1, opacity)); return this; }
    }
}