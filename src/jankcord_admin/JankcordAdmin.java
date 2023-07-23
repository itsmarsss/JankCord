package jankcord_admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import jankcord.objects.FullUser;
import jankcord.objects.Message;
import jankcord.tools.Base64Helper;
import jankcord.tools.IPHelper;
import jankcord.tools.JankFileKit;
import jankcord.tools.ServerCommunicator;
import jankcord_admin.apihandlers.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;

public class JankcordAdmin {

    private static final Scanner sc = new Scanner(System.in);

    public static void startAdmin() {
        System.out.println("Welcome to JankCord Admin Dashboard.");

        try {
            setParent(new File(JankcordAdmin.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent());
        } catch (Exception e) {
            System.out.println("Unable to obtain parent path.");
            System.exit(1);
        }

        System.out.println("Parent path: " + getParent());

        System.out.println("Reading accounts...");
        System.out.println(readAccounts());

        System.out.println("Reading messages...");
        loadInAllMessages();

        while (true) {
            System.out.print(">> ");

            String cmdReq = sc.next();

            if(cmdReq.equals("quit")) {
                break;
            }

            try {
                System.out.println(JankcordAdmin.class.getMethod(cmdReq).invoke(null));
            } catch (Exception e) {
                System.out.println("Command \"" + cmdReq + "\" is not recognized. Use 'help' for more commands.");
            }
        }

        sc.close();
    }

    public static String help() {
        Method[] methods = JankcordAdmin.class.getMethods();

        StringBuilder help = new StringBuilder();

        for (Method method : methods) {
            if (method.getName().equals("equals")) {
                break;
            }

            help.append(method.getName()).append("\n");
        }

        return help.toString();
    }

    public static String writeAccounts() {
        StringBuilder accountList = new StringBuilder();

        for (FullUser account : getAccounts()) {
            accountList.append("""
                      {
                          "id": %s,
                          "username": "%s",
                          "password": "%S",
                          "avatarURL": "%s",
                          "status": "%s"
                      },
                    """.formatted(account.getId(), account.getUsername(), account.getPassword(), account.getAvatarURL(), account.getStatus()));
        }

        String accounts = """
                {
                    "users": [
                        %s
                    ]
                }
                """.formatted(accountList.toString());

        if (JankFileKit.writeFile(getParent() + "/accounts/accounts.json", accounts)) {
            return "Successfully written accounts.json file.";
        } else {
            return "IO error writing.";
        }
    }

    public static String readAccounts() {
        String textJSON = JankFileKit.readFile(getParent() + "/accounts/accounts.json");

        if (textJSON == null) {
            return "IO error reading.";
        }

        getAccounts().clear();

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(textJSON);

            // Get the "messages" array from the JSON object
            JSONArray messagesArray = (JSONArray) jsonObject.get("users");

            // Loop through the "messages" array
            for (Object message : messagesArray) {
                JSONObject messageObject = (JSONObject) message;

                // Read values from each message object
                long id = (Long) messageObject.get("id");
                String username = (String) messageObject.get("username");
                String password = (String) messageObject.get("password");
                String avatarURL = (String) messageObject.get("avatarURL");
                String status = (String) messageObject.get("status");

                getAccounts().add(new FullUser(id, username, avatarURL, password, "", status));
            }
        } catch (Exception e) {
            return "JSON error parsing.";
        }

        return "Successfully read accounts.json file.";
    }

