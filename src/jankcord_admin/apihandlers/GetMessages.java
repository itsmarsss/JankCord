package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.Message;
import jankcord.tools.JankFileKit;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetMessages implements HttpHandler {
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

        // Get current and other User object
        FullUser current = null;
        FullUser other = null;

        // Loop through all user
        for (FullUser account : AdminDataBase.getAccounts()) {
            // If username matches
            if (account.getUsername().equals(username)) {
                // Update current User
                current = account;
            }

            // If id matches
            if (String.valueOf(account.getId()).equals(otherID)) {
                // Update other User
                other = account;
            }
        }

        // If current user is non-existent
        if (current == null) {
            // Return 403 response code
            ServerCommunicator.sendResponse(exchange, "403");
            // Return
            return;
        }

        // If other user is non-existent
        if (other == null) {
            // Return 404 response code
            ServerCommunicator.sendResponse(exchange, "404");
            // Return
            return;
        }

        /// Get id of users
        long otherIDNum = Long.parseLong(otherID);
        long currentIDNum = current.getId();

        // Get file name [<smaller id>-<larger id>.json]
        String fileName = Math.min(otherIDNum, currentIDNum) + "-" + Math.max(otherIDNum, currentIDNum);

        // Message template
        final String MESSAGE = """
                {
                    "id": %s,
                    "content": "%s",
                    "timestamp": %s
                },
                """;

        // Messages builder
        StringBuilder messages = new StringBuilder();

        // If chat not stored in database
        if (!AdminDataBase.getConversations().containsKey(fileName)) {
            // Create File object
            File file = new File(AdminDataBase.getParent() + "/messages/" + fileName + ".json");

            // If file exists
            if (file.exists()) {
                // Read file
                JankFileKit.readMessages(fileName + ".json");
            } else { // Otherwise
                JankFileKit.create(file);
            }
        } else { // If stored
            // Loop each message and append to list
            for (Message msg : AdminDataBase.getConversations().get(fileName)) {
                messages.append(MESSAGE.formatted(msg.getSenderID(), msg.getContent(), msg.getTimestamp()));
            }
        }

        // Main messages JSON string and format it with friends builder
        String dmMessages = """
                {
                    "users": [%s, %s],
                    "messages": [
                        %s
                    ]
                }
                """.formatted(currentIDNum, otherIDNum, messages.toString());

        // Write messages
        JankFileKit.writeFile(AdminDataBase.getParent() + "/messages/" + fileName + ".json", dmMessages);

        // Ud database doesn't contain this chat
        if (!AdminDataBase.getConversations().containsKey(fileName)) {
            // Read it
            JankFileKit.readMessages(fileName + ".json");
        }

        // Return dm messages to client
        ServerCommunicator.sendResponse(exchange, dmMessages);
    }
}
