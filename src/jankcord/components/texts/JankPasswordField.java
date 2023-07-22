package jankcord.components.texts;

import jankcord.texthelpers.DeletePrevCharAction;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;

public class JankPasswordField extends JPasswordField {
    public JankPasswordField(int width, int height, int x, int y) {
        setSize(width, height);
        setLocation(x, y);
        setBackground(new Color(56, 58, 64));
        setForeground(new Color(219, 222, 225));
        setCaretColor(Color.white);
        setFont(new Font("Whitney", Font.BOLD, 28));
        setBorder(null);

        getActionMap().put(DefaultEditorKit.deletePrevCharAction, new DeletePrevCharAction());
    }
}
