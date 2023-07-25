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

public class ConfigureAccount implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // System.out.println("Account configure Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String password = requestHeaders.get("password").get(0);

        String newUsername = requestHeaders.get("newUsername").get(0);
        String newPassword = requestHeaders.get("newPassword").get(0);
        String avatarURL = requestHeaders.get("avatarURL").get(0);

        if (!JankcordAdmin.usernameAvailable(newUsername)) {
            ServerCommunicator.sendResponse(exchange, "409");
            return;
        }

        if(JankcordAdmin.validateUsername(newUsername) != null) {
            ServerCommunicator.sendResponse(exchange, "404");
            return;
        }

        if(JankcordAdmin.validatePassword(newPassword) != null) {
            ServerCommunicator.sendResponse(exchange, "404");
            return;
        }

        for(FullUser account : AdminDataBase.getAccounts()) {
            if(account.getUsername().equals(username)){
                account.setUsername(newUsername);
                account.setPassword(password);
                account.setAvatarURL(avatarURL);
            }
        }

        JankcordAdmin.writeAccounts();
    }
}
