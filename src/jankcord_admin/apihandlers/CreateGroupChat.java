package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.GroupChat;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateGroupChat implements HttpHandler {
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

        String users = requestHeaders.get("users").get(0);

        // Get member list array
        String[] memberList = users.split(",");

        // Member list long
        ArrayList<Long> memberListLong = new ArrayList<>();

        // Convert all string to long
        for(String member : memberList) {
            memberListLong.add(Long.parseLong(member));
        }

        // Generate unique chat id
        String chatID = UUID.randomUUID().toString();

        // Add chat to database
        AdminDataBase.getGroupChats().put(chatID, new GroupChat(chatID, memberListLong, new ArrayList<>()));

        // Return 200 response code
        ServerCommunicator.sendResponse(exchange, "200");
    }
}
