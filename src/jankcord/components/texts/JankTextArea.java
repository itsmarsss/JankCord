package jankcord.components.texts;

import jankcord.texthelpers.DeletePrevCharAction;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;

public class JankTextArea extends JTextArea {
    public JankTextArea(int width, int height, int x, int  y) {
        setSize(width, height);
        setLocation(x, y);
        initTextArea();
    }
    public JankTextArea() {
        initTextArea();
    }

    private void initTextArea() {
        setBorder(null);
        setCaretColor(Color.white);
        setBackground(new Color(56, 58, 64));
        setForeground(new Color(219, 222, 225));
        setSelectionColor(new Color(9, 103, 215));
        setSelectedTextColor(new Color(255, 255, 255));
        setFont(new Font("Whitney", Font.BOLD, 28));
        getActionMap().put(DefaultEditorKit.deletePrevCharAction, new DeletePrevCharAction());
    }
}
