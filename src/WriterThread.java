import java.io.*;

public class WriterThread implements Runnable {
    private final String content;
    
    public WriterThread (String content) {
        this.content = content;
    }
    
    public void run() {
        //Get file path from Main
        String FILE_PATH = Main.file_path;

        System.out.println("Writer Start");

        //Write "content" to "FILE_PATH"
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(content);
            System.out.println("Write: " + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Writer End");
    }
}
