package tools;

import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Kotuc
 */
public class FindLinksTest {

    public static void main(String[] args) throws IOException {
//        Page pageFile = Page.getPageFile(new java.io.File("./mia-movies.htm"));
        Page pageFile = Page.getPage(new java.net.URL("http://mia-movies.com"));
        System.out.println("" + pageFile.getContent());
        System.out.println("***************************************");
        for (URL link : pageFile.findLinksBrute()) {
//        for (URL link : pageFile.findImages(pageFile.getContent())) {
//            if (link.getUrl().toString().contains("series")) {
//            if (link.getTitle().contains("</a")) {
            System.out.println(link);
//            }
        }
    }
}
