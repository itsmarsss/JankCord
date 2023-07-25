package jankcord_admin;

import jankcord.objects.FullUser;
import jankcord.objects.GroupChat;
import jankcord.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;

// Database for running JankCord Admin instance
public class AdminDataBase {
    // Private static fields
    private static final ArrayList<FullUser> accounts = new ArrayList<>();
    private static final HashMap<String, ArrayList<Message>> conversations = new HashMap<>();
    private static final HashMap<String, GroupChat> groupChats = new HashMap<>();
    private static String parent;


    // Getters
    public static ArrayList<FullUser> getAccounts() {
        return accounts;
    }

    public static HashMap<String, ArrayList<Message>> getConversations() {
        return conversations;
    }

    public static HashMap<String, GroupChat> getGroupChats() {
        return groupChats;
    }

    public static String getParent() {
        return parent;
    }

    // Setters
    public static void setParent(String newParent) {
        parent = newParent;
    }
}
