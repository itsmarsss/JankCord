package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.Message;
import jankcord_admin.JankcordAdmin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class GetMessages implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!JankcordAdmin.authorized(exchange)) {
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("403".getBytes());
            outputStream.close();

            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String otherID = requestHeaders.get("otherID").get(0);
        String currentID = "";

        for (FullUser account : JankcordAdmin.accounts) {
            if (account.getUsername().equals(username)) {
                currentID = String.valueOf(account.getId());
            }
        }

        long otherIDNum = Long.parseLong(otherID);
        long currentIDNum = Long.parseLong(otherID);

        String fileName = Math.min(otherIDNum, currentIDNum) + "-" + Math.max(otherIDNum, currentIDNum);

        String message = """
                {
                    "id": %s,
                    "username": "%s",
                    "avatarURL": "%s",
                    "content": "%s",
                    "timestamp": %s
                },
                """;

        String messages = "";

        if (!JankcordAdmin.conversations.containsKey(fileName)) {
            messages = JankcordAdmin.readMessages(fileName);
        } else {
            for (Message msg : JankcordAdmin.conversations.get(fileName)) {
                messages += message.formatted(msg.getSender().getId(), msg.getSender().getUsername(), msg.getSender().getAvatarURL(), msg.getContent(), msg.getTimestamp());
            }
        }

        String textJSON = """
                {
                    "users": [%s, %s],
                    "messages": [
                        %s
                    ]
                }
                """.formatted(currentIDNum, otherIDNum, messages);

        exchange.getResponseHeaders().set("Content-Type", "text/json");
        exchange.sendResponseHeaders(200, textJSON.length());

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(textJSON.getBytes());
        outputStream.close();
    }
}
