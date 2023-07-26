package jankcord.profiles;

import jankcord.Jankcord;
import jankcord.objects.GroupChat;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

// Server Profile, child of JLabel; group chat/server entry
public class ServerProfile extends JLabel {
    // Instance field
    private GroupChat groupChat;

    // Constructor to set serverIcon and groupChat
    public ServerProfile(Image serverIcon, GroupChat groupChat) {
        // Set field
        this.groupChat = groupChat;

        // Get group chat icon or default icon
        Image scaledIcon = serverIcon.getScaledInstance(106, 106, Image.SCALE_FAST);

        // Set JLabel properties
        setSize(96, 96);
        setIcon(new ImageIcon(scaledIcon));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener
        addMouseListener(new MouseAdapter() {
            // Mouse release
            @Override
            public void mouseReleased(MouseEvent e) {
                // If current text place isn't this text place
                if (!Jankcord.getOtherID().equals(groupChat.getId())) {
                    // Update text place to this one
                    Jankcord.setOtherID(groupChat.getId());

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
                    Jankcord.getChatBoxArea().setChannelName("# " + groupChat.getChatName());
                }
            }
        });
    }

    // Getters and setters
    public GroupChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }
}
