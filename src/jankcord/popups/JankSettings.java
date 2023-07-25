package jankcord.popups;

import jankcord.Jankcord;
import jankcord.components.button.JankButton;
import jankcord.components.button.buttonlistener.JankMLRunnable;
import jankcord.components.label.JankLabel;
import jankcord.components.texts.JankPasswordField;
import jankcord.components.texts.JankTextField;
import jankcord.components.windowbuttons.JankCloseButton;
import jankcord.objects.FullUser;
import jankcord.tools.Base64Helper;
import jankcord.tools.JankDraggable;
import jankcord.tools.ResourceLoader;
import jankcord.tools.ServerCommunicator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

public class JankSettings extends JFrame implements JankDraggable {
    public JankSettings() {
        super("JankCord Settings");

        setIconImages(ResourceLoader.loader.getIcons());

        // Frame Init
        setResizable(false);
        setUndecorated(true);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(32, 34, 37));
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(850, 670);
        setLocation((int) screenDim.getWidth() / 2 - getWidth() / 2, (int) screenDim.getHeight() / 2 - getHeight() / 2);

        getContentPane().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePress(e);
            }
        });
        getContentPane().addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                mouseDrag(e);
            }
        });


        JankLabel logoLabel = new JankLabel("JankCord Settings");
        getContentPane().add(logoLabel);

        JankCloseButton closeButton = new JankCloseButton(getWidth(), this);
        getContentPane().add(closeButton);

        JLabel iconLabel = new JLabel();
        iconLabel.setLocation(160, 100);
        iconLabel.setSize(160, 160);
        getContentPane().add(iconLabel);

        JLabel serverLabel = new JLabel("Avatar URL:");
        serverLabel.setLocation(100, 300);
        serverLabel.setSize(200, 30);
        serverLabel.setForeground(new Color(114, 118, 125));
        serverLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(serverLabel);

        JankTextField avatarInput = new JankTextField(300, 45, 100, 350);
        avatarInput.setText(Jankcord.getFullUser().getAvatarURL());
        getContentPane().add(avatarInput);

        JLabel statusLabel = new JLabel();

        JankButton uploadButton = new JankButton("Upload", 300, 45, 100, 450);
        uploadButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                Image avatar = ResourceLoader.loader.getTempProfileIcon().getImage();

                try {
                    URL url = new URL(avatarInput.getText());

                    BufferedImage image = ImageIO.read(url);

                    avatar = new ImageIcon(image).getImage();
                } catch (Exception e) {
                    statusLabel.setText("Error getting avatar.");
                }

                avatar = avatar.getScaledInstance(160, 160, Image.SCALE_FAST);
                iconLabel.setIcon(new ImageIcon(avatar));
            }
        });
        getContentPane().add(uploadButton);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setLocation(450, 100);
        usernameLabel.setSize(150, 30);
        usernameLabel.setForeground(new Color(114, 118, 125));
        usernameLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(usernameLabel);

        JankTextField usernameInput = new JankTextField(300, 45, 450, 150);
        usernameInput.setText(Jankcord.getFullUser().getUsername());
        getContentPane().add(usernameInput);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setLocation(450, 250);
        passwordLabel.setSize(150, 30);
        passwordLabel.setForeground(new Color(114, 118, 125));
        passwordLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(passwordLabel);

        JankPasswordField passwordInput = new JankPasswordField(300, 45, 450, 300);
        getContentPane().add(passwordInput);

        JLabel passwordAgainLabel = new JLabel("Password Again:");
        passwordAgainLabel.setLocation(450, 400);
        passwordAgainLabel.setSize(250, 30);
        passwordAgainLabel.setForeground(new Color(114, 118, 125));
        passwordAgainLabel.setFont(new Font("Whitney", Font.BOLD, 28));
        getContentPane().add(passwordAgainLabel);

        JankPasswordField passwordAgainInput = new JankPasswordField(300, 45, 450, 450);
        getContentPane().add(passwordAgainInput);

        statusLabel.setLocation(100, 515);
        statusLabel.setSize(300, 30);
        statusLabel.setForeground(new Color(237, 66, 69));
        statusLabel.setFont(new Font("Whitney", Font.BOLD, 20));
        getContentPane().add(statusLabel);

        JankButton loginButton = new JankButton("Update Profile", 650, 50, 100, 550);
        loginButton.getMouseListener().setMouseReleased(new JankMLRunnable() {
            @Override
            public void run() {
                HashMap<String, String> headers = new HashMap<>();

                String username = usernameInput.getText();
                String password = new String(passwordInput.getPassword());
                String server = Base64Helper.decode(avatarInput.getText()) + "/api/v1/";

                headers.put("username", username);
                headers.put("password", password);

                String response = ServerCommunicator.sendHttpRequest(server + "login", headers);

                // System.out.println(response);

                if (response == null) {
                    statusLabel.setText("Error contacting server.");

                    return;
                }

                if (response.equals("403")) {
                    System.out.println("Credentials incorrect; exiting program");
                    statusLabel.setText("Incorrect credentials.");
                }

                long id;
                String avatarURL;

                try {
                    // Parse the JSON string
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(response);

                    // Read values from each message object
                    id = (Long) jsonObject.get("id");
                    avatarURL = (String) jsonObject.get("avatarURL");

                    Jankcord.setFullUser(new FullUser(id, username, avatarURL, password, server));

                    dispose();

                    new Jankcord();
                } catch (Exception ex) {
                }
            }
        });
        getContentPane().add(loginButton);

        uploadButton.getMouseListener().getMouseReleased().run();
    }


    // Frame dragging
    private int posX = 0, posY = 0;
    private boolean drag = false;

    @Override
    public void mousePress(MouseEvent e) {
        drag = true;
        posX = e.getX();
        posY = e.getY();
    }

    @Override
    public void mouseDrag(MouseEvent e) {
        if (drag) {
            setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
        }
    }
}
