package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.texts.JankPasswordField;
import jankcord.components.texts.JankTextField;
import jankcord.tools.ResourceLoader;
import jankcord.tools.ServerCommunicator;
import jankcord.objects.FullUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class JankLogin extends JFrame {

    // Frame dragging
    private int posX = 0, posY = 0;
    private boolean drag = false;

    public JankLogin() {
        super("JankCord Login");

        ArrayList<Image> icons = new ArrayList<>();
        icons.add(ResourceLoader.loader.getIcon1().getImage());
        icons.add(ResourceLoader.loader.getIcon2().getImage());
        icons.add(ResourceLoader.loader.getIcon3().getImage());
        icons.add(ResourceLoader.loader.getIcon4().getImage());

        setIconImages(icons);

        // Frame Init
        setUndecorated(true);
        getContentPane().setLayout(null);
        setResizable(true);
        getContentPane().setBackground(new Color(32, 34, 37));
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(500, 750);
        setLocation((int) screenDim.getWidth() / 2 - getWidth() / 2, (int) screenDim.getHeight() / 2 - getHeight() / 2);

        getContentPane().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                drag = true;
                posX = e.getX();
                posY = e.getY();
            }
        });
        getContentPane().addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (drag) {
                    setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
                }
            }
        });


        Font windowButtonFont = new Font(null, Font.PLAIN, 28);
        JLabel closeButton = new JLabel("✕", SwingConstants.CENTER);

        closeButton.setOpaque(true);
        closeButton.setSize(62, 50);
        closeButton.setFont(windowButtonFont);
        closeButton.setLocation(getWidth() - closeButton.getWidth(), 0);
        closeButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                e.getComponent().setBackground(new Color(237, 66, 69));
                e.getComponent().setForeground(new Color(255, 255, 255));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                e.getComponent().setBackground(new Color(237, 66, 69));
                e.getComponent().setForeground(new Color(255, 255, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setBackground(new Color(32, 34, 37));
                e.getComponent().setForeground(new Color(185, 187, 190));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.exit(0);
            }
        });
        closeButton.setBackground(new Color(32, 34, 37));
        closeButton.setForeground(new Color(159, 161, 165));

        getContentPane().add(closeButton);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setSize(150, 30);
        usernameLabel.setLocation(100, 100);
        usernameLabel.setForeground(new Color(114, 118, 125));
        usernameLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(usernameLabel);

        JankTextField usernameInput = new JankTextField(300, 45, 100, 150);

        getContentPane().add(usernameInput);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setSize(150, 30);
        passwordLabel.setLocation(100, 250);
        passwordLabel.setForeground(new Color(114, 118, 125));
        passwordLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(passwordLabel);

        JankPasswordField passwordInput = new JankPasswordField(300, 45, 100, 300);

        getContentPane().add(passwordInput);

        JLabel serverLabel = new JLabel("Server:");
        serverLabel.setSize(150, 30);
        serverLabel.setLocation(100, 400);
        serverLabel.setForeground(new Color(114, 118, 125));
        serverLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(serverLabel);

        JankTextField serverInput = new JankTextField(300, 45, 100, 450);

        getContentPane().add(serverInput);

        JLabel statusLabel = new JLabel("");
        statusLabel.setSize(300, 30);
        statusLabel.setLocation(100, 550);
        statusLabel.setForeground(new Color(237, 66, 69));
        statusLabel.setFont(new Font("Whitney", Font.BOLD, 20));
        getContentPane().add(statusLabel);

        JankButton loginButton = new JankButton("Login", 300, 50, 100, 600);
        loginButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                HashMap<String, String> headers = new HashMap<>();

                String username = usernameInput.getText();
                String password = new String(passwordInput.getPassword());
                String server = serverInput.getText();

                headers.put("username", username);
                headers.put("password", password);

                String response = ServerCommunicator.sendHttpRequest(server + "/api/v1/login", headers);

                // System.out.println(response);

                if (response == null) {
                    statusLabel.setText("Error contacting server.");

                    return;
                }

                if (response.equals("403")) {
                    System.out.println("Credentials incorrect; exiting program");
                    statusLabel.setText("Incorrect credentials.");
                }

                long id = 0;
                String avatarURL = null;

                try {
                    // Parse the JSON string
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(response);

                    // Read values from each message object
                    id = (Long) jsonObject.get("id");
                    avatarURL = (String) jsonObject.get("avatarURL");

                    Jankcord.setFullUser(new FullUser(id, username, avatarURL, password, server + "/api/v1/"));

                    dispose();

                    new Jankcord();
                } catch (Exception ex) {
                }
            }
        });

        getContentPane().add(loginButton);
    }
}
