import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class WriterThreadUnsync implements Runnable{

    private final String content;
    private boolean useLock = false;
    private final ReentrantLock mutex = new ReentrantLock();

    public WriterThreadUnsync (String content, boolean useLock) {
        this.content = content;
        this.useLock = useLock;
    }

    public void run() {
        String FILE_PATH = Main.file_path;

        System.out.println("Writer Start");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (useLock)
                mutex.lock();
            writer.write(content);
            System.out.println("Write: " + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (useLock)
                mutex.unlock();
        }

        System.out.println("Writer End");
    }
}
