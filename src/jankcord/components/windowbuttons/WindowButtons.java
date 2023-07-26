package jankcord.components.windowbuttons;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jankcord.Jankcord;
import jankcord.components.windowbuttons.windowbuttonlistener.ButtonPerform;
import jankcord.components.windowbuttons.windowbuttonlistener.WindowButtonListener;

// JankCord's window buttons, child of JPanel
public class WindowButtons extends JPanel {
    // Constructor to create a windows button
    public WindowButtons() {
        // Set JPanel properties
        setLayout(null);
        setSize(186, 50);
        setBackground(new Color(0, 0, 0, 0));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setLocation(Jankcord.getViewPanel().getWidth() - 186, 0);

        // Button font
        Font windowButtonFont = new Font(null, Font.PLAIN, 28);

        // Use JankCloseButton as close button
        JankCloseButton closeButton = new JankCloseButton(getWidth(), null);
        // Use JLabel as fullscreen and minimize button
        JLabel fullscreenButton = new JLabel("□", SwingConstants.CENTER);
        JLabel minimizeButton = new JLabel("—", SwingConstants.CENTER);

        // Add close button to panel
        add(closeButton);


        // Set fullscreen button properties
        fullscreenButton.setOpaque(true);
        fullscreenButton.setFont(windowButtonFont);
        fullscreenButton.setSize(62, 50);
        fullscreenButton.setBackground(new Color(32, 34, 37));
        fullscreenButton.setForeground(new Color(159, 161, 165));
        fullscreenButton.setLocation(closeButton.getX() - fullscreenButton.getWidth(), 0);

        // Add fullscreen button mouse listener
        fullscreenButton.addMouseListener(new WindowButtonListener(
                ButtonPerform.FRAME_FULLSCREEN,
                new Color(40, 43, 46),
                new Color(190, 191, 193),
                new Color(43, 46, 50),
                new Color(220, 220, 221),
                new Color(32, 34, 37),
                new Color(160, 162, 164)));

        // Add fullscreen button to panel
        add(fullscreenButton);


        // Set fullscreen button properties
        minimizeButton.setOpaque(true);
        minimizeButton.setFont(windowButtonFont);
        minimizeButton.setSize(62, 50);
        minimizeButton.setBackground(new Color(32, 34, 37));
        minimizeButton.setForeground(new Color(159, 161, 165));
        minimizeButton.setLocation(fullscreenButton.getX() - minimizeButton.getWidth(), 0);

        // Add minimize button mouse listener
        minimizeButton.addMouseListener(new WindowButtonListener(
                ButtonPerform.FRAME_MINIMIZE,
                new Color(40, 43, 46),
                new Color(190, 191, 193),
                new Color(43, 46, 50),
                new Color(220, 220, 221),
                new Color(32, 34, 37),
                new Color(160, 162, 164)));

        // Add minimize button to panel
        add(minimizeButton);
    }
}
