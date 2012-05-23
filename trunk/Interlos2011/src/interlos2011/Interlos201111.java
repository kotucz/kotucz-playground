/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interlos2011;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author tkotula
 */
public class Interlos201111 {

    static boolean[][] m = new boolean[100][100]; 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        BufferedReader r = new BufferedReader(new FileReader("mrizka.txt"));
        
        String line = null;
        int h = 0;
        while ((line = r.readLine())!=null) {
            for (int i = 0; i < 100; i++) {
                m[h][i] = line.charAt(i)=='1';
//                System.out.print(""+((m[h][i])?'A':'-'));
            }
//            System.out.println("");
        // TODO code application logic here
//
//        list.add(0);
//        list.add(0);
//        list.add(0);
//        list.add(1);
//        list.add(1);
            h++;
        }
        
        for (int i = 0; i < 10000; i++) {
            for (int j = i+101; j < 10000; j++) {
                int x1 = i%100;
                int x2 = j%100;
                
                int y1 = i/100;
                int y2 = j/100;
                
                if (
                        x1 != x2 &&
                        y1 != y2 &&
                        
                        m[x1][y1] &&
                        m[x1][y2] &&
                        m[x2][y1] &&
                        m[x2][y2]
                        ) {
                    System.out.println(""+x1+" "+x2+" "+y1+" "+y2);
                }
                
            }
        }
        

    }
}
