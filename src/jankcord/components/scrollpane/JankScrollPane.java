package jankcord.components.scrollpane;

import jankcord.components.scrollbar.JankScrollBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private Timer scrollTimer;
    private final double scrollTimeInSeconds = 0.5;
    private final int totalSteps = 50; // Total steps to reach the bottom in 0.5 seconds

    public void smoothScrollBottom() {
        scrollTimer = new Timer((int) (scrollTimeInSeconds * 1000 / totalSteps), new ActionListener() {
            private int currentStep = 0;
            private int startValue;

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("down");

                JScrollBar verticalScrollBar = getVerticalScrollBar();
                int maximumValue = verticalScrollBar.getMaximum();

                if (currentStep == 0) {
                    startValue = verticalScrollBar.getValue();
                }

                int targetValue = Math.min(startValue + (maximumValue - startValue) * currentStep / totalSteps, maximumValue);
                verticalScrollBar.setValue(targetValue);

                if (currentStep++ > totalSteps - 40) {
                    scrollTimer.stop();
                }
            }
        });

        scrollTimer.start();

    }

    public int getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
