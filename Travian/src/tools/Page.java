/*
 * Page.java
 *
 * Created on 31. srpen 2007, 14:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Kotuc
 */
public class Page {

    private URL url;
    private String content;
    private String postdata;

    private Page() {
    }

    public static Page getPage(URL url) throws IOException {
        return getPage(url, "");
    }

    public static Page getPage(URL url, String postdata) throws IOException {
        Page page = new Page();
        page.content = HTTP.getSource(url, postdata);
        page.url = url;
        page.postdata = postdata;
        return page;
    }

    public void parseHTML() {
//        try {
//            SAXParserFactory factory= SAXParserFactory.newInstance();
////            factory.setValidating(true);
//            SAXParser parser = factory.newSAXParser();
////            parser.parse(new ByteArrayInputStream("<html></html>".getBytes()), new HTMLParser());
//            parser.parse(new ByteArrayInputStream(content.getBytes()), new HTMLParser());
//        } catch (SAXException ex) {
//            ex.printStackTrace();
//        } catch (ParserConfigurationException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
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

            System.out.println("LINK: " + matcher.group() + " -> " + link);
            try {
                links.add(new URL(link));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        System.out.println("pattern: " + matcher.pattern() + ", matches " + matches);

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
}
