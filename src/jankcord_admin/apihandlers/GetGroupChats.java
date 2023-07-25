package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.GroupChat;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetGroupChats implements HttpHandler {
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

        // Group chat builder
        StringBuilder groupChatsList = new StringBuilder();

        // Loop through all group chat
        for (Map.Entry<String, GroupChat> entry : AdminDataBase.getGroupChats().entrySet()) {
            // Store version of group chat
            GroupChat gc = entry.getValue();

            // Loop through all members of group chat
            for (long member : gc.getMembers()) {
                // If client is part of this group chat
                if (member == currentID) {
                    // Append JSON formatted group chat including id, name, and iconURL
                    groupChatsList.append("""
                            {
                                "chatID": "%s",
                                "chatName": "%s",
                                "chatIconURL": "%s"
                            },
                            """.formatted(gc.getId(), gc.getChatName(), gc.getChatIconURL()));
                    // Break Loops
                    break;
                }
            }
        }

        // Main group chats JSON string and format it with group chats builder
        String groupChats = """
                {
                    "groupChats": [
                        %s
                    ]
                }
                """.formatted(groupChatsList.toString());

        // Return group chats to client
        ServerCommunicator.sendResponse(exchange, groupChats);
    }
}
