package jankcord.containers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;

import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollbar.JankScrollBar;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.objects.User;
import jankcord.profiles.ChannelProfile;
import jankcord.profiles.FriendProfile;

// JankCord's channel list, child of JPanel
public class ChannelList extends JPanel {
    // Instance fields for channel list
    private JPanel channelPanel;
    private JankScrollPane channelScrollPane;
    private LinkedList<ChannelProfile> channelProfiles;
    private GridBagConstraints gbc;

    // Constructor, no parameters needed
    public ChannelList() {
        // Set JPanel properties
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

        // Add channel topbar to panel
        add(channelTopBarPanel);


        // Search label
        JLabel searchLabel = new JLabel("  Find or start a conversation");

        // Search Init
        searchLabel.setOpaque(true);
        searchLabel.setLocation(20, 25);
        searchLabel.setSize(440, 56);
        searchLabel.setBackground(new Color(32, 34, 37));
        searchLabel.setForeground(new Color(163, 166, 170));
        searchLabel.setFont(new Font("Whitney", Font.PLAIN, 25));

        // Add search label to top bar panel
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

        // Set insets
        gbc.insets = new Insets(0, 25, 3, 1);

        // Initialize channels
        initChannelPanel();

        // Add channel scroll pane to panel
        add(channelScrollPane);
    }

    /**
     * Initialize channel panel
     */
    public void initChannelPanel() {
        // Initialize channelProfiles list
        channelProfiles = new LinkedList<>();

        // Remove all channel entries
        channelPanel.removeAll();


        // Friend Profile
        FriendProfile friendProfile = new FriendProfile();

        // Update gbc layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 25, 10, 1);

        // Add friend profile
        channelPanel.add(friendProfile, gbc);


        // Splitter
        JLabel splitLabel = new JLabel("DIRECT MESSAGES");

        // Splitter init
        splitLabel.setForeground(new Color(142, 146, 151));
        splitLabel.setPreferredSize(new Dimension(420, 40));
        splitLabel.setFont(new Font("Whitney", Font.BOLD, 25));
        splitLabel.setVerticalAlignment(SwingConstants.TOP);

        // Update gbc layout
        gbc.gridx = 0;
        gbc.gridy = 1;

        // Add splitter
        channelPanel.add(splitLabel, gbc);

        // Make sure to repaint
        channelPanel.revalidate();
        channelPanel.repaint();
    }

    /**
     * Add channel entry to list
     *
     * @param friend friend channel to be added
     * @param index  which index
     */
    public void addChannel(User friend, int index) {
        // Channel Profile with friend as parameter
        ChannelProfile cp = new ChannelProfile(friend);

        // Add channelprofile to friends list
        channelProfiles.add(cp);

        // Update gbc layout
        gbc.gridx = 0;
        gbc.gridy = index;

        // Add channelprofile
        channelPanel.add(cp, gbc);


        // Check if current friend is selected already
        if (Jankcord.getOtherID().equals(friend.getId() + "")) {
            // If so, update colors
            cp.setBackground(new Color(59, 60, 66));
            cp.getUsernameLabel().setForeground(new Color(255, 255, 255));
        }

        // Make sure to repaint
        channelPanel.revalidate();
        channelPanel.repaint();
    }

    /**
     * Resets all entry's display colors
     */
    public void resetDisplays() {
        for (ChannelProfile profile : channelProfiles) {
            profile.resetDisplay();
        }
    }

    /**
     * Clears all entries from list
     */
    public void clear() {
        // Clear list
        channelPanel.removeAll();

        // Make sure to repaint
        channelPanel.revalidate();
        channelPanel.repaint();
    }

    public void showGroupChatSettings() {
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Add channelprofile
        channelPanel.add(new GroupChatSettings(), gbc);

        // Make sure to repaint
        channelPanel.revalidate();
        channelPanel.repaint();

    }

    // Getters and setters
    public JPanel getChannelPanel() {
        return channelPanel;
    }

    public void setChannelPanel(JPanel channelPanel) {
        this.channelPanel = channelPanel;
    }

    public JankScrollPane getChannelScrollPane() {
        return channelScrollPane;
    }

    public void setChannelScrollPane(JankScrollPane channelScrollPane) {
        this.channelScrollPane = channelScrollPane;
    }

    public LinkedList<ChannelProfile> getChannelProfiles() {
        return channelProfiles;
    }

    public void setChannelProfiles(LinkedList<ChannelProfile> channelProfiles) {
        this.channelProfiles = channelProfiles;
    }

    public GridBagConstraints getGbc() {
        return gbc;
    }

    public void setGbc(GridBagConstraints gbc) {
        this.gbc = gbc;
    }
}
