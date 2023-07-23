package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jankcord.objects.FullUser;
import jankcord.objects.GroupChat;
import jankcord.objects.User;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.AdminDataBase;
import jankcord_admin.JankcordAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetGroupChats implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Group Chats Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);

        long currentID = -1;
        for (FullUser account : AdminDataBase.getAccounts()) {
            if (account.getUsername().equals(username)) {
                currentID = account.getId();
            }
        }

        if (currentID == -1) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        StringBuilder groupChatsList = new StringBuilder();
        for (Map.Entry<String, GroupChat> entry : AdminDataBase.getGroupChats().entrySet()) {
            GroupChat gc = entry.getValue();

            for (long member : gc.getMembers()) {
                if (member == currentID) {
                    groupChatsList.append("""
                            {
                                "chatID": "%s",
                                "chatName": "%s",
                                "chatIconURL": "%s"
                            },
                            """.formatted(gc.getId(), gc.getChatName(), gc.getChatIconURL()));
                }
            }
        }

        String groupChats = """
                {
                    "groupChats": [
                        %s
                    ]
                }
                """.formatted(groupChatsList.toString());

        ServerCommunicator.sendResponse(exchange, groupChats);
    }
}
