package jankcord.components.label;

import javax.swing.*;
import java.awt.*;

public class JankLabel extends Label {
    public JankLabel(String text) {
        super(text);

        setSize(250, 30);
        setLocation(18, 10);
        setForeground(new Color(114, 118, 125));
        setFont(new Font("Whitney", Font.BOLD, 28));
    }
}
