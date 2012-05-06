/*
 * Page.java
 *
 * Created on 31. srpen 2007, 14:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tluda;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author PC
 */
public class Page {
    
    URL url;
    
    String content;
    
    /** Creates a new instance of Page */
    public Page() {
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
            System.out.println("Element "+qName);
        }
        
        public void characters(char[] ch, int start, int length) throws SAXException {
        }
        
        public void endElement(String uri, String localName, String qName) {
            
        }
        
    }
    
    
    
}
