package jankcord_admin;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class JankFileWriter {
    public static void writeFile(String file, String content) {
        // Try to write
        try {
            // Create a BufferedWriter for writing
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(content);

            // Make sure to close writer
            bw.close();
        } catch (Exception e) {
            System.out.println("IO error writing.");
        }
    }
}
