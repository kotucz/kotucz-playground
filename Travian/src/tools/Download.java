package tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Kotuc
 */
public class Download {

    private URL sourceURL;
    private File targetFile;
    private long totalSize;
    private long currentSize;
    private long startTime;

    /**
     * 
     * @param sourceURL
     * @param targetFile
     */
    public Download(URL sourceURL, File targetFile) {
        this.sourceURL = sourceURL;
        this.targetFile = targetFile;
    }

    /**
     * 
     * @throws java.io.IOException 
     */
    public void downloadFile() throws IOException {

        startTime = System.currentTimeMillis();
        
        System.out.println("DOWNLOAD: " + sourceURL + " to " + targetFile);

        URLConnection conn = sourceURL.openConnection();
//        conn.connect();
        totalSize = conn.getContentLength();

        if ((targetFile.exists()) && (targetFile.length() == totalSize)) {
            currentSize = totalSize;
        } else {

            File tmpfile = new File(targetFile.getCanonicalPath() + ".part");
            BufferedOutputStream dist = new BufferedOutputStream(new FileOutputStream(tmpfile));

            BufferedInputStream source = new BufferedInputStream(conn.getInputStream());

            while (currentSize < totalSize) {
                int avl = Math.min(source.available(), 100000);
                byte[] buf = new byte[100000];
                source.read(buf, 0, avl);
                dist.write(buf, 0, avl);
                currentSize += avl;

//                System.out.print("progress: " + currentSize + " of " + totalSize + "\r");

//                try {
//                    Thread.sleep(345);
//                } catch (InterruptedException interruptedException) {
//                }

            }
            source.close();
            dist.close();
            tmpfile.renameTo(targetFile);
        }

    }

    /**
     * 
     * @return
     */
    public long getTotalSize() {
        return totalSize;
    }

    /**
     * 
     * @return
     */
    public long getCurrentSize() {
        return currentSize;
    }

    /**
     * 
     * @return
     */
    public boolean completed() {
        return (currentSize == totalSize);
    }

    /**
     * 
     * @return
     */
    public int getProgress() {
        if (totalSize > 0) {
            return (int) (100 * currentSize / totalSize);
        } else {
            return 0;
        }
    }

    /**
     * 
     * @return
     */
    public URL getSourceURL() {
        return sourceURL;
    }

    /**
     * 
     * @return
     */
    public File getTargetFile() {
        return targetFile;
    }

    @Override
    public String toString() {
        return targetFile.getName();
    }
    
    /**
     * 
     * @return
     */
    public long getSpeed() {
        return 1000*currentSize/(System.currentTimeMillis()-startTime);
    }
    
}
