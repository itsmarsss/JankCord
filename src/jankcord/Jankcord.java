package jankcord;

// Imports
// AWT

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Swing
import javax.imageio.ImageIO;
import javax.swing.*;

// Components
import jankcord.components.label.JankLabel;
import jankcord.containers.ChannelList;
import jankcord.containers.ChatBoxArea;
import jankcord.containers.ServerList;
import jankcord.components.windowbuttons.WindowButtons;
import jankcord.components.scrollbar.JankScrollBar;
import jankcord.objects.*;
import jankcord.popups.JankLogin;
import jankcord.tools.*;
import jankcord_admin.JankcordAdmin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// JankCord Class
public class Jankcord implements JankDraggable {
    // Main Function
    public static void main(String[] args) {
        boolean isServer = false;

        for (String arg : args) {
            if (arg.equals("--server")) {
                isServer = true;
                break;
            }
        }

        if (isServer) {
            JankcordAdmin.startAdmin();
        } else {
            System.setProperty("sun.java2d.uiScale", "1");

            new JankLogin().setVisible(true);
        }
    }

    // Non-fullscreen dimensions and toggle
    private static int oldW;
    private static int oldH;
    private static int oldX;
    private static int oldY;
    private static boolean full = false;

    // Main frame and components
    private static JFrame frame;                    // Window holding everything
    private static JPanel viewPanel;
    private static WindowButtons windowButtons;        // Other components...
    private static ServerList serverList;
    private static ChannelList channelList;
    private static ChatBoxArea chatBoxArea;

    private static String otherID = "";
    private static boolean newOtherID = true;
    private static boolean inServer = false;
    private static FullUser fullUser;

    // JankCord Default Constructor
    public Jankcord() {
        // Init
        frame = new JFrame("JankCord");
        viewPanel = new JPanel();
        drawUI();
    }

    public static void setFullUser(FullUser fullUser) {
        Jankcord.fullUser = fullUser;
    }

    public static FullUser getFullUser() {
        return fullUser;
    }

    public static void setOtherID(String otherID) {
        Jankcord.otherID = otherID;
    }

    public static String getOtherID() {
        return otherID;
    }

    public static boolean isNewOtherID() {
        return newOtherID;
    }

    public static void setNewOtherID(boolean newOtherID) {
        Jankcord.newOtherID = newOtherID;
    }

    public static boolean isInServer() {
        return inServer;
    }

    public static void setInServer(boolean inServer) {
        Jankcord.inServer = inServer;
    }

