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

public class JankSearch extends JankFrame {
    public JankSearch() {
        super("JankCord Search", 700, 1000, false);

        JLabel searchLabel = new JLabel("Search Term:");

        searchLabel.setLocation(50, 100);
        searchLabel.setSize(200, 30);
        searchLabel.setForeground(new Color(114, 118, 125));
        searchLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        getContentPane().add(searchLabel);


        JankTextField searchInput = new JankTextField(600, 45, 50, 150);

        getContentPane().add(searchInput);


        JLabel resultsLabel = new JLabel("Results:");

        resultsLabel.setLocation(50, 250);
        resultsLabel.setSize(250, 30);
        resultsLabel.setForeground(new Color(114, 118, 125));
        resultsLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        getContentPane().add(resultsLabel);

        DefaultListModel<JankListItem> list = new DefaultListModel<>();

        for (User friend : Jankcord.getTempFriends()) {
            list.addElement(new JankListItem(friend.getUsername(), friend.getId() + "", false));
        }

        for (GroupChat groupChat : Jankcord.getTempGroupChats()) {
            list.addElement(new JankListItem(groupChat.getChatName(), groupChat.getId(), true));
        }

        JankListSearch resultsList = new JankListSearch(list);


        // User List Scroll
        JankScrollPane userListScroll = new JankScrollPane(600, 600, 50, 300, resultsList);

        // User List Scroll Init
        userListScroll.setMultiplier(25);
        userListScroll.getVerticalScrollBar().setUI(new JankScrollBar(new Color(46, 51, 56), new Color(0, 0, 0), true));

        // Add User List Scroll
        getContentPane().add(userListScroll);
    }
}
