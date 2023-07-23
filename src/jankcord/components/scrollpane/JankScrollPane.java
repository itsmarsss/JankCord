package jankcord.components.scrollpane;

import jankcord.components.scrollbar.JankScrollBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;

public class JankScrollPane extends JScrollPane {

    private int destinationY;
    private Timer timer;
    private int multiplier;
    private int counter;
    private int prevMax;

    public JankScrollPane(int width, int height, int x, int y, JComponent child) {
        super(child);

        setOpaque(true);
        setBorder(null);
        setLocation(x, y);
        setSize(width, height);
        setBackground(new Color(54, 57, 63));
        getVerticalScrollBar().setUnitIncrement(0);
        getHorizontalScrollBar().setUnitIncrement(15);
        getVerticalScrollBar().setBackground(getBackground());
        getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        getVerticalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(32, 34, 37), true));
        getHorizontalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(32, 34, 37), false));

        multiplier = 175;
        prevMax = -1;

        timer = new Timer(10, e -> {
            if (counter >= 40) {
                counter = 0;
                timer.stop();
            }

            counter++;

            int currentY = getViewport().getViewPosition().y;
            int diffY = destinationY - currentY;
            int step = Math.max(5, Math.abs(diffY) / 10);

            // System.out.println(destinationY + " " + Math.abs(diffY) + " " + step);
            if (Math.abs(diffY) <= step) {
                getVerticalScrollBar().setValue(destinationY);
                counter = 0;
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
            // System.out.println(maxScrollY);
            destinationY = Math.max(0, Math.min(currentY + (rotation * multiplier * 3), maxScrollY));
            timer.start();
        });

        double scrollTimeInSeconds = 0.5;
        scrollTimer = new Timer((int) (scrollTimeInSeconds * 1000 / totalSteps), new ActionListener() {
            private int currentStep = 0;
            private int startValue;

            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println(currentStep + "-" + totalSteps);

                JScrollBar verticalScrollBar = getVerticalScrollBar();
                int maximumValue = verticalScrollBar.getMaximum();

                if (currentStep == 0) {
                    startValue = verticalScrollBar.getValue();
                }

                int targetValue = Math.min(startValue + (maximumValue - startValue) * currentStep / totalSteps, maximumValue);
                verticalScrollBar.setValue(targetValue);

                if (currentStep++ > totalSteps) {
                    currentStep = 0;
                    scrollTimer.stop();
                }
            }
        });
    }

    private final Timer scrollTimer;
    private final int totalSteps = 25; // Total steps to reach the bottom in 0.5 seconds

    public void smoothScrollBottom() {
        scrollTimer.start();
    }

    public void artificialScroll(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        int currentY = getViewport().getViewPosition().y;
        int maxScrollY = getVerticalScrollBar().getMaximum();

        // System.out.println(maxScrollY - prevMax);
        if (prevMax == -1) {
            prevMax = maxScrollY;
        } else if (Math.abs(maxScrollY - prevMax) > 100) {
            // System.out.println(maxScrollY - prevMax);
            maxScrollY = prevMax;
        }

        destinationY = Math.max(0, Math.min(currentY + (rotation * multiplier * 3), maxScrollY));
        timer.start();
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
