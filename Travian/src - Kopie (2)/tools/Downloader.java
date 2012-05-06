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

/**
 *
 * @author Kotuc
 */
public class Downloader {

    private List<Download> downloads = new ArrayList<Download>();

    /**
     * 
     * @param address
     * @param directory
     * @return
     * @throws java.lang.Exception
     */
    public Download downloadFile(String address, String directory) throws Exception {

        URL sourceURL = new URL(address);

        String filename = new File(address).getName();

        File targetFile = new File(directory, filename);

        System.out.println("TARGET: "+targetFile);
       
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
