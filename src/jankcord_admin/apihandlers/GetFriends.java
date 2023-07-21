package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.io.OutputStream;

public class GetFriends implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!JankcordAdmin.authorized(exchange)) {
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("403".getBytes());
            outputStream.close();

            return;
        }

        String friends = """
                {
                    friends: [
                        %s
                    ]
                }        
                """;
    }
}
