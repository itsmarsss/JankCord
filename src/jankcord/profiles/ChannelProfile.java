package jankcord.profiles;

import jankcord.Jankcord;
import jankcord.objects.SimpleUserCache;
import jankcord.objects.User;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

// ChannelProfile, child of JPanel; channel listing
public class ChannelProfile extends JPanel {
    // Instance fields
    private User friend;
    private JLabel usernameLabel;

    // Constructor, parameter friend
    public ChannelProfile(User friend) {
        // Set field
        this.friend = friend;

        // Get avatar of friend or default to default pfp
        Image avatar = Jankcord.getAvatarCache().getOrDefault(friend.getId(), new SimpleUserCache()).getAvatar72();

        // Set JPanel Properties
        setLayout(null);
        setBackground(null);
        setPreferredSize(new Dimension(420, 92));


        // Icon
        JLabel channelIcon = new JLabel();

        // Icon Init
        channelIcon.setLocation(12, 8);
        channelIcon.setSize(72, 72);
        channelIcon.setIcon(new ImageIcon(avatar));

        // Add Icon
        add(channelIcon);


        // Username
        usernameLabel = new JLabel(friend.getUsername());

        // Username Init
        usernameLabel.setLocation(100, 23);
        usernameLabel.setSize(328, 40);
        usernameLabel.setForeground(new Color(142, 146, 151));
        usernameLabel.setFont(new Font("Whitney", Font.PLAIN, 28));

        // Add username
        add(usernameLabel);


        // Add mouse listener to listing
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            // Mouse pressed
            @Override
            public void mousePressed(MouseEvent e) {
                // Set colors
                setBackground(new Color(59, 60, 66));
                usernameLabel.setForeground(new Color(255, 255, 255));
            }

            // Mouse released
            @Override
            public void mouseReleased(MouseEvent e) {
                // Check if new text place
                if (!Jankcord.getOtherID().equals(friend.getId() + "")) {
                    // Set text place
                    Jankcord.setOtherID(friend.getId() + "");

                    // Inform new text place
                    Jankcord.setNewOtherID(true);
                    Jankcord.setInServer(false);
                    Jankcord.setInServerCheck(true);

                    // Reset messages
                    Jankcord.getChatBoxArea().resetMessages();

                    // Query for new messages
                    Jankcord.queryForNewMessages();

                    // Reset channel list display
                    Jankcord.getChannelList().resetDisplays();

                    // Set channel name
                    Jankcord.getChatBoxArea().setChannelName(friend.getUsername());

                    // Update colors
                    setBackground(new Color(59, 60, 66));
                    usernameLabel.setForeground(new Color(255, 255, 255));
                }
            }

            // Mouse enter
            @Override
            public void mouseEntered(MouseEvent e) {
                // If text place is not current
                if (!Jankcord.getOtherID().equals(friend.getId() + "")) {
                    // Update colors
                    setBackground(new Color(54, 55, 61));
                    usernameLabel.setForeground(new Color(219, 222, 225));
                }
            }

            // Mouse exit
            @Override
            public void mouseExited(MouseEvent e) {
                // If text place is not current
                if (!Jankcord.getOtherID().equals(friend.getId() + "")) {
                    // Update colors
                    setBackground(null);
                    usernameLabel.setForeground(new Color(142, 146, 151));
                }
            }
        });
    }

    /**
     * Reset display colors
     */
    public void resetDisplay() {
        // Reset colors
        setBackground(null);
        usernameLabel.setForeground(new Color(142, 146, 151));
    }

    // Getters and setters
    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public void setUsernameLabel(JLabel usernameLabel) {
        this.usernameLabel = usernameLabel;
    }
}
