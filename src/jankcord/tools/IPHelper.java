package jankcord.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class IPHelper {
    public static String getPrivateIP() {
        // Find private IP address
        String privateIP;

        try {
            InetAddress localhost = InetAddress.getLocalHost();

            privateIP = localhost.getHostAddress().trim();
        } catch (Exception e) {
            privateIP = "Unable to obtain private ip.";
        }

        return privateIP;
    }

    public static String getPublicIP() {
        // Find public IP address
        String publicIP;

        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));

            // reads system IPAddress
            publicIP = sc.readLine().trim();

            sc.close();
        } catch (Exception e) {
            publicIP = "Unable to obtain public ip.";
        }

        return publicIP;
    }
}
