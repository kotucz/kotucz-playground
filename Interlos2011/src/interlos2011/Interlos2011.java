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
public class Interlos2011 {

    enum Dir {

        N(0, -1),
        E(1, 0),
        S(0, 1),
        W(-1, 0);

        private Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        final int dx, dy;
    }
    static char[][] m = new char[100][100];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        BufferedReader r = new BufferedReader(new FileReader("draci_krivka.txt"));

        String line = null;
        
        int x = 0, y = 0;
        
        int h = 0;
        while ((line = r.readLine()) != null) {
//            System.out.println(""+line);
            for (int i = 0; i < 40; i++) {
                m[i][h] = line.charAt(i);
                if (line.charAt(i)=='*') {
                    x = i;
                    y = h;
                }
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

        String seq = "L";
        
        for (int i = 0; i < 10; i++) {
            int b = seq.length()/2;
            System.out.println(""+b);
            String newSeq = seq+"L"+seq.substring(0, b)+"R"+seq.substring(b+1, b+b+1);
            seq = newSeq;
            System.out.println(""+seq);
        }

        int dir = 10000*4+2;
        
        for (int i = 0; i < seq.length(); i++) {
            if (m[x][y]!=0) System.out.print(m[x][y]);
            m[x][y] = 0;
            x += Dir.values()[dir%4].dx;
            y += Dir.values()[dir%4].dy;
            dir+=(seq.charAt(i)=='R')?1:-1;
        }
        

    }
}
