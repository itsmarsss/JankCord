package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.Jankcord;
import jankcord.tools.ResourceLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// HomeProfile, child of JLabel; home listing
public class HomeProfile extends JLabel {
    // Constructor to create home listing
    public HomeProfile() {
        // Set icon to home profile icon
        setIcon(ResourceLoader.loader.getHomeProfileIcon());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            // Mouse release
            @Override
            public void mouseReleased(MouseEvent e) {
                // Reset channel name; tell user to select channel
                Jankcord.getChatBoxArea().setChannelName("~ Select a channel.");

                // Reset text place
                Jankcord.setOtherID("");

                // Notify text place switch
                Jankcord.setNewOtherID(true);
                Jankcord.setInServer(false);
                Jankcord.setInServerCheck(true);

                // Reset all messages
                Jankcord.getChatBoxArea().resetMessages();

                // Query for new messages
                Jankcord.queryForNewMessages();

                // Reinitialize channel list
                Jankcord.getChannelList().initChannelPanel();
                Jankcord.getChannelList().resetDisplays();

                // Query for new friends
                Jankcord.queryForNewFriend();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
}
