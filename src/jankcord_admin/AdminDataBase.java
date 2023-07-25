package jankcord_admin;

import jankcord.objects.FullUser;
import jankcord.objects.GroupChat;
import jankcord.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;

// Database for running JankCord Admin instance
public class AdminDataBase {
    // Private static fields
    private static ArrayList<FullUser> accounts = new ArrayList<>();
    private static HashMap<String, ArrayList<Message>> conversations = new HashMap<>();
    private static HashMap<String, GroupChat> groupChats = new HashMap<>();
    private static String parent;


    // Getters and setters
    public static ArrayList<FullUser> getAccounts() {
        return accounts;
    }

    public static void setAccounts(ArrayList<FullUser> accounts) {
        AdminDataBase.accounts = accounts;
    }

    public static HashMap<String, ArrayList<Message>> getConversations() {
        return conversations;
    }

    public static void setConversations(HashMap<String, ArrayList<Message>> conversations) {
        AdminDataBase.conversations = conversations;
    }

    public static HashMap<String, GroupChat> getGroupChats() {
        return groupChats;
    }

    public static void setGroupChats(HashMap<String, GroupChat> groupChats) {
        AdminDataBase.groupChats = groupChats;
    }

    public static String getParent() {
        return parent;
    }

    public static void setParent(String newParent) {
        parent = newParent;
    }
}
