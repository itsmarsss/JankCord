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

// Jankcord admin CLI
public class JankcordAdmin {
    // Class fields
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Starts admin CLI
     */
    public static void startAdmin() {
        // Welcome messages
        System.out.println("Welcome to JankCord Admin Dashboard.");

        // Try to get parents
        try {
            // Set parent
            setParent(new File(JankcordAdmin.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent());
        } catch (Exception e) {
            // Otherwise unable to obtain parent
            System.out.println("Unable to obtain parent path.");

            // Exit system
            System.exit(1);
        }

        // Output parents
        System.out.println("Parent path: " + getParent());

        // Read accounts
        System.out.println("Reading accounts...");
        System.out.println(readAccounts());

        // Read messages
        System.out.println("Reading messages...");
        loadInAllMessages();

        // While true, get input
        while (true) {
            // Command input indicator
            System.out.print(">> ");

            // Get command request
            String cmdReq = sc.next();

            // If equal to quit, quit program
            if (cmdReq.equals("quit")) {
                break;
            }

            // Try to invoke requested method
            try {
                System.out.println(JankcordAdmin.class.getMethod(cmdReq).invoke(null));
            } catch (Exception e) { // Otherise
                // Command not recognized
                System.out.println("Command \"" + cmdReq + "\" is not recognized. Use 'help' for more commands.");
            }
        }

        // Goodbye message
        System.out.println("Goodbye.");

        // Make sure to close scanner
        sc.close();
    }

    /**
     * Returns help menu
     *
     * @return help string
     */
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

    /**
     * Write all accounts to file
     *
     * @return status of account write
     */
    public static String writeAccounts() {
        // Account list builder
        StringBuilder accountList = new StringBuilder();

        // Loop through all accounts
        for (FullUser account : getAccounts()) {
            // Append to account list builder with formatted values
            accountList.append("""
                      {
                          "id": %s,
                          "username": "%s",
                          "password": "%s",
                          "avatarURL": "%s",
                          "status": "%s"
                      },
                    """.formatted(account.getId(), account.getUsername(), account.getPassword(), account.getAvatarURL(), account.getStatus()));
        }

        // Accounts template formatted with account list
        String accounts = """
                {
                    "users": [
                        %s
                    ]
                }
                """.formatted(accountList.toString());

        // Create File object
        File file = new File(getParent() + "/accounts/accounts.json");

        // If file exists
        if (file.exists()) {
            // Write to it
            if (JankFileKit.writeFile(getParent() + "/accounts/accounts.json", accounts)) {
                // Success
                return "Successfully written accounts.json file.";
            } else { // If unsuccessful
                // Error
                return "IO error writing.";
            }
        } else { // Otherwise
            // Create file
            JankFileKit.create(file);
            return "Created accounts.json.";
        }
    }

    /**
     * Read all accounts from file
     *
     * @return status of account read
     */
    public static String readAccounts() {
        // Create File object of directory
        File accountsDir = new File(getParent() + "/accounts");

        // If directory doesn't exist
        if (!accountsDir.isDirectory()) {
            // Create directory
            if(accountsDir.mkdir()) {
                // Print creation
                return "Created accounts directory.";
            }else { // Unsuccessful
                // Error
                System.out.println("Error creating messages directory.");
            }
        }

        // Read accounts.json
        String textJSON = JankFileKit.readFile(getParent() + "/accounts/accounts.json");

        // If error reading
        if (textJSON == null) {
            // Error
            return "IO error reading.";
        }

        // Clear current accounts
        getAccounts().clear();

        // Try to parse
        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(textJSON);

            // Get the "users" array from the JSON object
            JSONArray usersArray = (JSONArray) jsonObject.get("users");

            // Loop through the "users" array
            for (Object user : usersArray) {
                JSONObject messageObject = (JSONObject) user;

                // Read values from each user object
                long id = (Long) messageObject.get("id");
                String username = (String) messageObject.get("username");
                String password = (String) messageObject.get("password");
                String avatarURL = (String) messageObject.get("avatarURL");
                String status = (String) messageObject.get("status");

                // Get accounts and add new account with parameters
                getAccounts().add(new FullUser(id, username, avatarURL, password, "", status));
            }
        } catch (Exception e) { // If unsuccessful
            // Error parsing
            return "JSON error parsing.";
        }

        // Successfully read file
        return "Successfully read accounts.json file.";
    }

