package jankcord.tools;

import java.util.Base64;

public class Base64Helper {
    public static String encode(String decodedString) {
        // encode
        String encodedString = Base64.getEncoder().encodeToString(decodedString.getBytes());

        return encodedString;
    }

    public static String decode(String encodedString) {
        // decode
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);

        return decodedString;
    }
}
