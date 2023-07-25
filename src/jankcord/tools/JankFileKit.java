package jankcord.tools;

import jankcord.objects.GroupChat;
import jankcord.objects.Message;
import jankcord_admin.AdminDataBase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;

// File IO related Helper
public class JankFileKit {
    /**
     * Write to a file
     *
     * @param file    name + path
     * @param content content as a string
     * @return true/false depending on how successful write was
     */
    public static boolean writeFile(String file, String content) {
        // Try to write
        try {
            // Create a BufferedWriter for writing
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            // Write content
            bw.write(content);

            // Make sure to close writer
            bw.close();

            // Return successful
            return true;
        } catch (Exception e) {
            // Return unsuccessful
            return false;
        }
    }

    /**
     * Reads a file and returns its content
     *
     * @param file name + path
     * @return file content or null
     */
    public static String readFile(String file) {
        // String builder for content
        StringBuilder textJSON = new StringBuilder();

        // Try to read file
        try {
            // Create BufferedReader to read file
            BufferedReader br = new BufferedReader(new FileReader(file));

            // Declare String line to be used later on
            String line;

            // While the read line is not empty
            while ((line = br.readLine()) != null) {
                // Append line to builder
                textJSON.append(line).append("\n");
            }

            // Make sure to close reader
            br.close();
        } catch (Exception e) { // If error
            // Return null for error
            return null;
        }

        // Return content
        return textJSON.toString();
    }

    /**
     * Creates a missing file
     *
     * @param file name + path
     */
    public static void create(File file) {
        // If file doesn't exist
        if (!file.exists()) {
            // Try to create it
            try {
                // Create
                if (!file.createNewFile()) {
                    // If unsuccessful
                    // Throw exception
                    throw new RuntimeException();
                }
            } catch (IOException e) { // Otherwise
                // Throw exception
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Reads a dm message file
     *
     * @param file name + path
     * @return string content of file
     */
    public static String readMessages(String file) {
        // Read file in messages directory
        String textJSON = readFile(AdminDataBase.getParent() + "/messages/" + file);

        // If content is null
        if (textJSON == null) {
            // Inform user of error
            textJSON = "";
            System.out.println("IO error reading.");
        }

        // Messages arraylist
        ArrayList<Message> messages = new ArrayList<>();

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(textJSON);

            // Get the "messages" array from the JSON object
            JSONArray messagesArray = (JSONArray) jsonObject.get("messages");

            // Loop through the "messages" array
            for (Object message : messagesArray) {
                JSONObject messageObject = (JSONObject) message;

                // Read values from each message object
                long id = (Long) messageObject.get("id");
                String content = (String) messageObject.get("content");
                long timestamp = (Long) messageObject.get("timestamp");

                // Add new entry to messages
                messages.add(new Message(id, content, timestamp));
            }
        } catch (Exception e) {
        }

        // Add new conversation to hashmap in database
        AdminDataBase.getConversations().put(file.replaceFirst("[.][^.]+$", ""), messages);

        // Inform of reading
        System.out.println("File read. [" + file + "]");

        // Return file
        return textJSON;
    }

    /**
     * Reads a groupchat messages file
     *
     * @param fileName name + path
     * @return string content of file
     */
    public static String readGroupMessages(String fileName) {
        // Read file in groupmessages directory
        String textJSON = readFile(AdminDataBase.getParent() + "/groupmessages/" + fileName);

        // If content is null
        if (textJSON == null) {
            // Inform user of error
            textJSON = "";
            System.out.println("IO error reading.");
        }

        // Group chat name and icon url
        String chatName = "";
        String chatIconURL = "";

        // Members and messages arraylist
        ArrayList<Long> members = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(textJSON);

            // Get chatName and chatIconURL
            chatName = (String) jsonObject.get("chatName");
            chatIconURL = (String) jsonObject.get("chatIconURL");

            // Get the "users" array from the JSON object
            JSONArray usersArray = (JSONArray) jsonObject.get("users");
            for (Object member : usersArray) {
                // Get member id
                long id = (Long) member;

                // Add if to members
                members.add(id);
            }

            // Get the "messages" array from the JSON object
            JSONArray messagesArray = (JSONArray) jsonObject.get("messages");

            // Loop through the "messages" array
            for (Object message : messagesArray) {
                JSONObject messageObject = (JSONObject) message;

                // Read values from each message object
                long id = (Long) messageObject.get("id");
                String content = (String) messageObject.get("content");
                long timestamp = (Long) messageObject.get("timestamp");

                // Add new entry to messages
                messages.add(new Message(id, content, timestamp));
            }
        } catch (Exception e) {
        }

        // Remove file extension
        String rawFileName = fileName.replaceFirst("[.][^.]+$", "");

        // Add new groupchat to hashmap in database
        AdminDataBase.getGroupChats().put(rawFileName, new GroupChat(rawFileName, chatName, chatIconURL, members, messages));

        // Inform of reading
        System.out.println("File read. [" + fileName + "]");

        // Return file
        return textJSON;
    }
}
