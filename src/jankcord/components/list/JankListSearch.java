package jankcord.components.list;

import jankcord.Jankcord;
import jankcord.components.list.listitem.JankListItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// JankCord's search list, child of JList
public class JankListSearch extends JList {
    // Constructor, with list of objects
    public JankListSearch(DefaultListModel<JankListItem> list) {
        // Super; set list object
        super(list);

        // Set Jlist properties
        setBackground(new Color(43, 45, 49));
        setForeground(new Color(219, 222, 225));
        setFont(new Font("Whitney", Font.BOLD, 30));
        setSelectionBackground(new Color(71, 82, 196));
        setSelectionForeground(new Color(255, 255, 255));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add mouse listener; action when list item is being selected
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JankListItem selection = list.get(getSelectedIndex());
                if (selection.isGroupChat()) {
                    // If current text place isn't this text place
                    if (!Jankcord.getOtherID().equals(selection.getOtherId())) {
                        // Update text place to this one
                        Jankcord.setOtherID(selection.getOtherId());

                        // Notify of text place update
                        Jankcord.setNewOtherID(true);
                        Jankcord.setInServer(true);

                        // Reset all messages
                        Jankcord.getChatBoxArea().resetMessages();

                        // Query for new messages
                        Jankcord.queryForNewMessages();

                        // Clear channel list
                        Jankcord.getChannelList().clear();

                        // Show group chat settings list
                        Jankcord.getChannelList().showGroupChatSettings();

                        // Update channel name
                        Jankcord.getChatBoxArea().setChannelName("# " + selection.getDisplayName());
                    } else {
                        Jankcord.setOtherID(selection.getOtherId() + "");

                        // Inform new text place
                        Jankcord.setNewOtherID(true);
                        Jankcord.setInServer(false);

                        // Reset messages
                        Jankcord.getChatBoxArea().resetMessages();

                        // Query for new messages
                        Jankcord.queryForNewMessages();

                        // Reset channel list display
                        Jankcord.getChannelList().resetDisplays();

                        // Set channel name
                        Jankcord.getChatBoxArea().setChannelName("@ " + selection.getDisplayName());
                    }
                }
            }
        });
    }
}
