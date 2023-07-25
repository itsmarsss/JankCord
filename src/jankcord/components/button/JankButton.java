package jankcord.components.button;

import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.button.buttonlistener.JankMouseListener;

import javax.swing.*;
import java.awt.*;

// JankCord's Button ui presets, child of JButton
public class JankButton extends JButton {
    // Add a JankMouseListener listener
    private JankMouseListener jml;

    // Constructor to set text, size, and location
    public JankButton(String text, int width, int height, int x, int y) {
        // Super; set text
        super(text);

        // Initialize mouse listener
        jml = new JankMouseListener();

        // Set JButton properties
        setBorder(null);
        setLocation(x, y);
        setSize(width, height);
        setBackground(new Color(78, 80, 88));
        setForeground(new Color(219, 222, 225));
        setFont(new Font("Whitney", Font.BOLD, 28));

        // Initialize mouse events
        jml.setMouseClicked(new JankMLRunnable() {
            @Override
            public void run() {
            }
        });
        // Color change on press
        jml.setMousePressed(new JankMLRunnable() {
            @Override
            public void run() {
                e.getComponent().setBackground(new Color(128, 132, 142));
            }
        });
        jml.setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
            }
        });
        // Color change on hover and unhover
        jml.setMouseEntered(new JankMLRunnable() {
            @Override
            public void run() {
                e.getComponent().setBackground(new Color(109, 111, 120));
            }
        });
        jml.setMouseExited(new JankMLRunnable() {
            @Override
            public void run() {
                e.getComponent().setBackground(new Color(78, 80, 88));
            }
        });

        addMouseListener(jml);
    }

    // Getters and setters
    public JankMouseListener getMouseListener() {
        return jml;
    }

    public void setMouseListener(JankMouseListener jml) {
        this.jml = jml;
    }
}
