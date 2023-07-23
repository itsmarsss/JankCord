package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.User;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CreateGroupChat implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Create gc Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String users = requestHeaders.get("users").get(0);

        String[] memberList = users.split(",");

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
