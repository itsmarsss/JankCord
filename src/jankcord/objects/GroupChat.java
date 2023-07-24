package jankcord.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupChat {
    private String id;
    private String chatName;
    private String chatIconURL;
    private ArrayList<Long> members;
    private ArrayList<Message> messages;

    public GroupChat(String id, ArrayList<Long> members, ArrayList<Message> messages) {
        this.id = id;
        this.chatName = "Unnamed Group Chat";
        this.members = members;
        this.messages = messages;
    }

    public GroupChat(String id, String chatName, String chatIconURL) {
        this.id = id;
        this.chatName = chatName;
        this.chatIconURL = chatIconURL;
    }

    public boolean isEqual(GroupChat tempGroupChat) {
        if(!id.equals(tempGroupChat.getId())) {
            return false;
        }
        if(!chatName.equals(tempGroupChat.getChatName())) {
            return false;
        }
        return chatIconURL.equals(tempGroupChat.getChatIconURL());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatIconURL() {
        return chatIconURL;
    }

    public void setChatIconURL(String chatIconURL) {
        this.chatIconURL = chatIconURL;
    }

    public ArrayList<Long> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Long> members) {
        this.members = members;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
