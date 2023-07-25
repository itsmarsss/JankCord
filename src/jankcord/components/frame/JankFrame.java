package jankcord.components.frame;

import jankcord.components.frame.draggable.JankDraggable;
import jankcord.components.label.JankTitleLabel;
import jankcord.components.windowbuttons.JankCloseButton;
import jankcord.components.windowbuttons.WindowButtons;
import jankcord.tools.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JankFrame extends JFrame implements JankDraggable {
    // Instance fields
    private JankTitleLabel titleLabel;
    private WindowButtons windowButtons;

    // Frame dragging fields
    public int posX = 0, posY = 0;
    public boolean drag = false;

    // Constructor for JankFrame, requires name, size, and window controls state
    public JankFrame(String name, int width, int height, boolean fullWindowControls) {
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

        // Calculate center of screen
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) screenDim.getWidth() / 2 - getWidth() / 2, (int) screenDim.getHeight() / 2 - getHeight() / 2);

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

        // Add window topbar logo
        titleLabel = new JankTitleLabel(name);
        getContentPane().add(titleLabel);

        // Check windows control
        if (fullWindowControls) {
            windowButtons = new WindowButtons();
            getContentPane().add(windowButtons);
        } else {
            // Add window close button only
            getContentPane().add(new JankCloseButton(getWidth(), this));
        }
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

    // Getters and setters
    public JankTitleLabel getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(JankTitleLabel titleLabel) {
        this.titleLabel = titleLabel;
    }

    public WindowButtons getWindowButtons() {
        return windowButtons;
    }

    public void setWindowButtons(WindowButtons windowButtons) {
        this.windowButtons = windowButtons;
    }
}
