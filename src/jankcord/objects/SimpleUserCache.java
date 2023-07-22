package jankcord.objects;

import jankcord.ResourceLoader;

import java.awt.*;

public class SimpleUserCache {
    private String username;
    private String avatarURL;
    private Image avatar72;
    private Image avatar80;
    private static Image temp72 = ResourceLoader.loader.getTempProfileIcon().getImage().getScaledInstance(72, 72, Image.SCALE_FAST);
    private static Image temp80 = ResourceLoader.loader.getTempProfileIcon().getImage().getScaledInstance(80, 80, Image.SCALE_FAST);

    public SimpleUserCache(String username, String avatarURL, Image avatar) {
        this.username = username;
        this.avatarURL = avatarURL;
        this.avatar72 = avatar.getScaledInstance(72, 72, Image.SCALE_FAST);
        this.avatar80 = avatar.getScaledInstance(80, 80, Image.SCALE_FAST);
    }

    public SimpleUserCache() {
        this.avatar72 = null;
        this.avatar80 = null;
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

    public Image getAvatar72() {
        if (avatar72 == null) {
            return temp72;
        }
        return avatar72;
    }

    public void setAvatar72(Image avatar72) {
        this.avatar72 = avatar72;
    }

    public Image getAvatar80() {
        if (avatar80 == null) {
            return temp80;
        }
        return avatar80;
    }

    public void setAvatar80(Image avatar80) {
        this.avatar80 = avatar80;
    }
}
