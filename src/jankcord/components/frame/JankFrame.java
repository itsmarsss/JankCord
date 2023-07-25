package jankcord.components.frame;

import jankcord.components.frame.draggable.JankDraggable;
import jankcord.tools.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JankFrame extends JFrame implements JankDraggable {
    // Frame dragging fields
    private int posX = 0, posY = 0;
    private boolean drag = false;

    // Constructor to set name location and size
    public JankFrame(String name, int width, int height, int x, int y) {
        // Super; set JFrame name
        super(name);

        // Load icons
        setIconImages(ResourceLoader.loader.getIcons());

        // Set JFrame properties
        setResizable(false);
        setUndecorated(true);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(32, 34, 37));
        setSize(width, height);
        setLocation(x, y);

        // Add mouse press listener
        getContentPane().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Call method mousePress
                mousePress(e);
            }
        });

        // Add mouse dragged listener
        getContentPane().addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                // Call method mouseDrag
                mouseDrag(e);
            }
        });
    }

    // mousePress method, children classes can override
    @Override
    public void mousePress(MouseEvent e) {
        drag = true;
        posX = e.getX();
        posY = e.getY();
    }

    // mouseDrag method, children classes can override
    @Override
    public void mouseDrag(MouseEvent e) {
        if (drag) {
            setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
        }
    }
}
