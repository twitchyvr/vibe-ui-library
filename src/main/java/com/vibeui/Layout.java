package com.vibeui;

import java.awt.*;

public class Layout {
    
    public static FlowLayout flow() {
        return new FlowLayout();
    }
    
    public static FlowLayout flow(int align) {
        return new FlowLayout(align);
    }
    
    public static FlowLayout flowLeft() {
        return new FlowLayout(FlowLayout.LEFT);
    }
    
    public static FlowLayout flowCenter() {
        return new FlowLayout(FlowLayout.CENTER);
    }
    
    public static FlowLayout flowRight() {
        return new FlowLayout(FlowLayout.RIGHT);
    }
    
    public static BorderLayout border() {
        return new BorderLayout();
    }
    
    public static GridLayout grid(int rows, int cols) {
        return new GridLayout(rows, cols);
    }
    
    public static GridLayout grid(int rows, int cols, int hgap, int vgap) {
        return new GridLayout(rows, cols, hgap, vgap);
    }
    
    public static GridBagLayout gridBag() {
        return new GridBagLayout();
    }
    
    public static CardLayout card() {
        return new CardLayout();
    }
    
    public static class BorderConstraints {
        public static final String NORTH = BorderLayout.NORTH;
        public static final String SOUTH = BorderLayout.SOUTH;
        public static final String EAST = BorderLayout.EAST;
        public static final String WEST = BorderLayout.WEST;
        public static final String CENTER = BorderLayout.CENTER;
    }
    
    public static GridBagConstraints gridBagConstraints() {
        return new GridBagConstraints();
    }
    
    public static GridBagConstraints gridBagConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }
    
    public static GridBagConstraints gridBagConstraints(int x, int y, int width, int height) {
        GridBagConstraints gbc = gridBagConstraints(x, y);
        gbc.gridwidth = width;
        gbc.gridheight = height;
        return gbc;
    }
}