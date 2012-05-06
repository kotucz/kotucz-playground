package tluda;

import tools.Downloader;
import tools.DownPanel;
import tools.Download;
import tools.Bookmarks;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author  Kotuc
 */
public class Tluda5 extends JPanel implements Runnable {

    public Tluda5() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(400, 300));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                final Tluda5 mf = new Tluda5();

                JFrame mmf = new JFrame("Tluda5");
                mmf.setLocation(200, 200);
//                mmf.setUndecorated(true);
                mmf.add(mf);
                mmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mmf.pack();
                mmf.setVisible(true);

                new Thread(mf).start();


            }
        });



    }
    private Downloader downloader = new Downloader();

    public void run() {
        try {

            Bookmarks addrs = new Bookmarks();

//            addrs.addLink("http://www.mia-movies.com/");
//            addrs.addLink("http://www.slutsvideos.com/");
//            addrs.addLink("http://www.nastyalien.com/");
//            addrs.addLink("http://www.hornytiger.com/");
//            addrs.addLink("http://www.whorevideos.com/");
//            addrs.addLink("http://www.amazingcum.com/");

            addrs.importLinks(new File(getClass().getResource("/tluda.links").getFile()));

            for (URL addr : addrs.getLinks()) {
//        for (String addr : addrs) {
                try {

                    println(addr.toString());

                    String content = downloader.getContent(addr.toString());

                    println(addr + " (link searching)");

                    for (String pageLink : downloader.findOpenWindowPages(addr.toString(), content)) {

                        content = downloader.getContent(pageLink);

                        for (String vidLink : downloader.findVidLinks(content)) {
//                    String vidLink = downloader.findLinkToVid(content);
                            println("vidLink: " + vidLink);

                            String part = vidLink.substring(vidLink.length() - 6, vidLink.length() - 4);

                            println(addr + " part " + part);

                            try {
                                Download down = downloader.downloadFile(vidLink, "D:/tomas/Videos/serials/");
                                if (down != null) {
                                    add(new DownPanel(down));
                                    System.out.println("components" + getComponents());
                                    repaint();
                                    down.downloadFile();
                                    System.out.println(down + " complete");
                                }
                            } catch (Exception ex) {
                                println(addr + " " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (Exception ex) {
                    println(addr + " " + ex.getMessage());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tluda5.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tluda5.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void println(String s) {
        System.out.println(s);
    }
}
