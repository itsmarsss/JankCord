package jankcord.components.frame;

import jankcord.components.frame.draggable.JankDraggable;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class JankFrame extends JFrame implements JankDraggable {
    // Frame dragging
    private int posX = 0, posY = 0;
    private boolean drag = false;

    public JankFrame() {

    }

    @Override
    public void mousePress(MouseEvent e) {

    }

    @Override
    public void mouseDrag(MouseEvent e) {

    }
}
