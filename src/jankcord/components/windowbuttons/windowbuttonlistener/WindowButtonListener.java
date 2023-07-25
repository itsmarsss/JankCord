package jankcord.components.windowbuttons.windowbuttonlistener;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jankcord.Jankcord;

// JankCord's window button listener, implements mouse listener
public class WindowButtonListener implements MouseListener {
    // All instance fields
    // What button action
    private ButtonPerform perform;

    // Hover colors
    private Color hoverBack;
    private Color hoverFore;

    // Click colors
    private Color clickBack;
    private Color clickFore;

    // Unhover colors
    private Color exitBack;
    private Color exitFore;

    // Constructor to set all values
    public WindowButtonListener(
            ButtonPerform perform,
            Color hoverBack,
            Color hoverFore,
            Color clickBack,
            Color clickFore,
            Color exitBack,
            Color exitFore) {
        // Set all fields
        this.perform = perform;

        this.hoverBack = hoverBack;
        this.hoverFore = hoverFore;

        this.clickBack = clickBack;
        this.clickFore = clickFore;

        this.exitBack = exitBack;
        this.exitFore = exitFore;
    }

    // Override all mouse action buttons
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // Mouse pressed
    @Override
    public void mousePressed(MouseEvent e) {
        // Update colors
        e.getComponent().setBackground(clickBack);
        e.getComponent().setForeground(clickFore);
    }

    // Mouse entered
    @Override
    public void mouseEntered(MouseEvent e) {
        // Update colors
        e.getComponent().setBackground(hoverBack);
        e.getComponent().setForeground(hoverFore);
    }

    // Mouse exited
    @Override
    public void mouseExited(MouseEvent e) {
        // Update colors
        e.getComponent().setBackground(exitBack);
        e.getComponent().setForeground(exitFore);
    }

    // Mouse released
    @Override
    public void mouseReleased(MouseEvent e) {
        // Check which button actions
        switch (perform) {
            case FRAME_CLOSE -> System.exit(0); // Close
            case FRAME_FULLSCREEN -> Jankcord.doFullscreen(); // Fullscreen
            case FRAME_MINIMIZE -> Jankcord.getFrame().setExtendedState(Frame.ICONIFIED); // Minimize
        }
    }

    // Getters and setters
    public ButtonPerform getButtonPerform() {
        return perform;
    }

    public void setButtonPerform(ButtonPerform perform) {
        this.perform = perform;
    }

    public Color getHoverBack() {
        return hoverBack;
    }

    public void setHoverBack(Color hoverBack) {
        this.hoverBack = hoverBack;
    }

    public Color getHoverFore() {
        return hoverFore;
    }

    public void setHoverFore(Color hoverFore) {
        this.hoverFore = hoverFore;
    }

    public Color getClickBack() {
        return clickBack;
    }

    public void setClickBack(Color clickBack) {
        this.clickBack = clickBack;
    }

    public Color getClickFore() {
        return clickFore;
    }

    public void setClickFore(Color clickFore) {
        this.clickFore = clickFore;
    }

    public Color getExitBack() {
        return exitBack;
    }

    public void setExitBack(Color exitBack) {
        this.exitBack = exitBack;
    }

    public Color getExitFore() {
        return exitFore;
    }

    public void setExitFore(Color exitFore) {
        this.exitFore = exitFore;
    }
}
