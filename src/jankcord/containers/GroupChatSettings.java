package jankcord.containers;

import jankcord.Jankcord;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.texts.JankTextField;
import jankcord.objects.GroupChat;
import jankcord.objects.SimpleUserCache;
import jankcord.tools.ResourceLoader;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.JankcordAdmin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;

public class GroupChatSettings extends JPanel {
    public GroupChatSettings() {
        setLayout(null);
        setBackground(null);
        setLocation(0, 0);
        setPreferredSize(new Dimension(400, 700));


        JLabel iconLabel = new JLabel();

        iconLabel.setLocation(5, 100);
        iconLabel.setSize(200, 200);
        iconLabel.setLocation(90, 0);

        add(iconLabel);


        JLabel chatNameLabel = new JLabel("Chat Name:");

        chatNameLabel.setLocation(5, 250);
        chatNameLabel.setSize(250, 30);
        chatNameLabel.setForeground(new Color(114, 118, 125));
        chatNameLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        add(chatNameLabel);


        JankTextField chatNameInput = new JankTextField(390, 40, 0, 300);

        add(chatNameInput);


        JLabel iconURLLabel = new JLabel("Icon URL:");

        iconURLLabel.setLocation(5, 400);
        iconURLLabel.setSize(250, 30);
        iconURLLabel.setForeground(new Color(114, 118, 125));
        iconURLLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        add(iconURLLabel);


        JankTextField iconInput = new JankTextField(390, 40, 0, 450);

        for(GroupChat gc : Jankcord.getTempGroupChats()) {
            if(gc.getId().equals(Jankcord.getOtherID())) {
                chatNameInput.setText(gc.getChatName());
                iconInput.setText(gc.getChatIconURL());
            }
        }

        add(iconInput);


        JLabel statusLabel = new JLabel();

        JankButton previewButton = new JankButton("Preview", 390, 45, 0, 500);

        // Edit mouse release listener
        previewButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                // Set avatar to temp avatar for now
                Image avatar = ResourceLoader.loader.getTempProfileIcon().getImage();

                // Try to get user profile
                try {
                    // URL of avatar
                    URL url = new URL(iconInput.getText());

                    // Read image and circularize it, set to avatar
                    avatar = SimpleUserCache.circularize(ImageIO.read(url));
                } catch (Exception e) {
                    // If error, statusLabel display it
                    statusLabel.setText("Error getting avatar.");
                }

                // Scale image to fit iconLabel
                avatar = avatar.getScaledInstance(200, 200, Image.SCALE_FAST);

                // Set icon
                iconLabel.setIcon(new ImageIcon(avatar));
            }
        });

        // Add Preview
        add(previewButton);


        statusLabel.setLocation(0, 615);
        statusLabel.setSize(390, 30);
        statusLabel.setForeground(new Color(237, 66, 69));
        statusLabel.setFont(new Font("Whitney", Font.BOLD, 20));

        add(statusLabel);


        // Update
        JankButton updateGroupChat = new JankButton("Update Group Chat", 390, 50, 0, 650);

        // Edit mouse release listener
        updateGroupChat.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                String newChatName = chatNameInput.getText();

                if(ServerCommunicator.notHeaderable(newChatName)) {
                    statusLabel.setText("Chat Name: ASCII, < 30 character.");
                    return;
                }

                // Set headers; login information, new login information, and avatarURL
                HashMap<String, String> headers = new HashMap<>();
                headers.put("username", Jankcord.getFullUser().getUsername());
                headers.put("password", Jankcord.getFullUser().getPassword());

                headers.put("chatID", Jankcord.getOtherID());
                headers.put("chatName", newChatName);
                headers.put("chatIconURL", iconInput.getText());

                // Send http request with headers to end point
                String response = ServerCommunicator.sendHttpRequest(Jankcord.getFullUser().getEndPointHost() + "editgroupchat", headers);

                // If null
                if (response == null) {
                    // Error contacting
                    statusLabel.setText("Error contacting server.");

                    // Set text and color to offline and red
                    Jankcord.getTitleLabel().setText("JankCord - OFFLINE");
                    Jankcord.getTitleLabel().setForeground(new Color(198, 36, 36));

                    // Return
                    return;
                }
                // Otherwise set text to normal and grey
                Jankcord.getTitleLabel().setText("JankCord");
                Jankcord.getTitleLabel().setForeground(new Color(114, 118, 125));
            }
        });

        add(updateGroupChat);

        // Just preview button load in avatar
        previewButton.getMouseListener().getMouseReleased().run();

    }
}
