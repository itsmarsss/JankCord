package jankcord.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

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
}
