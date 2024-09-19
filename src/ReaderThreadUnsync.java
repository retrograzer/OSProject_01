import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ReaderThreadUnsync implements Runnable{

    private boolean useLock = false;
    private final ReentrantLock mutex = new ReentrantLock();

    public ReaderThreadUnsync (boolean useLock) {
        this.useLock = useLock;
    }

    public void run() {
        System.out.println("Reader Start");

        String FILE_PATH = Main.file_path;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            if (useLock)
                mutex.lock();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Read: " + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (useLock)
                mutex.unlock();
        }

        System.out.println("Reader End");
    }
}
