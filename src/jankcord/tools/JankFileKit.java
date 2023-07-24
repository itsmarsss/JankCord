package jankcord.tools;

import jankcord.objects.GroupChat;
import jankcord.objects.Message;
import jankcord_admin.AdminDataBase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;

public class JankFileKit {
    public static boolean writeFile(String file, String content) {
        // Try to write
        try {
            // Create a BufferedWriter for writing
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(content);

            // Make sure to close writer
            bw.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String readFile(String file) {
        StringBuilder textJSON = new StringBuilder();

        // Try to read file
        try {
            // Create BufferedReader to ready inventory.txt
            BufferedReader br = new BufferedReader(new FileReader(file));

            // Declare String line to be used later on
            String line;

            // While the read line is not empty
            while ((line = br.readLine()) != null) {
                textJSON.append(line).append("\n");
            }

            br.close();
        } catch (Exception e) { // If error
            // Notify user that there was a reading error
            return null;
        }

        return textJSON.toString();
    }

    public static void create(File file) {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static String readMessages(String fileName) {
        String textJSON = JankFileKit.readFile(AdminDataBase.getParent() + "/messages/" + fileName);

        if (textJSON == null) {
            textJSON = "";
            System.out.println("IO error reading.");
        }

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

                messages.add(new Message(id, content, timestamp));
            }
        } catch (Exception e) {}

        AdminDataBase.getConversations().put(fileName.replaceFirst("[.][^.]+$", ""), messages);

        System.out.println("File read. [" + fileName + "]");

        return textJSON;
    }

    public static String readGroupMessages(String fileName) {
        String textJSON = JankFileKit.readFile(AdminDataBase.getParent() + "/groupmessages/" + fileName);

        if (textJSON == null) {
            textJSON = "";
            System.out.println("IO error reading.");
        }

        ArrayList<Long> members = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        try {
            // Parse the JSON string
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(textJSON);

            // Get the "messages" array from the JSON object
            JSONArray membersArray = (JSONArray) jsonObject.get("users");
            for (Object member : membersArray) {
                long id = (Long) member;

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

                messages.add(new Message(id, content, timestamp));
            }
        } catch (Exception e) {}

        String rawFileName = fileName.replaceFirst("[.][^.]+$", "");

        AdminDataBase.getGroupChats().put(rawFileName, new GroupChat(rawFileName, members ,messages));

        System.out.println("File read. [" + fileName + "]");

        return textJSON;
    }
}
