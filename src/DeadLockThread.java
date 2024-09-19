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
        //Get the two file paths from main
        String FILE_PATH1 = Main.file_path;
        String FILE_PATH2 = Main.file_path2;

        System.out.println("Deadlock Start");

        //Lock down 1
        lock1.lock();
        try {
            //Both threads will write to this file at the same time
            WriteToFile(FILE_PATH1, Thread.currentThread().getName() + " Writing To: " + FILE_PATH1 + "\nCONTENT:" + content);
            System.out.println(Thread.currentThread().getName() + " is Locked to " + FILE_PATH1);

            //This sleep makes deadlock more common by waiting for the second thread to also access file 1(so they tell me)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            //This is where it will get stuck, as both lock1's haven't been unlocked and it's trying to lock 2 at the same time
            System.out.println(Thread.currentThread().getName() + " is waiting for " + FILE_PATH2);
            lock2.lock();
            //If it's somehow successful, we've avoided deadlock
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

    //Write "content" to path "path"
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
