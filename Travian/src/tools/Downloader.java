/*
 * Downloader.java
 *
 * Created on 28. cerven 2006, 21:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tools;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Downloader {

    private List<Download> downloads = new ArrayList<Download>();
    private int maxParalellDownloads = 2;

    private int downloadsNow = 0;
    
    public void append(Download download) {
        downloads.add(download);
    }

    public void run() {
        while (true) {
            
            if (downloadsNow<maxParalellDownloads) {
                // startNewDownload();
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param address
     * @param directory
     * @return
     * @throws java.lang.Exception
     * 
     * @deprecated
     * 
     */
    public Download downloadFile(String address, String directory) throws Exception {

        URL sourceURL = new URL(address);

        String filename = new File(address).getName();

        File targetFile = new File(directory, filename);

        System.out.println("TARGET: " + targetFile);

        final Download download = new Download(sourceURL, targetFile);

//        new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    download.downloadFile();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            
//        }.start();

        return download;

    }
}
