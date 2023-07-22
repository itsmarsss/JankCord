package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.objects.FullUser;
import jankcord.tools.ResourceLoader;
import jankcord.components.scrollbar.JankScrollBar;
import jankcord.texthelpers.DeletePrevCharAction;
import jankcord.tools.ServerCommunicator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

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
        setSize(550, 750);
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
                dispose();
            }
        });
        closeButton.setBackground(new Color(32, 34, 37));
        closeButton.setForeground(new Color(159, 161, 165));

        getContentPane().add(closeButton);


        JLabel reasonLabel = new JLabel("Reason:");
        reasonLabel.setSize(200, 30);
        reasonLabel.setLocation(100, 100);
        reasonLabel.setForeground(new Color(114, 118, 125));
        reasonLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(reasonLabel);


        JTextArea reasonTextArea = new JTextArea();
        reasonTextArea.setSize(350, 450);
        reasonTextArea.setLocation(100, 150);
        reasonTextArea.setBackground(new Color(56, 58, 64));
        reasonTextArea.setForeground(new Color(219, 222, 225));
        reasonTextArea.setCaretColor(Color.white);
        reasonTextArea.setFont(new Font("Whitney", Font.BOLD, 28));
        reasonTextArea.setBorder(null);
        reasonTextArea.getActionMap().put(DefaultEditorKit.deletePrevCharAction, new DeletePrevCharAction());

        JScrollPane reasonTextAreaCont = new JScrollPane(reasonTextArea);

        reasonTextAreaCont.setOpaque(true);
        reasonTextAreaCont.setBorder(null);
        reasonTextAreaCont.setLocation(100, 150);
        reasonTextAreaCont.setSize(350, 450);
        reasonTextAreaCont.setBackground(new Color(43, 45, 49));
        reasonTextAreaCont.getVerticalScrollBar().setUnitIncrement(15);
        reasonTextAreaCont.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        reasonTextAreaCont.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        reasonTextAreaCont.getVerticalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(12, 14, 17), false));
        getContentPane().add(reasonTextAreaCont);

        JankButton submitButton = new JankButton("Submit", 350, 50, 100, 600);
        submitButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                HashMap<String, String> headers = new HashMap<>();

                String username = Jankcord.getFullUser().getUsername();
                String password = Jankcord.getFullUser().getPassword();
                String server = Jankcord.getFullUser().getEndPointHost();

                headers.put("username", username);
                headers.put("password", password);
                headers.put("reason", reasonTextArea.getText());

                ServerCommunicator.sendHttpRequest(server + "/requestgroupchat", headers);
            }
        });

        getContentPane().add(submitButton);
    }
}
