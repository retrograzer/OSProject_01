import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ReaderThreadUnsync implements Runnable{

    private boolean useLock = false; //Part A is False, Part B is true
    private final ReentrantLock mutex = new ReentrantLock();

    public ReaderThreadUnsync (boolean useLock) {
        this.useLock = useLock;
    }

    public void run() {
        System.out.println("Reader Start");

        //Get path
        String FILE_PATH = Main.file_path;

        //Try to read the file like before
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            //Lock the thread so that 2 readers cannot work at the same time
            if (useLock)
                mutex.lock();

            //Read file like before
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Read: " + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //If we set a lock, unlock it after processing
            if (useLock)
                mutex.unlock();
        }

        System.out.println("Reader End");
    }
}
