import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReaderThread implements Runnable{
    public ReaderThread () {

    }

    public void run() {
        System.out.println("Reader Start");

        String FILE_PATH = Main.file_path;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Read: " + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Reader End");
    }
}
