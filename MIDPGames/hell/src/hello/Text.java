/*
 * Text.java
 *
 * Created on 17. záøí 2006, 9:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/**
 *
 * @author PC
 */
public class Text {
    
    /** Creates a new instance of Text */
    public Text() {
     

    }
    
    public Text(String text) {
        this.text = text.toLowerCase();
    }
    
//    public static String HELL = "H*E*L*L";
    
    private Image image;
    private String text;
    
//    public static Text 
//            ohre, nametk, nameak, nameag,
//            hell,
//            abc, youlose, misscompl;
//            loading = new Text("loading");

    public static String 
        loading = "loading",
        contin = "pokraèovat",
        controls = "ovládání",
        about = "o nás",
        exitg = "konec",
        play = "hrát",
        newg = "nová hra",
        nametk = "tomáš kotula",
        nameak = "adam koneèný",
        nameag = "adam gajdoš",
        hell = "hell",
        abc = "abcdefghijklmnopqrstuvwxyz0123456789",
        youlose = "you lose",
        misscompl = "level completed", 
        briefing = "pøehled mise",
        mission = "mise",
        locked = "zamèeno",
        unlocked = "odemèeno";
    
    static Sprite font;
    static Sprite fontb;    
    
    public static void initTexts() {
        Hell.println("Initializing Texts:");
//        ohre = new Text("o høe");
//        nametk = new Text("Tomáš Kotula");
//        nameak = new Text("Adam Koneèný");
//        nameag = new Text("Adam Gajdoš");
//        hell = new Text("HELL");
//     
////        ohre = getText("ohre");
////        nametk = getText("tomk");
////        nameak = getText("adamk");
////        nameag = getText("adamg");
////        hell = getText("hell");
//
//        abc = new Text("abcdefghijklmnopqrstuvwxyz0123456789");
//        youlose = new Text("you lose");
//        misscompl = new Text("level completed");
        
        font = new Sprite(HImage.font, 6, 6);
        fontb = new Sprite(HImage.fontb, 7, 6);
        
        Hell.println("Initializing Texts COMPLETED");
    }
        
//    private static Text getText(String name) {
//        name = "/images/menu about/"+name+".png";
//        
//        Image img = null;
//        
//        try {
//            Hell.println("loading text: " + name);
//            img = Image.createImage(name);
//        } catch (java.io.IOException ex) {
//            Hell.println(ex.getMessage());
//            Hell.println(name);
//        }
//        
//        Text txt = new Text();
//        txt.image = img;
//        txt.text = name;
////        return img;
//        
//        return txt;
//    }
    
//    private static Text getText(String name) {
//        name = "/images/menu about/"+name+".png";
//        
//        Image img = null;
//        
//        try {
//            Hell.println("loading text: " + name);
//            img = Image.createImage(name);
//        } catch (java.io.IOException ex) {
//            Hell.println(ex.getMessage());
//            Hell.println(name);
//        }
//        
//        Text txt = new Text();
//        txt.image = img;
//        
////        return img;
//        return txt;
//    }
    
/*    public void paint(Graphics g, int x, int y, int anchor) {
//        g.drawImage(this.image, x, y, anchor);
         paint(g, x, y);
    }*/
    
    public void paint(Graphics g, int x, int y) {
        Sprite font = Text.fontb;
        for (int i = 0; i<text.length(); i++) {
            try {
                char c = text.charAt(i);
                switch (c) {
                case 'á':
                    // write hook
                    font.setFrame('a'-'a'+1);
                break;
                case 'š':
                    // write hook
                    font.setFrame('s'-'a'+1);
                break;   
                case 'è':
                    // write hook
                    font.setFrame('c'-'a'+1);
                break;   
                case ' ':
                    font.setFrame(0);
                break;
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    font.setFrame(c-'a'+1);
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    font.setFrame(c-'0'+27);
                    break;
                }
            } catch (IndexOutOfBoundsException ex) {
                Hell.println(ex.getMessage());
                Hell.println("ERROR: unknown character: '"+text.charAt(i)+"'");
            }
            font.setPosition(x, y);
            font.paint(g);
            x+=7;
        }
    }
    
    public static final int BOLD = 1, PLAIN = 0;
    public static final int LEFT = -1, CENTER = 0, RIGHT = 1;
    
    public static void write(Graphics g, String text, int x, int y, int ftype) {
        Sprite font;
        switch (x) {
        case LEFT:
            x = 10; break;
        case CENTER:
            x = HCanvas.width/2-(text.length()*((BOLD==ftype)?7:6)/2); break;
            
        case RIGHT:
            x = HCanvas.width-(text.length()*((BOLD==ftype)?7:6))-10; break;
                
        }
        if (BOLD==ftype) font = Text.fontb;
        else if (PLAIN==ftype) font = Text.font;
        else {
            Hell.println("ERROR: Text.write() invalid ftype ");
            return;
        }
        for (int i = 0; i<text.length(); i++) {
            try {
                char c = text.charAt(i);
                switch (c) {
                case 'á':
                    // write hook
                    g.setColor(0xFFFFFFFF);
                    g.drawLine(x, y, x+5, y-5);
                    font.setFrame('a'-'a'+1);
                break;
                case 'š':
                    // write hook
                    g.setColor(0xFFFFFFFF);
                    g.drawLine(x, y, x+5, y-5);
                    font.setFrame('s'-'a'+1);
                break;   
                case 'è':
                    // write hook
                    g.setColor(0xFFFFFFFF);
                    g.drawLine(x, y, x+5, y-5);
                    font.setFrame('c'-'a'+1);
                break;   
                case ' ':
                    font.setFrame(0);
                break;
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    font.setFrame(c-'a'+1);
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    font.setFrame(c-'0'+27);
                    break;
                }
            } catch (IndexOutOfBoundsException ex) {
                Hell.println(ex.getMessage());
                Hell.println("ERROR: unknown character: '"+text.charAt(i)+"'");
            }
            font.setPosition(x, y);
            font.paint(g);
            x+=7;
        }
    }
    
/** paints to the center of the screen
 */
/*    public void paint(Graphics g, int y) {
        int x = 0;
        for (int i = 0; i<text.length(); i++) {
            try {
                
                font.setFrame(text.charAt(i)-'a');
            } catch (IndexOutOfBoundsException ex) {
                Hell.println(ex.getMessage());
                Hell.println("ERROR: unknown character: '"+text.charAt(i)+"'");
            }
            font.setPosition(x, y);
            font.paint(g);
            x+=7;
        }
    }*/
    /*
    public static void paint(Graphics g, String text, int x, int y) {
        for (int i = 0; i<text.length(); i++) {
            try {
                
                font.setFrame(text.charAt(i)-'a');
            } catch (IndexOutOfBoundsException ex) {
                Hell.println(ex.getMessage());
                Hell.println("ERROR: unknown character: '"+text.charAt(i)+"'");
            }
            font.setPosition(x, y);
            font.paint(g);
            x+=7;
        }
    }
    */
}
