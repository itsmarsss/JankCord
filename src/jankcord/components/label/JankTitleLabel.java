package jankcord.components.label;

import javax.swing.*;
import java.awt.*;

// JankCord's label, child of JLabel
public class JankTitleLabel extends JLabel {
    // Constructor to set text
    public JankTitleLabel(String text) {
        // Super; set text
        super(text);

        // Set Jlabel properties
        setSize(400, 30);
        setLocation(18, 10);
        setForeground(new Color(114, 118, 125));
        setFont(new Font("Whitney", Font.BOLD, 28));
    }
}
