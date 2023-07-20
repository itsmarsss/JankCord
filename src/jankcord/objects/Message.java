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
}
