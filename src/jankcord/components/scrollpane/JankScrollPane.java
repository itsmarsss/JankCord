package jankcord.components.scrollpane;

import jankcord.components.scrollbar.JankScrollBar;

import javax.swing.*;
import java.awt.*;

public class JankScrollPane extends JScrollPane {

    private int destinationY;
    private Timer timer;
    private int multiplier;

    public JankScrollPane(int width, int height, int x, int y, JComponent child) {
        super(child);

        setOpaque(true);
        setBorder(null);
        setLocation(x, y);
        setSize(width, height);
        setBackground(new Color(54, 57, 63));
        getVerticalScrollBar().setUnitIncrement(0);
        getVerticalScrollBar().setBackground(getBackground());
        getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        getVerticalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(32, 34, 37), true));

        multiplier = 175;

        timer = new Timer(10, e -> {
            int currentY = getViewport().getViewPosition().y;
            int diffY = destinationY - currentY;
            int step = Math.max(1, Math.abs(diffY) / 10);

            if (Math.abs(diffY) <= step) {
                getVerticalScrollBar().setValue(destinationY);
                timer.stop();
            } else {
                int newY = currentY + (diffY > 0 ? step : -step);
                getVerticalScrollBar().setValue(newY);
            }
        });

        // Smooth scroll on mouse wheel events
        addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            int currentY = getViewport().getViewPosition().y;
            int maxScrollY = getVerticalScrollBar().getMaximum();
            destinationY = Math.max(0, Math.min(currentY + (rotation * multiplier * 3), maxScrollY));
            timer.start();
        });
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
