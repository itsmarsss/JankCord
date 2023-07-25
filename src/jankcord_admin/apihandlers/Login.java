package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Login implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Set default response to 403 response code
        String response = "403";

        // Check if user is authorized
        if (JankcordAdmin.authorized(exchange)) {
            // Get header
            Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

            String username = requestHeaders.get("username").get(0);

            // Find user
            FullUser user = null;

            // Loop through all users
            for (FullUser account : AdminDataBase.getAccounts()) {
                // If usernames match
                if (account.getUsername().equals(username)) {
                    // Update User
                    user = account;
                }
            }

            // If user not updated
            if (user == null) {
                // Return 403 response code
                ServerCommunicator.sendResponse(exchange, "403");
                // Return
                return;
            }

            // Update response including user id and avatarURL
            response = """
                    {
                        "id": %s,
                        "avatarURL": "%s"
                    }
                    """.formatted(user.getId(), user.getAvatarURL());
        }

        // Return response to client
        ServerCommunicator.sendResponse(exchange, response);
    }
}
