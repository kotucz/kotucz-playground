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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kotuc
 */
public class Downloader {

    private List<Download> downloads = new ArrayList<Download>();

    public String findLinkToPage(String text, String content) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 
     * @param urlstring
     * @return
     * @throws java.io.IOException 
     */
    public String getContent(String urlstring) throws IOException {

        StringBuffer content = new StringBuffer();

        URL url = new URL(urlstring);
        println("downloading page:" + url + ":");

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        reader.close();

        println("download complete");

        return content.toString();
    }

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



//    public String findLinkToPage(String hostname, String content) {
//        int pos = 0;
//        pos = content.indexOf("<a href=\"\" ", pos);
//        if (pos < 0) {
//            System.err.println("ERROR: link not found");
//            println("#################ERROR: link not found###########");
//            return "NOT FOUND";
//        }
//
//        String link = hostname + content.substring(pos = content.indexOf("('", pos) + 3, pos = content.indexOf("',", pos));
//
//        return link;
//    }
    


//        public String[] findOpenWindowPages(String hostname, String content) {
//
//        String[] link = new String[3];
//
//        int pos = 0;
//
//        for (int i = 0; i < 3; i++) {
//
//            pos = content.indexOf("openWindow", pos);
//            if (pos < 0) {
//                System.err.println("ERROR: link not found");
//                println("#################ERROR: link not found###########");
//                return new String[]{"NOT FOUND"};
//            }
//
//            link[i] = hostname + content.substring(pos = content.indexOf("('", pos) + 3, pos = content.indexOf("',", pos));
//
//            println("link " + i + "=" + link[i]);
//
//        }
//
//        return link;
//    }

    
    public Set<String> findVidLinks(String content) {
        
        println("trying to find vid link:");
    
        Set<String> links = new HashSet<String>();
        
        String formats = "wmv"; // example mp3|wmv|mpg
        
        Pattern pattern = Pattern.compile("href=\\p{Punct}?(http[^<>\" ]*("+formats+"))[\\p{Punct}|>| ]");
        Matcher matcher = pattern.matcher(content);
   
        int matches = 0;
        
        while (matcher.find()) {
            
            matches++;
            
            links.add(matcher.group(1));
            
            
            
        }
               
        println("pattern: "+matcher.pattern()+", matches "+matches);
        
        return links;
    
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
        end = content.indexOf(".wmv", start) + 4;

//        print("s: "+start+" end: " + end +"\n" );

        String link = content.substring(start, end);

        return link;
    }

    
    protected void println(String s) {
        System.out.println(s);
    }
}
