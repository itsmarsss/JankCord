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

public class GetFriends implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Friends Requested");

        if (!JankcordAdmin.authorized(exchange)) {
            ServerCommunicator.sendResponse(exchange, "403");
            return;
        }

        StringBuilder friendsList = new StringBuilder();

        for (FullUser account : AdminDataBase.getAccounts()) {
            friendsList.append("""
                    {
                        "id": %s,
                        "username": "%s",
                        "avatarURL": "%s"
                    },
                    """.formatted(account.getId(), account.getUsername(), account.getAvatarURL()));
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
            boolean save = false;

            StringBuilder groupChatMembers = new StringBuilder();

            for (User member : entry.getValue().getMembers()) {
                if (member.getId() == currentID) {
                    save = true;
                }
                groupChatMembers.append("""
                        {
                            "id": %s,
                            "username": "%s",
                            "avatarURL": "%s"
                        },
                        """.formatted(member.getId(), member.getUsername(), member.getAvatarURL()));
            }

            if (save) {
                groupChatsList.append("""
                        {
                            "chatID": "%s",
                            "members": [
                                %s
                            ]
                        },
                        """.formatted(entry.getValue().getId(), groupChatMembers.toString()));
            }

        }
        String friends = """
                {
                    "friends": [
                        %s
                    ],
                    "groupChats": [
                        %s
                    ]
                }
                """.formatted(friendsList.toString(), groupChatsList.toString());

        ServerCommunicator.sendResponse(exchange, friends);
    }
}
