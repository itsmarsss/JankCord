package jankcord.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupChat {
    private long id;
    private ArrayList<Message> messages;

    public GroupChat(long id, ArrayList<Message> messages) {
        this.id = id;
        this.messages = messages;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
