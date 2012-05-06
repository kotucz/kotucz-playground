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
    private File asociatedFile = null;

    private void setAsociation(File file) {
        this.asociatedFile = file;
        try {
            readLinks(asociatedFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bookmarks.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bookmarks.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {

                    write(asociatedFile);
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
    public void readLinks(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int count = 0;
        int errors = 0;
        while ((line = reader.readLine()) != null) {
            try {
                add(line);
                count++;
            } catch (MalformedURLException malformedURLException) {
                System.err.println("Malformed: " + line);
                errors++;
            }
        }
        System.out.println("Imported " + count + " links. " + errors + " errors.");
        reader.close();
    }

    /**
     * 
     * @param file
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void readLinksFromHTML(File file) throws FileNotFoundException, IOException {
        Page page = Page.getPage(file.toURI().toURL());
        System.out.println("page length: " + page.getContent().length());
        links.addAll(page.findLinks(""));
    }

    /**
     * 
     * @param file
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void readLinksFromOpearaSessions(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            for (String url : line.split("[= ]")) {
//                System.out.println("adding "+url);
                try {
                    add(url);
                } catch (MalformedURLException malformedURLException) {
                    System.err.println(malformedURLException.getLocalizedMessage());
                }
                try {
                    if (url.startsWith("www")) {
                        add("http://" + url);
                    }
                } catch (MalformedURLException malformedURLException) {
                    System.err.println(malformedURLException.getLocalizedMessage());
                }
            }

        }
        reader.close();
    }

    public void add(URL url) {
        links.add(url);
    }

    public void add(String url) throws MalformedURLException {
        add(new URL(url));
    }

    public void addAll(Bookmarks books2) {
        this.links.addAll(books2.links);
    }

    public void sort() {
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
    public void write(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (URL url : links) {
            writer.write(url.toString());
            writer.newLine();
        }
        writer.close();
    }

    public void remove(URL url) {
        links.remove(url);
    }

    public void removeAll(Collection<?> c) {
        links.removeAll(c);
    }
}
