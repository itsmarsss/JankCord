package jankcord;

import java.awt.Color;
import java.awt.Dimension;
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

import javax.imageio.ImageIO;
import javax.swing.*;

import jankcord.components.frame.JankFrame;
import jankcord.components.frame.draggable.JankDraggable;
import jankcord.components.label.JankTitleLabel;
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

// JankCord Main Class
public class Jankcord implements JankDraggable {
    // Main Function
    public static void main(String[] args) {
        // Check if instance is a server
        boolean isServer = false;

        // Loop through all args
        for (String arg : args) {
            // If --server found
            if (arg.equals("--server")) {
                // Set flag
                isServer = true;
                break;
            }
        }

        // Determine which to initialize
        if (isServer) {
            // If server -> startAdmin
            JankcordAdmin.startAdmin();
        } else {
            // If client -> set ui scaling
            System.setProperty("sun.java2d.uiScale", "1");

            // Jank login
            new JankLogin().setVisible(true);
        }
    }

    // Main frame and components
    private static JFrame frame;                    // Window holding everything
    private static JPanel viewPanel;
    private static JankTitleLabel titleLabel;
    private static WindowButtons windowButtons;
    private static ServerList serverList;
    private static ChannelList channelList;
    private static ChatBoxArea chatBoxArea;

    // Useful fields for setting text place
    private static String otherID = "";
    private static boolean newOtherID = true;
    private static boolean inServer = false;
    private static boolean inServerCheck = false;
    private static FullUser fullUser;

    // ScheduledExecutorServices
    private static ScheduledExecutorService sesFriend, sesGroup, sesMessage;

    // JankCord Default Constructor
    public Jankcord() {
        // Init -> straight to drawUI
        drawUI();
    }

