package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.Message;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class NewMessage implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Messages Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            exchange.getResponseHeaders().set("Content-Type", "text/json");
            exchange.sendResponseHeaders(200, 3);

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("403".getBytes());
            outputStream.close();

            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String otherID = requestHeaders.get("otherID").get(0);
        String content = requestHeaders.get("content").get(0);

        FullUser current = null;
        FullUser other = null;

        for (FullUser account : JankcordAdmin.accounts) {
            if (account.getUsername().equals(username)) {
                current = account;
            }

            if(String.valueOf(account.getId()).equals(otherID)) {
                other = account;
            }
        }

        if(other == null) {
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("403".getBytes());
            outputStream.close();

            return;
        }

        long otherIDNum = Long.parseLong(otherID);
        long currentIDNum = current.getId();

        String fileName = Math.min(otherIDNum, currentIDNum) + "-" + Math.max(otherIDNum, currentIDNum);

        JankcordAdmin.conversations.get(fileName).add(new Message(current, content, System.currentTimeMillis()));

        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 3);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write("200".getBytes());
        outputStream.close();
    }
}
