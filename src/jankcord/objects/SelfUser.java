package jankcord.objects;

public class SelfUser extends User {
    private String password;

    public SelfUser(long id, String username, String avatarURL, String password) {
        super(id, username, avatarURL);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
