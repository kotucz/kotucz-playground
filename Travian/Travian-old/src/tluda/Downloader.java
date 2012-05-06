/*
 * Downloader.java
 *
 * Created on 28. cerven 2006, 21:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tluda;

import java.net.*;
import java.io.*;

/**
 *
 * @author PC
 */
public class Downloader {

    /** Downloadernew instance of DoDownloader*/
//    public Downloader() {

//    }

       
    public Downloader() {
    }
            
    public String loadPage(String filename) {

        URL url = null;

        String content = "";

        try {
          url  = new URL(filename);
          println("downloading page:"+url+":");

          DataInputStream dis = new DataInputStream(url.openStream());      
            byte[] b = new byte[dis.available()];
            while (0<dis.read(b)) {
                content+=new String(b);
            };

//            println(content);

            dis.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            println("loadPage() Exception"+ex);
        }

        println("download complete");

        return content;
    }

    protected String currentFile="";
    protected long filesize = 5000000;//new File(url.toString()).length();
    protected int totalsize = 0;
     
    
    public void downloadFile(String address, String directory) {


        URL url = null;


        File file, targetFile = null;
        FileOutputStream fos = null;
        DataInputStream dis = null;

//            file = new javax.swing.JFileChooser().getSelectedFile();

        String filename = address.substring(address.lastIndexOf("/")+1, address.length());

        targetFile = new File(directory, filename);
        
        currentFile = targetFile.getName();
        
        if (targetFile.exists()) {
            println("file "+targetFile+" already downloaded");
            return;
        }
        
        file = new File(directory, "file.tmp");

        try {
            fos = new FileOutputStream(file);
            


         url  = new URL(address);
          println("\ndownloading file:"+url+":\n");

           dis = new DataInputStream(url.openStream());      
//                byte[] b = new byte[dis.available()];
//                while (0<dis.read(b)) {

           filesize = 5000000;//new File(url.toString()).length();
           totalsize = 0;
           int i = 0;

           while (true) {
               i = dis.read();
               if (i==-1) break;
               totalsize++;
               fos.write(i);                   
           }


//               while (filesize>0) {
//                   totalsize += filesize;
//                   
//                   print("file size: "+filesize+"B\n");
//
//                   for (; i<totalsize; i++) {
//                       progressBar1.setValue(100*i/totalsize);
//                       fos.write(dis.read());
//                   };
//
//                   Thread.sleep(50000);
//            
//                   filesize = dis.available();
//               
//               }

           println("total size: "+totalsize+"B\n");


           dis.close();
           fos.close();

           file.renameTo(targetFile);
           
        } catch (Exception ex) {

            ex.printStackTrace();
            println("\nIOException"+ex);
            return;

        }

        
        println("download complete");
        println("saved as: "+file);

        currentFile="";
        filesize=1;
        totalsize=1;
        
    }

    public String findLinkToPage(String hostname, String content) {
        int pos = 0;
        pos = content.indexOf("<a href=\"\" ", pos);
        if (pos<0) {
            System.err.println("ERROR: link not found");
            println("#################ERROR: link not found###########");
            return "NOT FOUND";
        }

        String link = hostname+content.substring(pos=content.indexOf("('", pos)+3, pos=content.indexOf("',", pos));

        return link;
    }       

    public String[] findOpenWindowPages(String hostname, String content) {
        
        String[] link = new String[3];
        
        int pos = 0;
        
        for (int i = 0; i<3; i++) {
        
            pos = content.indexOf("openWindow", pos);
            if (pos<0) {
                System.err.println("ERROR: link not found");
                println("#################ERROR: link not found###########");
                return new String[] {"NOT FOUND"};
            }

            link[i] = hostname+content.substring(pos=content.indexOf("('", pos)+3, pos=content.indexOf("',", pos));
            
            println("link "+i+"="+link[i]);
            
        }
        
        return link;
    }
    
    public String findLinkToVid(String content) {
        int pos = 0, start = 0, end = 0;
//            pos = content.indexOf("<a href=\"\" ", pos);
//            if (pos<0) {
//                System.err.println("ERROR: link not found");
//                print("#################ERROR: link not found###########");
//                return "NOT FOUND";
//            }

        println("trying to find vid link:");

        start = content.indexOf("http://whore", pos);
        end = content.indexOf(".wmv", start)+4;

//        print("s: "+start+" end: " + end +"\n" );

        String link = content.substring(start, end);

        return link;
    }       

    
    protected void println(String s) {
        System.out.println(s);
    }
    
    public int getTotalsize() {
        return totalsize;
    }

    public long getFilesize() {
        return filesize;
    }

    public String getCurrentFile() {
        return currentFile;
    }
    
    public int getProgress() {
        return (int)(100*((float)totalsize/(float)filesize));
    }
    
}
