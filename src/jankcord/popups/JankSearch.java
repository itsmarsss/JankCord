package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.frame.JankFrame;
import jankcord.components.list.JankListSearch;
import jankcord.components.list.listitem.JankListItem;
import jankcord.components.scrollbar.JankScrollBar;
import jankcord.components.scrollpane.JankScrollPane;
import jankcord.components.texts.JankTextField;
import jankcord.objects.GroupChat;
import jankcord.objects.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Popup window for user to search message places
public class JankSearch extends JankFrame {
    // Constructor to create JankSearch
    public JankSearch() {
        // Super; set name, size, and window controls
        super("JankCord Search", 700, 950, false);


        // Search Label
        JLabel searchLabel = new JLabel("Search Term:");

        // Search Label Init
        searchLabel.setLocation(50, 100);
        searchLabel.setSize(200, 30);
        searchLabel.setForeground(new Color(114, 118, 125));
        searchLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Search Label
        getContentPane().add(searchLabel);


        // Search Input
        JankTextField searchInput = new JankTextField(600, 45, 50, 150);

        // Add Search Input
        getContentPane().add(searchInput);

        // Results
        JLabel resultsLabel = new JLabel("Results:");

        // Results Init
        resultsLabel.setLocation(50, 250);
        resultsLabel.setSize(250, 30);
        resultsLabel.setForeground(new Color(114, 118, 125));
        resultsLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Results
        getContentPane().add(resultsLabel);

        // List of results
        DefaultListModel<JankListItem> list = new DefaultListModel<>();

        // Loop through all friends
        for (User friend : Jankcord.getTempFriends()) {
            // If friend is self
            if ((friend.getId() + "").equals(Jankcord.getFullUser().getId() + "")) {
                // Skip to next
                continue;
            }

            // Otherwise add to list
            list.addElement(new JankListItem(friend.getUsername(), friend.getId() + "", false));
        }

        // Loop through all group chats
        for (GroupChat groupChat : Jankcord.getTempGroupChats()) {
            // Add to list
            list.addElement(new JankListItem(groupChat.getChatName(), groupChat.getId(), true));
        }


        // Results List
        JankListSearch resultsList = new JankListSearch(list);


        // User List Scroll
        JankScrollPane userListScroll = new JankScrollPane(600, 600, 50, 300, resultsList);

        // User List Scroll Init
        userListScroll.setMultiplier(25);
        userListScroll.getVerticalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(0, 0, 0), true));

        // Add User List Scroll
        getContentPane().add(userListScroll);

        // Add key listener search input
        searchInput.addKeyListener(new KeyAdapter() {
            // Key released
            @Override
            public void keyReleased(KeyEvent e) {
                // Get search term
                String searchTerm = searchInput.getText().toLowerCase();

                // Clear list
                list.clear();

                // Loop through all friends
                for (User friend : Jankcord.getTempFriends()) {
                    // If friend is self
                    if ((friend.getId() + "").equals(Jankcord.getFullUser().getId() + "")) {
                        // Skip to next
                        continue;
                    }

                    // If item contains search term
                    if (("@ " + friend.getUsername().toLowerCase()).contains(searchTerm)) {
                        // Add to list
                        list.addElement(new JankListItem(friend.getUsername(), friend.getId() + "", false));
                    }
                }

                // Loop through all group chats
                for (GroupChat groupChat : Jankcord.getTempGroupChats()) {
                    // If item contains search term
                    if (("# " + groupChat.getChatName().toLowerCase()).contains(searchTerm)) {
                        // Add to list
                        list.addElement(new JankListItem(groupChat.getChatName(), groupChat.getId(), true));
                    }
                }
            }
        });

        // Add mouse listener to results list
        resultsList.addMouseListener(new MouseAdapter() {
            // Mouse release
            @Override
            public void mouseReleased(MouseEvent e) {
                // Dispose window on selection
                dispose();
            }
        });
    }
}
