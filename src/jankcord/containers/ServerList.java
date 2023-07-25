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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jankcord.Jankcord;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.objects.GroupChat;
import jankcord.popups.JankGroupChat;
import jankcord.tools.ResourceLoader;
import jankcord.profiles.AddServerProfile;
import jankcord.profiles.HomeProfile;
import jankcord.profiles.ServerProfile;

public class ServerList extends JankScrollPane {
    private LinkedList<ServerProfile> serverProfiles;
    private JPanel serverPanel;
    private GridBagConstraints gbc;

    private JankGroupChat jankGroupChat;

    public ServerList() {
        super(106, Jankcord.getFrame().getHeight() - 50, 30, 50, null);
        // Init
        setName("ServerList");

        setBackground(new Color(32, 34, 37));
        getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        // Server Section
        serverPanel = new JPanel();
        setViewportView(serverPanel);

        // Server Init
        serverPanel.setBackground(getBackground());

        // Set Layout
        serverPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // Add Servers
        initChannelPanel();
        addTrailingProfiles();
    }

    private int index;
    public void initChannelPanel() {
        // Servers
        serverProfiles = new LinkedList<>();

        serverPanel.removeAll();

        index = 0;

        // Home Profile
        HomeProfile dmProfile = new HomeProfile();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        serverPanel.add(dmProfile, gbc);

        // Splitter
        JLabel splitLabel = new JLabel("����", SwingConstants.CENTER);
        splitLabel.setForeground(new Color(85, 87, 90));
        splitLabel.setPreferredSize(new Dimension(106, 20));
        splitLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);

        serverPanel.add(splitLabel, gbc);
    }

    public void addTrailingProfiles() {
        // Add Profile
        AddServerProfile addServerProfile = new AddServerProfile();

        addServerProfile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (jankGroupChat != null) {
                    jankGroupChat.dispose();
                }
                jankGroupChat = new JankGroupChat();
                jankGroupChat.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        gbc.gridx = 0;
        gbc.gridy = index + 3;
        gbc.insets = new Insets(0, 0, 30, 0);

        serverPanel.add(addServerProfile, gbc);

        serverPanel.revalidate();
        serverPanel.repaint();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addServer(GroupChat groupChat, int i) {
        ServerProfile sp = new ServerProfile(ResourceLoader.loader.getTempProfileIcon().getImage(), groupChat);
        serverProfiles.add(sp);
        gbc.gridx = 0;
        gbc.gridy = i;
        serverPanel.add(sp, gbc);

        index++;
    }
}
