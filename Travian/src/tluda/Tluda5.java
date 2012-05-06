package tluda;

import java.io.File;
import java.util.Collection;
import tools.Downloader;
import tools.DownPanel;
import tools.Download;
import tools.Bookmarks;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import tools.DownloaderPanel;
import tools.Link;
import tools.Page;

/**
 *
 * @author  Kotuc
 */
public class Tluda5 implements Runnable {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Tluda5 tluda = new Tluda5();

                JFrame mmf = new JFrame("Tluda5");
                mmf.setLocation(200, 200);
//                mmf.setUndecorated(true);
                mmf.add(tluda.downloaderPanel);
                mmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mmf.pack();
                mmf.setVisible(true);

                new Thread(tluda).start();

            }
        });



    }
    private Downloader downloader = new Downloader();
    private DownloaderPanel downloaderPanel = new DownloaderPanel();

    public void run() {
        new Thread(downloader).start();
        try {

            Bookmarks addrs = new Bookmarks();

            addrs.readLinks(new File("./tluda.links"));
//            addrs.readLinks(new File(getClass().getResource("/tluda.links").getFile()));

            for (URL addr : addrs.getLinks()) {
                System.out.println(addr.toString());

                Page page = Page.getPage(addr);

                System.out.println(addr + " (link searching)");

                Collection<URL> findOpenWindowLinks = page.findOpenWindowLinks();
                if (findOpenWindowLinks.isEmpty()) {
                    System.err.println(addr + " Nothing found! -> mia");
                    for (URL url : page.findLinksBrute()) {
                        if (url.toString().contains("series")) {
                            System.err.println(url);
                            findOpenWindowLinks.add(url);

                        }
                    }
                }
                for (URL pageLink : findOpenWindowLinks) {

                    page = Page.getPage(pageLink);
                    System.out.println("Page: "+pageLink);

                    for (Link vidLink : page.findLinksLinks()) {
                        if (!vidLink.getUrl().toString().endsWith("wmv")) {
                            continue;
                        }
//                    String vidLink = downloader.findLinkToVid(content);
                        System.out.println("vidLink: " + vidLink);

//                            String part = vidLink.toString().substring(vidLink.length() - 6, vidLink.length() - 4);
//                            println(addr + " part " + part);
                        try {
                            String filename = new File(vidLink.getUrl().getFile()).getName();
                            File targetFile = new File("C:/Users/Kotuc/Videos/mia", filename);
                            System.out.println("TARGET: " + targetFile);

                            Download down = new Download(vidLink, targetFile);
//                                Download down = downloader.downloadFile(vidLink.toString(), Settings.getProperty("tluda.dir", "D:/tomas/Videos/serials/"));

                            downloaderPanel.add(new DownPanel(down));

                            downloader.append(down); // manager
                            // down.download(); // blocking

                            System.out.println(down + " complete");
                        } catch (Exception ex) {
                            System.out.println(addr + " " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }

            }

            downloaderPanel.write("That's all :)");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tluda5.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tluda5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
