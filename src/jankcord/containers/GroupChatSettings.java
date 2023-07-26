package jankcord.containers;

import jankcord.Jankcord;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.texts.JankTextField;
import jankcord.objects.GroupChat;
import jankcord.objects.SimpleUserCache;
import jankcord.tools.ResourceLoader;
import jankcord.tools.ServerCommunicator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;

// JankCord's group chat settings, child of JPanel
public class GroupChatSettings extends JPanel {
    // Constructor to create gc settings
    public GroupChatSettings() {
        // Set JPanel properties
        setLayout(null);
        setBackground(null);
        setLocation(0, 0);
        setPreferredSize(new Dimension(400, 700));


        // Icon Label
        JLabel iconLabel = new JLabel();

        // Icon Label Init
        iconLabel.setLocation(5, 100);
        iconLabel.setSize(200, 200);
        iconLabel.setLocation(90, 0);

        // Add Icon Label
        add(iconLabel);


        // Chat Name
        JLabel chatNameLabel = new JLabel("Chat Name:");

        // Chat Name Init
        chatNameLabel.setLocation(5, 250);
        chatNameLabel.setSize(250, 30);
        chatNameLabel.setForeground(new Color(114, 118, 125));
        chatNameLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Chat Name
        add(chatNameLabel);


        // Chat Name Input
        JankTextField chatNameInput = new JankTextField(390, 40, 0, 300);

        // Add Chat Name Input
        add(chatNameInput);


        // Icon URL
        JLabel iconURLLabel = new JLabel("Icon URL:");

        // Icon URL Init
        iconURLLabel.setLocation(5, 400);
        iconURLLabel.setSize(250, 30);
        iconURLLabel.setForeground(new Color(114, 118, 125));
        iconURLLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Icon URL
        add(iconURLLabel);


        // Icon Input
        JankTextField iconInput = new JankTextField(390, 40, 0, 450);

        // Loop through all group chats
        for (GroupChat gc : Jankcord.getTempGroupChats()) {
            // If ID matches
            if (gc.getId().equals(Jankcord.getOtherID())) {
                // Set name and icon url
                chatNameInput.setText(gc.getChatName());
                iconInput.setText(gc.getChatIconURL());
            }
        }

        // Add icon input
        add(iconInput);


        // Status
        JLabel statusLabel = new JLabel();


        // Preview
        JankButton previewButton = new JankButton("Preview", 390, 45, 0, 500);

        // Edit mouse release listener
        previewButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                // Set icon to temp icon for now
                Image icon = ResourceLoader.loader.getTempProfileIcon().getImage();

                // Try to get inputted URL
                try {
                    // URL of icon
                    URL url = new URL(iconInput.getText());

                    // Read image and circularize it, set to icon
                    icon = SimpleUserCache.circularize(ImageIO.read(url));

                    // Reset status label
                    statusLabel.setText("");
                } catch (Exception e) {
                    // If error, statusLabel display it
                    statusLabel.setText("Error getting icon.");
                }

                // Scale image to fit iconLabel
                icon = icon.getScaledInstance(200, 200, Image.SCALE_FAST);

                // Set icon
                iconLabel.setIcon(new ImageIcon(icon));
            }
        });

        // Add Preview
        add(previewButton);


        // Status Init
        statusLabel.setLocation(0, 565);
        statusLabel.setSize(390, 30);
        statusLabel.setForeground(new Color(237, 66, 69));
        statusLabel.setFont(new Font("Whitney", Font.BOLD, 20));

        // Add status
        add(statusLabel);


        // Update
        JankButton updateGroupChat = new JankButton("Update Group Chat", 390, 50, 0, 600);

        // Edit mouse release listener
        updateGroupChat.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                // Get new chat name
                String newChatName = chatNameInput.getText();

                // Check validity: should not be larger than 20 chars and should be headerable
                if (ServerCommunicator.notHeaderable(newChatName) || newChatName.length() > 20) {
                    // Error
                    statusLabel.setText("Chat Name: ASCII, < 20 character.");
                    // Return
                    return;
                }

                // Set headers; login information, chat id, new chat name, new chat icon url
                HashMap<String, String> headers = new HashMap<>();
                headers.put("username", Jankcord.getFullUser().getUsername());
                headers.put("password", Jankcord.getFullUser().getPassword());

                headers.put("chatID", Jankcord.getOtherID());
                headers.put("newChatName", newChatName);
                headers.put("newChatIconURL", iconInput.getText());

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

                // Update channel name
                Jankcord.getChatBoxArea().setChannelName("# " + newChatName);

                // Reset label
                statusLabel.setText("");
            }
        });

        // Add update
        add(updateGroupChat);

        // Click preview button load in icon
        previewButton.getMouseListener().getMouseReleased().run();
    }
}
