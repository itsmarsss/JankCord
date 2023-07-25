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
        // System.out.println("Create gc Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String users = requestHeaders.get("users").get(0);

        String[] memberList = users.split(",");

        ArrayList<Long> memberListLong = new ArrayList<>();

        for(String member : memberList) {
            memberListLong.add(Long.parseLong(member));
        }

        String chatID = UUID.randomUUID().toString();

        AdminDataBase.getGroupChats().put(chatID, new GroupChat(chatID, memberListLong, new ArrayList<>()));

        ServerCommunicator.sendResponse(exchange, "200");
    }
}
