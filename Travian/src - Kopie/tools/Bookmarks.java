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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Bookmarks {

    private List<URL> links = new ArrayList<URL>();

    /**
     * 
     * @param file
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void importLinks(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            addLink(line);
        }
        reader.close();
    }

    public void addLink(URL url) {
        links.add(url);
    }

    public void addLink(String url) {
        try {
            addLink(new URL(url));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Bookmarks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @return
     */
    public Collection<URL> getLinks() {
        return links;
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