    /**
     * Creates new account for user
     *
     * @return status of account creation
     */
    public static String createAccount() {
        // Print what is happening
        System.out.println("Creating new JankCord account for user...");

        // Ask for username
        System.out.print("Username (no spaces | 20 characters max): ");

        // Similar to [BufferedReader#flush();]
        sc.nextLine();

        // Get username
        String username = sc.nextLine();

        // Check validity of username
        String usernameValid = validateUsername(username);

        // If there is error code
        if (usernameValid != null) {
            // Return it
            return usernameValid;
        }


        // Ask for password
        System.out.print("Password (no spaces | 20 characters max): ");

        // Get password
        String password = sc.nextLine();

        // Check validity of password
        String passwordValid = validatePassword(password);

        // If there is error code
        if (passwordValid != null) {
            // Return it
            return passwordValid;
        }


        // Check availability of username
        if (!usernameAvailable(username)) {
            // If not available, already in use
            return "Username already in use; command sequence exited";
        }

        // If accounts is empty
        if (getAccounts().isEmpty()) {
            // Set user id to 0 and add to list
            getAccounts().add(new FullUser(0, username, "", password, "", "active"));
        } else { // Otherwise
            // ID is 1 more than previous
            long id = getAccounts().get(getAccounts().size() - 1).getId() + 1;

            // Add user to list
            getAccounts().add(new FullUser(id, username, "", password, "", "active"));
        }

        // Write accounts to file
        writeAccounts();

        // Return account information
        return String.format("Account \"%s\" with password \"%s\" created successfully",
                username, password
        );
    }

    /**
     * View all accounts
     *
     * @return status of account viewing
     */
    public static String viewAccounts() {
        // If no accounts
        if (getAccounts().isEmpty()) {
            // Empty list
            return "Empty list.";
        }

        // Print headers with format for spacing
        System.out.printf("%-10.10s | %-25.25s | %-25.25s | %-25.25s%n", "ID #", "USERNAME", "PASSWORD", "STATUS");
        System.out.println("---------------------------------------------------------------------------------");

        // Loop through all accounts
        for (FullUser account : getAccounts()) {
            // Print entry with format for spacing
            System.out.printf("%-10.10s | %-25.25s | %-25.25s | %-25.25s%n", account.getId(), account.getUsername(), account.getPassword(), account.getStatus());
        }

        // Return end of list
        return "End of list.";
    }

    /**
     * Delete account from accounts
     *
     * @return status of account delete
     */
    public static String deleteAccount() {
        // Ask for account ID
        System.out.print("Account ID #: ");

        // Get ID
        String idString = sc.next();

        // Use idNum later in parsing
        int idNum;

        // Try to parse
        try {
            // Attempt String -> Int
            idNum = Integer.parseInt(idString);
        } catch (Exception e) { // Unsuccessful
            // Invalid input
            return "Invalid ID #; command sequence exited";
        }

        // Use binary search to search for account's index
        int index = searchForAccount(idNum, 0, getAccounts().size() - 1);

        // If index == -1, then it doesn't exist
        if (index == -1) {
            // Not found
            return "ID # not found.";
        }

        // Otherwise remove it
        getAccounts().remove(index);

        // Write accounts to file
        writeAccounts();

        // Return account deleted
        return "Account ID [" + idNum + "] has been deleted.";
    }

