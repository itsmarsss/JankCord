package jankcord.objects;

// Message, contains all message information
public class Message {
    // Instance fields
    private long senderID;
    private String content;
    private long timestamp;

    // Constructor, set all fields
    public Message(long senderID, String content, long timestamp) {
        // Set all fields
        this.senderID = senderID;
        this.content = content;
        this.timestamp = timestamp;
    }

    /**
     * Checks if two Message objects are the same
     *
     * @param tempMessage Message to be compared with
     * @return true/false depending on similarity
     */
    public boolean isEqual(Message tempMessage) {
        if(!content.equals(tempMessage.getContent())) {
            return false;
        }
        return timestamp == tempMessage.getTimestamp();
    }

    // Getters and setters
    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
