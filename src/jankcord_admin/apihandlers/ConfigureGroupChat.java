package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigureGroupChat implements HttpHandler {
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

        String chatID = requestHeaders.get("chatID").get(0);
        String newChatName = requestHeaders.get("newChatName").get(0);
        String newChatIconURL = requestHeaders.get("newChatIconURL").get(0);

        // Check if new chat name is valid, must be headerable and no longer than 20
        if(ServerCommunicator.notHeaderable(newChatName) || newChatName.length() > 20) {
            // Return 404 response code
            ServerCommunicator.sendResponse(exchange, "404");
            // Return
            return;
        }

        // Check if group chat exists
        if (!AdminDataBase.getGroupChats().containsKey(chatID)) {
            // If not
            // Return 404 response code
            ServerCommunicator.sendResponse(exchange, "404");
            // Return
            return;
        }

        // Set chat name and icon
        AdminDataBase.getGroupChats().get(chatID).setChatName(newChatName);
        AdminDataBase.getGroupChats().get(chatID).setChatIconURL(newChatIconURL);

        // Return 200 response code
        ServerCommunicator.sendResponse(exchange, "200");
    }
}
