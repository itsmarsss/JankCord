package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class Login implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // System.out.println("Account Login Requested");

        String response = "403";

        if(JankcordAdmin.authorized(exchange)) {
            Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

            String username = requestHeaders.get("username").get(0);

            FullUser user = null;

            for(FullUser account : AdminDataBase.getAccounts()) {
                if(account.getUsername().equals(username)){
                    user = account;
                }
            }
            if(user == null) {
                ServerCommunicator.sendResponse(exchange, "403");
                return;
            }

            response = """
                    {
                        "id": %s,
                        "avatarURL": "%s"
                    }
                    """.formatted(user.getId(), user.getAvatarURL());
        }


        ServerCommunicator.sendResponse(exchange, response);
    }
}