    // Render frame and viewPanel
    private void drawUI() {
        // Frame and View Panel Init
        frame = new JFrame("JankCord");
        viewPanel = new JPanel();

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

        // Component Resizer
        ComponentResizer cr = new ComponentResizer();

        // Component Resizer Init
        cr.registerComponent(frame);
        cr.setSnapSize(new Dimension(1, 1));
        cr.setMinimumSize(new Dimension(1880, 1000));
        cr.setMaximumSize(screenDim);

        // View Init
        viewPanel.setLayout(null);
        viewPanel.setLocation(5, 5);
        viewPanel.setBackground(new Color(32, 34, 37));
        viewPanel.setSize(frame.getWidth() - 10, frame.getHeight() - 10);

        // Drag 'n' Drop Mouse listeners
        viewPanel.addMouseListener(new MouseAdapter() {
            // Call mouse press on mouse press
            public void mousePressed(MouseEvent e) {
                mousePress(e);
            }

            // Toggle full screen on double click
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    doFullscreen();
                }
            }
        });
        viewPanel.addMouseMotionListener(new MouseAdapter() {
            // Call mouse drag on drag
            public void mouseDragged(MouseEvent e) {
                mouseDrag(e);
            }
        });

        // Add View Panel to Frame
        frame.getContentPane().add(viewPanel);

        // Title
        titleLabel = new JankTitleLabel("JankCord");

        // Add Title to View panel
        viewPanel.add(titleLabel);


        // Other components
        windowButtons = new WindowButtons();
        serverList = new ServerList();
        channelList = new ChannelList();
        chatBoxArea = new ChatBoxArea();

        // Add Other Components
        viewPanel.add(windowButtons);
        viewPanel.add(serverList);
        viewPanel.add(channelList);
        viewPanel.add(chatBoxArea);

        // Friend List Panel
        JScrollPane friendsScrollPane = new JScrollPane();

        // Friend List Panel Init
        friendsScrollPane.getVerticalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(32, 34, 37), true));

        // Channel List Panel
        JScrollPane channelScrollPane = new JScrollPane();

        // Channel List Panel Init
        channelScrollPane.getVerticalScrollBar().setUI(new JankScrollBar(new Color(43, 45, 49), new Color(32, 34, 37), true));

        // Make frame visible
        frame.setVisible(true);

        // Set default channel name
        chatBoxArea.setChannelName("~ Select a channel.");

        // SchedulesExecutorService for friend querying
        sesFriend = Executors.newSingleThreadScheduledExecutor();
        sesFriend.scheduleAtFixedRate(Jankcord::queryForNewFriend, 0, 5, TimeUnit.SECONDS);

        // SchedulesExecutorService for group chat querying
        sesGroup = Executors.newSingleThreadScheduledExecutor();
        sesGroup.scheduleAtFixedRate(Jankcord::queryForNewGroupChats, 0, 5, TimeUnit.SECONDS);

        // SchedulesExecutorService for message querying
        sesMessage = Executors.newSingleThreadScheduledExecutor();
        sesMessage.scheduleAtFixedRate(Jankcord::queryForNewMessages, 0, 500, TimeUnit.MILLISECONDS);
    }

    private static HashMap<Long, SimpleUserCache> avatarCache = new HashMap<>();
    private static ArrayList<User> tempFriends = new ArrayList<>();

    public static void queryForNewFriend() {
        System.out.println("New friend query");
        // Query api endpoint

        // Get messages
        HashMap<String, String> headers = new HashMap<>();
        headers.put("username", fullUser.getUsername());
        headers.put("password", fullUser.getPassword());

        String friendsJSON = ServerCommunicator.sendHttpRequest(fullUser.getEndPointHost() + "friends", headers);

        System.out.println(friendsJSON);

        if (friendsJSON == null) {
            titleLabel.setText("JankCord - OFFLINE");
            titleLabel.setForeground(new Color(198, 36, 36));
            return;
        }
        titleLabel.setText("JankCord");
        titleLabel.setForeground(new Color(114, 118, 125));

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
        } catch (Exception e) {
        }

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
            User friend = friends.get(i);
            SimpleUserCache cachedFriend = avatarCache.get(friend.getId());

            if(!cachedFriend.getUsername().equals(friend.getUsername()) ||
            !cachedFriend.getAvatarURL().equals(friend.getAvatarURL())) {
                avatarCache.remove(friend.getId());
            }

            if (!friend.getUsername().equals(fullUser.getUsername())) {
                channelList.addChannel(friends.get(i), i + 2);
                // System.out.println(friends.get(i).getUsername());
            }
        }
    }

    private static ArrayList<GroupChat> tempGroupChats = new ArrayList<>();

    private static void queryForNewGroupChats() {
        System.out.println("New group chat query");
        // Query api endpoint

        // Get messages
        HashMap<String, String> headers = new HashMap<>();
        headers.put("username", fullUser.getUsername());
        headers.put("password", fullUser.getPassword());

        String groupsJSON = ServerCommunicator.sendHttpRequest(fullUser.getEndPointHost() + "groupchats", headers);

        System.out.println(groupsJSON);

        if (groupsJSON == null) {
            titleLabel.setText("JankCord - OFFLINE");
            titleLabel.setForeground(new Color(198, 36, 36));
            return;
        }
        titleLabel.setText("JankCord");
        titleLabel.setForeground(new Color(114, 118, 125));

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
        } catch (Exception e) {
        }


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
         System.out.println("New messages query");
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

        System.out.println(messagesJSON);

        if (messagesJSON == null) {
            titleLabel.setText("JankCord - OFFLINE");
            titleLabel.setForeground(new Color(198, 36, 36));
            return;
        }
        titleLabel.setText("JankCord");
        titleLabel.setForeground(new Color(114, 118, 125));

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
        } catch (Exception e) {
        }


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

    /**
     * Caches a user's avatar
     *
     * @param id        user id
     * @param username  user username
     * @param avatarURL user avatar url
     */
    public static void cacheAvatar(long id, String username, String avatarURL) {
        // Set avatar to default for now
        Image avatar = ResourceLoader.loader.getTempProfileIcon().getImage();

        // Try to load image
        try {
            // URL of avatar url
            URL url = new URL(avatarURL);

            // Read image
            BufferedImage image = ImageIO.read(url);

            // Set avatar to read image
            avatar = new ImageIcon(image).getImage();
        } catch (Exception e) {
        }

        // Cache image
        avatarCache.put(id, new SimpleUserCache(username, avatarURL, avatar));
    }

    /**
     * Resizes Frame's children components
     */
    public static void resize() {
        // View Panel size
        viewPanel.setSize(frame.getWidth() - 10, frame.getHeight() - 10);

        // Window Buttons location
        windowButtons.setLocation(Jankcord.viewPanel.getWidth() - 186, 0);

        // Server List size
        serverList.setSize(serverList.getWidth(), Jankcord.viewPanel.getHeight() - 50);

        // Channel List size and size of scroll pane
        channelList.setSize(channelList.getWidth(), Jankcord.viewPanel.getHeight() - 50);
        channelList.getChannelScrollPane().setSize(477, channelList.getHeight() - 110);

        // Chat Box area size and its children components
        chatBoxArea.setSize(Jankcord.getViewPanel().getWidth() - 646, Jankcord.getViewPanel().getHeight() - 50);
        chatBoxArea.getChatBoxTopBarPanel().setSize(chatBoxArea.getWidth(), 106);
        chatBoxArea.getSettingsLabel().setLocation(chatBoxArea.getChatBoxTopBarPanel().getWidth() - 320, 20);
        chatBoxArea.getChatBoxScrollPane().setSize(chatBoxArea.getWidth() - 540, chatBoxArea.getHeight() - 256);

        // Chat Box area: Type Panel
        chatBoxArea.getTypePanel().setSize(chatBoxArea.getChatBoxScrollPane().getWidth() - 60, 100);
        chatBoxArea.getTypePanel().setLocation(30, chatBoxArea.getHeight() - 125);
        chatBoxArea.getTypeScrollPane().setSize(chatBoxArea.getTypePanel().getWidth() - 20, chatBoxArea.getTypePanel().getHeight() - 16);
        chatBoxArea.reline();

        // Chat Box area: Members Scroll Pane
        chatBoxArea.getMembersScrollPane().setSize(540, chatBoxArea.getHeight() - 106);
        chatBoxArea.getMembersScrollPane().setLocation(chatBoxArea.getChatBoxScrollPane().getWidth() + 5, 106);
        chatBoxArea.resetMessageWidths();
    }

    // Non-fullscreen dimensions and toggle
    private static int oldW;
    private static int oldH;
    private static int oldX;
    private static int oldY;
    private static boolean full = false;

    // Frame dragging fields
    public int posX = 0, posY = 0;
    public boolean drag = false;

    // Override existing press
    @Override
    public void mousePress(MouseEvent e) {
        // Make sure the tap bar is the draggable one
        if (e.getY() < 50 && e.getY() > 10) {
            // Set dragging to true and store location
            drag = true;
            posX = e.getX();
            posY = e.getY();
        } else {
            // Otherwise set not dragging
            drag = false;
        }
    }

    // Override exiting draggable
    @Override
    public void mouseDrag(MouseEvent e) {
        // If frame is dragging
        if (drag) {
            // If frame is in fullscreen
            if (full) {
                // Set size and location to default
                frame.setSize(oldW, oldH);
                frame.setLocation(oldX, oldY);

                // Set full screen to false
                full = false;

                // Resize components
                resize();
            }

            // Set new frame location
            frame.setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
        }
    }

    /**
     * Toggle frame full screen
     */
    public static void doFullscreen() {
        // If is already full
        if (full) {
            // Set to original size and location
            frame.setSize(oldW, oldH);
            frame.setLocation(oldX, oldY);

            // Make full screen false
            full = false;
        } else { // Otherwise
            // Save current size and location
            oldW = frame.getWidth();
            oldH = frame.getHeight();
            oldX = frame.getX();
            oldY = frame.getY();

            // Set state to max
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);

            // Make full screen true
            full = true;
        }

        // Resize components
        resize();
    }

    // Getters and setters
    public static JFrame getFrame() {
        return frame;
    }

    public static void setFrame(JankFrame frame) {
        Jankcord.frame = frame;
    }

    public static JPanel getViewPanel() {
        return viewPanel;
    }

    public static void setViewPanel(JPanel viewPanel) {
        Jankcord.viewPanel = viewPanel;
    }

    public static JankTitleLabel getTitleLabel() {
        return titleLabel;
    }

    public static void setTitleLabel(JankTitleLabel titleLabel) {
        Jankcord.titleLabel = titleLabel;
    }

    public static WindowButtons getWindowButtons() {
        return windowButtons;
    }

    public static void setWindowButtons(WindowButtons windowButtons) {
        Jankcord.windowButtons = windowButtons;
    }

    public static ServerList getServerList() {
        return serverList;
    }

    public static void setServerList(ServerList serverList) {
        Jankcord.serverList = serverList;
    }

    public static ChannelList getChannelList() {
        return channelList;
    }

    public static void setChannelList(ChannelList channelList) {
        Jankcord.channelList = channelList;
    }

    public static ChatBoxArea getChatBoxArea() {
        return chatBoxArea;
    }

    public static void setChatBoxArea(ChatBoxArea chatBoxArea) {
        Jankcord.chatBoxArea = chatBoxArea;
    }

    public static String getOtherID() {
        return otherID;
    }

    public static void setOtherID(String otherID) {
        Jankcord.otherID = otherID;
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

    public static boolean getInServerCheck() {
        return inServerCheck;
    }

    public static void setInServerCheck(boolean inServerCheck) {
        Jankcord.inServerCheck = inServerCheck;
    }

    public static FullUser getFullUser() {
        return fullUser;
    }

    public static void setFullUser(FullUser fullUser) {
        Jankcord.fullUser = fullUser;
    }

    public static HashMap<Long, SimpleUserCache> getAvatarCache() {
        return avatarCache;
    }

    public static void setAvatarCache(HashMap<Long, SimpleUserCache> avatarCache) {
        Jankcord.avatarCache = avatarCache;
    }

    public static ArrayList<User> getTempFriends() {
        return tempFriends;
    }

    public static void setTempFriends(ArrayList<User> tempFriends) {
        Jankcord.tempFriends = tempFriends;
    }

    public static ArrayList<GroupChat> getTempGroupChats() {
        return tempGroupChats;
    }

    public static void setTempGroupChats(ArrayList<GroupChat> tempGroupChats) {
        Jankcord.tempGroupChats = tempGroupChats;
    }

    public static ArrayList<Message> getTempMessages() {
        return tempMessages;
    }

    public static void setTempMessages(ArrayList<Message> tempMessages) {
        Jankcord.tempMessages = tempMessages;
    }

    public static ArrayList<User> getTempMembers() {
        return tempMembers;
    }

    public static void setTempMembers(ArrayList<User> tempMembers) {
        Jankcord.tempMembers = tempMembers;
    }

    public static ScheduledExecutorService getSesFriend(){
        return sesFriend;
    }

    public static void setSesFriend(ScheduledExecutorService sesFriend){
        Jankcord.sesFriend = sesFriend;
    }

    public static ScheduledExecutorService getSesGroup(){
        return sesGroup;
    }

    public static void setSesGroup(ScheduledExecutorService sesGroup){
        Jankcord.sesGroup = sesGroup;
    }

    public static ScheduledExecutorService getSesMessage(){
        return sesMessage;
    }

    public static void setSesMessage(ScheduledExecutorService sesMessage){
        Jankcord.sesMessage = sesMessage;
    }
}
