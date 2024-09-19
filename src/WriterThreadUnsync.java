import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class WriterThreadUnsync implements Runnable{

    private final String content;
    private boolean useLock = false; //Part A is false, Part B is true
    private final ReentrantLock mutex = new ReentrantLock(); //This is how we avoid conflict

    public WriterThreadUnsync (String content, boolean useLock) {
        this.content = content;
        this.useLock = useLock;
    }

    public void run() {
        //Get Path
        String FILE_PATH = Main.file_path;

        System.out.println("Writer Start");

        //Try to write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            //Lock will make sure that no more than one writer is active at the same time
            if (useLock)
                mutex.lock();

            //Write content in file
            writer.write(content);
            System.out.println("Write: " + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //If you lock, you must then unlock
            if (useLock)
                mutex.unlock();
        }

        System.out.println("Writer End");
    }
}
