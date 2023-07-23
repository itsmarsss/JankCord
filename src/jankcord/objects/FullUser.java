package jankcord.objects;

public class FullUser extends User {
    private String password;
    private String endPointHost;
    private String status;

    public FullUser(long id, String username, String avatarURL, String password, String endPointHost) {
        super(id, username, avatarURL);

        this.password = password;
        this.endPointHost = endPointHost;
    }

    public FullUser(long id, String username, String avatarURL, String password, String endPointHost, String status) {
        super(id, username, avatarURL);

        this.password = password;
        this.status = status;
    }

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
