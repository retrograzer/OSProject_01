import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockThread implements Runnable {
    private final String content;
    private ReentrantLock lock1 = new ReentrantLock();
    private ReentrantLock lock2 = new ReentrantLock();

    public DeadLockThread (String content, ReentrantLock lock1, ReentrantLock lock2) {
        this.content = content;
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    public void run() {
        String FILE_PATH1 = Main.file_path;
        String FILE_PATH2 = Main.file_path2;

        System.out.println("Deadlock Start");

        lock1.lock();
        try {
            WriteToFile(FILE_PATH1, Thread.currentThread().getName() + " Writing To: " + FILE_PATH1 + "\nCONTENT:" + content);
            System.out.println(Thread.currentThread().getName() + " is Locked to " + FILE_PATH1);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(Thread.currentThread().getName() + " is waiting for " + FILE_PATH2);
            lock2.lock();
            try {
                WriteToFile(FILE_PATH2, Thread.currentThread().getName() + " Writing To: " + FILE_PATH2 + "\nCONTENT:" + content);
                System.out.println(Thread.currentThread().getName() + " is Locked to " + FILE_PATH2);
            } finally {
                lock2.unlock();
            }
        } finally {
            lock1.unlock();
        }

        System.out.println("Writer End");
    }

    private void WriteToFile (String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(content);
            writer.newLine();
            System.out.println("Write: " + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
