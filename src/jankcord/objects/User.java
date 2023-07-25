package jankcord.objects;

// User, contains all relevant user information to a typical user
public class User {
    // Instance fields
    private long id;
    private String username;
    private String avatarURL;

    // Constructor to set all fields
    public User(long id, String username, String avatarURL) {
        // Set all fields
        this.id = id;
        this.username = username;
        this.avatarURL = avatarURL;
    }

    /**
     * Checks if two User objects are the same
     *
     * @param tempFriend to compare to
     * @return true/false depending on similarity
     */
    public boolean isEqual(User tempFriend) {
        // Check if each field is equal
        // If not, return false

        if (id != tempFriend.id) {
            return false;
        }
        if (!username.equals(tempFriend.getUsername())) {
            return false;
        }
        return avatarURL.equals(tempFriend.getAvatarURL());
    }

    /**
     * Override toString method to only return username
     *
     * @return username
     */
    @Override
    public String toString() {
        return username;
    }

    // Getters and setters
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
}
