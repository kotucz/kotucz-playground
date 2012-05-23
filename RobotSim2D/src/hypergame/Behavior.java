package hypergame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public abstract class Behavior implements Runnable {

    volatile boolean actallowed = false;
    volatile boolean actdone = false; // sure ???
//    volatile CountDownLatch yieldLatch = new CountDownLatch(1);
//    volatile CountDownLatch actLatch;
//    final Semaphore semaphore = new Semaphore(1);
    Thread process = null;

    public abstract void run();

    /**
     *  Reenter method where last ended.
     *  Run until thread calls yield then return.
     */
    public synchronized void act() {
//        {
//            semaphore.release();
//            // now method should run
//            if (process == null) {
//                (process = new Thread(this)).start();
//            }
//            try {
//                semaphore.acquire(); // after method yieldes
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Behavior.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        {
//            actLatch = new CountDownLatch(1);
//            if (process == null) {
//                (process = new Thread(this)).start();
//            }
//            yieldLatch.countDown();
//            try {
//                System.out.println("launch run");
//                actLatch.await();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Behavior.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            System.out.println("run finished");
        }
        {
            actdone = false;
            if (process == null) {
                (process = new Thread(this)).start();
            } else {
                actallowed = true;
                notifyAll();
            }
            while (!actdone) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Behavior.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Wait until act is called.
     */
    protected synchronized void byield() {

//        {
//            semaphore.release();
//            // return values to act
//            try {
//                semaphore.acquire();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Behavior.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        {
//            System.out.println("yelding ");
//            yieldLatch = new CountDownLatch(1);
//            actLatch.countDown();
//            try {
//                System.out.println("waiting for act");
//                yieldLatch.await();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Behavior.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            System.out.println("running");
//        }
        {
            actdone = true;
            notifyAll();
            while (!actallowed) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Behavior.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            actallowed = false;
        }
        return; // to yielded function
    }
    volatile static int j = 3;

    public static void main(String[] args) {
        Behavior behavior = new Behavior() {

            @Override
            public void run() {
                int i = 2;
                while (true) {
                    System.out.println("we are happy " + (i += 2) + " " + j++);
                    assert i == j;
                    byield();
                }
            }
        };
        int i = 1;
        while (true) {
            System.out.println("Are we happy? " + (i += 2) + " " + j++);
            assert i == j;
            behavior.act();
        }
    }
}
