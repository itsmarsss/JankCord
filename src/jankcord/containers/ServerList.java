package jankcord.containers;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.*;

import jankcord.Jankcord;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.objects.GroupChat;
import jankcord.objects.SimpleUserCache;
import jankcord.popups.JankGroupChat;
import jankcord.tools.ResourceLoader;
import jankcord.profiles.AddServerProfile;
import jankcord.profiles.HomeProfile;
import jankcord.profiles.ServerProfile;

// JankCord's server list, child of JankScrollPane
public class ServerList extends JankScrollPane {
    // Instance fields of server list
    private LinkedList<ServerProfile> serverProfiles;
    private JPanel serverPanel;

    // Constraints
    private GridBagConstraints gbc;

    // Groupchat frame
    private JankGroupChat jankGroupChat;

    // Constructor, no parameters needed
    public ServerList() {
        // Super; set JankScrollPane size and location
        super(106, Jankcord.getFrame().getHeight() - 50, 30, 50, null);

        // Set JankScrollPane properties
        setBackground(new Color(32, 34, 37));
        getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        // Server Section
        serverPanel = new JPanel();

        // Set serverPanel as viewport
        setViewportView(serverPanel);

        // Server Init
        serverPanel.setBackground(getBackground());

        // Set Layout
        serverPanel.setLayout(new GridBagLayout());

        // Initialize gbc constraints
        gbc = new GridBagConstraints();

        // Set remaining JankScrollPane properties
        initServerPanel();

        // Add ending server profiles
        addTrailingProfiles();
    }

    // Index of next server
    private int index;

    /**
     * Sets remaining JankScrollPane properties
     */
    public void initServerPanel() {
        // Initialize server profiles list
        serverProfiles = new LinkedList<>();

        // Remove all entries
        serverPanel.removeAll();

        // Reset index
        index = 0;


        // Home Profile
        HomeProfile dmProfile = new HomeProfile();

        // Update gbc loyout
        gbc.gridx = 0;
        gbc.gridy = 0;
        // Set insets
        gbc.insets = new Insets(10, 0, 10, 0);

        // Add home profile to server panel
        serverPanel.add(dmProfile, gbc);


        // Splitter
        JLabel splitLabel = new JLabel("____", SwingConstants.CENTER);

        // Splitter init
        splitLabel.setForeground(new Color(85, 87, 90));
        splitLabel.setPreferredSize(new Dimension(106, 20));
        splitLabel.setFont(new Font("Arial", Font.BOLD, 15));

        // Update gbc layout
        gbc.gridx = 0;
        gbc.gridy = 1;
        // Set insets
        gbc.insets = new Insets(0, 0, 20, 0);

        // Add splitter to serverPanel
        serverPanel.add(splitLabel, gbc);
    }

    /**
     * Add all trailing profiles like AddServerProfile
     */
    public void addTrailingProfiles() {
        // Add Profile
        AddServerProfile addServerProfile = new AddServerProfile();

        // Add mouse listener to add server profile
        addServerProfile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            // Mouse released
            @Override
            public void mouseReleased(MouseEvent e) {
                // If jank group chat exists
                if (jankGroupChat != null) {
                    // Dispose/destroy it
                    jankGroupChat.dispose();
                }

                // Create new instance of JankSettings
                jankGroupChat = new JankGroupChat();
                // Make it visible
                jankGroupChat.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        // Update gbc layout
        gbc.gridx = 0;
        gbc.gridy = index + 3;
        // Set insets
        gbc.insets = new Insets(0, 0, 30, 0);

        // Add add server profile to panel
        serverPanel.add(addServerProfile, gbc);

        // Make sure to repaint
        serverPanel.revalidate();
        serverPanel.repaint();
    }

    /**
     * Adds new group chat entry to server list
     *
     * @param groupChat group chat to add to list
     * @param index     index of insertion
     */
    public void addServer(GroupChat groupChat, int index) {
        Image icon = ResourceLoader.loader.getTempProfileIcon().getImage();

        // Try to load image
        try {
            // URL of icon url
            URL url = new URL(groupChat.getChatIconURL());

            // Read image
            BufferedImage image = ImageIO.read(url);

            // Set icon to read image
            icon = new ImageIcon(image).getImage();
        } catch (Exception e) {
        }

        // Server profile with icon and groupchat as parameters
        ServerProfile sp = new ServerProfile(SimpleUserCache.circularize(icon), groupChat);

        // Add serverProfiles to arraylist
        serverProfiles.add(sp);

        // Set gbc layout
        gbc.gridx = 0;
        gbc.gridy = index;

        // Add server profile to list
        serverPanel.add(sp, gbc);

        // Increment list index, will be used for trailing profiles
        this.index++;
    }

    // Getters and setters
    public LinkedList<ServerProfile> getServerProfiles() {
        return serverProfiles;
    }

    public void setServerProfiles(LinkedList<ServerProfile> serverProfiles) {
        this.serverProfiles = serverProfiles;
    }

    public JPanel getServerPanel() {
        return serverPanel;
    }

    public void setServerPanel(JPanel serverPanel) {
        this.serverPanel = serverPanel;
    }

    public GridBagConstraints getGbc() {
        return gbc;
    }

    public void setGbc(GridBagConstraints gbc) {
        this.gbc = gbc;
    }

    public JankGroupChat getJankGroupChat() {
        return jankGroupChat;
    }

    public void setJankGroupChat(JankGroupChat jankGroupChat) {
        this.jankGroupChat = jankGroupChat;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
