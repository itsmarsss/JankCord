package jankcord.components;

import jankcord.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class RequestGroupChat extends JFrame {

    // Frame dragging
    private int posX = 0, posY = 0;
    private boolean drag = false;


    public RequestGroupChat() {
        super("JankCord Request Group Chat");

        ArrayList<Image> icons = new ArrayList<>();
        icons.add(ResourceLoader.loader.getIcon1().getImage());
        icons.add(ResourceLoader.loader.getIcon2().getImage());
        icons.add(ResourceLoader.loader.getIcon3().getImage());
        icons.add(ResourceLoader.loader.getIcon4().getImage());

        setIconImages(icons);

        // Frame Init
        setUndecorated(true);
        getContentPane().setLayout(null);
        setResizable(true);
        getContentPane().setBackground(new Color(32, 34, 37));
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(500, 750);
        setLocation((int) screenDim.getWidth() / 2 - getWidth() / 2, (int) screenDim.getHeight() / 2 - getHeight() / 2);

        getContentPane().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                drag = true;
                posX = e.getX();
                posY = e.getY();
            }
        });
        getContentPane().addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (drag) {
                    setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
                }
            }
        });


        Font windowButtonFont = new Font(null, Font.PLAIN, 28);
        JLabel closeButton = new JLabel("✕", SwingConstants.CENTER);

        closeButton.setOpaque(true);
        closeButton.setSize(62, 50);
        closeButton.setFont(windowButtonFont);
        closeButton.setLocation(getWidth() - closeButton.getWidth(), 0);
        closeButton.addMouseListener(new MouseListener() {
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
                System.exit(0);
            }
        });
        closeButton.setBackground(new Color(32, 34, 37));
        closeButton.setForeground(new Color(159, 161, 165));

        getContentPane().add(closeButton);
    }
}
