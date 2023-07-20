package jankcord.profiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jankcord.Jankcord;

public class MessageProfile extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Image userIcon;
    private String userID;
    private String messageID;
    private String username;
    private String content;

    public MessageProfile(Image userIcon, String userID, String messageID, String username, String content) {
        // Init
        setLayout(null);
        setBackground(null);
        setPreferredSize(new Dimension(Jankcord.getViewPanel().getWidth() - 646, 100));

        // Icon
        Image scaledIcon = userIcon.getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        JLabel usersIcon = new JLabel();
        usersIcon.setSize(80, 80);
        usersIcon.setLocation(15, 10);
        usersIcon.setIcon(new ImageIcon(scaledIcon));

        add(usersIcon);

        // Username
        JLabel usernameLabel = new JLabel(username);
        Font font = new Font("Whitney", Font.PLAIN, 28);
        usernameLabel.setFont(font);
        usernameLabel.setLocation(110, 10);
        usernameLabel.setForeground(new Color(255, 255, 255));

        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int w = (int) (font.getStringBounds(userID, frc).getWidth());

        usernameLabel.setSize(w + 5, 40);

        // Time stamp
        String timeStamp = "TempTimeStamp";
        JLabel timeLabel = new JLabel(timeStamp);
        Font font2 = new Font("Whitney", Font.PLAIN, 24);
        timeLabel.setFont(font2);
        timeLabel.setForeground(new Color(162, 165, 169));
        timeLabel.setLocation(usernameLabel.getX() + usernameLabel.getWidth() + 10, 12);

        FontRenderContext frc2 = new FontRenderContext(new AffineTransform(), true, true);
        int w2 = (int) (font.getStringBounds(timeStamp, frc2).getWidth());

        timeLabel.setSize(w2, 40);

        // Message content
        JLabel messageLabel = new JLabel(content);
        messageLabel.setFont(font);
        messageLabel.setSize((int) getPreferredSize().getWidth() - messageLabel.getX(), (int) getPreferredSize().getHeight());
        messageLabel.setForeground(new Color(255, 255, 255));
        messageLabel.setLocation(usernameLabel.getX(), usernameLabel.getY() + 10);

        add(usernameLabel);
        add(timeLabel);
        add(messageLabel);

        this.userIcon = userIcon;
        this.userID = userID;
        this.messageID = messageID;
        this.username = username;
        this.content = content;
    }

    public Image getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Image userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
