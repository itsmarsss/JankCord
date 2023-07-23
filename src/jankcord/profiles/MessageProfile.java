package jankcord.profiles;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Date;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.objects.SimpleUserCache;
import jankcord.objects.Message;

public class MessageProfile extends JPanel {
    private final Message message;
    private final JLabel messageLabel;
    private final String content;

    public static final String template = "<html>%s</html>";

    public MessageProfile(Message message) {
        SimpleUserCache cachedUser = Jankcord.avatarCache.get(message.getSenderID());

        Image avatar = Jankcord.avatarCache.getOrDefault(message.getSenderID(), new SimpleUserCache()).getAvatar80();

        // Init
        setLayout(null);
        setBackground(null);
        setPreferredSize(new Dimension(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - 30, 0));

        // Icon
        JLabel usersIcon = new JLabel();
        usersIcon.setSize(80, 80);
        usersIcon.setLocation(15, 10);
        usersIcon.setIcon(new ImageIcon(avatar));

        add(usersIcon);

        // Username
        JLabel usernameLabel = new JLabel(cachedUser.getUsername());
        usernameLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        usernameLabel.setLocation(110, 10);
        usernameLabel.setForeground(new Color(242, 243, 245));

        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int w = (int) (usernameLabel.getFont().getStringBounds(cachedUser.getUsername(), frc).getWidth());

        usernameLabel.setSize(w + 5, 40);

        // Time stamp
        String timeStamp = new Date(message.getTimestamp()).toString();
        JLabel timeLabel = new JLabel(timeStamp);

        timeLabel.setFont(new Font("Whitney", Font.PLAIN, 24));
        timeLabel.setForeground(new Color(162, 165, 169));
        timeLabel.setLocation(usernameLabel.getX() + usernameLabel.getWidth() + 10, 12);

        FontRenderContext frc2 = new FontRenderContext(new AffineTransform(), true, true);
        int w2 = (int) (timeLabel.getFont().getStringBounds(timeStamp, frc2).getWidth());

        timeLabel.setSize(w2, 40);

        // Message content
        content = message.getContent().replaceAll("\\n", "<br/>");

        messageLabel = new JLabel(String.format(template, content));

        messageLabel.setForeground(new Color(242, 243, 245));
        messageLabel.setFont(new Font("Whitney", Font.PLAIN, 28));
        messageLabel.setLocation(usernameLabel.getX(), usernameLabel.getY() + 45);

        add(usernameLabel);
        add(timeLabel);
        add(messageLabel);

        updateMessageWidth();

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

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(46, 48, 53));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(null);
            }
        });

        this.message = message;
    }

    public void updateMessageWidth() {
        // Remove any existing HTML tags and line breaks
        System.out.println("asdasdasdsddadsdasdsddad");
        String cleanedText = content.replaceAll("<br>", "").replaceAll("<html>|</html>", "");

        FontMetrics fontMetrics = messageLabel.getFontMetrics(messageLabel.getFont());
        int labelWidth = (int) getPreferredSize().getWidth() - messageLabel.getX() - 30;


        StringBuilder wrappedText = new StringBuilder("<html>");
        String[] words = cleanedText.split("\\s+");

        for (String word : words) {
            int wordWidth = SwingUtilities.computeStringWidth(fontMetrics, word);

            if (labelWidth < wordWidth) {
                System.out.println("too long: " + word);
                String[] letters = word.split("");
                int currentLineWidth = 0;

                for (String letter : letters) {
                    int letterWidth = SwingUtilities.computeStringWidth(fontMetrics, letter);

                    if (currentLineWidth + letterWidth <= labelWidth) {
                        wrappedText.append(letter);
                        currentLineWidth += letterWidth;
                    } else {
                        wrappedText.append("<br>").append(letter);
                        currentLineWidth = letterWidth;
                    }
                }
            } else {
                wrappedText.append(word).append(" ");
            }
        }

        wrappedText.append("</html>");
        messageLabel.setText(wrappedText.toString());

        //System.out.println(labelWidth);
        System.out.println(messageLabel.getText());

        messageLabel.setSize((int) getPreferredSize().getWidth() - messageLabel.getX(), (int) messageLabel.getPreferredSize().getHeight());
        setPreferredSize(new Dimension(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - 30, (int) (messageLabel.getPreferredSize().getHeight() + 60)));
    }

    public Message getMessage() {
        return message;
    }
}
