import java.awt.*;
import java.io.*;
import java.nio.Buffer;

public class WriterThread implements Runnable {
    private final String content;
    
    public WriterThread (String content) {
        this.content = content;
    }
    
    public void run() {
        String FILE_PATH = Main.file_path;

        System.out.println("Writer Start");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(content);
            System.out.println("Write: " + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Writer End");
    }
}
