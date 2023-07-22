package jankcord.profiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jankcord.Jankcord;
import jankcord.objects.SimpleUserCache;
import jankcord.objects.Message;

public class MessageProfile extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Message message;

    public MessageProfile(Message message) {
        SimpleUserCache cachedUser = Jankcord.avatarCache.get(message.getSenderID());

        Image avatar = Jankcord.avatarCache.getOrDefault(message.getSenderID(), new SimpleUserCache()).getAvatar80();

        // Init
        setLayout(null);
        setBackground(null);
        setPreferredSize(new Dimension(Jankcord.getViewPanel().getWidth() - 646, 100));

        // Icon
        JLabel usersIcon = new JLabel();
        usersIcon.setSize(80, 80);
        usersIcon.setLocation(15, 10);
        usersIcon.setIcon(new ImageIcon(avatar));

        add(usersIcon);

        // Username
        JLabel usernameLabel = new JLabel(cachedUser.getUsername());
        Font font = new Font("Whitney", Font.PLAIN, 28);
        usernameLabel.setFont(font);
        usernameLabel.setLocation(110, 10);
        usernameLabel.setForeground(new Color(255, 255, 255));

        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int w = (int) (font.getStringBounds(cachedUser.getUsername(), frc).getWidth());

        usernameLabel.setSize(w + 5, 40);

        // Time stamp
        String timeStamp = new Date(message.getTimestamp()).toString();
        JLabel timeLabel = new JLabel(timeStamp);
        Font font2 = new Font("Whitney", Font.PLAIN, 24);
        timeLabel.setFont(font2);
        timeLabel.setForeground(new Color(162, 165, 169));
        timeLabel.setLocation(usernameLabel.getX() + usernameLabel.getWidth() + 10, 12);

        FontRenderContext frc2 = new FontRenderContext(new AffineTransform(), true, true);
        int w2 = (int) (font.getStringBounds(timeStamp, frc2).getWidth());

        timeLabel.setSize(w2, 40);

        // Message content
        JLabel messageLabel = new JLabel(message.getContent());
        messageLabel.setFont(font);
        messageLabel.setSize((int) getPreferredSize().getWidth() - messageLabel.getX(), (int) getPreferredSize().getHeight());
        messageLabel.setForeground(new Color(255, 255, 255));
        messageLabel.setLocation(usernameLabel.getX(), usernameLabel.getY() + 10);

        add(usernameLabel);
        add(timeLabel);
        add(messageLabel);

        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
