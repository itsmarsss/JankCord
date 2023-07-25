package jankcord.components.texts;

import jankcord.texthelpers.DeletePrevCharAction;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;

// JankCord's text area, child of JTextArea
public class JankTextArea extends JTextArea {
    // Constructor to set size and location
    public JankTextArea(int width, int height, int x, int y) {
        // Set JTextArea properties
        setSize(width, height);
        setLocation(x, y);

        // Set rest of properties
        initTextArea();
    }

    // Alternate constructor for no size nor location yet
    public JankTextArea() {
        initTextArea();
    }

    /**
     * Initializes all JTextArea properties
     */
    private void initTextArea() {
        // Set JTextArea properties
        setBorder(null);
        setCaretColor(Color.white);
        setBackground(new Color(56, 58, 64));
        setForeground(new Color(219, 222, 225));
        setSelectionColor(new Color(9, 103, 215));
        setSelectedTextColor(new Color(255, 255, 255));
        setFont(new Font("Whitney", Font.BOLD, 28));

        // Add deletePrevCharAction to action map
        getActionMap().put(DefaultEditorKit.deletePrevCharAction, new DeletePrevCharAction());
    }
}
