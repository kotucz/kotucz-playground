package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Bookmarks {

    private List<URL> links = new ArrayList<URL>();

    public Bookmarks() {
    }
    
    /**
     * constructs bookmarks
     * @param asociatedFile firstly imports bookmarks from file
     * then stores it back, when program terminates
     */
    public Bookmarks(File asociatedFile) {
        setAsociation(asociatedFile);
    }
    
    private File asociatedFile = null;

    
    void setAsociation(File file) {
        this.asociatedFile = file;
        try {
            importLinks(asociatedFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bookmarks.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bookmarks.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {

                    store(asociatedFile);
                } catch (IOException ex) {
                    Logger.getLogger(Bookmarks.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            });

    }

    /**
     * 
     * @param file
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void importLinks(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int count = 0;
        int errors = 0;
        while ((line = reader.readLine()) != null) {
            try {
                addLink(line);
                count++;
            } catch (MalformedURLException malformedURLException) {
                System.err.println("Malformed: "+line);
                errors++;
            }
        }
        System.out.println("Imported "+count+" links. "+errors+" errors.");
        reader.close();
    }

    /**
     * 
     * @param file
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void importLinksFromHTML(File file) throws FileNotFoundException, IOException {
        Page page = Page.getPage(file.toURI().toURL());
        System.out.println("page length: "+page.getContent().length());
        links.addAll(page.findLinks(""));
    }

    /**
     * 
     * @param file
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void importLinksFromOpearaSessions(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                addLink(line.split("=")[1]);
            } catch (ArrayIndexOutOfBoundsException aiobex) {
            } catch (MalformedURLException malformedURLException) {
            }
        }
        reader.close();
    }

    public void addLink(URL url) {
        links.add(url);
    }

    public void addLink(String url) throws MalformedURLException {
        addLink(new URL(url));
    }

    public void order() {
        Collections.sort(links, new URLComparator());
    }
    
    /**
     * 
     * @return
     */
    public Collection<URL> getLinks() {
        return links;
    }

    public boolean contains(URL url) {
        return links.contains(url);
    }

    /**
     * 
     * @param file
     * @throws java.io.IOException
     */
    public void store(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (URL url : links) {
            writer.write(url.toString());
            writer.newLine();
        }
        writer.close();
    }
}
