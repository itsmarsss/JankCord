package jankcord.containers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollbar.JankScrollBar;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.objects.User;
import jankcord.popups.RequestGroupChat;
import jankcord.profiles.ChannelProfile;
import jankcord.profiles.FriendProfile;

public class ChannelList extends JPanel {
    private final JPanel channelPanel;
    private final JankScrollPane channelScrollPane;
    private LinkedList<ChannelProfile> channelProfiles;
    private final GridBagConstraints gbc;

    private RequestGroupChat requestGroupChat;

    public ChannelList() {
        // Init
        setName("ChannelList");

        setOpaque(true);
        setBorder(null);
        setLayout(null);
        setLocation(166, 50);
        setBackground(new Color(43, 45, 49));
        setSize(480, Jankcord.getViewPanel().getHeight() - 50);

        // Channel TopBar
        JPanel channelTopBarPanel = new JPanel();
        // Channel TopBar Init
        channelTopBarPanel.setLayout(null);
        channelTopBarPanel.setSize(480, 106);
        channelTopBarPanel.setBackground(new Color(43, 45, 49));
        channelTopBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(39, 40, 45)));

        add(channelTopBarPanel);

        // Search
        JLabel searchLabel = new JLabel("  Find or start a conversation");

        // Search Init
        searchLabel.setOpaque(true);
        searchLabel.setLocation(20, 25);
        searchLabel.setSize(440, 56);
        searchLabel.setBackground(new Color(32, 34, 37));
        searchLabel.setForeground(new Color(163, 166, 170));
        searchLabel.setFont(new Font("Whitney", Font.PLAIN, 25));

        channelTopBarPanel.add(searchLabel);

        // Channel Section
        channelPanel = new JPanel();
        channelPanel.setBackground(new Color(43, 45, 49));
        channelScrollPane = new JankScrollPane(477, getHeight() - 110, 0, 106, channelPanel);

        // Channel Init
        channelScrollPane.setBackground(new Color(43, 45, 49));
        channelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        channelScrollPane.getVerticalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(32, 34, 37), false));

        // Set Layout
        channelPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // Channels
        initChannelPanel();

        gbc.insets = new Insets(0, 25, 3, 1);

        add(channelScrollPane);
    }

    public void addChannel(User friend, int index) {
        ChannelProfile cp = new ChannelProfile(friend);
        channelProfiles.add(cp);
        gbc.gridx = 0;
        gbc.gridy = index;

        channelPanel.add(cp, gbc);

        channelPanel.revalidate();
        channelPanel.repaint();
    }

    public void initChannelPanel() {
        channelProfiles = new LinkedList<>();

        channelPanel.removeAll();

        // Friend Profile
        FriendProfile friendProfile = new FriendProfile();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 25, 10, 1);

        channelPanel.add(friendProfile, gbc);

        // Splitter
        JLabel splitLabel = new JLabel("DIRECT MESSAGES                  ➕");
        splitLabel.setForeground(new Color(142, 146, 151));
        splitLabel.setPreferredSize(new Dimension(420, 40));
        splitLabel.setFont(new Font("Whitney", Font.BOLD, 25));
        splitLabel.setVerticalAlignment(SwingConstants.TOP);
        splitLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                splitLabel.setBackground(new Color(255, 255, 255));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                splitLabel.setForeground(new Color(219, 222, 225));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                splitLabel.setForeground(new Color(142, 146, 151));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (requestGroupChat != null) {
                    requestGroupChat.dispose();
                }
                requestGroupChat = new RequestGroupChat();
                requestGroupChat.setVisible(true);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;

        channelPanel.add(splitLabel, gbc);

        channelPanel.revalidate();
        channelPanel.repaint();
    }

    public void resetDisplays() {
        for (ChannelProfile profile : channelProfiles) {
            profile.resetDisplay();
        }
    }

    public JankScrollPane getChannelScrollPane() {
        return channelScrollPane;
    }
}
