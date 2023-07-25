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

public class GetFriends implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Check if user is authorized
        if (!JankcordAdmin.authorized(exchange)) {
            // If not authorized, return 403 response code
            ServerCommunicator.sendResponse(exchange, "403");
            // Return
            return;
        }

        // Friends builder
        StringBuilder friendsList = new StringBuilder();

        // Loop through all accounts
        for (FullUser account : AdminDataBase.getAccounts()) {
            // Append JSON formatted friend including id, username, and avatarURL
            friendsList.append("""
                    {
                        "id": %s,
                        "username": "%s",
                        "avatarURL": "%s"
                    },
                    """.formatted(account.getId(), account.getUsername(), account.getAvatarURL()));
        }

        // Get header
        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);

        // Find current account ID, set to -1
        long currentID = -1;

        // Loop through all account
        for (FullUser account : AdminDataBase.getAccounts()) {
            // If username matches
            if (account.getUsername().equals(username)) {
                // Update current ID
                currentID = account.getId();
            }
        }

        // If account ID is -1 (unchanged)
        if (currentID == -1) {
            // Return 403 response code
            ServerCommunicator.sendResponse(exchange, "403");
            // Return
            return;
        }

        // Main friends JSON string and format it with friends builder
        String friends = """
                {
                    "friends": [
                        %s
                    ]
                }
                """.formatted(friendsList.toString());

        // Return friends to client
        ServerCommunicator.sendResponse(exchange, friends);
    }
}
