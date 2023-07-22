package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.io.OutputStream;

public class GetFriends implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Friends Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            exchange.getResponseHeaders().set("Content-Type", "text/json");
            exchange.sendResponseHeaders(200, 3);

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("403".getBytes());
            outputStream.close();

            return;
        }

        String friendsList = "";

        for (FullUser account : JankcordAdmin.accounts) {
            friendsList += """
                    {
                        "id": %s,
                        "username": "%s",
                        "avatarURL": "%s"
                    },
                    """.formatted(account.getId(), account.getUsername(), account.getAvatarURL());
        }

        String friends = """
                {
                    "friends": [
                        %s
                    ]
                }        
                """.formatted(friendsList);
        ;

        exchange.getResponseHeaders().set("Content-Type", "text/json");
        exchange.sendResponseHeaders(200, friends.length());

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(friends.getBytes());
        outputStream.close();
    }
}
