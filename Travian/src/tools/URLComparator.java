/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import java.net.URL;
import java.util.Comparator;

/**
 *
 * @author Kotuc
 */
public class URLComparator implements Comparator<URL> {

    public int compare(URL o1, URL o2) {
        return o1.toString().compareTo(o2.toString());
    }
    
}
