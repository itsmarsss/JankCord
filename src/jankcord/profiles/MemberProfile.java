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

public class MemberProfile extends JPanel {
    private final User member;

    public MemberProfile(User member) {
        Image avatar = Jankcord.avatarCache.getOrDefault(member.getId(), new SimpleUserCache()).getAvatar72();

        // Init
        setLayout(null);
        setBackground(null);
        setPreferredSize(new Dimension(475, 92));

        // Icon
        JLabel channelIcon = new JLabel();
        channelIcon.setSize(72, 72);
        channelIcon.setLocation(12, 8);
        channelIcon.setIcon(new ImageIcon(avatar));

        add(channelIcon);

        // Username
        JLabel usernameLabel = new JLabel(member.getUsername());
        usernameLabel.setSize(328, 40);
        usernameLabel.setLocation(100, 23);
        usernameLabel.setForeground(new Color(142, 146, 151));
        usernameLabel.setFont(new Font("Whitney", Font.PLAIN, 28));

        add(usernameLabel);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(new Color(59, 60, 66));
                usernameLabel.setForeground(new Color(255, 255, 255));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!Jankcord.getOtherID().equals(member.getId() + "") &&
                        Jankcord.getFullUser().getId() != member.getId()) {
                    Jankcord.setOtherID(member.getId() + "");
                    Jankcord.setNewOtherID(true);
                    Jankcord.setInServer(false);
                    Jankcord.setInServerCheck(true);
                    Jankcord.getChatBoxArea().resetMessages();
                    Jankcord.queryForNewMessages();
                    Jankcord.getChannelList().initChannelPanel();
                    Jankcord.getChannelList().resetDisplays();

                    Jankcord.queryForNewFriend();
                    Jankcord.getChatBoxArea().setChannelName(member.getUsername());
                }

                setBackground(new Color(59, 60, 66));
                usernameLabel.setForeground(new Color(219, 222, 225));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(54, 55, 61));
                usernameLabel.setForeground(new Color(219, 222, 225));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(null);
                usernameLabel.setForeground(new Color(142, 146, 151));
            }
        });

        this.member = member;
    }

    public User getMember() {
        return member;
    }
}
