/*
 * Downloader.java
 *
 * Created on 28. cerven 2006, 21:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Downloader implements Runnable {

    private final BlockingQueue<Download> queue = new LinkedBlockingQueue<Download>();
    private int maxParalellDownloads = 1;
    private int downloadsNow = 0;
    private final Object lock = new Object();

    public void append(Download download) {
        queue.add(download);
    }

    public void run() {
        while (true) {
            try {
                while (downloadsNow >= maxParalellDownloads) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                startNewDownload();
            } catch (InterruptedException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void startNewDownload() throws InterruptedException {
        // wait for queue to fill and start downloading
        final Download take = queue.take();
        // fill slot
        downloadsNow++;

        new Thread() {

            @Override
            public void run() {
                try {
                    take.download();
                } catch (Exception ex) {
                    queue.add(take);
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    // empty slot
                    downloadsNow--;
                    System.out.println(take + " DONE");
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
        }.start();
    }
}
