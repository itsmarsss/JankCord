package jankcord.containers;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.LinkedList;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.components.texts.JankTextArea;
import jankcord.tools.ServerCommunicator;
import jankcord.texthelpers.UndoRedo;
import jankcord.objects.Message;
import jankcord.objects.User;
import jankcord.profiles.MemberProfile;
import jankcord.profiles.MessageProfile;

public class ChatBoxArea extends JPanel {
    private final JPanel chatBoxTopBarPanel;
    private final JPanel chatPanel;
    private final JankScrollPane chatBoxScrollPane;
    private final JPanel typePanel;

    private final JankTextArea textArea;
    private final JankScrollPane typeScrollPane;
    private final JPanel membersPanel;
    private final JankScrollPane membersScrollPane;

    private final LinkedList<MemberProfile> memberProfiles;
    private final LinkedList<MessageProfile> messageProfiles;

    private final GridBagConstraints gbc;

    public ChatBoxArea() {
        // Init
        setName("ChatBoxArea");

        setOpaque(true);
        setBorder(null);
        setLayout(null);
        setLocation(646, 50);
        setBackground(new Color(49, 51, 56));
        setSize(Jankcord.getViewPanel().getWidth() - 646, Jankcord.getViewPanel().getHeight() - 50);

        // Chat TopBar
        chatBoxTopBarPanel = new JPanel();
        // Chat Topbar Init
        chatBoxTopBarPanel.setLayout(null);
        chatBoxTopBarPanel.setSize(getWidth(), 106);
        chatBoxTopBarPanel.setBackground(new Color(54, 57, 63));
        chatBoxTopBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(39, 40, 45)));

        add(chatBoxTopBarPanel);

        // Chat Section
        chatPanel = new JPanel();
        chatPanel.setBackground(new Color(49, 51, 56));
        chatBoxScrollPane = new JankScrollPane(getWidth() - 540, 1168, 0, 106, chatPanel);
        // Chat Init
        add(chatBoxScrollPane);

        // Type Section
        typePanel = new JPanel();
        typePanel.setOpaque(true);
        typePanel.setBorder(null);
        typePanel.setLayout(null);
        typePanel.setBackground(new Color(56, 58, 64));
        typePanel.setSize(chatBoxScrollPane.getWidth() - 60, 81);
        typePanel.setLocation(30, getHeight() - 120);

        textArea = new JankTextArea();
        typeScrollPane = new JankScrollPane(typePanel.getWidth() - 40, (int) (Math.floor(typePanel.getHeight() / 45) * 45), 20, 24, textArea);

        typeScrollPane.setMultiplier(25);
        typeScrollPane.setBackground(new Color(56, 58, 64));
        typeScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        typePanel.add(typeScrollPane);

        textArea.setLineWrap(true);
        textArea.setText("Write a Message");
        textArea.setBackground(new Color(56, 58, 64));
        textArea.setForeground(new Color(255, 255, 255));
        textArea.setFont(new Font("Whitney", Font.PLAIN, 35));

        UndoRedo.makeUndoable(textArea);

        textArea.addMouseListener(new MouseListener() {
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

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        textArea.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    HashMap<String, String> headers = new HashMap<>();
//                    headers.put("username", Jankcord.getFullUser().getUsername());
//                    headers.put("password", Jankcord.getFullUser().getPassword());
//                    headers.put("otherID", Jankcord.getOtherID());
//                    headers.put("content", textArea.getText());
//
//                    ServerCommunicator.sendHttpRequest(Jankcord.getFullUser().getEndPointHost() + "sendmessage", headers);
//
//                    textArea.setText("");
                    reline();
                } else {
                    reline();
                }
            }

        });

        add(typePanel);

        // Members Section
        membersPanel = new JPanel();
        membersPanel.setBackground(new Color(43, 45, 49));

        membersScrollPane = new JankScrollPane(540, getHeight() - 106, chatBoxScrollPane.getWidth() + 5, 106, membersPanel);

        // MemberList Init
        membersScrollPane.setBackground(new Color(43, 45, 49));
        membersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        membersScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        add(membersScrollPane);

        // Add Members
        membersPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // Members
        gbc.insets = new Insets(0, 25, 12, 25);
        memberProfiles = new LinkedList<>();

        // Add Messages
        chatPanel.setLayout(new GridBagLayout());

        // Messages
        gbc.insets = new Insets(0, 0, 12, 5);
        messageProfiles = new LinkedList<>();

        JScrollBar vertical = chatBoxScrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        vertical.setValue(vertical.getMaximum());
    }

    public void reline() {
        System.out.println(textArea.getLineCount());
        int h = textArea.getLineCount() * 45 + 36;

        if (h <= 500) {
            typePanel.setSize(chatBoxScrollPane.getWidth() - 60, h);
            typePanel.setLocation(typePanel.getX(), (int) (getHeight() - ((Math.floor(typePanel.getHeight() / 45) * 45) + 75)));

            typeScrollPane.setSize(typePanel.getWidth() - 40, (int) (Math.floor(typePanel.getHeight() / 45) * 45));

            chatBoxScrollPane.setSize(chatBoxScrollPane.getWidth(), getHeight() - (25 + 106 + typePanel.getHeight()));

            revalidate();
            repaint();
        }
    }

    private int index = 0;

    public void addMessage(Message message) {
        MessageProfile mp = new MessageProfile(message);
        messageProfiles.add(mp);

        gbc.gridx = 0;
        gbc.gridy = index;
        chatPanel.add(mp, gbc);

        membersScrollPane.revalidate();
        membersScrollPane.repaint();

        reline();

        index++;
    }

    public void addMember(User member, int index) {
        MemberProfile cp = new MemberProfile(member);
        memberProfiles.add(cp);

        gbc.gridx = 0;
        gbc.gridy = index;

        membersPanel.add(cp, gbc);

        membersPanel.revalidate();
        membersPanel.repaint();
    }

    public void resetMessages() {
        chatPanel.removeAll();
        messageProfiles.clear();

        chatPanel.revalidate();
        chatPanel.repaint();

        index = 0;
    }

    public void resetMembers() {
        membersPanel.removeAll();
        memberProfiles.clear();

        membersPanel.revalidate();
        membersPanel.repaint();
    }

    public void setMaxChatScroll() {
        SwingUtilities.invokeLater(() -> {
            JViewport viewport = chatBoxScrollPane.getViewport();
            Point bottom = new Point(0, chatPanel.getHeight());
            viewport.setViewPosition(bottom);
            System.out.println("scroll");
        });
    }

    public int getMessageIndex() {
        return index;
    }

    public JPanel getChatBoxTopBarPanel() {
        return chatBoxTopBarPanel;
    }

    public JScrollPane getChatBoxScrollPane() {
        return chatBoxScrollPane;
    }

    public JScrollPane getMembersScrollPane() {
        return membersScrollPane;
    }

    public JPanel getTypePanel() {
        return typePanel;
    }

    public JScrollPane getTypeScrollPane() {
        return typeScrollPane;
    }

    public LinkedList<MessageProfile> getMessageProfiles() {
        return messageProfiles;
    }
}
