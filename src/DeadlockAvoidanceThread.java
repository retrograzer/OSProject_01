import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockAvoidanceThread implements Runnable {
    private final String content;
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    private static boolean file1Locked = false;
    private static boolean file2Locked = false;
    String file_path1;
    String file_path2;

    public DeadlockAvoidanceThread (String content, String file1, String file2) {
        this.content = content;
        file_path1 = file1;
        file_path2 = file2;
    }

    public void run() {

        System.out.println("Deadlock Avoidance " + Thread.currentThread().getName() + " Start");

        //Perform a loop that checks to see if either lock is locked
        while (true) {
            if (CanAcquireLocks()) { //If both locks are unlocked, write to file
                AcquireLocks();
                break;
            } else { //If either lock is occupied, wait
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        WriteToFile(file_path1, "Writing " + content + " to " + file_path1);
        WriteToFile(file_path2, "Writing " + content + " to " + file_path2);

        System.out.println("Writer End, unlocking threads...");

        UnlockLocks();
    }

    //Are either locks occupied?
    private synchronized boolean CanAcquireLocks() {
        return !file1Locked && !file2Locked;
    }

    //Make sure the locks are in the correct order, despite what the user gave
    private synchronized void AcquireLocks () {
        if (file_path1.equals(Main.file_path)) {
            lock1.lock();
            file1Locked = true;

            lock2.lock();
            file2Locked = true;
        } else {
            lock2.lock();
            file2Locked = true;

            lock1.lock();
            file1Locked = true;
        }
    }

    //Unlock all the locks in the correct order
    private synchronized void UnlockLocks () {
        if (file_path1.equals(Main.file_path)) {
            lock2.unlock();
            file2Locked = false;

            lock1.unlock();
            file1Locked = false;
        } else {
            lock1.unlock();
            file1Locked = false;

            lock2.unlock();
            file2Locked = false;
        }
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
