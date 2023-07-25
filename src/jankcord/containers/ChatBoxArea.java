package jankcord.containers;

import java.awt.*;
import java.awt.event.*;

import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.components.texts.JankTextArea;
import jankcord.popups.JankSettings;
import jankcord.tools.Base64Helper;
import jankcord.tools.ServerCommunicator;
import jankcord.texthelpers.UndoRedo;
import jankcord.objects.Message;
import jankcord.objects.User;
import jankcord.profiles.MemberProfile;
import jankcord.profiles.MessageProfile;

// JankCode's chat box area, child of JPanel
public class ChatBoxArea extends JPanel {
    // Instance fields for chat box area
    // Top bar
    private JPanel chatBoxTopBarPanel;
    private JLabel channelName;
    private JLabel settingsLabel;

    // Chat area
    private JPanel chatPanel;
    private JankScrollPane chatBoxScrollPane;
    private JPanel typePanel;
    private JankTextArea textArea;
    private JankScrollPane typeScrollPane;

    // Members area
    private JPanel membersPanel;
    private JankScrollPane membersScrollPane;

    // LinkedLists
    private LinkedList<MemberProfile> memberProfiles;
    private LinkedList<MessageProfile> messageProfiles;

    // Constraints
    private GridBagConstraints gbc;

    // Settings frame
    private JankSettings jankSettings;

