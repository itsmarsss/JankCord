package jankcord.containers;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.components.texts.JankTextArea;
import jankcord.tools.Base64Helper;
import jankcord.tools.ServerCommunicator;
import jankcord.texthelpers.UndoRedo;
import jankcord.objects.Message;
import jankcord.objects.User;
import jankcord.profiles.MemberProfile;
import jankcord.profiles.MessageProfile;

public class ChatBoxArea extends JPanel {
    private final JPanel chatBoxTopBarPanel;
    private final JLabel channelName;
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

    private boolean shifting = false;

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

        channelName = new JLabel();
        channelName.setLocation(50, 20);
        channelName.setBackground(new Color(56, 58, 64));
        channelName.setForeground(new Color(255, 255, 255));
        channelName.setFont(new Font("Whitney", Font.BOLD, 45));

        setChannelName("Select a channel.");

        chatBoxTopBarPanel.add(channelName);

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
        typeScrollPane = new JankScrollPane(typePanel.getWidth() - 40, (int) (Math.floor(typePanel.getHeight() / 37) * 37), 20, 24, textArea);

        typeScrollPane.setMultiplier(25);
        typeScrollPane.setBackground(new Color(56, 58, 64));
        typeScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        typePanel.add(typeScrollPane);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("Write a Message");
        textArea.setBackground(new Color(56, 58, 64));
        textArea.setForeground(new Color(255, 255, 255));
        textArea.setFont(new Font("Whitney", Font.PLAIN, 28));

        UndoRedo.makeUndoable(textArea);

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shifting = true;
                } else if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (!shifting)) {
                    e.consume();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shifting = false;
                } else if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (!shifting)) {
                    String content = Base64Helper.encode(textArea.getText().trim());

                    textArea.setText("");
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("username", Jankcord.getFullUser().getUsername());
                    headers.put("password", Jankcord.getFullUser().getPassword());
                    headers.put("otherID", Jankcord.getOtherID());
                    headers.put("content", content);

                    String dest = "sendmessage";
                    if (Jankcord.isInServer()) {
                        dest = "sendgroupmessage";
                    }

                    String response = ServerCommunicator.sendHttpRequest(Jankcord.getFullUser().getEndPointHost() + dest, headers);

                    //System.out.println(response);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    textArea.insert("\n", textArea.getCaretPosition());
                }
                reline();
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
    }

    public void reline() {
        int h = textArea.getLineCount() * 37 + 36;

        if (h <= 500) {
            typePanel.setSize(chatBoxScrollPane.getWidth() - 60, h);
            typePanel.setLocation(typePanel.getX(), (int) (getHeight() - ((Math.floor(typePanel.getHeight() / 37) * 37) + 75)));

            typeScrollPane.setSize(typePanel.getWidth() - 40, (int) (Math.floor(typePanel.getHeight() / 37) * 37));

            chatBoxScrollPane.setSize(chatBoxScrollPane.getWidth(), getHeight() - (25 + 106 + typePanel.getHeight()));
        }
    }

    private int index = 0;

    public void addMessage(Message message) {
        MessageProfile mp = new MessageProfile(message);
        messageProfiles.add(mp);

        gbc.gridx = 0;
        gbc.gridy = index;
        chatPanel.add(mp, gbc);

        chatPanel.revalidate();
        chatPanel.repaint();

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
            if (Jankcord.isNewOtherID()) {
                JViewport viewport = chatBoxScrollPane.getViewport();
                Point bottom = new Point(0, chatPanel.getHeight());
                viewport.setViewPosition(bottom);

                Jankcord.setNewOtherID(false);
            } else {
                chatBoxScrollPane.smoothScrollBottom();
            }
            // System.out.println("scroll");
        });
    }

    public void resetMessageWidths() {
        SwingUtilities.invokeLater(() -> {
            for (MessageProfile mp : messageProfiles) {
                mp.updateMessageWidth();
            }
        });
    }

    public int getMessageIndex() {
        return index;
    }

    public JPanel getChatBoxTopBarPanel() {
        return chatBoxTopBarPanel;
    }

    public void setChannelName(String chatName) {
        channelName.setText(chatName);
        channelName.setSize(channelName.getPreferredSize());
    }

    public JPanel getChatPanel() {
        return chatPanel;
    }


    public JankScrollPane getChatBoxScrollPane() {
        return chatBoxScrollPane;
    }

    public JankScrollPane getMembersScrollPane() {
        return membersScrollPane;
    }

    public JPanel getTypePanel() {
        return typePanel;
    }

    public JankScrollPane getTypeScrollPane() {
        return typeScrollPane;
    }

    public LinkedList<MessageProfile> getMessageProfiles() {
        return messageProfiles;
    }
}
