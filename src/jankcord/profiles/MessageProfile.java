package jankcord.profiles;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Date;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.components.texts.JankTextArea;
import jankcord.objects.SimpleUserCache;
import jankcord.objects.Message;

public class MessageProfile extends JPanel {
    private final Message message;
    private final JankScrollPane messageAreaScroll;
    private final JLabel messageArea;
    private final String content;

    public MessageProfile(Message message) {
        SimpleUserCache cachedUser = Jankcord.avatarCache.get(message.getSenderID());

        Image avatar = Jankcord.avatarCache.getOrDefault(message.getSenderID(), new SimpleUserCache()).getAvatar80();

        // Init
        setLayout(null);
        setBackground(new Color(49, 51, 56));
        setPreferredSize(new Dimension(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - 30, 0));

        // Icon
        JLabel usersIcon = new JLabel();
        usersIcon.setSize(80, 80);
        usersIcon.setLocation(15, 10);
        usersIcon.setIcon(new ImageIcon(avatar));

        add(usersIcon);

        // Username
        JLabel usernameLabel = new JLabel(cachedUser.getUsername());
        usernameLabel.setLocation(110, 10);
        usernameLabel.setForeground(new Color(242, 243, 245));
        usernameLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int w = (int) (usernameLabel.getFont().getStringBounds(cachedUser.getUsername(), frc).getWidth());

        usernameLabel.setSize(w + 5, 40);

        // Time stamp
        String timeStamp = new Date(message.getTimestamp()).toString();
        JLabel timeLabel = new JLabel(timeStamp);

        timeLabel.setForeground(new Color(162, 165, 169));
        timeLabel.setFont(new Font("Whitney", Font.PLAIN, 24));
        timeLabel.setLocation(usernameLabel.getX() + usernameLabel.getWidth() + 10, 12);

        FontRenderContext frc2 = new FontRenderContext(new AffineTransform(), true, true);
        int w2 = (int) (timeLabel.getFont().getStringBounds(timeStamp, frc2).getWidth());

        timeLabel.setSize(w2, 40);

        // Message content
        content = message.getContent().replaceAll("\\n", "<br>");

        messageArea = new JLabel();

        messageArea.setText("<html><body>" + content + "</body></html>");
        messageArea.setOpaque(true);
        messageArea.setBackground(new Color(49, 51, 56));
        messageArea.setForeground(new Color(242, 243, 245));
        messageArea.setFont(new Font("Whitney", Font.PLAIN, 28));

        FontRenderContext frc3 = new FontRenderContext(new AffineTransform(), true, true);
        int w3 = (int) (messageArea.getFont().getStringBounds(content, frc3).getWidth());
        int h3 = (int) (messageArea.getFont().getStringBounds(content, frc3).getHeight());

        messageAreaScroll = new JankScrollPane(w3, h3, usernameLabel.getX(), usernameLabel.getY() + 45, messageArea);

        messageAreaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        messageAreaScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 15));

        add(usernameLabel);
        add(timeLabel);
        add(messageAreaScroll);

        updateMessageWidth();

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

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(46, 48, 53));
                messageArea.setBackground(new Color(46, 48, 53));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(49, 51, 56));
                messageArea.setBackground(new Color(49, 51, 56));
            }
        });

        messageAreaScroll.addMouseWheelListener(e -> {
            if(messageAreaScroll.getHorizontalScrollBar().getMaximum() == messageAreaScroll.getWidth()) {
                Jankcord.getChatBoxArea().getChatBoxScrollPane().artificialScroll(e);
            }
        });

        for(Component jc : getComponents()) {
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
                    messageArea.setBackground(new Color(46, 48, 53));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(49, 51, 56));
                    messageArea.setBackground(new Color(49, 51, 56));
                }
            });
        }


        this.message = message;
    }

    public void updateMessageWidth() {
//        int labelWidth = (int) getPreferredSize().getWidth() - messageArea.getX() - 60;
//
//        int currentLineWidth = 0;
//
//        StringBuilder wrappedContent = new StringBuilder("<html><body style='font-family: Whitney; font-size: 28px; color: white;'>");
//        String[] words = content.split("\\s+");
//
//        for (String word : words) {
//            int wordWidth = messageArea.getFontMetrics(messageArea.getFont()).stringWidth(word + " ");
//
//            // If the word is longer than the container width, wrap it with <nobr> tag
//            if (wordWidth > labelWidth) {
//                String wrappedWord = "<nobr>" + wrapWord(word, messageArea.getFontMetrics(messageArea.getFont()), labelWidth) + "</nobr>";
//                int wrappedWordWidth = messageArea.getFontMetrics(messageArea.getFont()).stringWidth(wrappedWord + " ");
//                if (currentLineWidth + wrappedWordWidth <= labelWidth) {
//                    wrappedContent.append(wrappedWord).append(" ");
//                    currentLineWidth += wrappedWordWidth;
//                } else {
//                    wrappedContent.append("<br>").append(wrappedWord).append(" ");
//                    currentLineWidth = wrappedWordWidth;
//                }
//            } else {
//                // Wrap regular words
//                if (currentLineWidth + wordWidth <= labelWidth) {
//                    wrappedContent.append(word).append(" ");
//                    currentLineWidth += wordWidth;
//                } else {
//                    wrappedContent.append("<br>").append(word).append(" ");
//                    currentLineWidth = wordWidth;
//                }
//            }
//        }
//
//        wrappedContent.append("</body></html>");
//        messageArea.setText(wrappedContent.toString());

        messageArea.setSize((int) (messageArea.getPreferredSize().getWidth() + 15), (int) (messageArea.getPreferredSize().getHeight() + 15));
        messageAreaScroll.setSize((int) getPreferredSize().getWidth() - messageArea.getX(), (int) messageArea.getPreferredSize().getHeight() + 15);
        setPreferredSize(new Dimension(Jankcord.getChatBoxArea().getChatBoxScrollPane().getWidth() - 30, (int) (messageArea.getPreferredSize().getHeight() + 75)));
    }

    public Message getMessage() {
        return message;
    }
}
