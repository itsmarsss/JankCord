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

public class JankScrollBar extends BasicScrollBarUI {
    private final Dimension dim = new Dimension();

    private Color trackColor;
    private Color thumbColor;
    private boolean round;

    public JankScrollBar(Color trackColor, Color thumbColor, boolean round) {
        this.trackColor = trackColor;
        this.thumbColor = thumbColor;
        this.round = round;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dim;
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dim;
            }
        };
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(trackColor);
        if (round) {
            g2.fillRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
            g2.drawRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
        } else {
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.drawRect(r.x, r.y, r.width, r.height);
        }
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(thumbColor);
        if (round) {
            g2.fillRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
            g2.drawRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
        }else {
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.drawRect(r.x, r.y, r.width, r.height);
        }
        g2.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }

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
}