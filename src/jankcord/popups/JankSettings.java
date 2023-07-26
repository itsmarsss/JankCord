package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.frame.JankFrame;
import jankcord.components.texts.JankPasswordField;
import jankcord.components.texts.JankTextField;
import jankcord.objects.SimpleUserCache;
import jankcord.tools.ResourceLoader;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.JankcordAdmin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;

// Popup window containing user account configuration functionality
public class JankSettings extends JankFrame {
    // Constructor to create new JankSettings
    public JankSettings() {
        // Super; set name size and window control state
        super("JankCord Settings", 850, 670, false);

        // Frame content Init


        // Icon
        JLabel iconLabel = new JLabel();

        // Icon Init
        iconLabel.setLocation(160, 100);
        iconLabel.setSize(160, 160);

        // Add Icon
        getContentPane().add(iconLabel);


        // Avatar Label
        JLabel avatarLabel = new JLabel("Avatar URL:");

        // Avatar Label Init
        avatarLabel.setLocation(100, 300);
        avatarLabel.setSize(200, 30);
        avatarLabel.setForeground(new Color(114, 118, 125));
        avatarLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Avatar Label
        getContentPane().add(avatarLabel);


        // Avatar Input
        JankTextField avatarInput = new JankTextField(300, 45, 100, 350);

        // Avatar Input Init
        avatarInput.setText(Jankcord.getFullUser().getAvatarURL());

        // Add Avatar Input
        getContentPane().add(avatarInput);


        // Status
        JLabel statusLabel = new JLabel();


        // Preview
        JankButton previewButton = new JankButton("Preview", 300, 45, 100, 450);

        // Edit mouse release listener
        previewButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                // Set avatar to temp avatar for now
                Image avatar = ResourceLoader.loader.getTempProfileIcon().getImage();

                // Try to get user profile
                try {
                    // URL of avatar
                    URL url = new URL(avatarInput.getText());

                    // Read image and circularize it, set to avatar
                    avatar = SimpleUserCache.circularize(ImageIO.read(url));

                    // Reset status label
                    statusLabel.setText("");
                } catch (Exception e) {
                    // If error, statusLabel display it
                    statusLabel.setText("Error getting avatar.");
                }

                // Scale image to fit iconLabel
                avatar = avatar.getScaledInstance(160, 160, Image.SCALE_FAST);

                // Set icon
                iconLabel.setIcon(new ImageIcon(avatar));

            }
        });

        // Add Preview
        getContentPane().add(previewButton);


        // Username Label
        JLabel usernameLabel = new JLabel("Username:");

        // Username Label Init
        usernameLabel.setLocation(450, 100);
        usernameLabel.setSize(150, 30);
        usernameLabel.setForeground(new Color(114, 118, 125));
        usernameLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Username Label
        getContentPane().add(usernameLabel);


        // Username
        JankTextField usernameInput = new JankTextField(300, 45, 450, 150);

        // Username Init
        usernameInput.setText(Jankcord.getFullUser().getUsername());

        // Add Username
        getContentPane().add(usernameInput);


        // Password Label
        JLabel passwordLabel = new JLabel("Password:");

        // Password Label Init
        passwordLabel.setLocation(450, 250);
        passwordLabel.setSize(150, 30);
        passwordLabel.setForeground(new Color(114, 118, 125));
        passwordLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Password Label
        getContentPane().add(passwordLabel);


        // Password
        JankPasswordField passwordInput = new JankPasswordField(300, 45, 450, 300);

        // Add Password
        getContentPane().add(passwordInput);


        // Password Again Label
        JLabel passwordAgainLabel = new JLabel("Password Again:");

        // Password Again Label Init
        passwordAgainLabel.setLocation(450, 400);
        passwordAgainLabel.setSize(250, 30);
        passwordAgainLabel.setForeground(new Color(114, 118, 125));
        passwordAgainLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Password Again Label
        getContentPane().add(passwordAgainLabel);


        // Password Again
        JankPasswordField passwordAgainInput = new JankPasswordField(300, 45, 450, 450);

        // Add Password Again
        getContentPane().add(passwordAgainInput);


        // Status Label Init
        statusLabel.setLocation(100, 515);
        statusLabel.setSize(650, 30);
        statusLabel.setForeground(new Color(237, 66, 69));
        statusLabel.setFont(new Font("Whitney", Font.BOLD, 20));

        // Add Status Label
        getContentPane().add(statusLabel);


        // Update
        JankButton updateProfile = new JankButton("Update Profile", 650, 50, 100, 550);

        // Edit mouse release listener
        updateProfile.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                // Store new username
                String newUsername = usernameInput.getText();

                // Check if valid
                if (JankcordAdmin.validateUsername(newUsername) != null) { // If there is an error message
                    // Set status label to username specifications
                    statusLabel.setText("Username: ASCII, no spaces, not blank, < 15 characters");
                    return;
                }


                // Store new password
                String newPassword = new String(passwordInput.getPassword());

                // Check if both password inputs are equal
                if (!newPassword.equals(new String(passwordAgainInput.getPassword()))) {
                    // If not, set status label to warn
                    statusLabel.setText("Passwords do not match.");
                    return;
                }

                // Check if valid
                if (JankcordAdmin.validatePassword(newPassword) != null) { // If there is an error message
                    // Set status label to password specifications
                    statusLabel.setText("Password: ASCII, no spaces, not blank, < 15 characters");
                    return;
                }

                // Store new user avatar
                String avatarURL = avatarInput.getText();

                // Set headers; login information, new login information, and avatarURL
                HashMap<String, String> headers = new HashMap<>();
                headers.put("username", Jankcord.getFullUser().getUsername());
                headers.put("password", Jankcord.getFullUser().getPassword());
                headers.put("newUsername", newUsername);
                headers.put("newPassword", newPassword);
                headers.put("avatarURL", avatarURL);

                // Send http request with headers to end point
                String response = ServerCommunicator.sendHttpRequest(Jankcord.getFullUser().getEndPointHost() + "editaccount", headers);

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

                // If conflict
                if(response.equals("409")) {
                    // Conflict
                    statusLabel.setText("Username already taken.");

                    // Return
                    return;
                }

                // Otherwise set text to normal and grey
                Jankcord.getTitleLabel().setText("JankCord");
                Jankcord.getTitleLabel().setForeground(new Color(114, 118, 125));

                // Update local username, password, and avatarURL
                Jankcord.getFullUser().setUsername(newUsername);
                Jankcord.getFullUser().setPassword(newPassword);
                Jankcord.getFullUser().setAvatarURL(avatarURL);

                // Dispose settings window
                dispose();
            }
        });

        // Add Update
        getContentPane().add(updateProfile);


        // Click preview button load in avatar
        previewButton.getMouseListener().getMouseReleased().run();
    }
}
