package jankcord.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupChat {
    private String id;
    private ArrayList<User> members;
    private ArrayList<Message> messages;

    public GroupChat(String id, ArrayList<User> members, ArrayList<Message> messages) {
        this.id = id;
        this.members = members;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
