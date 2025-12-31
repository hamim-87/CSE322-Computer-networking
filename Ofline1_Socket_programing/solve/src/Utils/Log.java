package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log{

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String username, String action, String filename) {
        try {
            File userDir = new File("src/Server/Storage/" + username);
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File logFile = new File(userDir, "history.log");
            FileWriter writer = new FileWriter(logFile, true);

            String time = LocalDateTime.now().format(formatter);
            writer.write(
                    String.format("[%s] %-8s filename=%s%n", time, action, filename)
            );

            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to write history log for " + username);
        }
    }
}
