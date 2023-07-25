package jankcord.tools;

import java.util.Base64;

// Base 64 related helper
public class Base64Helper {
    /**
     * Encodes a decoded string
     *
     * @param decodedString string to encode
     * @return string encoded version of decodedString
     */
    public static String encode(String decodedString) {
        // Encode and return
        return Base64.getEncoder().encodeToString(decodedString.getBytes());
    }

    /**
     * Decodes an encoded string
     *
     * @param encodedString string to decode
     * @return string decoded version of encodedString
     */
    public static String decode(String encodedString) {
        // Try decode
        try {
            // Decode
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);

            // Return decoded
            return new String(decodedBytes);
        } catch (Exception e) { // Unsuccessful
            // Return original string
            return encodedString;
        }
    }
}
