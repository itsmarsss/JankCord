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
        //System.out.println("Group Messages send Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String chatID = requestHeaders.get("otherID").get(0);
        String content = requestHeaders.get("content").get(0);

        FullUser current = null;

        for (FullUser account : AdminDataBase.getAccounts()) {
            if (account.getUsername().equals(username)) {
                current = account;
            }
        }

        if(current == null) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }


        AdminDataBase.getGroupChats().get(chatID).getMessages().add(new Message(current.getId(), content, System.currentTimeMillis()));

        ServerCommunicator.sendResponse(exchange, "200");
    }
}
