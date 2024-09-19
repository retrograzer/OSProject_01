import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReaderThread implements Runnable{

    public void run() {
        System.out.println("Reader Start");

        //Get path from Main
        String FILE_PATH = Main.file_path;

        //Read file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            //Keep going till the end of the file
            while ((line = reader.readLine()) != null) {
                //Print read line to console
                System.out.println("Read: " + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Reader End");
    }
}
