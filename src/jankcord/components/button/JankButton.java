package jankcord.components.button;

import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.button.buttonlistener.JankMouseListener;

import javax.swing.*;
import java.awt.*;

public class JankButton extends JButton {

    private JankMouseListener jml;

    public JankButton(String text, int width, int height, int x, int y) {
        super(text);

        jml = new JankMouseListener();

        setSize(width, height);
        setLocation(x, y);
        setBackground(new Color(78, 80, 88));
        setForeground(new Color(219, 222, 225));
        setFont(new Font("Whitney", Font.BOLD, 28));
        setBorder(null);

        jml.setMouseClicked(new JankMLRunnable() {
            @Override
            public void run() {
            }
        });
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

    public JankMouseListener getMouseListener() {
        return jml;
    }

    public void setMouseListener(JankMouseListener jml) {
        this.jml = jml;
    }
}
