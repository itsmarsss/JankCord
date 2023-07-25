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

public class ConfigureAccount implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Check if user is authorized
        if (!JankcordAdmin.authorized(exchange)) {
            // If not authorized, return 403 response code
            ServerCommunicator.sendResponse(exchange, "403");
            // Return
            return;
        }

        // Get header
        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);

        String newUsername = requestHeaders.get("newUsername").get(0);
        String newPassword = requestHeaders.get("newPassword").get(0);
        String avatarURL = requestHeaders.get("avatarURL").get(0);

        // If username changed
        if (!username.equals(newUsername)) {
            // Check if is available
            if (!JankcordAdmin.usernameAvailable(newUsername)) {
                // If not, return 409 conflict response code
                ServerCommunicator.sendResponse(exchange, "409");
                // Return
                return;
            }
        }

        // If username is not valid
        if (JankcordAdmin.validateUsername(newUsername) != null) {
            // Return 404 response code
            ServerCommunicator.sendResponse(exchange, "404");
            // Return
            return;
        }

        // If password is not valid
        if (JankcordAdmin.validatePassword(newPassword) != null) {
            // Return 404 response code
            ServerCommunicator.sendResponse(exchange, "404");
            // Return
            return;
        }

        // Loop through all accounts
        for (FullUser account : AdminDataBase.getAccounts()) {
            // If username matches
            if (account.getUsername().equals(username)) {
                // Update username, password, and avatar url
                account.setUsername(newUsername);
                account.setPassword(newPassword);
                account.setAvatarURL(avatarURL);

                // Break loop
                break;
            }
        }

        // Write accounts
        JankcordAdmin.writeAccounts();

        // Return 200 response code
        ServerCommunicator.sendResponse(exchange, "200");
    }
}
