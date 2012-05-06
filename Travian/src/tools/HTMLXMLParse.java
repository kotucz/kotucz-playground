package tools;

import java.io.File;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kotuc
 */
public class HTMLXMLParse {

    public static void main(String[] args) throws Exception {
//        File file = new File("www.seznam.cz.htm");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setExpandEntityReferences(false);
        
        DocumentBuilder db = dbf.newDocumentBuilder();
//        System.out.println("Validatin "+db.isXIncludeAware());
        Document doc = db.parse(new URL("http://mia-movies.com").openStream());
//        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        System.out.println("Root element " + doc.getDocumentElement().getNodeName());
        NodeList nodeLst = doc.getElementsByTagName("employee");
        System.out.println("Information of all employees");

        for (int s = 0; s < nodeLst.getLength(); s++) {

            Node fstNode = nodeLst.item(s);

            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                Element fstElmnt = (Element) fstNode;
                NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("firstname");
                Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                NodeList fstNm = fstNmElmnt.getChildNodes();
                System.out.println("First Name : " + ((Node) fstNm.item(0)).getNodeValue());
                NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("lastname");
                Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
                NodeList lstNm = lstNmElmnt.getChildNodes();
                System.out.println("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());
            }

        }
    }
}
