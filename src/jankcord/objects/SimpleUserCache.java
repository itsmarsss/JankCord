package jankcord.objects;

import jankcord.tools.ResourceLoader;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class SimpleUserCache {
    private String username;
    private String avatarURL;
    private Image avatar72;
    private Image avatar80;
    public static Image avatarOriginal = circularize(ResourceLoader.loader.getTempProfileIcon().getImage());
    public static final Image temp72 = circularize(ResourceLoader.loader.getTempProfileIcon().getImage().getScaledInstance(72, 72, Image.SCALE_FAST));
    public static final Image temp80 = circularize(ResourceLoader.loader.getTempProfileIcon().getImage().getScaledInstance(80, 80, Image.SCALE_FAST));

    public SimpleUserCache(String username, String avatarURL, Image avatar) {
        this.username = username;
        this.avatarURL = avatarURL;
        this.avatarOriginal = avatar;
        this.avatar72 = circularize(avatar.getScaledInstance(72, 72, Image.SCALE_FAST));
        this.avatar80 = circularize(avatar.getScaledInstance(80, 80, Image.SCALE_FAST));
    }

    public SimpleUserCache() {
        this.avatar72 = null;
        this.avatar80 = null;
    }

    public static Image circularize(Image originalImage) {
        if(originalImage.getWidth(null) <= 0 || originalImage.getHeight(null) <= 0) {
            return originalImage;
        }

        BufferedImage circularImage = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImage.createGraphics();
        g2.setClip(new Ellipse2D.Float(0, 0, originalImage.getWidth(null), originalImage.getHeight(null)));
        g2.drawImage(originalImage, 0, 0, null);
        g2.dispose();

        return circularImage;
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

    public Image getAvatarOrginal() {
        return avatarOriginal;
    }

    public void setAvatarOrginal(Image avatarOriginal) {
        this.avatarOriginal = avatarOriginal;
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
