package jankcord.objects;

public class User {
    private long id;
    private String username;
    private String avatarURL;

    public User(long id, String username, String avatarURL) {
        this.id = id;
        this.username = username;
        this.avatarURL = avatarURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public boolean isEqual(User tempFriend) {
        if (id != tempFriend.id) {
            return false;
        }
        if (!username.equals(tempFriend.getUsername())) {
            return false;
        }
        return avatarURL.equals(tempFriend.getAvatarURL());
    }
}
