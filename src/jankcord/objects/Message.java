package jankcord.objects;

public class Message {
    private long senderID;
    private String content;
    private long timestamp;

    public Message(long senderID, String content, long timestamp) {
        this.senderID = senderID;
        this.content = content;
        this.timestamp = timestamp;
    }

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

    public boolean isEqual(Message tempMessage) {
        if(!content.equals(tempMessage.getContent())) {
            return false;
        }
        if(timestamp != tempMessage.getTimestamp()) {
            return false;
        }

        return true;
    }
}
