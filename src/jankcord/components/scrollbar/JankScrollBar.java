package jankcord.components.scrollbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

// JankCord's scrollbar, child of BasicScrollBarUI
public class JankScrollBar extends BasicScrollBarUI {
    // Dimension for this scrollbar
    private Dimension dim;

    // Track and thumb color
    private Color trackColor;
    private Color thumbColor;

    // Rounded thumb corners or not
    private boolean round;

    // Constructor to ser track and thumb color and rounded or not
    public JankScrollBar(Color trackColor, Color thumbColor, boolean round) {
        // Assign dim to nothing
        dim = new Dimension();

        // Assign fields
        this.trackColor = trackColor;
        this.thumbColor = thumbColor;
        this.round = round;
    }

    // Override decrease and increase button, don't want them
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                // When preferred size is queried, (0, 0) is returned
                return dim;
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        // When preferred size is queried, (0, 0) is returned
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dim;
            }
        };
    }

    // Override track painting
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        // Graphics2D setup
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(trackColor);

        // Paint depending on round or not
        if (round) {
            // Paint rounded rectangles
            g2.fillRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
            g2.drawRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
        } else {
            // Paint rectangle
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.drawRect(r.x, r.y, r.width, r.height);
        }

        // Dispose of painter
        g2.dispose();
    }

    // Override thumb painting
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        // Graphics2D setup
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(thumbColor);

        // Paint depending on round or not
        if (round) {
            // Paint rounded rectangles
            g2.fillRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
            g2.drawRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
        } else {
            // Paint rectangles
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.drawRect(r.x, r.y, r.width, r.height);
        }

        // Dispose of painter
        g2.dispose();
    }

    // Override thumb bounds painter
    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        // Super; set thumb bounds
        super.setThumbBounds(x, y, width, height);
        // Just repaint the whole scrollbar
        scrollbar.repaint();
    }

    // Getters and setters
    public Color getTrackColor() {
        return trackColor;
    }

    public void setTrackColor(Color trackColor) {
        this.trackColor = trackColor;
    }

    public Color getThumbColor() {
        return thumbColor;
    }

    public void setThumbColor(Color thumbColor) {
        this.thumbColor = thumbColor;
    }

    public boolean getRound() {
        return round;
    }

    public void setRound(boolean round) {
        this.round = round;
    }

    public void setDim(Dimension dim) {
        this.dim = dim;
    }

    public Dimension getDim() {
        return dim;
    }
}