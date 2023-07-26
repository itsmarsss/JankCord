package jankcord.objects;

import java.util.ArrayList;

// GroupChat, contains all group chat information
public class GroupChat {
    // Instance fields
    private String id;
    private String chatName;
    private String chatIconURL;
    private ArrayList<Long> members;
    private ArrayList<Message> messages;

    // Constructor to set all values
    public GroupChat(String id, String chatName, String chatIconURL, ArrayList<Long> members, ArrayList<Message> messages) {
        // Set all fields
        this.id = id;
        this.chatName = chatName;
        this.chatIconURL = chatIconURL;
        this.members = members;
        this.messages = messages;
    }

    // Alternate constructor, set only id, members, and messages
    public GroupChat(String id, ArrayList<Long> members, ArrayList<Message> messages) {
        // Set all fields
        this.id = id;
        this.chatName = "Unnamed Group Chat"; // Placeholder, cannot be empty
        this.chatIconURL = ""; // Better than "null"
        this.members = members;
        this.messages = messages;
    }

    // Alternate constructor, set only id, chatname, and chaticon
    public GroupChat(String id, String chatName, String chatIconURL) {
        // Set all fields
        this.id = id;
        this.chatName = chatName;
        this.chatIconURL = chatIconURL;
    }

    /**
     * Checks if two GroupChat objects are the same
     *
     * @param tempGroupChat GroupChat to be compared with
     * @return true/false depending on similarity
     */
    public boolean isEqual(GroupChat tempGroupChat) {
        // Check if each field is equal
        // If not, return false

        if(!id.equals(tempGroupChat.getId())) {
            return false;
        }
        if(!chatName.equals(tempGroupChat.getChatName())) {
            return false;
        }
        return chatIconURL.equals(tempGroupChat.getChatIconURL());
    }

    /**
     * Override toString method to only return chat name
     *
     * @return string chatName
     */
    @Override
    public String toString() {
        return chatName;
    }

    // Getters and setters
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
