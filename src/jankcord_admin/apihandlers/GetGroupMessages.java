package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.Message;
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
        //System.out.println("Group Messages Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String chatID = requestHeaders.get("otherID").get(0);

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
            File file = new File(AdminDataBase.getParent() + "/groupmessages/" + chatID + ".json");
            if (file.exists()) {
                JankFileKit.readGroupMessages(chatID + ".json");
            } else {
                JankFileKit.create(file);
            }
        } else {
            for (long member : AdminDataBase.getGroupChats().get(chatID).getMembers()) {
                users.append(member).append(",");
            }

            for (Message msg : AdminDataBase.getGroupChats().get(chatID).getMessages()) {
                messages.append(message.formatted(msg.getSenderID(), msg.getContent(), msg.getTimestamp()));
            }
        }

        String textJSON = """
                {
                    "users": [%s],
                    "messages": [
                        %s
                    ],
                    "chatID": "%s",
                    "chatName": "%s",
                    "chatIconURL": "%s"
                }
                """.formatted(users.toString(), messages.toString(), chatID, AdminDataBase.getGroupChats().get(chatID).getChatName(), AdminDataBase.getGroupChats().get(chatID).getChatIconURL());

        JankFileKit.writeFile(AdminDataBase.getParent() + "/groupmessages/" + chatID + ".json", textJSON);

        if (!AdminDataBase.getGroupChats().containsKey(chatID)) {
            JankFileKit.readGroupMessages(chatID + ".json");
        }

        ServerCommunicator.sendResponse(exchange, textJSON);
    }
}
