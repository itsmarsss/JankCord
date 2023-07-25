package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.Message;
import jankcord.tools.JankFileKit;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetGroupMessages implements HttpHandler {
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

        String chatID = requestHeaders.get("otherID").get(0);

        // Message template
        final String MESSAGE = """
                {
                    "id": %s,
                    "content": "%s",
                    "timestamp": %s
                },
                """;

        // Users and messages builder
        StringBuilder users = new StringBuilder();
        StringBuilder messages = new StringBuilder();

        // If chat not stored in database
        if (!AdminDataBase.getGroupChats().containsKey(chatID)) {
            // Create File object
            File file = new File(AdminDataBase.getParent() + "/groupmessages/" + chatID + ".json");

            // If file exists
            if (file.exists()) {
                // Read file
                JankFileKit.readGroupMessages(chatID + ".json");
            } else { // Otherwise
                // Create it
                JankFileKit.create(file);
            }
        } else { // If stored
            // Loop each member and append to list
            for (long member : AdminDataBase.getGroupChats().get(chatID).getMembers()) {
                users.append(member).append(",");
            }

            // Loop each message and format then append to list
            for (Message msg : AdminDataBase.getGroupChats().get(chatID).getMessages()) {
                messages.append(MESSAGE.formatted(msg.getSenderID(), msg.getContent(), msg.getTimestamp()));
            }
        }

        // Main group messages JSON string and format it with friends builder
        String groupmessages = """
                {
                    "users": [%s],
                    "messages": [
                        %s
                    ],
                    "chatID": "%s",
                    "chatName": "%s",
                    "chatIconURL": "%s"
                }
                """.formatted(users.toString(), messages.toString(), chatID, AdminDataBase.getGroupChats().get(chatID).getChatName(), AdminDataBase.getGroupChats().get(chatID).getChatIconURL());

        // Write group messages
        JankFileKit.writeFile(AdminDataBase.getParent() + "/groupmessages/" + chatID + ".json", groupmessages);

        // If database doesn't contain this group chat
        if (!AdminDataBase.getGroupChats().containsKey(chatID)) {
            // Read it
            JankFileKit.readGroupMessages(chatID + ".json");
        }

        // Return group messages to client
        ServerCommunicator.sendResponse(exchange, groupmessages);
    }
}
