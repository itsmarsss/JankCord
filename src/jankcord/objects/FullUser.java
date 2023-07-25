package jankcord.objects;

// FullUser, contains all user information, child of User
public class FullUser extends User {
    // Instance fields, built upon user: password, endPointHost, and status
    private String password;
    private String endPointHost;
    private String status;

    // Constructor, set everything but status
    public FullUser(long id, String username, String avatarURL, String password, String endPointHost) {
        // Super; set id, username, and avatarURL
        super(id, username, avatarURL);

        // Set password and endPointHost
        this.password = password;
        this.endPointHost = endPointHost;
    }

    // Alternate constructor, set everything
    public FullUser(long id, String username, String avatarURL, String password, String endPointHost, String status) {
        // Super; set id, username, and avatarURL
        super(id, username, avatarURL);

        // Set remaining values
        this.endPointHost = endPointHost;
        this.password = password;
        this.status = status;
    }

    // Getters and setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEndPointHost() {
        return endPointHost;
    }

    public void setEndPointHost(String endPointHost) {
        this.endPointHost = endPointHost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
