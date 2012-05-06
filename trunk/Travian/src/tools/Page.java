/*
 * Page.java
 *
 * Created on 31. srpen 2007, 14:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Kotuc
 */
public class Page {

    private URL url;
    private String content;
    private PostData postdata;

    private Page() {
    }

//    public static Page getPageFile(File file) throws IOException {
//
//        BufferedReader rd = new BufferedReader(new FileReader(file));
////            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String line;
//        StringBuilder content = new StringBuilder();
//        while ((line = rd.readLine()) != null) {
//            content.append(line + "\n");
//        }
//        rd.close();
//        Page page = new Page();
//        page.content = content.toString();
//        page.url = file.toURL();
//        return page;
//    }
    public static Page getPage(URL url) throws IOException {
        return getPage(url, null);
    }

//    public static Page getPage(URL url) throws IOException {
    public static Page getPage(URL url, PostData postdata) throws IOException {
        Page page = new Page();
        page.content = HTTP.getSource(url, postdata);
        page.url = url;
        page.postdata = postdata;
        return page;
    }

    public void parseHTML() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
//            factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();
//            parser.parse(new ByteArrayInputStream("<html></html>".getBytes()), new HTMLParser());
            parser.parse(new ByteArrayInputStream(content.getBytes()), new HTMLParser());
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    class HTMLParser extends DefaultHandler {

        void startElement(String uri, String localName, String qName, Attributes attributes) {
            System.out.println("Element " + qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
        }
    }

    public String getContent() {
        return content;
    }

    public URL getURL() {
        return url;
    }

    public Collection<URL> findOpenWindowLinks() {

        Set<URL> links = new TreeSet<URL>(new URLComparator());
//        [a-zA-Z0-9][/_:.-]
        Pattern pattern = Pattern.compile("openWindow\\('/([^<>]*)/',");
        Matcher matcher = pattern.matcher(content);

        int matches = 0;

        while (matcher.find()) {

            matches++;

            String link = "http://" + url.getHost() + "/" + matcher.group(1);

//            System.out.println("LINK: " + matcher.group() + " -> " + link);
            try {
                links.add(new URL(link));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

//        System.out.println("pattern: " + matcher.pattern() + ", matches " + matches);

        return links;
    }

    /**
     * 
     * @param formats example "",  "wmv", "wmv|amv|mp3"
     * @return
     */
    public Collection<URL> findLinks(String formats) {

        Set<URL> links = new TreeSet<URL>(new URLComparator());

        Pattern pattern = Pattern.compile("[href|HREF]=\\p{Punct}?([^<>\" ]*(" + formats + "))[\\p{Punct}|>| ]");
//        Pattern pattern = Pattern.compile("[href|HREF]=\\p{Punct}?([^<>\" ]*(" + formats + "))[\\p{Punct}|>| ]");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            try {
                String relLink = matcher.group(1);
                if (relLink.startsWith("http")) {
                    links.add(new URL(relLink));
                } else {
                    links.add(new URL(url.toString().substring(0, 1 + url.toString().lastIndexOf("/")) + relLink));
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return links;

    }

    /**
     *
     * @param formats example "",  "wmv", "wmv|amv|mp3"
     * @return
     */
    public Collection<URL> findLinksBrute() {

        Set<URL> links = new HashSet<URL>();

        Pattern pattern = Pattern.compile(
                "<a\\s*href=\"([^>\\s]*)\"[^>]*>"//
                //                + "(.*)"
                //                + "</a>"
                //                , Pattern.DOTALL
                );

        Pattern endpattern = Pattern.compile("</a>");


//        System.out.println(content);

        Matcher matcher = pattern.matcher(content);
        Matcher endmatcher = endpattern.matcher(content);
        int j = 0;
        while (matcher.find()) {
            j++;
//            { // debug
//                System.out.print(j);
//                for (int i = 1; i <= matcher.groupCount(); i++) {
//                    System.out.print("," + matcher.group(i));
//
//                }
//                System.out.println();
//            }

            String href = matcher.group(1);
//            String title = matcher.group(2);
            href = makeAbsolute(this.url, href);

            if (endmatcher.find(matcher.end())) {
                String title = content.substring(matcher.end(), endmatcher.start());

                Collection<URL> findImages = findImages(title);
                if (!findImages.isEmpty()) {
                    title = findImages.toString();
                }

                System.out.println(title);
            }

//            System.out.println(href + "   " + title);

            try {
//                links.add(new Link(new URL(href), title));
                links.add(new URL(href));
            } catch (MalformedURLException ex) {
                System.err.println("" + ex);
            }
        }
        System.out.println(j + " links found");
        return links;

    }

    /**
     * @return
     */
    public Collection<Link> findLinksLinks() {

        List<Link> links = new ArrayList<Link>();

        Pattern pattern = Pattern.compile(
                "<a\\s*href=\"([^>\\s]*)\"[^>]*>"//
                //                + "(.*)"
                //                + "</a>"
                //                , Pattern.DOTALL
                );

        Pattern endpattern = Pattern.compile("</a>");


//        System.out.println(content);

        Matcher matcher = pattern.matcher(content);
        Matcher endmatcher = endpattern.matcher(content);
        int j = 0;
        while (matcher.find()) {
            j++;

//            { // debug
//                System.out.print(j);
//                for (int i = 1; i <= matcher.groupCount(); i++) {
//                    System.out.print("," + matcher.group(i));
//
//                }
//                System.out.println();
//            }


            String href = matcher.group(1);
//            String title = matcher.group(2);
            href = makeAbsolute(this.url, href);

            Link link;
            try {
                link = new Link(new URL(href));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }

            link.page = this;

            if (endmatcher.find(matcher.end())) {
                String title = content.substring(matcher.end(), endmatcher.start());
                link.desc = title;
                
                final Collection<URL> findImages = findImages(title);

                for (URL url : findImages) {
                    link.addThumb(url);
                }                
                if (findImages.isEmpty()) {
                    System.err.println("NO THUMBS: "+title);
                }
            } else {
                System.err.println("No </a> found!");
            }

//            System.out.println(href + "   " + title);


//                links.add(new Link(new URL(href), title));

            links.add(link);


        }
        System.out.println(j + " links found");
        return links;

    }

    public Collection<URL> findImages(String html) {
//    public Collection<URL> findImages(int start, int end) {

        Set<URL> links = new HashSet<URL>();

        Pattern pattern = Pattern.compile(
                "<img\\s*src=\"([^>\\s]*)\"[^>]*>"//
                //                + "(.*)"
                //                + "</a>"
                //                , Pattern.DOTALL
                );

//        System.out.println(content);

        Matcher matcher = pattern.matcher(html);
        int j = 0;
        while (matcher.find()) {
            j++;
//            { // debug
//                System.out.print(j);
//                for (int i = 1; i <= matcher.groupCount(); i++) {
//                    System.out.print("," + matcher.group(i));
//
//                }
//                System.out.println();
//            }
            String href = matcher.group(1);
//            String title = matcher.group(2);

            href = makeAbsolute(url, href);


//            System.out.println(href + "   " + title);

            try {
//                links.add(new Link(new URL(href), title));
                links.add(new URL(href));
            } catch (MalformedURLException ex) {
                System.err.println("" + ex);
            }
        }
//        System.out.println(j + " links found");
        return links;

    }

    public static String makeAbsolute(URL url, String relative) {
        if (!relative.startsWith("http")) {
            String path = url.toString();
//            relative = new File(url.getFile()).getPath() + relative;
            System.err.println("relative "+path);
            if (path.lastIndexOf(".")<path.lastIndexOf("/")) {
                path += "/";
            }
//            relative = url.toString().substring(0, 1 + (url.toString()+(url.toString().endsWith("htm")?:)).lastIndexOf("/")) + relative;
            relative = path.substring(0, 1 + path.lastIndexOf("/")) + relative;
        }
        return relative;
    }

    public static void main(String[] args) throws IOException {
//        getPage(new URL("http://seznam.cz")).parseHTML();
        getPage(new URL("http://www.google.cz/")).parseHTML();
    }
}
