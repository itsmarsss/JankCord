package jankcord.components.button.buttonlistener;

import java.awt.event.MouseEvent;

// JankCord's Mouse Listener Runnable item, modified Runnable
public class JankMLRunnable implements Runnable {

    // Protected MouseEvent so implementations can use it without declaring it
    protected MouseEvent e;

    // Empty constructor
    public JankMLRunnable() {
    }

    // Same as @Override public void run(), except it sets MouseEvent e first
    public void runEvent(MouseEvent e) {
        // Assign mouse event
        this.e = e;
        // Run runnable
        run();
    }

    // For child class to override
    @Override
    public void run() {
    }
}
