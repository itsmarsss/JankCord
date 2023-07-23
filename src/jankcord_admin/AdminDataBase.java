package jankcord_admin;

import jankcord.objects.FullUser;
import jankcord.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminDataBase {
    private static final ArrayList<FullUser> accounts = new ArrayList<>();
    private static final HashMap<String, ArrayList<Message>> conversations = new HashMap<>();
    private static String parent;


    public static ArrayList<FullUser> getAccounts() {
        return accounts;
    }

    public static HashMap<String, ArrayList<Message>> getConversations() {
        return conversations;
    }

    public static String getParent() {
        return parent;
    }

    public static void setParent(String newParent) {
        parent = newParent;
    }
}
