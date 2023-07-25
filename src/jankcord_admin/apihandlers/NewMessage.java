package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.Message;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NewMessage implements HttpHandler {
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
        String otherID = requestHeaders.get("otherID").get(0);
        String content = requestHeaders.get("content").get(0);

        // If content is blank
        if (content.isBlank()) {
            // Return 405 not allowed response code
            ServerCommunicator.sendResponse(exchange, "405");
            // Return
            return;
        }

        // Current and other User object
        FullUser current = null;
        FullUser other = null;

        // Loop through all accounts
        for (FullUser account : AdminDataBase.getAccounts()) {
            // If usernames matches
            if (account.getUsername().equals(username)) {
                // Update current User object
                current = account;
            }

            // If id matches
            if (String.valueOf(account.getId()).equals(otherID)) {
                // Update other User object
                other = account;
            }
        }

        // If current user not exist
        if (current == null) {
            // Return 403 response code
            ServerCommunicator.sendResponse(exchange, "403");
            // Return
            return;
        }

        // If other user not exist
        if (other == null) {
            // Return 404 response code
            ServerCommunicator.sendResponse(exchange, "404");
            return;
        }

        // Get current and other users id
        long otherIDNum = Long.parseLong(otherID);
        long currentIDNum = current.getId();

        // Get file name [<smaller id>-<larger id>.json]
        String fileName = Math.min(otherIDNum, currentIDNum) + "-" + Math.max(otherIDNum, currentIDNum);

        // Add message to corresponding chat
        AdminDataBase.getConversations().get(fileName).add(new Message(current.getId(), content, System.currentTimeMillis()));

        // Return 200 response code
        ServerCommunicator.sendResponse(exchange, "200");
    }
}
