package jankcord.objects;

import java.util.ArrayList;

public class Channel {
    private ArrayList<User> users;
    private ArrayList<Message> messages;

    public Channel(ArrayList<User> users, ArrayList<Message> messages) {
        this.users = users;
        this.messages = messages;
    }
}
