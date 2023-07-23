package jankcord.tools;

import java.util.Base64;

public class Base64Helper {
    public static String sToBase64(String decodedString) {
        // encode
        String encodedString = Base64.getEncoder().encodeToString(decodedString.getBytes());

        return encodedString;
    }

    public static String b64ToString(String encodedString) {
        // decode
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);

        return decodedString;
    }
}
