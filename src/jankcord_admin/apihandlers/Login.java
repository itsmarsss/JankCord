package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class Login implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String password = requestHeaders.get("password").get(0);

        String response = "denied";

        for(FullUser account : JankcordAdmin.accounts) {
            if(account.getUsername().equals(username) && account.getPassword().equals(password)) {
                response = "succeeded";
            }
        }

        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length());

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
