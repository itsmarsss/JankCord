package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.Message;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NewMessage implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Messages send Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String otherID = requestHeaders.get("otherID").get(0);
        String content = requestHeaders.get("content").get(0);

        FullUser current = null;
        FullUser other = null;

        for (FullUser account : JankcordAdmin.getAccounts()) {
            if (account.getUsername().equals(username)) {
                current = account;
            }

            if(String.valueOf(account.getId()).equals(otherID)) {
                other = account;
            }
        }

        if(current == null || other == null) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        long otherIDNum = Long.parseLong(otherID);
        long currentIDNum = current.getId();

        String fileName = Math.min(otherIDNum, currentIDNum) + "-" + Math.max(otherIDNum, currentIDNum);

        JankcordAdmin.getConversations().get(fileName).add(new Message(current.getId(), content, System.currentTimeMillis()));

        ServerCommunicator.sendResponse(exchange, "200");
    }
}
