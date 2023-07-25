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

// Member Profile, child of JPanel; member listing
public class MemberProfile extends JPanel {
    // Instance field
    private User member;

    // Constructor create member listing, member as parameter
    public MemberProfile(User member) {
        // Set field
        this.member = member;

        // Get avatar of member or default to default pfp
        Image avatar = Jankcord.avatarCache.getOrDefault(member.getId(), new SimpleUserCache()).getAvatar72();

        // Set JPanel properties
        setLayout(null);
        setBackground(null);
        setPreferredSize(new Dimension(475, 92));


        // Icon
        JLabel channelIcon = new JLabel();

        // Icon init
        channelIcon.setSize(72, 72);
        channelIcon.setLocation(12, 8);
        channelIcon.setIcon(new ImageIcon(avatar));

        // Add Icon
        add(channelIcon);


        // Username
        JLabel usernameLabel = new JLabel(member.getUsername());

        // Username Init
        usernameLabel.setSize(328, 40);
        usernameLabel.setLocation(100, 23);
        usernameLabel.setForeground(new Color(142, 146, 151));
        usernameLabel.setFont(new Font("Whitney", Font.PLAIN, 28));

        // Add username
        add(usernameLabel);


        // Add mouse listener to listing
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            // Mouse press
            @Override
            public void mousePressed(MouseEvent e) {
                // Update colors
                setBackground(new Color(59, 60, 66));
                usernameLabel.setForeground(new Color(255, 255, 255));
            }

            // Mouse released
            @Override
            public void mouseReleased(MouseEvent e) {
                // If current chat place if not same as member and member is not self
                if (!Jankcord.getOtherID().equals(member.getId() + "") &&
                        Jankcord.getFullUser().getId() != member.getId()) {
                    // Set new chat place
                    Jankcord.setOtherID(member.getId() + "");

                    // Notify new chat palce
                    Jankcord.setNewOtherID(true);
                    Jankcord.setInServer(false);
                    Jankcord.setInServerCheck(true);

                    // Reset all messages
                    Jankcord.getChatBoxArea().resetMessages();

                    // Query for new messages
                    Jankcord.queryForNewMessages();

                    // Reinitialize channel listing
                    Jankcord.getChannelList().initChannelPanel();
                    Jankcord.getChannelList().resetDisplays();

                    // Query for new friends
                    Jankcord.queryForNewFriend();

                    // Update channel name
                    Jankcord.getChatBoxArea().setChannelName(member.getUsername());
                }

                // Update colors
                setBackground(new Color(59, 60, 66));
                usernameLabel.setForeground(new Color(219, 222, 225));
            }

            // Mouse enter
            @Override
            public void mouseEntered(MouseEvent e) {
                // Update colors
                setBackground(new Color(54, 55, 61));
                usernameLabel.setForeground(new Color(219, 222, 225));
            }

            // Mouse exited
            @Override
            public void mouseExited(MouseEvent e) {
                // Update colors
                setBackground(null);
                usernameLabel.setForeground(new Color(142, 146, 151));
            }
        });
    }

    // Getters and setters
    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }
}
