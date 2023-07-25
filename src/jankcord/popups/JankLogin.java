package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.frame.JankFrame;
import jankcord.components.texts.JankPasswordField;
import jankcord.components.texts.JankTextField;
import jankcord.tools.Base64Helper;
import jankcord.tools.ServerCommunicator;
import jankcord.objects.FullUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

// Popup window for user to login
public class JankLogin extends JankFrame {
    public JankLogin() {
        // Super; set name size and window control state
        super("JankCord Login", 500, 750, false);


        // Username Label
        JLabel usernameLabel = new JLabel("Username:");

        // Username Init
        usernameLabel.setSize(150, 30);
        usernameLabel.setLocation(100, 100);
        usernameLabel.setForeground(new Color(114, 118, 125));
        usernameLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Username Label
        getContentPane().add(usernameLabel);


        // Username
        JankTextField usernameInput = new JankTextField(300, 45, 100, 150);

        // Add Username
        getContentPane().add(usernameInput);


        // Password Label
        JLabel passwordLabel = new JLabel("Password:");

        // Password Label Init
        passwordLabel.setSize(150, 30);
        passwordLabel.setLocation(100, 250);
        passwordLabel.setForeground(new Color(114, 118, 125));
        passwordLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Password Label
        getContentPane().add(passwordLabel);


        // Password
        JankPasswordField passwordInput = new JankPasswordField(300, 45, 100, 300);

        // Add password
        getContentPane().add(passwordInput);


        // Server Label
        JLabel serverLabel = new JLabel("Server Key:");

        // Server Label Init
        serverLabel.setSize(200, 30);
        serverLabel.setLocation(100, 400);
        serverLabel.setForeground(new Color(114, 118, 125));
        serverLabel.setFont(new Font("Whitney", Font.BOLD, 28));

        // Add Server Label
        getContentPane().add(serverLabel);


        // Server
        JankTextField serverInput = new JankTextField(300, 45, 100, 450);

        // Add server
        getContentPane().add(serverInput);


        // Status Label
        JLabel statusLabel = new JLabel();

        // Status Label Init
        statusLabel.setSize(300, 30);
        statusLabel.setLocation(100, 550);
        statusLabel.setForeground(new Color(237, 66, 69));
        statusLabel.setFont(new Font("Whitney", Font.BOLD, 20));

        // Add Status Label
        getContentPane().add(statusLabel);


        // Login
        JankButton loginButton = new JankButton("Login", 300, 50, 100, 600);

        // Edit mouse release listener
        loginButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                // Save inputted values
                String username = usernameInput.getText();
                String password = new String(passwordInput.getPassword());
                String server = Base64Helper.decode(serverInput.getText()) + "/api/v1/";

                // Set headers; login information
                HashMap<String, String> headers = new HashMap<>();
                headers.put("username", username);
                headers.put("password", password);

                // Send http request with headers to end point
                String response = ServerCommunicator.sendHttpRequest(server + "login", headers);

                // If null
                if (response == null) {
                    // Error contacting server
                    statusLabel.setText("Error contacting server.");
                    // Return
                    return;
                }

                // If response is 403
                if (response.equals("403")) {
                    // Incorrect credentials
                    statusLabel.setText("Incorrect credentials.");
                }

                // Otherwise read id and avatarURL
                long id;
                String avatarURL;

                // Try reading
                try {
                    // Parse the JSON string
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(response);

                    // Read values from each message object
                    id = (Long) jsonObject.get("id");
                    avatarURL = (String) jsonObject.get("avatarURL");

                    // Set Jankcord's full local user with these values
                    Jankcord.setFullUser(new FullUser(id, username, avatarURL, password, server));

                    // New Jankcord instance
                    new Jankcord();

                    // Dispose of login windows
                    dispose();
                } catch (Exception ex) {}
            }
        });

        // Add Login
        getContentPane().add(loginButton);
    }
}
