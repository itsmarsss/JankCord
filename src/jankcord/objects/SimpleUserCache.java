package jankcord.objects;

import java.awt.*;

public class SimpleUserCache {
    private String username;
    private String avatarURL;
    private Image avatar;

    public SimpleUserCache(String username, String avatarURL, Image avatar) {
        this.username = username;
        this.avatarURL = avatarURL;
        this.avatar = avatar;
    }

    public SimpleUserCache(Image avatar) {
        this.avatar = avatar;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
