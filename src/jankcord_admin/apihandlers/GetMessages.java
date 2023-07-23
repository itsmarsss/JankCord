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
        //System.out.println("Messages Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String otherID = requestHeaders.get("otherID").get(0);

        FullUser current = null;
        FullUser other = null;

        for (FullUser account : AdminDataBase.getAccounts()) {
            if (account.getUsername().equals(username)) {
                current = account;
            }

            if (String.valueOf(account.getId()).equals(otherID)) {
                other = account;
            }
        }

        if (current == null || other == null) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        long otherIDNum = Long.parseLong(otherID);
        long currentIDNum = current.getId();

        String fileName = Math.min(otherIDNum, currentIDNum) + "-" + Math.max(otherIDNum, currentIDNum);

        String message = """
                {
                    "id": %s,
                    "content": "%s",
                    "timestamp": %s
                },
                """;

        StringBuilder messages = new StringBuilder("");

        if (!AdminDataBase.getConversations().containsKey(fileName)) {
            File file = new File(AdminDataBase.getParent() + "/messages/" + fileName + ".json");
            if (file.exists()) {
                JankFileKit.readMessages(fileName + ".json");
            } else {
                JankFileKit.create(file);
            }
        } else {
            for (Message msg : AdminDataBase.getConversations().get(fileName)) {
                messages.append(message.formatted(msg.getSenderID(), msg.getContent(), msg.getTimestamp()));
            }
        }

        String textJSON = """
                {
                    "users": [
                        {
                            "id": %s,
                            "username": "%s",
                            "avatarURL": "%s"
                        },
                        {
                            "id": %s,
                            "username": "%s",
                            "avatarURL": "%s"
                        }
                    ],
                    "messages": [
                        %s
                    ]
                }
                """.formatted(currentIDNum, current.getUsername(), current.getAvatarURL(), otherIDNum, other.getUsername(), other.getAvatarURL(), messages.toString());

        JankFileKit.writeFile(AdminDataBase.getParent() + "/messages/" + fileName + ".json", textJSON);

        if (!AdminDataBase.getConversations().containsKey(fileName)) {
            JankFileKit.readMessages(fileName + ".json");
        }

        ServerCommunicator.sendResponse(exchange, textJSON);
    }
}