    // Constructor for chat box area, no parameters required
    public ChatBoxArea() {
        // Set JPanel properties
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
        chatBoxTopBarPanel.setBackground(new Color(49, 51, 56));
        chatBoxTopBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(39, 40, 45)));

        // Add Chat Topbar
        add(chatBoxTopBarPanel);


        // Channel name
        channelName = new JLabel();

        // Channel name Init
        channelName.setLocation(50, 20);
        channelName.setBackground(new Color(56, 58, 64));
        channelName.setForeground(new Color(255, 255, 255));
        channelName.setFont(new Font("Whitney", Font.BOLD, 45));

        // Add Channel name to top bar
        chatBoxTopBarPanel.add(channelName);


        // Settings label
        settingsLabel = new JLabel(" Account ⚙ ", SwingConstants.CENTER);

        // Settings label Init
        settingsLabel.setOpaque(true);
        settingsLabel.setBackground(null);
        settingsLabel.setForeground(new Color(255, 255, 255));
        settingsLabel.setFont(new Font("Whitney", Font.BOLD, 45));
        settingsLabel.setLocation(chatBoxTopBarPanel.getWidth() - 320, 20);
        settingsLabel.setSize((int) (settingsLabel.getPreferredSize().getWidth() + 40), (int) settingsLabel.getPreferredSize().getHeight());

        // Add settings label mouse listener
        settingsLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            // Mouse pressed
            @Override
            public void mousePressed(MouseEvent e) {
                // Update background color
                settingsLabel.setBackground(new Color(0, 0, 0));
            }

            // Mouse released
            @Override
            public void mouseReleased(MouseEvent e) {
                // If jankSettings exists
                if (jankSettings != null) {
                    // Dispose/destroy it
                    jankSettings.dispose();
                }

                // Create new instance of JankSettings
                jankSettings = new JankSettings();
                // Make it visible
                jankSettings.setVisible(true);

                // Update background color
                settingsLabel.setBackground(new Color(45, 45, 45));
            }

            // Mouse entered
            @Override
            public void mouseEntered(MouseEvent e) {
                // Update background color
                settingsLabel.setBackground(new Color(45, 45, 45));
            }

            // Mouse exited
            @Override
            public void mouseExited(MouseEvent e) {
                // Update background color
                settingsLabel.setBackground(null);
            }
        });

        // Add settings label to top bar
        chatBoxTopBarPanel.add(settingsLabel);


        // Chat Section
        chatPanel = new JPanel();

        // Chat Section Init
        chatPanel.setBackground(new Color(49, 51, 56));
        chatBoxScrollPane = new JankScrollPane(getWidth() - 540, 1168, 0, 106, chatPanel);

        // Add chat section
        add(chatBoxScrollPane);

        // Type Section
        typePanel = new JPanel();

        // Type Section init
        typePanel.setOpaque(true);
        typePanel.setBorder(null);
        typePanel.setLayout(null);
        typePanel.setBackground(new Color(52, 54, 60));
        typePanel.setSize(chatBoxScrollPane.getWidth() - 60, 73);
        typePanel.setLocation(30, getHeight() - 120);

        // Add Type Section
        add(typePanel);


        // Text area
        textArea = new JankTextArea();

        // text area Init
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("Write a Message");
        textArea.setBackground(new Color(52, 54, 60));
        textArea.setForeground(new Color(255, 255, 255));
        textArea.setFont(new Font("Whitney", Font.PLAIN, 28));

        // Allow undo redo in textarea
        UndoRedo.makeUndoable(textArea);

        // Add text area key listener
        textArea.addKeyListener(new KeyListener() {
            // Key listener
            @Override
            public void keyPressed(KeyEvent e) {
                // If enter key and not shifting
                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (!e.isShiftDown())) {
                    // Ignore enter press
                    e.consume();
                }

                // Reline text area
                reline();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            // Key released
            @Override
            public void keyReleased(KeyEvent e) {
                // If enter key and not shifting
                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (!e.isShiftDown())) {
                    // If textarea is blank
                    if (textArea.getText().isBlank()) {
                        // Return, don't send http request
                        return;
                    }

                    // Get content and encode in base 64
                    String content = Base64Helper.encode(textArea.getText().trim());

                    // Reset text area
                    textArea.setText("");

                    // Set headers; login information, chat id, and content
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("username", Jankcord.getFullUser().getUsername());
                    headers.put("password", Jankcord.getFullUser().getPassword());
                    headers.put("otherID", Jankcord.getOtherID());
                    headers.put("content", content);

                    // Determine where to send this request to
                    String dest = "sendmessage"; // Default dm end point
                    // If in server
                    if (Jankcord.isInServer()) {
                        // Set dest to server end point
                        dest = "sendgroupmessage";
                    }

                    // Send http request with headers to end point
                    String response = ServerCommunicator.sendHttpRequest(Jankcord.getFullUser().getEndPointHost() + dest, headers);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) { // If enter and shit
                    // Insert new line at text area caret
                    textArea.insert("\n", textArea.getCaretPosition());
                }

                // Reline text area
                reline();
            }
        });


        // Text area's scroll pane
        typeScrollPane = new JankScrollPane(typePanel.getWidth() - 40, (int) (Math.floor(typePanel.getHeight() / 37) * 37), 20, 24, textArea);

        // Text area's scroll pane Init
        typeScrollPane.setMultiplier(25);
        typeScrollPane.setBackground(new Color(56, 58, 64));
        typeScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        // Add scrollpane to type section
        typePanel.add(typeScrollPane);


        // Members Section
        membersPanel = new JPanel();

        // Members section init
        membersPanel.setBackground(new Color(43, 45, 49));


        // MemberList
        membersScrollPane = new JankScrollPane(540, getHeight() - 106, chatBoxScrollPane.getWidth() + 5, 106, membersPanel);

        // MemberList Init
        membersScrollPane.setBackground(new Color(43, 45, 49));
        membersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        membersScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        // Add MemberList
        add(membersScrollPane);


        // Set layout
        membersPanel.setLayout(new GridBagLayout());

        // Initialize constraints
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

    /**
     * Checks how many lines in a text field
     *
     * @param textArea text area to check
     * @return number of lines
     */
    public int getNumberOfLines(JTextArea textArea) {
        // Calculate number of lines and return
        return (int) (textArea.getPreferredSize().getHeight() / textArea.getFontMetrics(textArea.getFont()).getHeight());
    }

    /**
     * Resizes all components according to text area input
     */
    public void reline() {
        // Get number of lines and calculate required height
        int h = getNumberOfLines(textArea) * 37 + 36;

        // If height is under max height
        if (h <= 500) {
            // Set typePanel size and location
            typePanel.setSize(chatBoxScrollPane.getWidth() - 60, h);
            typePanel.setLocation(typePanel.getX(), (int) (getHeight() - ((Math.floor(typePanel.getHeight() / 37) * 37) + 83)));

            // Set typeScrollPane size and scroll amount
            typeScrollPane.setSize(typePanel.getWidth() - 40, (int) (Math.floor(typePanel.getHeight() / 37) * 37));
            typeScrollPane.getVerticalScrollBar().setValue(0);

            // Set chatBoxScrollPane size
            chatBoxScrollPane.setSize(chatBoxScrollPane.getWidth(), getHeight() - (25 + 114 + typePanel.getHeight()));
        }
    }

    // Index of next message index
    private int index = 0;

    /**
     * Adds new message listing to chat box
     *
     * @param message message entry to add to chat box list
     */
    public void addMessage(Message message) {
        // Message profile, pass in message as parameter
        MessageProfile mp = new MessageProfile(message);

        // Add message profile to arraylist
        messageProfiles.add(mp);

        // Update gbc layout
        gbc.gridx = 0;
        gbc.gridy = index;

        // Add message profile to list
        chatPanel.add(mp, gbc);

        // Make sure to repaint
        chatPanel.revalidate();
        chatPanel.repaint();

        // Reline
        reline();

        // Increment index
        index++;
    }

    /**
     * Adds new member listing to members list
     *
     * @param member member entry to add to members list
     * @param index  index to insert member
     */
    public void addMember(User member, int index) {
        // Member profile, pass in member as parameter
        MemberProfile cp = new MemberProfile(member);

        // Add member profile to arraylist
        memberProfiles.add(cp);

        // Update gbc layout
        gbc.gridx = 0;
        gbc.gridy = index;

        // Add member profile to list
        membersPanel.add(cp, gbc);

        // Make sure to repaint
        membersPanel.revalidate();
        membersPanel.repaint();
    }

    /**
     * Resets messages list
     */
    public void resetMessages() {
        // Clear panel and arraylist
        chatPanel.removeAll();
        messageProfiles.clear();

        // Make sure to repaint
        chatPanel.revalidate();
        chatPanel.repaint();

        // Reset index
        index = 0;
    }

    /**
     * Resets members list
     */
    public void resetMembers() {
        // Clear panel and arraylist
        membersPanel.removeAll();
        memberProfiles.clear();

        // Make sure to repaint
        membersPanel.revalidate();
        membersPanel.repaint();
    }

    /**
     * Set maximum scroll on new message
     */
    public void setMaxChatScroll() {
        // Invoke later in case JSwing is still working
        SwingUtilities.invokeLater(() -> {
            // If chat is in a new place
            if (Jankcord.isNewOtherID()) {
                // Get chat box view port
                JViewport viewport = chatBoxScrollPane.getViewport();
                // Get bottom
                Point bottom = new Point(0, chatPanel.getHeight());
                // Set view to bottom
                viewport.setViewPosition(bottom);

                // Set is in new place to false
                Jankcord.setNewOtherID(false);
            } else {
                // Otherwise, smooth scroll to bottom
                chatBoxScrollPane.smoothScrollBottom();
            }
        });
    }

    /**
     * Resets all message widths, helpful when windows resize
     */
    public void resetMessageWidths() {
        // Invoke later in case JSwing is working
        SwingUtilities.invokeLater(() -> {
            // Loop through all message profiles
            for (MessageProfile mp : messageProfiles) {
                // Update width
                mp.updateMessageWidth();
            }
        });
    }

    // Getters and setters
    public JPanel getChatBoxTopBarPanel() {
        return chatBoxTopBarPanel;
    }

    public void setChatBoxTopBarPanel(JPanel chatBoxTopBarPanel) {
        this.chatBoxTopBarPanel = chatBoxTopBarPanel;
    }

    public JLabel getChannelName() {
        return channelName;
    }

    public void setChannelName(JLabel channelName) {
        this.channelName = channelName;
    }

    public void setChannelName(String chatName) {
        channelName.setText(chatName);
        // Update size to reflect name length
        channelName.setSize(channelName.getPreferredSize());
    }

    public JLabel getSettingsLabel() {
        return settingsLabel;
    }

    public void setSettingsLabel(JLabel settingsLabel) {
        this.settingsLabel = settingsLabel;
    }

    public JPanel getChatPanel() {
        return chatPanel;
    }

    public void setChatPanel(JPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

    public JankScrollPane getChatBoxScrollPane() {
        return chatBoxScrollPane;
    }

    public void setChatBoxScrollPane(JankScrollPane chatBoxScrollPane) {
        this.chatBoxScrollPane = chatBoxScrollPane;
    }

    public JPanel getTypePanel() {
        return typePanel;
    }

    public void setTypePanel(JPanel typePanel) {
        this.typePanel = typePanel;
    }

    public JankTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JankTextArea textArea) {
        this.textArea = textArea;
    }

    public JankScrollPane getTypeScrollPane() {
        return typeScrollPane;
    }

    public void setTypeScrollPane(JankScrollPane typeScrollPane) {
        this.typeScrollPane = typeScrollPane;
    }

    public JPanel getMembersPanel() {
        return membersPanel;
    }

    public void setMembersPanel(JPanel membersPanel) {
        this.membersPanel = membersPanel;
    }

    public JankScrollPane getMembersScrollPane() {
        return membersScrollPane;
    }

    public void setMembersScrollPane(JankScrollPane membersScrollPane) {
        this.membersScrollPane = membersScrollPane;
    }

    public LinkedList<MemberProfile> getMemberProfiles() {
        return memberProfiles;
    }

    public void setMemberProfiles(LinkedList<MemberProfile> memberProfiles) {
        this.memberProfiles = memberProfiles;
    }

    public LinkedList<MessageProfile> getMessageProfiles() {
        return messageProfiles;
    }

    public void setMessageProfiles(LinkedList<MessageProfile> messageProfiles) {
        this.messageProfiles = messageProfiles;
    }

    public GridBagConstraints getGbc() {
        return gbc;
    }

    public void setGbc(GridBagConstraints gbc) {
        this.gbc = gbc;
    }

    public JankSettings getJankSettings() {
        return jankSettings;
    }

    public void setJankSettings(JankSettings jankSettings) {
        this.jankSettings = jankSettings;
    }

    public int getMessageIndex() {
        return index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