    /**
     * Edit account attributes
     *
     * @return status of account edit
     */
    public static String editAccount() {
        // Ask for account ID
        System.out.print("Account ID #: ");

        // Get ID
        String idString = sc.next();

        // Use idNum later in parsing
        int idNum;

        // Try to parse
        try {
            // Attempt String -> Int
            idNum = Integer.parseInt(idString);
        } catch (Exception e) { // Unsuccessful
            // Invalid input
            return "Invalid ID #; command sequence exited";
        }

        // Use binary search to search for account's index
        int index = searchForAccount(idNum, 0, getAccounts().size() - 1);

        // If index == -1, then it doesn't exist
        if (index == -1) {
            // Not found
            return "ID # not found.";
        }


        // Otherwise get it
        FullUser user = getAccounts().get(index);

        // Ask for username
        System.out.print("Username [" + user.getUsername() + "]: ");

        // Similar to [BufferedReader#flush();]
        sc.nextLine();

        // Get username
        String username = sc.nextLine();

        // Check validity of username
        String usernameValid = validateUsername(username);

        // If there is error code
        if (usernameValid != null) {
            // Return it
            return usernameValid;
        }


        // Ask for password
        System.out.print("Password [" + user.getPassword() + "]: ");

        // Get password
        String password = sc.nextLine();

        // Check validity of password
        String passwordValid = validatePassword(password);

        // If there is error code
        if (passwordValid != null) {
            // Return it
            return passwordValid;
        }


        // Ask for avatarURL
        System.out.print("AvatarURL [" + user.getAvatarURL() + "]: ");

        // Get avatarURL
        String avatarURL = sc.nextLine();

        // Check if headerable
        if (ServerCommunicator.notHeaderable(avatarURL)) {
            // Return invalid is not headerable
            return "Invalid URL; command sequence exited";
        }

        // If username has been changed
        if (!user.getUsername().equals(username)) {
            // Check availability of username
            if (!usernameAvailable(username)) {
                // If not available, already in use
                return "Username already in use; command sequence exited";
            }
        }

        // Update username, password, and avatarURL
        getAccounts().get(index).setUsername(username);
        getAccounts().get(index).setPassword(password);
        getAccounts().get(index).setAvatarURL(avatarURL);

        // Write accounts to file
        writeAccounts();

        // Return new account information
        return String.format("Account ID [%s] now has username \"%s\", password \"%s\", and avatarURL \"%s\".",
                idNum, username, password, avatarURL
        );
    }

    /**
     * Set a status on an account (eg: warning, active, suspended... etc)
     *
     * @return status of account status setting
     */
    public static String setAccountStatus() {
        // Ask for account ID
        System.out.print("Account ID #: ");

        // Get ID
        String idString = sc.next();

        // Use idNum later in parsing
        int idNum;

        // Try to parse
        try {
            // Attempt String -> Int
            idNum = Integer.parseInt(idString);
        } catch (Exception e) { // Unsuccessful
            // Invalid input
            return "Invalid ID #; command sequence exited";
        }

        // Use binary search to search for account's index
        int index = searchForAccount(idNum, 0, getAccounts().size() - 1);

        // If index == -1, then it doesn't exist
        if (index == -1) {
            // Not found
            return "ID # not found.";
        }

        // Otherwise; ask for status
        System.out.print("Status (10 characters max): ");

        // Get status
        String status = sc.next();

        // Check if too long
        if (status.length() > 10) {
            // If too long, error message
            return "Status cannot be longer than 10 character; command sequence exited";
        }

        // Otherwise update status
        getAccounts().get(index).setStatus(status);

        // Return account id with status
        return String.format("Account ID [%s] new has status \"%s\".",
                idNum, status
        );
    }

    /**
     * Validates a username
     *
     * @param username to validate
     * @return error message or null for no error
     */
    public static String validateUsername(String username) {
        // Check if headerable
        if (ServerCommunicator.notHeaderable(username)) {
            // Return error
            return "Username must only contain ASCII characters; command sequence exited";
        }

        // Check if contains space
        if (username.contains(" ")) {
            // Return error
            return "Username cannot contain spaces; command sequence exited";
        }

        // Check if blank
        if (username.isBlank()) {
            // Return error
            return "Username cannot be blank; command sequence exited";
        }

        // Check if too long
        if (username.length() > 20) {
            // Return error
            return "Username cannot be longer than 20 character; command sequence exited";
        }

        // No error
        return null;
    }

    /**
     * Validates a password
     *
     * @param password to validate
     * @return error message or null for no error
     */
    public static String validatePassword(String password) {
        // Check if headerable
        if (ServerCommunicator.notHeaderable(password)) {
            // Return error
            return "Password must only contain ASCII characters; command sequence exited";
        }

        // Check if contains space
        if (password.contains(" ")) {
            // Return error
            return "Password cannot contain spaces; command sequence exited";
        }

        // Check if blank
        if (password.isBlank()) {
            // Return error
            return "Password cannot be blank; command sequence exited";
        }

        // Check if too long
        if (password.length() > 20) {
            // Return error
            return "Password cannot be longer than 20 character; command sequence exited";
        }

        // No error
        return null;
    }

