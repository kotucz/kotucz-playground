/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interlos2011;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author tkotula
 */
public class Interlos20111 {

    static LinkedList<Integer> list = new LinkedList<Integer>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//
//        list.add(0);
//        list.add(0);
//        list.add(0);
//        list.add(1);
//        list.add(1);

        
        list.add(1);
        list.add(1);
        list.add(1);
        
        list.add(1);
        list.add(1);
        list.add(1);
        
        list.add(1);
        list.add(0);
        list.add(0);
        
        list.add(0);
        list.add(0);
        list.add(0);
        
        int i = 0;
        
        while (list.size() > 2) {
            int first = (int) list.getFirst();
            list.removeFirst();
            list.removeFirst();
            list.removeFirst();
            if (first == 1) {

                list.add(1);
                list.add(1);
                list.add(0);
                list.add(1);
            } else {

                list.add(0);
                list.add(0);
            }
            i++;
            System.out.println(i+" "+list);
        }

    }
}
