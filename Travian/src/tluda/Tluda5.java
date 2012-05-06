package tluda;

import tools.Downloader;
import tools.DownPanel;
import tools.Download;
import tools.Bookmarks;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import tools.DownloaderPanel;
import tools.Page;
import tools.Settings;

/**
 *
 * @author  Kotuc
 */
public class Tluda5 implements Runnable {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

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
        try {

            Bookmarks addrs = new Bookmarks();

            addrs.readLinks(Settings.getFile("/tluda.links"));
//            addrs.readLinks(new File(getClass().getResource("/tluda.links").getFile()));
            
            for (URL addr : addrs.getLinks()) {
                try {

                    System.out.println(addr.toString());

                    Page page = Page.getPage(addr);

                    System.out.println(addr + " (link searching)");

                    for (URL pageLink : page.findOpenWindowLinks()) {

                        page = Page.getPage(pageLink, "");

                        for (URL vidLink : page.findLinks("wmv")) {
//                    String vidLink = downloader.findLinkToVid(content);
                            System.out.println("vidLink: " + vidLink);

//                            String part = vidLink.toString().substring(vidLink.length() - 6, vidLink.length() - 4);
//                            println(addr + " part " + part);
                            try {
                                Download down = downloader.downloadFile(vidLink.toString(), Settings.getProperty("tluda.dir", "D:/tomas/Videos/serials/"));
                                if (down != null) {
                                    downloaderPanel.add(new DownPanel(down));
                                    down.downloadFile();
                                    System.out.println(down + " complete");
                                }
                            } catch (Exception ex) {
                                System.out.println(addr + " " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.err.println(addr + " " + ex.getMessage());
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
