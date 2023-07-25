package jankcord.components.scrollpane;

import jankcord.components.scrollbar.JankScrollBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;

// JankCord's scroll panes, child of JScrollPane
public class JankScrollPane extends JScrollPane {
    // Instance variables for JankScrollPane
    // Used for smooth scrolling
    private int destinationY;
    private Timer smoothScrollTimer;
    private int multiplier;
    private int counter;
    private int prevMax;

    // Used for smooth scrolling to bottom
    private Timer scrollBottomTimer;
    private int totalSteps = 25; // Total steps to reach the bottom in 0.5 seconds

    // Constructor, sets scroll pane size and location and child component
    public JankScrollPane(int width, int height, int x, int y, JComponent child) {
        // Super; set child
        super(child);

        // Set JScrollPane properties
        setOpaque(true);
        setBorder(null);
        setLocation(x, y);
        setSize(width, height);
        getVerticalScrollBar().setUnitIncrement(0);
        getHorizontalScrollBar().setUnitIncrement(15);
        setBackground(new Color(54, 57, 63));
        getVerticalScrollBar().setBackground(getBackground());
        getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        getVerticalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(26, 27, 30), true));
        getHorizontalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(26, 27, 30), false));

        // Assign smooth scrolling variables
        multiplier = 175;
        prevMax = -1;

        // Assign timer with 10 ms delay
        smoothScrollTimer = new Timer(10, (e) -> {
            // Check if counter has reached max, prevent infinite looping
            if (counter >= 40) {
                // Reset counter
                counter = 0;
                // Stop timer
                smoothScrollTimer.stop();
            }

            // Increment timer
            counter++;

            // Make necessary calculations; current y scroll, needed y scroll, current step value
            int currentY = getViewport().getViewPosition().y;
            int diffY = destinationY - currentY;
            int step = Math.max(5, Math.abs(diffY) / 10);

            // Check if destination arrived
            if (Math.abs(diffY) <= step) {
                // Set current scroll to destination
                getVerticalScrollBar().setValue(destinationY);
                // Reset counter
                counter = 0;
                // Stop timer
                smoothScrollTimer.stop();
            } else { // Otherwise
                // Calculate new y value
                int newY = currentY + (diffY > 0 ? step : -step);
                // Set current scroll to new y value
                getVerticalScrollBar().setValue(newY);
                // Keep timer running
            }
        });

        // Smooth scroll on mouse wheel events
        addMouseWheelListener(this::smoothScroll);

        // Scroll to bottom time
        double scrollTimeInSeconds = 0.5;

        // Assign scroll to bottom timer, with required interval time
        scrollBottomTimer = new Timer((int) (scrollTimeInSeconds * 1000 / totalSteps), new ActionListener() {
            // Relevant instance variables; current step number, start scroll value
            private int currentStep = 0;
            private int startValue;

            // Implement code when action on scroll bar is performed
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the vertical scroll bar and maximum of it
                JScrollBar verticalScrollBar = getVerticalScrollBar();
                int maximumValue = verticalScrollBar.getMaximum();

                // If step value 0
                if (currentStep == 0) {
                    // Get start value; current scroll value
                    startValue = verticalScrollBar.getValue();
                }

                // Calculate next target value given step number
                int targetValue = Math.min(startValue + (maximumValue - startValue) * currentStep / totalSteps, maximumValue);
                // Scroll scrollbar to this value
                verticalScrollBar.setValue(targetValue);

                // If end of steps reached
                if (currentStep++ > totalSteps) {
                    // Reset step count
                    currentStep = 0;
                    // Stop timer
                    scrollBottomTimer.stop();
                }
            }
        });
    }

    /**
     * Smooth scroll to bottom method that calls to start scrollBottomTimer
     */
    public void smoothScrollBottom() {
        scrollBottomTimer.start();
    }

    /**
     * Smooth scroll method that calls to start smoothScrollTimer
     *
     * @param e MouseWheelEvent, used to get how much the scroll wheel has scrolled
     */
    public void smoothScroll(MouseWheelEvent e) {
        // Get what rotation
        int rotation = e.getWheelRotation();
        // Get current scroll
        int currentY = getViewport().getViewPosition().y;
        // Get maximum scroll to prevent glitching
        int maxScrollY = getVerticalScrollBar().getMaximum();

        // If previous max is -1, unset
        if (prevMax == -1) {
            // Set it to max scroll
            prevMax = maxScrollY;
        } else if (Math.abs(maxScrollY - prevMax) > 100) { // Check if scroll value has changed too much
            // If so, reset scroll value
            maxScrollY = prevMax;
        }

        // Calculate destination
        destinationY = Math.max(0, Math.min(currentY + (rotation * multiplier * 3), maxScrollY));
        // Start smooth scrolling
        smoothScrollTimer.start();
    }

    // Getters and setters
    public int getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }

    public Timer getSmoothScrollTimer() {
        return smoothScrollTimer;
    }

    public void setSmoothScrollTimer(Timer smoothScrollTimer) {
        this.smoothScrollTimer = smoothScrollTimer;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getPrevMax() {
        return prevMax;
    }

    public void setPrevMax(int prevMax) {
        this.prevMax = prevMax;
    }

    public Timer getScrollBottomTimer() {
        return scrollBottomTimer;
    }

    public void setScrollBottomTimer(Timer scrollBottomTimer) {
        this.scrollBottomTimer = scrollBottomTimer;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }
}
