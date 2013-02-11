/*
 * MySystem.java
 *
 * Created on 4. èerven 2006, 13:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package spygame;

import java.util.*;
import javax.microedition.lcdui.*;
import java.io.*;

/**
 *
 * @author PC
 */
public class MySystem {
    
  
    public static void error(String error) {
        System.err.println(error);
    }
    
    public static void print(String text) {
        System.err.print(text);
    }
    
    public static void println(String text) {
        System.err.println(text);
    }
    
    static Random randomGenerator = new Random(System.currentTimeMillis());
    
    public static int random(int i) {
        if (i<2) return 0;
        int r = Math.abs(randomGenerator.nextInt())%i;
 //       Game.error("random "+i+":"+r);
        return r;
    }
          
    /**
     *  loads texts from UTF-8 File every row finished by \n has new index
     */
    public static String[] loadTexts(String filename) {
        return system.lTexts(filename);
    }
    
    static MySystem system = new MySystem();
    
    protected String[] lTexts(String filename) {
        MySystem.println("Reading \""+filename+"\" ... ");
        InputStream is = getClass().getResourceAsStream(filename);
        DataInputStream dis = new DataInputStream(is);
        
        byte[] b = null;
        char c;
        String content = new String();
        
        Vector vec = new Vector();
        
        try {
            b = new byte[dis.available()];
            dis.read(b);
/*            int i = 0;
            for (int j = 0; j<b.length; j++) {
                c = (char)dis.readChar();
                if ('\n'==c) {// naète znaku kolik v b
                        
                    txts[i] = content.toString();
                    MySystem.print("\n>"+txts[i]+"<");
                    i++;
                    content = new StringBuffer();
                } else content.append(c);
            }
  */          
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        content = new String(b);
                
        int j = 0, k = 0;
        while (j<content.length()) {
            k = content.indexOf('\n', j);//end of line
            vec.addElement(new String(b, j, k-j-1)); 
            j=k+1;
           //    for (int l = 0; l<txts[i].length(); l++) MySystem.print(" "+(int)txts[i].charAt(l)); 
        }
        
        
        MySystem.println("vec.size:"+vec.size());
        String[] txts = new String[vec.size()];
        vec.copyInto(txts);       
        
/*        int i;
        Enumeration e;
        for (e = vec.elements(), i = 0; e.hasMoreElements(); i++) {
            txts[i] = (String)e.nextElement();
//            MySystem.print("\n>"+txts[i]+"<");
            getWords(txts[i]);
        }
  */      
        return txts;
    }
    
      
    /**
     *  @throws NullPointerException if s is null
     */
    public static int[] getInts(String s) {
        int pos=0, epos=0, i=0;
        int[] ints = new int[50];
        
        while (pos<s.length()) {
            try {    
                epos = s.indexOf(" ", pos);
                if (epos<=pos) epos=s.length();
                ints[i] = Integer.parseInt(s.substring(pos, epos));
                i++;
      
            } catch (Exception ex) {};
            pos = epos+1;
        }
        
//        println("ints: "+ints);
        
        return ints;
    }

    /**
     *  @throws NullPointerException if s is null
     * 
     */
    public static String[] getWords(String s) {
        if (s==null) {
            MySystem.error("getWords(): string null");
            return null;
        }
                
        Vector vec = new Vector();
        String sb = new String();
        
        char c=' ';
        for (int i=0; i<s.length(); i++) {
            c = s.charAt(i);
            if (c==' ') {
                if (sb.length()>0) {
                    vec.addElement(sb);
                }
                sb = "";
            } else 
                sb+=c;
        }
        if (sb.length()>0) {
            vec.addElement(sb);
        }
        
        MySystem.println("words:"+vec.size());
        String[] params = new String[vec.size()];
        
        vec.copyInto(params);
        
/*      int p=0;
        Enumeration e;
        for (e = vec.elements(), p = 0; e.hasMoreElements(); p++) {
            params[p] = (String)e.nextElement();
 //           MySystem.print("-"+params[p]+"-");
        }
  */              
//      println("ints: "+ints);
        
        return params;
    }
    
    
}
