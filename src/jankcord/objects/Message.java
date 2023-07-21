package jankcord.objects;

public class Message {
    private User sender;
    private String content;
    private long timestamp;

    public Message(User sender, String content, long timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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
        if(!sender.isEqual(tempMessage.getSender())) {
            return false;
        }
        if(!content.equals(tempMessage.getContent())) {
            return false;
        }
        if(timestamp != tempMessage.getTimestamp()) {
            return false;
        }

        return true;
    }
}