    // render frame and viewPanel
    private void drawUI() {
        // Frame Icon
        ArrayList<Image> icons = new ArrayList<>();
        icons.add(ResourceLoader.loader.getIcon1().getImage());
        icons.add(ResourceLoader.loader.getIcon2().getImage());
        icons.add(ResourceLoader.loader.getIcon3().getImage());
        icons.add(ResourceLoader.loader.getIcon4().getImage());

        frame.setIconImages(icons);

        // Frame Init
        frame.setUndecorated(true);
        frame.getContentPane().setLayout(null);
        frame.setMinimumSize(new Dimension(1880, 1000));
        frame.getContentPane().setBackground(new Color(32, 34, 37));
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenDim.getWidth() / 1.5), (int) (screenDim.getHeight() / 1.5));
        frame.setLocation((int) screenDim.getWidth() / 2 - frame.getWidth() / 2, (int) screenDim.getHeight() / 2 - frame.getHeight() / 2);

        // Entire View

        // View Init
        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(frame);
        cr.setSnapSize(new Dimension(1, 1));
        cr.setMinimumSize(new Dimension(1880, 1000));
        cr.setMaximumSize(screenDim);

        viewPanel.setLayout(null);
        viewPanel.setLocation(5, 5);
        viewPanel.setBackground(new Color(32, 34, 37));
        viewPanel.setSize(frame.getWidth() - 10, frame.getHeight() - 10);

        // Drag 'n' Drop
        viewPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePress(e);
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    doFullscreen();
                }
            }
        });
        viewPanel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                mouseDrag(e);
            }
        });

        frame.getContentPane().add(viewPanel);

        // Logo
        JankLabel logoLabel = new JankLabel("JankCord");
        logoLabel.setName("JankCordLogo");

        viewPanel.add(logoLabel);

        // Add Other Components
        windowButtons = new WindowButtons();
        serverList = new ServerList();
        channelList = new ChannelList();
        chatBoxArea = new ChatBoxArea();

        viewPanel.add(windowButtons);
        viewPanel.add(serverList);
        viewPanel.add(channelList);
        viewPanel.add(chatBoxArea);

        for (Component i : viewPanel.getComponents())
            System.out.println(i.getName());

        // Friend list panel
        JScrollPane friendsScrollPane = new JScrollPane();
        friendsScrollPane.getVerticalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(32, 34, 37), true));

        // Channel list panel
        JScrollPane channelScrollPane = new JScrollPane();
        channelScrollPane.getVerticalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(32, 34, 37), true));

        frame.setVisible(true);

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(Jankcord::queryForNewFriend, 0, 5, TimeUnit.SECONDS);

        ScheduledExecutorService ses1 = Executors.newSingleThreadScheduledExecutor();
        ses1.scheduleAtFixedRate(Jankcord::queryForNewMessages, 0, 500, TimeUnit.MILLISECONDS);

        ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();
        ses2.scheduleAtFixedRate(Jankcord::queryForNewGroupChats, 0, 1, TimeUnit.SECONDS);
    }

    public static final HashMap<Long, SimpleUserCache> avatarCache = new HashMap<>();
    private static ArrayList<User> tempFriends = new ArrayList<>();

    private static boolean inServerCheck = false;

    public static void queryForNewFriend() {
        //System.out.println("New friend query");
        // Query api endpoint

        // Get messages
        HashMap<String, String> headers = new HashMap<>();
        headers.put("username", fullUser.getUsername());
        headers.put("password", fullUser.getPassword());

        String friendsJSON = ServerCommunicator.sendHttpRequest(fullUser.getEndPointHost() + "friends", headers);

        // System.out.println(friendsJSON);

        ArrayList<User> friends = new ArrayList<>();

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(friendsJSON);

            // Get the "messages" array from the JSON object
            JSONArray messagesArray = (JSONArray) jsonObject.get("friends");

            // Loop through the "messages" array
            for (Object message : messagesArray) {
                JSONObject messageObject = (JSONObject) message;

                // Read values from each message object
                long id = (Long) messageObject.get("id");
                String username = (String) messageObject.get("username");
                String avatarURL = (String) messageObject.get("avatarURL");

                friends.add(new User(id, username, avatarURL));

                try {
                    if (!avatarCache.get(id).getAvatarURL().equals(avatarURL)) {
                        cacheAvatar(id, username, avatarURL);
                    }
                } catch (Exception e) {
                    cacheAvatar(id, username, avatarURL);
                }
            }
        } catch (Exception e) {}

        if (inServer) {
            return;
        }

        boolean isSame = true;
        if (friends.size() != tempFriends.size()) {
            isSame = false;
        } else {
            for (int i = 0; i < friends.size(); i++) {
                User friend = friends.get(i);
                User tempFriend = tempFriends.get(i);

                if (!friend.isEqual(tempFriend)) {
                    isSame = false;
                }
            }
        }

        if (inServerCheck) {
            //System.out.println("Artificial");
            inServerCheck = false;
            isSame = false;
        }

        if (isSame) {
            //System.out.println("Friend list no updates");
            return;
        }

        tempFriends = friends;


        channelList.initChannelPanel();

        for (int i = 0; i < friends.size(); i++) {
            if (!friends.get(i).getUsername().equals(fullUser.getUsername())) {
                channelList.addChannel(friends.get(i), i + 2);
                // System.out.println(friends.get(i).getUsername());
            }
        }
    }

    public static void setInServerCheck(boolean inServerCheck) {
        Jankcord.inServerCheck = inServerCheck;
    }

    public static ArrayList<User> getTempFriends() {
        return tempFriends;
    }

    private static ArrayList<GroupChat> tempGroupChats = new ArrayList<>();

    private static void queryForNewGroupChats() {
        //System.out.println("New group chat query");
        // Query api endpoint

        // Get messages
        HashMap<String, String> headers = new HashMap<>();
        headers.put("username", fullUser.getUsername());
        headers.put("password", fullUser.getPassword());

        String groupsJSON = ServerCommunicator.sendHttpRequest(fullUser.getEndPointHost() + "groupchats", headers);

        //System.out.println(groupsJSON);
        ArrayList<GroupChat> groupChats = new ArrayList<>();

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(groupsJSON);

            // Get the "messages" array from the JSON object
            JSONArray groupChatsArray = (JSONArray) jsonObject.get("groupChats");

            // Loop through the "messages" array
            for (Object groupChat : groupChatsArray) {
                JSONObject groupChatObject = (JSONObject) groupChat;

                // Read values from each message object
                String chatID = (String) groupChatObject.get("chatID");
                String chatName = (String) groupChatObject.get("chatName");
                String chatIconURL = (String) groupChatObject.get("chatIconURL");

                groupChats.add(new GroupChat(chatID, chatName, chatIconURL));
            }
        } catch (Exception e) {}


        boolean isSame = true;
        if (groupChats.size() != tempGroupChats.size()) {
            isSame = false;
        } else {
            for (int i = 0; i < groupChats.size(); i++) {
                GroupChat groupChat = groupChats.get(i);
                GroupChat tempGroupChat = tempGroupChats.get(i);

                if (!groupChat.isEqual(tempGroupChat)) {
                    isSame = false;
                }
            }
        }

        if (isSame) {
            // System.out.println("Group list no updates");
            return;
        }

        tempGroupChats = groupChats;

        serverList.initChannelPanel();

        for (int i = 0; i < groupChats.size(); i++) {
            serverList.addServer(groupChats.get(i), i + 2);
        }

        serverList.addTrailingProfiles();
    }

    private static ArrayList<Message> tempMessages = new ArrayList<>();
    private static ArrayList<User> tempMembers = new ArrayList<>();

    public static void queryForNewMessages() {
        // System.out.println("New messages query");
        // Query api endpoint
        // Get messages
        HashMap<String, String> headers = new HashMap<>();

        headers.put("username", fullUser.getUsername());
        headers.put("password", fullUser.getPassword());
        headers.put("otherID", otherID);

        String dest = "messages";

        if (inServer) {
            dest = "groupmessages";
        }

        String messagesJSON = ServerCommunicator.sendHttpRequest(fullUser.getEndPointHost() + dest, headers);

        //System.out.println(messagesJSON);

        ArrayList<Message> messages = new ArrayList<>();
        ArrayList<User> members = new ArrayList<>();

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(messagesJSON);

            // Get the "members" array from the JSON object
            JSONArray membersArray = (JSONArray) jsonObject.get("users");

            // Loop through the "messages" array
            for (Object member : membersArray) {
                long id = (Long) member;

                for (User friend : tempFriends) {
                    if (friend.getId() == id) {
                        members.add(friend);
                        break;
                    }
                }
            }

            // Get the "messages" array from the JSON object
            JSONArray messagesArray = (JSONArray) jsonObject.get("messages");

            // Loop through the "messages" array
            for (Object message : messagesArray) {
                JSONObject messageObject = (JSONObject) message;

                // Read values from each message object
                long id = (Long) messageObject.get("id");
                String content = (String) messageObject.get("content");
                long timestamp = (Long) messageObject.get("timestamp");

                String username = avatarCache.get(id).getUsername();
                String avatarURL = avatarCache.get(id).getAvatarURL();

                messages.add(new Message(id, Base64Helper.decode(content), timestamp));

                try {
                    if (!avatarCache.get(id).getAvatarURL().equals(avatarURL)) {
                        cacheAvatar(id, username, avatarURL);
                    }
                } catch (Exception e) {
                    cacheAvatar(id, username, avatarURL);
                }
            }
        } catch (Exception e) {}


        boolean isSame = true;
        if (messages.size() != tempMessages.size()) {
            isSame = false;
        } else {
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                Message tempMessage = tempMessages.get(i);

                if (!message.isEqual(tempMessage)) {
                    isSame = false;
                }
            }
        }

        if (isSame) {
            //System.out.println("Message list no updates");
        } else {
            tempMessages = messages;

            for (int i = chatBoxArea.getMessageIndex(); i < messages.size(); i++) {
                chatBoxArea.addMessage(messages.get(i));
            }

            chatBoxArea.setMaxChatScroll();
        }


        boolean isSame2 = true;
        if (members.size() != tempMembers.size()) {
            isSame2 = false;
        } else {
            for (int i = 0; i < members.size(); i++) {
                User member = members.get(i);
                User tempMember = tempMembers.get(i);

                if (!member.isEqual(tempMember)) {
                    isSame2 = false;
                }
            }
        }

        if (isSame2) {
            //System.out.println("Member list no updates");
            return;
        }

        tempMembers = members;

        chatBoxArea.resetMembers();
        for (int i = 0; i < members.size(); i++) {
            chatBoxArea.addMember(members.get(i), i);
        }
    }

    public static void cacheAvatar(long id, String username, String avatarURL) {
        Image avatar = ResourceLoader.loader.getTempProfileIcon().getImage();

        try {
            URL url = new URL(avatarURL);

            BufferedImage image = ImageIO.read(url);

            avatar = new ImageIcon(image).getImage();
        } catch (Exception e) {
            //System.out.println("Error getting avatar");
        }

        avatarCache.put(id, new SimpleUserCache(username, avatarURL, avatar));
    }

    public static void doFullscreen() {
        if (full) {
            frame.setSize(oldW, oldH);
            frame.setLocation(oldX, oldY);
            full = false;
        } else {
            oldW = frame.getWidth();
            oldH = frame.getHeight();
            oldX = frame.getX();
            oldY = frame.getY();
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            full = true;
        }
        resize();
    }

    public static void resize() {
        viewPanel.setLocation(5, 5);
        viewPanel.setSize(frame.getWidth() - 10, frame.getHeight() - 10);

        windowButtons.setLocation(Jankcord.viewPanel.getWidth() - 186, 0);

        serverList.setSize(serverList.getWidth(), Jankcord.viewPanel.getHeight() - 50);

        channelList.setSize(channelList.getWidth(), Jankcord.viewPanel.getHeight() - 50);
        channelList.getChannelScrollPane().setSize(477, channelList.getHeight() - 110);

        chatBoxArea.setSize(Jankcord.getViewPanel().getWidth() - 646, Jankcord.getViewPanel().getHeight() - 50);
        chatBoxArea.getChatBoxTopBarPanel().setSize(chatBoxArea.getWidth(), 106);
        chatBoxArea.getChatBoxScrollPane().setSize(chatBoxArea.getWidth() - 540, chatBoxArea.getHeight() - 256);

        chatBoxArea.getTypePanel().setSize(chatBoxArea.getChatBoxScrollPane().getWidth() - 60, 100);
        chatBoxArea.getTypePanel().setLocation(30, chatBoxArea.getHeight() - 125);
        chatBoxArea.getTypeScrollPane().setSize(chatBoxArea.getTypePanel().getWidth() - 20, chatBoxArea.getTypePanel().getHeight() - 16);
        chatBoxArea.reline();

        chatBoxArea.getMembersScrollPane().setSize(540, chatBoxArea.getHeight() - 106);
        chatBoxArea.getMembersScrollPane().setLocation(chatBoxArea.getChatBoxScrollPane().getWidth() + 5, 106);
        chatBoxArea.resetMessageWidths();

        chatBoxArea.resetMessageWidths();

        chatBoxArea.revalidate();
        chatBoxArea.repaint();
    }


    public static ChatBoxArea getChatBoxArea() {
        return chatBoxArea;
    }

    public static ChannelList getChannelList() {
        return channelList;
    }

    public static JPanel getViewPanel() {
        return viewPanel;
    }

    public static JFrame getFrame() {
        return frame;
    }


    // Frame dragging
    private int posX = 0, posY = 0;
    private boolean drag = false;

    @Override
    public void mousePress(MouseEvent e) {
        if (e.getY() < 50 && e.getY() > 10) {
            drag = true;
            posX = e.getX();
            posY = e.getY();
        } else {
            drag = false;
        }
    }

    @Override
    public void mouseDrag(MouseEvent e) {
        if (drag) {
            if (full) {
                frame.setSize(oldW, oldH);
                frame.setLocation(oldX, oldY);
                full = false;
                resize();
            }
            frame.setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
        }
    }
}
