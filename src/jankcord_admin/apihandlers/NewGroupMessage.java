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

public class NewGroupMessage implements HttpHandler {
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
        String chatID = requestHeaders.get("otherID").get(0);
        String content = requestHeaders.get("content").get(0);

        // If content is blank
        if(content.isBlank()) {
            // Return 405 not allowed response code
            ServerCommunicator.sendResponse(exchange, "405");
            // Return
            return;
        }

        // Current User object
        FullUser current = null;

        // Loop through all accounts
        for (FullUser account : AdminDataBase.getAccounts()) {
            // If usernames matches
            if (account.getUsername().equals(username)) {
                // Update current User object
                current = account;
            }
        }

        // If user unchanged
        if(current == null) {
            // Return 403 response code
            ServerCommunicator.sendResponse(exchange, "403");
            // Return
            return;
        }

        // Add message to corresponding chat
        AdminDataBase.getGroupChats().get(chatID).getMessages().add(new Message(current.getId(), content, System.currentTimeMillis()));

        // Return 200 response code
        ServerCommunicator.sendResponse(exchange, "200");
    }
}