    /**
     * Checks if a username is available
     *
     * @param username to check availability of
     * @return true/false depending on availability
     */
    public static boolean usernameAvailable(String username) {
        // Loop through all accounts
        for (FullUser account : getAccounts()) {
            // If username found
            if (account.getUsername().equalsIgnoreCase(username)) {
                // Return false;not available
                return false;
            }
        }

        // Return true; availabile
        return true;
    }

    // HTTP server fields
    private static HttpServer server;
    private static int port;

    /**
     * Starts http server
     *
     * @return status of starting
     */
    public static String startServer() {
        // Try to start server
        try {
            // Start on arbitrary port
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", 0), 0);
        } catch (IOException e) { // If unsuccessful
            // Error
            return "JankCord server failed to start.";
        }

        // Create all contexts
        server.createContext("/api/v1/login", new Login());
        server.createContext("/api/v1/messages", new GetMessages());
        server.createContext("/api/v1/groupmessages", new GetGroupMessages());
        server.createContext("/api/v1/friends", new GetFriends());
        server.createContext("/api/v1/groupchats", new GetGroupChats());
        server.createContext("/api/v1/sendmessage", new NewMessage());
        server.createContext("/api/v1/sendgroupmessage", new NewGroupMessage());
        server.createContext("/api/v1/creategroupchat", new CreateGroupChat());
        server.createContext("/api/v1/editaccount", new ConfigureAccount());

        // No executor; small scaled no need to thread... yet
        server.setExecutor(null);

        // Start http server
        server.start();

        // Set port
        port = server.getAddress().getPort();

        // Get endpoint
        String endPoint = "http://" + IPHelper.getPrivateIP() + ":" + port;

        // Print endpoint raw and encoded
        System.out.println("IP: " + endPoint);
        System.out.println("Server Key: " + Base64Helper.encode(endPoint));

        // Return started
        return "JankCord server started.";
    }

    /**
     * Stops HTTP server
     *
     * @return stopped
     */
    public static String stopServer() {
        // Stop with no delay
        server.stop(0);

        // Stop server
        return "JankCord server stopped.";
    }

    /**
     * Load in all messages; dm and gc
     */
    public static void loadInAllMessages() {
        // Create File object of messages directory
        File messageDir = new File(getParent() + "/messages");

        // If directory doesn't exist
        if (!messageDir.isDirectory()) {
            // Create directory
            if (messageDir.mkdir()) {
                // Print creation
                System.out.println("Created messages directory.");
            } else { // Unsuccessful
                // Error
                System.out.println("Error creating messages directory.");
            }
        } else { // If directory exists
            // Get all subfiles
            File[] files = messageDir.listFiles();

            // Loop each file
            for (File file : files) {
                // Read messages
                JankFileKit.readMessages(file.getName());
            }
        }


        // Create File object of groupmessages directory
        File groupMessageDir = new File(getParent() + "/groupmessages");

        // If directory doesn't exist
        if (!groupMessageDir.isDirectory()) {
            // Create directory
            if (groupMessageDir.mkdir()) {
                // Print creation
                System.out.println("Created groupmessages directory.");
            } else { // Unsuccessful
                // Error
                System.out.println("Error creating messages directory.");
            }
        } else { // If directory exists
            // Get all subfiles
            File[] files = groupMessageDir.listFiles();

            // Loop each file
            for (File file : files) {
                // Read messages
                JankFileKit.readGroupMessages(file.getName());
            }
        }
    }

    /**
     * Binary search for account
     *
     * @param idNum      id number of account
     * @param leftPoint  left pivot/point
     * @param rightPoint right pivot/point
     * @return index of account or -1 if not found
     */
    public static int searchForAccount(long idNum, int leftPoint, int rightPoint) {
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

    /**
     * Checks if request is authorized or not
     *
     * @param exchange exchange containing headers
     * @return true/false depending on authorization
     */
    public static boolean authorized(HttpExchange exchange) {
        // Get headers
        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        String username = requestHeaders.get("username").get(0);
        String password = requestHeaders.get("password").get(0);

        // Loop through all accounts
        for (FullUser account : JankcordAdmin.getAccounts()) {
            // If username and password matches
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                // Return true; authorized
                return true;
            }
        }

        // Return true; not authorized
        return false;
    }

    // Getters and setters
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
