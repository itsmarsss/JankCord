package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.JankList.JankList;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.label.JankLabel;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.components.windowbuttons.JankCloseButton;
import jankcord.objects.User;
import jankcord.tools.JankDraggable;
import jankcord.tools.ResourceLoader;
import jankcord.components.scrollbar.JankScrollBar;
import jankcord.tools.ServerCommunicator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

public class JankGroupChat extends JFrame implements JankDraggable {
    public JankGroupChat() {
        super("JankCord New Group Chat");

        setIconImages(ResourceLoader.loader.getIcons());

        // Frame Init
        setResizable(false);
        setUndecorated(true);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(32, 34, 37));
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(700, 800);
        setLocation((int) screenDim.getWidth() / 2 - getWidth() / 2, (int) screenDim.getHeight() / 2 - getHeight() / 2);

        getContentPane().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePress(e);
            }
        });
        getContentPane().addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                mouseDrag(e);
            }
        });


        JankLabel logoLabel = new JankLabel("New Group Chat");

        getContentPane().add(logoLabel);

        JankCloseButton closeButton = new JankCloseButton(getWidth(), this);

        getContentPane().add(closeButton);


        JLabel createLabel = new JLabel("Create New Group Chat:");
        createLabel.setSize(500, 30);
        createLabel.setLocation(100, 100);
        createLabel.setForeground(new Color(114, 118, 125));
        createLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(createLabel);


        JLabel numOfUsersLabel = new JLabel("You can add 20 more friends.");

        numOfUsersLabel.setSize(500, 30);
        numOfUsersLabel.setLocation(100, 610);
        numOfUsersLabel.setForeground(new Color(219, 222, 225));
        numOfUsersLabel.setFont(new Font("Whitney", Font.BOLD, 25));
        getContentPane().add(numOfUsersLabel);


        DefaultListModel<User> users = new DefaultListModel<>();

        for (User friend : Jankcord.getTempFriends()) {
            if (friend.getId() != Jankcord.getFullUser().getId()) {
                users.addElement(friend);
            }
        }

        JankList userList = new JankList(users, numOfUsersLabel, "You can add %s more friends.");

        JankScrollPane userListScroll = new JankScrollPane(480, 450, 100, 150, userList);
        userListScroll.setMultiplier(25);
        userListScroll.getVerticalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(0, 0, 0), true));

        getContentPane().add(userListScroll);


        JankButton submitButton = new JankButton("Create Group Chat", 500, 50, 100, 650);
        submitButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                HashMap<String, String> headers = new HashMap<>();

                String username = Jankcord.getFullUser().getUsername();
                String password = Jankcord.getFullUser().getPassword();
                String server = Jankcord.getFullUser().getEndPointHost();

                List<User> usersResult = userList.getSelectedValuesList();

                String users = Jankcord.getFullUser().getId() + ",";

                for (User user : usersResult) {
                    users += user.getId() + ",";
                }

                headers.put("username", username);
                headers.put("password", password);
                headers.put("users", users);

                String response = ServerCommunicator.sendHttpRequest(server + "creategroupchat", headers);

                //System.out.println(response);

                dispose();
            }
        });

        getContentPane().add(submitButton);
    }


    // Frame dragging
    private int posX = 0, posY = 0;
    private boolean drag = false;
    @Override
    public void mousePress(MouseEvent e) {
        drag = true;
        posX = e.getX();
        posY = e.getY();
    }

    @Override
    public void mouseDrag(MouseEvent e) {
        if (drag) {
            setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
        }
    }
}
