package jankcord.tools;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// Add client <-> server communication related Helper
public class ServerCommunicator {
    /**
     * Sends a http request for client
     *
     * @param apiEndpoint endpoint url
     * @param header      header values
     * @return string response from server
     */
    public static String sendHttpRequest(String apiEndpoint, HashMap<String, String> header) {
        // Try to send request
        try {
            // Open a connection
            URL url = new URL(apiEndpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set method and timeouts
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Set headers
            for (Map.Entry<String, String> entry : header.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            // If response code is good
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // String builder
                StringBuilder response = new StringBuilder();
                String line;

                // Loop through lines
                while ((line = reader.readLine()) != null) {
                    // Add to builder
                    response.append(line);
                }

                // Make sure to close reader
                reader.close();

                // Return response
                return response.toString();
            } else {
                // Return null for not ok response
                return null;
            }
        } catch (Exception e) {
            // Return null for error
            return null;
        }
    }

    /**
     * Sends a response for server
     *
     * @param exchange http exchange
     * @param response response to send
     * @throws IOException if error
     */
    public static void sendResponse(HttpExchange exchange, String response) throws IOException {
        // Set return type
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length());

        // Write response
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    /**
     * Checks if a string can be used a header
     *
     * @param string string to check headerable
     * @return true/false depending on how headerable string is
     */
    public static boolean notHeaderable(String string) {
        // Allowed characters
        final String CHECK = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_ :;.,\\/\"'?!(){}[]@<>=-+*#$&`|~^%";

        // Loop through each character of string
        for (char c : string.toCharArray()) {
            // If character invalid
            if (!CHECK.contains(c + "")) {
                // Return false
                return true;
            }
        }

        // If for loop does latch onto something, return true
        return false;
    }
}
