package jankcord.components.scrollpane;

import jankcord.components.scrollbar.JankScrollBar;

import javax.swing.*;
import java.awt.*;

public class JankScrollPane extends JScrollPane {
    public JankScrollPane(int width, int height, int x, int y, JComponent child) {
        super(child);

        setOpaque(true);
        setBorder(null);
        setLocation(x, y);
        setSize(width, height);
        setBackground(new Color(54, 57, 63));
        getVerticalScrollBar().setUnitIncrement(15);
        getVerticalScrollBar().setBackground(getBackground());
        getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        getVerticalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(32, 34, 37), true));
    }
}
