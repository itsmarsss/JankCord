package jankcord.components.frame.draggable;

import java.awt.event.MouseEvent;

// Jank Draggable interface that can be used
public interface JankDraggable {
    // Mouse pressed event
    void mousePress(MouseEvent e);

    // Mouse dragged event
    void mouseDrag(MouseEvent e);
}
