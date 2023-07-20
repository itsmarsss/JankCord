package jankcord.profiles;

import jankcord.newclasses.ResourceLoader;
import jankcord.objects.User;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
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
        Image avatar = ResourceLoader.loader.getTempProfileIcon().getImage();

        try {
            URL url = new URL(member.getAvatarURL());

            BufferedImage image = ImageIO.read(url);

            avatar = new ImageIcon(image).getImage();
        } catch (Exception e) {
            System.out.println("Error getting avatar");
        }

        // Init
        setLayout(null);
        setBackground(null);
        setPreferredSize(new Dimension(475, 92));

        // Icon
        Image scaledIcon = avatar.getScaledInstance(72, 72, Image.SCALE_DEFAULT);
        JLabel channelIcon = new JLabel();
        channelIcon.setSize(72, 72);
        channelIcon.setLocation(12, 8);
        channelIcon.setIcon(new ImageIcon(scaledIcon));

        add(channelIcon);

        // Username
        JLabel usernameLabel = new JLabel("Place Holder");
        usernameLabel.setSize(328, 40);
        usernameLabel.setLocation(100, 12);
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
