package jankcord.components.windowbuttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JankCloseButton extends JLabel {

    private JFrame parentFrame;

    public JankCloseButton(int containerWidth, JFrame parentFrame) {
        super("✕", SwingConstants.CENTER);
        this.parentFrame = parentFrame;

        initCloseButton(containerWidth);
    }

    private void initCloseButton(int containerWidth) {
        setOpaque(true);
        setSize(62, 50);
        setFont(new Font(null, Font.PLAIN, 28));
        setBackground(new Color(32, 34, 37));
        setForeground(new Color(159, 161, 165));
        setLocation(containerWidth - getWidth(), 0);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                e.getComponent().setBackground(new Color(237, 66, 69));
                e.getComponent().setForeground(new Color(255, 255, 255));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // System.out.println();
                e.getComponent().setBackground(new Color(237, 66, 69));
                e.getComponent().setForeground(new Color(255, 255, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setBackground(new Color(32, 34, 37));
                e.getComponent().setForeground(new Color(185, 187, 190));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(parentFrame == null) {
                    System.exit(0);
                }
                parentFrame.dispose();
            }
        });
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParent(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }
}
