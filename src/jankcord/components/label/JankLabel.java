package jankcord.components.label;

import javax.swing.*;
import java.awt.*;

// JankCord's label, child of JLabel
public class JankLabel extends JLabel {
    // Constructor to set text
    public JankLabel(String text) {
        // Super; set text
        super(text);

        // Set Jlabel properties
        setSize(250, 30);
        setLocation(18, 10);
        setForeground(new Color(114, 118, 125));
        setFont(new Font("Whitney", Font.BOLD, 28));
    }
}
