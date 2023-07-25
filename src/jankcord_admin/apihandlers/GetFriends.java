package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetFriends implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Friends Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        StringBuilder friendsList = new StringBuilder();

        for (FullUser account : AdminDataBase.getAccounts()) {
            friendsList.append("""
                    {
                        "id": %s,
                        "username": "%s",
                        "avatarURL": "%s"
                    },
                    """.formatted(account.getId(), account.getUsername(), account.getAvatarURL()));
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);

        long currentID = -1;
        for (FullUser account : AdminDataBase.getAccounts()) {
            if (account.getUsername().equals(username)) {
                currentID = account.getId();
            }
        }

        if (currentID == -1) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        String friends = """
                {
                    "friends": [
                        %s
                    ]
                }
                """.formatted(friendsList.toString());

        ServerCommunicator.sendResponse(exchange, friends);
    }
}
