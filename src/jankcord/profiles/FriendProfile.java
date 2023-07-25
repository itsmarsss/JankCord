package jankcord.profiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jankcord.tools.ResourceLoader;

// FriendProfile, child of JPanel; friend listing
public class FriendProfile extends JPanel {
    // Constructor to create friend listing
    public FriendProfile() {
        // Set JPanel properties
        setLayout(null);
        setBackground(new Color(43, 45, 49));
        setPreferredSize(new Dimension(420, 92));


        // Icon
        JLabel channelIcon = new JLabel();

        // Icon init
        channelIcon.setLocation(12, 8);
        channelIcon.setSize(72, 72);
        channelIcon.setIcon(ResourceLoader.loader.getFriendProfileIcon());

        // Add Icon
        add(channelIcon);


        // Username
        JLabel usernameLabel = new JLabel("Friends");

        // Username Init
        usernameLabel.setLocation(100, 26);
        usernameLabel.setSize(328, 40);
        usernameLabel.setForeground(new Color(142, 146, 151));
        usernameLabel.setFont(new Font("Whitney", Font.PLAIN, 28));

        // Add username
        add(usernameLabel);
    }
}
