package jankcord.profiles;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.objects.SimpleUserCache;
import jankcord.objects.Message;

// Message Profile, child of JPanel; message listing
public class MessageProfile extends JPanel {
    // Instance fields
    private Message message;
    private JankScrollPane messageAreaScroll;
    private JLabel messageArea;

    // Constructor to set message
    public MessageProfile(Message message) {
        // Set field; message
        this.message = message;

        // Get cached user
        SimpleUserCache cachedUser = Jankcord.getAvatarCache().get(message.getSenderID());

        // Get avatar of friend or default to default pfp
        Image avatar = Jankcord.getAvatarCache().getOrDefault(message.getSenderID(), new SimpleUserCache()).getAvatar80();

        // Set JPanel properties
        setLayout(null);
        setBackground(new Color(49, 51, 56));
        setPreferredSize(new Dimension(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - 30, 0));


        // Icon
        JLabel usersIcon = new JLabel();

        // Icon Init
        usersIcon.setLocation(15, 10);
        usersIcon.setSize(80, 80);
        usersIcon.setIcon(new ImageIcon(avatar));

        // Add Icon
        add(usersIcon);


        // Username
        JLabel usernameLabel = new JLabel(cachedUser.getUsername());

        // Username init
        usernameLabel.setLocation(110, 10);
        usernameLabel.setForeground(new Color(242, 243, 245));
        usernameLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        usernameLabel.setSize((int) (usernameLabel.getPreferredSize().getWidth() + 20), 40);

        // Add Username
        add(usernameLabel);


        // Time stamp formatting
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        String timeStamp = sdf.format(new Date(message.getTimestamp()));

        // Time Label
        JLabel timeLabel = new JLabel(timeStamp);

        // Time Label init
        timeLabel.setForeground(new Color(162, 165, 169));
        timeLabel.setFont(new Font("Whitney", Font.PLAIN, 24));
        timeLabel.setLocation(usernameLabel.getX() + usernameLabel.getWidth() + 10, 12);
        timeLabel.setSize((int) (timeLabel.getPreferredSize().getWidth() + 20), 40);

        // Add Time Label
        add(timeLabel);


        // Message content
        String content = message.getContent().replaceAll("\\n", "<br>");


        // Message Area
        messageArea = new JLabel("<html><body>" + content + "</body></html>");

        // Message Area Init
        messageArea.setOpaque(true);
        messageArea.setBackground(new Color(49, 51, 56));
        messageArea.setForeground(new Color(242, 243, 245));
        messageArea.setFont(new Font("Whitney", Font.PLAIN, 28));
        messageArea.setSize((int) (messageArea.getPreferredSize().getWidth() + 15), (int) (messageArea.getPreferredSize().getHeight() + 15));


        // Message Area Scroll
        messageAreaScroll = new JankScrollPane(
                Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - (messageArea.getX() + 300), (int) messageArea.getPreferredSize().getHeight() + 30,
                usernameLabel.getX(), usernameLabel.getY() + 45, messageArea
        );

        // Message Area Scroll Init
        messageAreaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        messageAreaScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 15));

        // Add Message Area Scroll
        add(messageAreaScroll);


        // Set preferred size of listing based on parent and children size
        setPreferredSize(new Dimension(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - 30, (int) (messageArea.getPreferredSize().getHeight() + 100)));

        // Update width of all messages
        updateMessageWidth();

        // Add message area mouse listener
        messageArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            // Mouse entered
            @Override
            public void mouseEntered(MouseEvent e) {
                // Update colors
                setBackground(new Color(46, 48, 53));
                messageArea.setBackground(new Color(46, 48, 53));
            }

            // Mouse exited
            @Override
            public void mouseExited(MouseEvent e) {
                // Update colors
                setBackground(new Color(49, 51, 56));
                messageArea.setBackground(new Color(49, 51, 56));
            }
        });

        // Add scroll wheel listener; lambda expression
        messageAreaScroll.addMouseWheelListener(e -> {
            // Check if content has horizontal scroll
            if (messageAreaScroll.getHorizontalScrollBar().getMaximum() == messageAreaScroll.getWidth()) {
                // If not, elevate to message listing scroll
                Jankcord.getChatBoxArea().getChatBoxScrollPane().smoothScroll(e);
            }
        });

        // Add mouse listener to listing
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            // Mouse entered
            @Override
            public void mouseEntered(MouseEvent e) {
                // Update colors
                setBackground(new Color(46, 48, 53));
                messageArea.setBackground(new Color(46, 48, 53));
            }

            // Mouse exited
            @Override
            public void mouseExited(MouseEvent e) {
                // Update colors
                setBackground(new Color(49, 51, 56));
                messageArea.setBackground(new Color(49, 51, 56));
            }
        });
    }

    public void updateMessageWidth() {
        // Update message area and parent scrollpane sizes based on chat scroll size
        messageArea.setSize((int) (messageArea.getPreferredSize().getWidth() + 15), (int) (messageArea.getPreferredSize().getHeight()));
        messageAreaScroll.setSize(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - (messageArea.getX() + 300), (int) messageArea.getPreferredSize().getHeight() + 30);

        // Set preferred size of listing based on children changes
        setPreferredSize(new Dimension(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - 30, (int) (messageArea.getPreferredSize().getHeight() + 100)));
    }

    // Getters and setters
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public JankScrollPane getMessageAreaScroll() {
        return messageAreaScroll;
    }

    public void setMessageAreaScroll(JankScrollPane messageAreaScroll) {
        this.messageAreaScroll = messageAreaScroll;
    }

    public JLabel getMessageArea() {
        return messageArea;
    }

    public void setMessageArea(JLabel messageArea) {
        this.messageArea = messageArea;
    }
}
