import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static final String file_path = "shared_file.txt";
    public static final String file_path2 = "shared_file_2.txt";

    static void ClearFile () {
        try (FileWriter writer = new FileWriter(file_path, false)) {
            System.out.println("File Cleared of Previous Contents...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(file_path2, false)) {
            System.out.println("File Cleared of Previous Contents...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Threads
    static void Part01 () {
        ClearFile();
        //Set up two readers and two writers
        WriterThread writeThread1 = new WriterThread("First Writer Thread Hello!");
        WriterThread writeThread2 = new WriterThread(" Second Writer Thread Hello!");
        ReaderThread readThread1 = new ReaderThread();
        ReaderThread readThread2 = new ReaderThread();

        //Make objects into threads
        Thread wThread1 = new Thread(writeThread1);
        Thread wThread2 = new Thread(writeThread2);
        Thread rThread1 = new Thread(readThread1);
        Thread rThread2 = new Thread(readThread2);

        //Write with first thread
        wThread1.start();
        try {
            wThread1.join();
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e);
        }

        //Read first thread's writing
        rThread1.start();
        try {
            rThread1.join();
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e);
        }

        //Write with second thread
        wThread2.start();
        try {
            wThread2.join();
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e);
        }

        //Read second thread's writing
        rThread2.start();
        try {
            rThread2.join();
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e);
        }
    }

    //No Mutex
    static void Part02A () {
        ClearFile();
        //Set up two readers and two writers (both of them without mutex for now)
        WriterThreadUnsync wUnsync1 = new WriterThreadUnsync("(Unsync) First Writer Thread Hello!", false);
        WriterThreadUnsync wUnsync2 = new WriterThreadUnsync(" (Unsync) Second Writer Thread Hello!", false);
        ReaderThreadUnsync rUnsync1 = new ReaderThreadUnsync(false);
        ReaderThreadUnsync rUnsync2 = new ReaderThreadUnsync(false);

        //Make objects into threads
        Thread wThread1 = new Thread(wUnsync1);
        Thread wThread2 = new Thread(wUnsync2);
        Thread rThread1 = new Thread(rUnsync1);
        Thread rThread2 = new Thread(rUnsync2);

        try {
            wThread1.start();
            //wThread1.join();

            rThread1.start();
            //rThread1.join();

            wThread2.start();
            //wThread2.join();

            rThread2.start();
            //rThread2.join();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    //With Mutex
    static void Part02B () {
        ClearFile();
        //Set up two readers and two writers (both of them with mutex)
        WriterThreadUnsync wUnsync1 = new WriterThreadUnsync("(Locked) First Writer Thread Hello!", true);
        WriterThreadUnsync wUnsync2 = new WriterThreadUnsync(" (Locked) Second Writer Thread Hello!", true);
        ReaderThreadUnsync rUnsync1 = new ReaderThreadUnsync(true);
        ReaderThreadUnsync rUnsync2 = new ReaderThreadUnsync(true);

        //Make objects into threads
        Thread wThread1 = new Thread(wUnsync1);
        Thread wThread2 = new Thread(wUnsync2);
        Thread rThread1 = new Thread(rUnsync1);
        Thread rThread2 = new Thread(rUnsync2);

        wThread1.start();
        rThread1.start();
        wThread2.start();
        rThread2.start();

        try {
            wThread1.join();
            rThread1.join();
            wThread2.join();
            rThread2.join();
        } catch (RuntimeException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Deadlock
    static void Part03 () {
        ClearFile();
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        DeadLockThread dlThread1 = new DeadLockThread("Hello!", lock1, lock2);
        DeadLockThread dlThread2 = new DeadLockThread("Goodbye!", lock2, lock1);

        //Make objects into threads
        Thread wThread1 = new Thread(dlThread1);
        Thread wThread2 = new Thread(dlThread2);

        wThread1.start();
        wThread2.start();

        try {
            wThread1.join();
            wThread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Deadlock Avoidance
    static void Part04() {
        ClearFile();
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        DeadlockAvoidanceThread dlThread1 = new DeadlockAvoidanceThread("Hello!", file_path, file_path2);
        DeadlockAvoidanceThread dlThread2 = new DeadlockAvoidanceThread("Goodbye!", file_path2, file_path);

        //Make objects into threads
        Thread wThread1 = new Thread(dlThread1);
        Thread wThread2 = new Thread(dlThread2);

        wThread1.start();
        wThread2.start();

        try {
            wThread1.join();
            wThread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        //Part01();

        //Part02A();

        //Part02B();

        //Part03();

        Part04();

        System.out.println("Main Thread ended");
    }
}

