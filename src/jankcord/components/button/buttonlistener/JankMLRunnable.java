package jankcord.components.button.buttonlistener;

import java.awt.event.MouseEvent;

public class JankMLRunnable implements Runnable {

    protected MouseEvent e;

    public JankMLRunnable() {
    }

    public void runEvent(MouseEvent e) {
        this.e = e;
        run();
    }

    @Override
    public void run() {}
}
