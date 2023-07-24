package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.Message;
import jankcord.objects.User;
import jankcord.tools.JankFileKit;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetGroupMessages implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Messages Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String chatID = requestHeaders.get("oherID").get(0);

        String user = """
                {
                    "id": %s,
                    "username": "%s",
                    "avatarURL": "%s"
                },
                """;

        String message = """
                {
                    "id": %s,
                    "content": "%s",
                    "timestamp": %s
                },
                """;

        StringBuilder users = new StringBuilder("");
        StringBuilder messages = new StringBuilder();

        if (!AdminDataBase.getGroupChats().containsKey(chatID)) {
            File file = new File(AdminDataBase.getParent() + "/messages/" + chatID + ".json");
            if (file.exists()) {
                JankFileKit.readMessages(chatID + ".json");
            } else {
                JankFileKit.create(file);
            }
        } else {
            for (long msg : AdminDataBase.getGroupChats().get(chatID).getMembers()) {
                int index = JankcordAdmin.searchForAccount(msg, 0, AdminDataBase.getAccounts().size() - 1);
                if(index == -1) {
                    continue;
                }

                User usr = AdminDataBase.getAccounts().get(index);

                users.append(user.formatted(usr.getId(), usr.getUsername(), usr.getAvatarURL()));
            }

            for (Message msg : AdminDataBase.getGroupChats().get(chatID).getMessages()) {
                messages.append(message.formatted(msg.getSenderID(), msg.getContent(), msg.getTimestamp()));
            }
        }

        String textJSON = """
                {
                    "users": [
                        %s
                    ],
                    "messages": [
                        %s
                    ]
                }
                """.formatted(users.toString(), messages.toString());

        JankFileKit.writeFile(AdminDataBase.getParent() + "/messages/" + chatID + ".json", textJSON);

        if (!AdminDataBase.getGroupChats().containsKey(chatID)) {
            JankFileKit.readMessages(chatID + ".json");
        }

        ServerCommunicator.sendResponse(exchange, textJSON);
    }
}