    public static String createAccount() {
        System.out.println("Creating new JankCord account for user...");
        System.out.print("Username (no spaces | 20 characters max): ");
        sc.nextLine();
        String username = sc.nextLine();
        if (!ServerCommunicator.headerable(username)) {
            return "Username must only contain ASCII characters; command sequence exited";
        }

        if (username.contains(" ")) {
            return "Username cannot contain spaces; command sequence exited";
        }

        if (username.length() > 20) {
            return "Username cannot be longer than 20 character; command sequence exited";
        }


        System.out.print("Password (no spaces | 20 characters max): ");
        String password = sc.nextLine();
        if (!ServerCommunicator.headerable(password)) {
            return "Password must only contain ASCII characters; command sequence exited";
        }

        if (password.contains(" ")) {
            return "Password cannot contain spaces; command sequence exited";
        }

        if (password.length() > 20) {
            return "Password cannot be longer than 20 character; command sequence exited";
        }

        for (FullUser account : getAccounts()) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return "Username already in use; command sequence exited";
            }
        }

        if (getAccounts().isEmpty()) {
            getAccounts().add(new FullUser(0, username, "N/A", password, "", "active"));
        } else {
            long id = getAccounts().get(getAccounts().size() - 1).getId() + 1;
            getAccounts().add(new FullUser(id, username, "N/A", password, "", "active"));
        }

        writeAccounts();

        return "Account \"" + username + "\" with password \"" + password + "\" created successfully";
    }

    public static String viewAccounts() {
        if (getAccounts().isEmpty()) {
            return "Empty list.";
        }

        System.out.printf("%-10.10s | %-25.25s | %-25.25s | %-25.25s%n", "ID #", "USERNAME", "PASSWORD", "STATUS");
        System.out.println("---------------------------------------------------------------------------------");
        for (FullUser account : getAccounts()) {
            System.out.printf("%-10.10s | %-25.25s | %-25.25s | %-25.25s%n", account.getId(), account.getUsername(), account.getPassword(), account.getStatus());
        }

        return "End of list.";
    }

    public static String deleteAccount() {
        System.out.print("Account ID #: ");
        String idString = sc.next();
        int idNum;

        try {
            idNum = Integer.parseInt(idString);
        } catch (Exception e) {
            return "Invalid ID #; command sequence exited";
        }

        int index = searchForAccount(idNum, 0, getAccounts().size() - 1);

        if (index == -1) {
            return "ID # not found.";
        }

        getAccounts().remove(index);

        return "Account ID [" + idNum + "] has been deleted.";
    }

    public static String editAccount() {
        System.out.print("Account ID #: ");
        String idString = sc.next();
        int idNum;

        try {
            idNum = Integer.parseInt(idString);
        } catch (Exception e) {
            return "Invalid ID #; command sequence exited";
        }

        int index = searchForAccount(idNum, 0, getAccounts().size() - 1);

        if (index == -1) {
            return "ID # not found.";
        }

        FullUser user = getAccounts().get(index);


        System.out.print("Username [" + user.getUsername() + "]: ");
        sc.nextLine();
        String username = sc.nextLine();
        if (!ServerCommunicator.headerable(username)) {
            return "Username must only contain ASCII characters; command sequence exited";
        }

        if (username.contains(" ")) {
            return "Username cannot contain spaces; command sequence exited";
        }

        if (username.length() > 20) {
            return "Username cannot be longer than 20 character; command sequence exited";
        }


        System.out.print("Password [" + user.getPassword() + "]: ");
        String password = sc.nextLine();
        if (!ServerCommunicator.headerable(password)) {
            return "Password must only contain ASCII characters; command sequence exited";
        }

        if (password.contains(" ")) {
            return "Password cannot contain spaces; command sequence exited";
        }

        if (password.length() > 20) {
            return "Password cannot be longer than 20 character; command sequence exited";
        }


        System.out.print("AvatarURL [" + user.getAvatarURL() + "]: ");
        String avatarURL = sc.nextLine();
        if (!ServerCommunicator.headerable(avatarURL)) {
            return "Invalid URL; command sequence exited";
        }

        if (!user.getUsername().equals(username)) {
            for (FullUser account : getAccounts()) {
                if (account.getUsername().equalsIgnoreCase(username)) {
                    return "Username already in use; command sequence exited";
                }
            }
        }


        getAccounts().get(index).setUsername(username);
        getAccounts().get(index).setPassword(password);
        getAccounts().get(index).setAvatarURL(avatarURL);

        return "Account ID [" + idNum + "] now has username \"" + username + "\", password \"" + password + "\", and avatarURL \"" + avatarURL + "\".";
    }

    public static String setAccountStatus() {
        System.out.print("Account ID #: ");
        String idString = sc.next();
        int idNum;

        try {
            idNum = Integer.parseInt(idString);
        } catch (Exception e) {
            return "Invalid ID #; command sequence exited";
        }

        int index = searchForAccount(idNum, 0, getAccounts().size() - 1);

        if (index == -1) {
            return "ID # not found.";
        }

        System.out.print("Status (10 characters max): ");
        String status = sc.next();

        if (status.length() > 10) {
            return "Status cannot be longer than 10 character; command sequence exited";
        }

        getAccounts().get(index).setStatus(status);

        return "Account ID [" + idNum + "] new has status \"" + status + "\".";
    }


    private static int port;
    private static HttpServer server;


    public static String startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", 6969), 0);
        } catch (IOException e) {
            return "JankCord server failed to start.";
        }

        server.createContext("/api/v1/login", new Login());
        server.createContext("/api/v1/messages", new GetMessages());
        server.createContext("/api/v1/friends", new GetFriends());
        server.createContext("/api/v1/groupchats", new GetGroupChats());
        server.createContext("/api/v1/sendmessage", new NewMessage());
        server.createContext("/api/v1/creategroupchat", new CreateGroupChat());
        server.setExecutor(null);
        server.start();

        port = server.getAddress().getPort();

        String endPoint = "http://" + IPHelper.getPrivateIP() + ":" + port;
        System.out.println(endPoint);
        System.out.println(Base64Helper.encode(endPoint));

        return "JankCord server started.";
    }

    public static String stopServer() {
        server.stop(0);
        return "JankCord server stopped.";
    }

    public static void loadInAllMessages() {
        File messageDir = new File(getParent() + "/messages");

        if(!messageDir.isDirectory()) {
            messageDir.mkdir();
        } else {
            File[] files = messageDir.listFiles();
            for(File file : files) {
                JankFileKit.readMessages(file.getName());
            }
        }
    }

    private static int searchForAccount(long idNum, int leftPoint, int rightPoint) {
        // Check if left and right point have converged
        if (leftPoint > rightPoint) { // If converged
            // Return null, no matching found
            return -1;
        }

        // Set a middle "pivot" point, a point of comparison
        int middle = (leftPoint + rightPoint) / 2;

        // Get the middle's value
        long current = getAccounts().get(middle).getId();

        // Check if it is what we're looking for
        if (current == idNum) { // if equal
            // Return title
            return middle;
        }

        // Check if current is larger or smaller
        if (current > idNum) { // If current reference number is larger
            // Use recursion; search left of middle, so leftPoint to middle - 1
            return searchForAccount(idNum, leftPoint, middle - 1);
        } else { // If current reference number is smaller
            // Use recursion; search right of middle, so middle + 1 to rightPoint
            return searchForAccount(idNum, middle + 1, rightPoint);
        }
    }

    public static boolean authorized(HttpExchange exchange) {
        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String password = requestHeaders.get("password").get(0);

        for (FullUser account : JankcordAdmin.getAccounts()) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    private static ArrayList<FullUser> getAccounts() {
        return AdminDataBase.getAccounts();
    }

    private static HashMap<String, ArrayList<Message>> getConversations() {
        return AdminDataBase.getConversations();
    }

    private static String getParent() {
        return AdminDataBase.getParent();
    }

    private static void setParent(String parent) {
        AdminDataBase.setParent(parent);
    }
}
