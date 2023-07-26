package jankcord.components.list.listitem;

public class JankListItem {
    // Instance fields for list item
    private String displayName;
    private String otherId;
    private boolean isGroupChat;

    public JankListItem(String displayName, String otherId, boolean isGroupChat) {
        // Set instance fields
        this.displayName = displayName;
        this.otherId = otherId;
        this.isGroupChat = isGroupChat;
    }

    @Override
    public String toString() {
        return (isGroupChat ? "# " : "@ ") + displayName;
    }

    // Getters and setters
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }
}
