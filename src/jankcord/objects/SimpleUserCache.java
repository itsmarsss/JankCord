package jankcord.objects;

import jankcord.tools.ResourceLoader;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

// SimpleUserCache, caches user profile pictures
public class SimpleUserCache {
    // Instance fields
    // General user information
    private String username;
    private String avatarURL;

    // Different types of icons
    private Image avatar72;
    private Image avatar80;
    public Image avatarOriginal = circularize(ResourceLoader.loader.getTempProfileIcon().getImage());
    public static final Image temp72 = circularize(ResourceLoader.loader.getTempProfileIcon().getImage().getScaledInstance(72, 72, Image.SCALE_FAST));
    public static final Image temp80 = circularize(ResourceLoader.loader.getTempProfileIcon().getImage().getScaledInstance(80, 80, Image.SCALE_FAST));

    // Constructor to set general information
    public SimpleUserCache(String username, String avatarURL, Image avatar) {
        // Set general fields
        this.username = username;
        this.avatarURL = avatarURL;
        this.avatarOriginal = avatar;

        // Make cached icons circles
        this.avatar72 = circularize(avatar.getScaledInstance(72, 72, Image.SCALE_FAST));
        this.avatar80 = circularize(avatar.getScaledInstance(80, 80, Image.SCALE_FAST));
    }

    // Alternate constructor, empty one
    public SimpleUserCache() {
        // Reset caches to null
        this.avatar72 = null;
        this.avatar80 = null;
    }

    /**
     * Turns any shaped images into a circle
     *
     * @param originalImage image to circularize
     * @return image that is successfully or not successfully circularized
     */
    public static Image circularize(Image originalImage) {
        // Check if originalImage is an image via size
        if (originalImage.getWidth(null) <= 0 || originalImage.getHeight(null) <= 0) {
            // Return if not image
            return originalImage;
        }

        // Convert image to Buffered Image
        BufferedImage circularImage = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Graphics2D init
        Graphics2D g2 = circularImage.createGraphics();

        // Draw ellipse
        g2.setClip(new Ellipse2D.Float(0, 0, originalImage.getWidth(null), originalImage.getHeight(null)));
        g2.drawImage(originalImage, 0, 0, null);

        // Dispose of painter
        g2.dispose();

        // Return circularized image
        return circularImage;
    }

    // Getters and setters
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
        // If cache doesn't exist
        if (avatar72 == null) {
            // Return default
            return temp72;
        }

        // Return cache
        return avatar72;
    }

    public void setAvatar72(Image avatar72) {
        this.avatar72 = avatar72;
    }

    public Image getAvatar80() {
        // If cache doesn't exist
        if (avatar80 == null) {
            // Return default
            return temp80;
        }

        // Return cache
        return avatar80;
    }

    public void setAvatar80(Image avatar80) {
        this.avatar80 = avatar80;
    }

    public Image getAvatarOriginal() {
        return avatarOriginal;
    }

    public void setAvatarOriginal(Image avatarOriginal) {
        this.avatarOriginal = avatarOriginal;

        // Make cached icons circles
        this.avatar72 = circularize(avatarOriginal.getScaledInstance(72, 72, Image.SCALE_FAST));
        this.avatar80 = circularize(avatarOriginal.getScaledInstance(80, 80, Image.SCALE_FAST));

    }
}
