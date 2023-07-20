package jankcord.objects;

public class SelfUser extends User {
    private String password;
    private String endPointHost;

    public SelfUser(long id, String username, String avatarURL, String password, String endPointHost) {
        super(id, username, avatarURL);

        this.password = password;
        this.endPointHost = endPointHost;
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
}
