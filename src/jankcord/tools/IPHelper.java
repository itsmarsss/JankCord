package jankcord.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

// IP related Helper
public class IPHelper {
    /**
     * Gets device's private IP
     *
     * @return string of private ip
     */
    public static String getPrivateIP() {
        // Find private IP address
        String privateIP;

        // Try method
        try {
            // Use InetAddress
            InetAddress localhost = InetAddress.getLocalHost();

            // Get host address
            privateIP = localhost.getHostAddress().trim();
        } catch (Exception e) {
            // Unable to obtain
            privateIP = "Unable to obtain private ip.";
        }

        // Return private IP
        return privateIP;
    }

    /**
     * Get device's public IP
     *
     * @return string of public ip
     */
    public static String getPublicIP() {
        // Find public IP address
        String publicIP;

        //  Try method
        try {
            // Use site to get public ip
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            // Read response
            BufferedReader br = new BufferedReader(new InputStreamReader(url_name.openStream()));

            // Reads IP address
            publicIP = br.readLine().trim();

            // Close reader
            br.close();
        } catch (Exception e) {
            // Unable to obtain
            publicIP = "Unable to obtain public ip.";
        }

        // Return public IP
        return publicIP;
    }
}
