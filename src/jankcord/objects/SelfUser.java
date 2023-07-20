package jankcord.objects;

import java.util.Base64;

public class SelfUser extends User {
    private String password;
    private String endPointHost;

    public SelfUser(long id, String username, String avatarURL, String password, String endPointHost) {
        super(id, username, avatarURL);

        this.password = password;
        this.endPointHost = decode(endPointHost) + "/api/v1/";
    }

    private String decode(String endPointHost) {
        byte[] decodedBytes = Base64.getDecoder().decode(endPointHost);
        String decodedString = new String(decodedBytes);

        return decodedString;
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
