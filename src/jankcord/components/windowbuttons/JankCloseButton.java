package jankcord.components.windowbuttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// JankCord's close button, child of JLabel
public class JankCloseButton extends JLabel {
    // Parent frame to dispose
    private JFrame parentFrame;

    // Constructor, requires the container width and the parent
    public JankCloseButton(int containerWidth, JFrame parentFrame) {
        // Super; set X button and center it
        super("✕", SwingConstants.CENTER);

        // Set the parent frame
        this.parentFrame = parentFrame;

        // Set JLabel properties
        initCloseButton(containerWidth);
    }

    /**
     * Set JLabel properties
     *
     * @param containerWidth container width so the x is placed properly
     */
    private void initCloseButton(int containerWidth) {
        // Set JLabel properties
        setOpaque(true);
        setSize(62, 50);
        setBackground(new Color(32, 34, 37));
        setForeground(new Color(159, 161, 165));
        setLocation(containerWidth - getWidth(), 0);
        setFont(new Font(null, Font.PLAIN, 28));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener to set colors or perform events
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            // Mouse pressed
            @Override
            public void mousePressed(MouseEvent e) {
                // Update colors
                setBackground(new Color(237, 66, 69));
                setForeground(new Color(255, 255, 255));
            }

            // Mouse entered
            @Override
            public void mouseEntered(MouseEvent e) {
                // Update colors
                setBackground(new Color(237, 66, 69));
                setForeground(new Color(255, 255, 255));
            }

            // Mouse exited
            @Override
            public void mouseExited(MouseEvent e) {
                // Update colors
                setBackground(new Color(32, 34, 37));
                setForeground(new Color(185, 187, 190));
            }

            // Mouse released
            @Override
            public void mouseReleased(MouseEvent e) {
                // If no parent frame
                if (parentFrame == null) {
                    // Terminate program
                    System.exit(0);
                }
                // Otherwise; dispose parent
                parentFrame.dispose();
            }
        });
    }

    // Getters and setters
    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParent(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }
}
