package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.list.JankList;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.frame.JankFrame;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.objects.User;
import jankcord.components.scrollbar.JankScrollBar;
import jankcord.tools.ServerCommunicator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

// Popup window for user to create new group chat
public class JankGroupChat extends JankFrame {
    // Constructor to create new JankGroupChat
    public JankGroupChat() {
        // Super: set name, size, and window control type
        super("JankCord New Group Chat", 700, 800, false);


        // Create Label
        JLabel createLabel = new JLabel("Create New Group Chat:");

        // Create Label Init
        createLabel.setSize(500, 30);
        createLabel.setLocation(100, 100);
        createLabel.setForeground(new Color(114, 118, 125));
        createLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Create Label
        getContentPane().add(createLabel);


        // Num Of Users Label
        JLabel numOfUsersLabel = new JLabel("You can add 20 more friends.");

        // Num Of Users Label Init
        numOfUsersLabel.setSize(500, 30);
        numOfUsersLabel.setLocation(100, 610);
        numOfUsersLabel.setForeground(new Color(219, 222, 225));
        numOfUsersLabel.setFont(new Font("Whitney", Font.BOLD, 25));

        // Add Num Of Users Label
        getContentPane().add(numOfUsersLabel);


        // Users list
        DefaultListModel<User> users = new DefaultListModel<>();

        // Loop through all temp friends
        for (User friend : Jankcord.getTempFriends()) {
            // If friend isn't local user (by checking ID)
            if (friend.getId() != Jankcord.getFullUser().getId()) {
                // Add friend to list
                users.addElement(friend);
            }
        }


        // User List
        JankList userList = new JankList(users, numOfUsersLabel, "You can add %s more friends.");


        // User List Scroll
        JankScrollPane userListScroll = new JankScrollPane(480, 450, 100, 150, userList);

        // User List Scroll Init
        userListScroll.setMultiplier(25);
        userListScroll.getVerticalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(0, 0, 0), true));

        // Add User List Scroll
        getContentPane().add(userListScroll);


        // Submit
        JankButton submitButton = new JankButton("Create Group Chat", 500, 50, 100, 650);

        // Edit mouse release listener
        submitButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                // Format all users by user delimited by a comma
                List<User> usersResult = userList.getSelectedValuesList();

                // Add local user id first
                StringBuilder users = new StringBuilder(Jankcord.getFullUser().getId() + ",");

                // Loop through all users
                for (User user : usersResult) {
                    // Add remaining member id
                    users.append(user.getId()).append(",");
                }

                // Set headers; login information and
                HashMap<String, String> headers = new HashMap<>();
                headers.put("username", Jankcord.getFullUser().getUsername());
                headers.put("password", Jankcord.getFullUser().getPassword());
                headers.put("users", users.toString());

                // Send http request with headers to end point
                String response = ServerCommunicator.sendHttpRequest(Jankcord.getFullUser().getEndPointHost() + "creategroupchat", headers);

                // Dispose group chat window
                dispose();
            }
        });

        // Add submit
        getContentPane().add(submitButton);
    }
}
