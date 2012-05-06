package tluda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import tools.DownPanel;
import tools.Download;
import tools.Bookmarks;
import java.io.File;
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
public class SlutMachine implements Runnable {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

//      urlTest();
//      namingTest();

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SlutMachine machine = new SlutMachine();

                JFrame mmf = new JFrame("SlutMachine");
                mmf.setLocation(200, 200);
//                mmf.setUndecorated(true);
                mmf.add(machine.downloaderPanel);
                mmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mmf.pack();
                mmf.setVisible(true);

                new Thread(machine).start();

            }
        });

    }
    
    private File downloadedBooksFile = Settings.getFile("downloaded.links");
    
    private void loadDownloadedPages() {
        downloaded = new Bookmarks();
        try {
            downloaded.importLinks(downloadedBooksFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlutMachine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlutMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    downloaded.store(downloadedBooksFile);
                } catch (IOException ex) {
                    Logger.getLogger(SlutMachine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private DownloaderPanel downloaderPanel = new DownloaderPanel();
    private Bookmarks downloaded = new Bookmarks();
    private Bookmarks bookmarks = new Bookmarks();

    private void loadBookmarks() {
        for (int i = 140; i < 444; i++) {
            try {
// i <- [536 .. 900 .. *]                
//                  bookmarks.addLink("http://www.sweet-ftvgirls.com/mgals/"+i+"/slutsvideos.html");
// i <- [100 .. 200 .. *]                         
                bookmarks.addLink("http://www.fanclubporn.com/mgals/"+i+"/slutsvideos.html");
// i <- [01 .. 100 .. *]         
//                http://www.tinasvids.com/brazbucks/bwb/09/index.html
//      http://www.sapphiclesbos.com/sapphic/044/indextrffcs.html            
            } catch (MalformedURLException ex) {
                Logger.getLogger(SlutMachine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void run() {

        loadDownloadedPages();
        loadBookmarks();

        for (URL addr : bookmarks.getLinks()) {
            if (downloaded.contains(addr)) {
                System.err.println("Skipping "+addr+" because already downloaded");
                continue;
            }
            try {
                System.out.println(addr.toString());

                Page page = Page.getPage(addr);

                int count = 0;
                for (URL vidLink : page.findLinks("wmv|mpg|jpg")) {
                    count++;
                    System.out.println("vidLink " + count + ": " + vidLink);
                    try {
                        Download down = new Download(vidLink, getTargetFile(vidLink));
                        if (down != null) {
                            downloaderPanel.add(new DownPanel(down));
                            down.downloadFile();
                            System.out.println(down + " complete");
                        }
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage()+" during "+vidLink);
                    }
                }
                if (count == 0) {
                    throw new Exception("No vidlinks found!");
                }
                downloaded.addLink(addr);
            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " during " + addr);
            }
        }
        downloaderPanel.write("That's all :)");
    }
    private static String SLUT_DIR = Settings.getProperty("sluts.dir", "D:/tomas/Videos/sluts/");

    private static void namingTest() {
        String[] addrs = new String[]{
            "http://www.fanclubporn.com/mgals/100/video4.mpg"
        };
        for (String addr : addrs) {
            try {
                System.out.println("from: " + addr);
                System.out.println("to: " + getTargetFile(new URL(addr)).toString());
            } catch (MalformedURLException ex) {
                Logger.getLogger(SlutMachine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.exit(1);
    }

    private static void urlTest() {
        try {

            URL url = new URL("http://www.fanclubporn.com/mgals/100/slutsvideos.html");
            System.out.println("URL TEST");
            System.out.println("url: " + url.toString());
            System.out.println("authority: " + url.getAuthority());
            System.out.println("file: " + url.getFile());
            System.out.println("host: " + url.getHost());
            System.out.println("path: " + url.getPath());
            System.out.println("protocol: " + url.getProtocol());
            System.out.println("query: " + url.getQuery());
            System.out.println("ref: " + url.getRef());
            System.out.println("userinfo: " + url.getUserInfo());

        } catch (MalformedURLException ex) {
            Logger.getLogger(SlutMachine.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.exit(1);
        }
    }

    private static File getTargetFile(URL url) {
        String path = SLUT_DIR + url.toString().substring(7).replaceAll("/", "_");
        return new File(path);
    }
}
