package jankcord.profiles;

import jankcord.Jankcord;
import jankcord.objects.SimpleUserCache;
import jankcord.objects.User;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MemberProfile extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private User member;

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

        this.member = member;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }
}
