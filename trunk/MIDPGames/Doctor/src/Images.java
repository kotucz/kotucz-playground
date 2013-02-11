/*
 * Image.java
 *
 * Created on 17. zברם 2006, 9:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



import javax.microedition.lcdui.Image;

/**
 *
 * @author PC
 */
public class Images {
           
//    private HImage() {
//        super.;
//    }
    
    static Image 
            dr = getImage("dr"),
            bg = getImage("bg1"),
            scr1 = getImage("sketch176"),
            lista = getImage("lista176");
    
    private static Image getImage(String name) {
        name = "/images/"+name+".png";
        
        
        
        Image img = null;
        
        try {
            Game.println("loading: " + name);
            img = Image.createImage(name);
        } catch (java.io.IOException ex) {
            Game.println(""+ex);
            Game.println("ERROR: loading image "+name);
            return getImage("noimg16");
        }
        
        return img;
    }
    
    private static Image[] getImages(String name, int count) { 
        Image[] imgs = new Image[count]; 
        for (int fr = 0; fr<count; fr++) {
            imgs[fr] = getImage(name+fr);
        }
        return imgs;
    }
    
    public static Image 
            rock1,
            ms1,
            me1,
            missile1,
            le1,
            energybar,
            font,
            fontb,
            boss1,
            shot1,// = getImage("B1"),
            shot2,// = getImage("B1"),
            shot3,// = getImage("B1"),
            target1,// = getImage("T1"),
            n2 = getImage("N2"),
            logo1,// = getImage("N2"),
            sqrt,// = getImage("ST"),
            sqrf,// = getImage("SF"),
            menu1,// = getImage("MN"),
            sea1,// = getImage("SEA1"),
            grass1,
            pab1;// = getImage("PAB");
   
    
//    public static javax.microedition.lcdui.Image[] pct; 
//    public static javax.microedition.lcdui.Image[] plt;
    public static Image[][] player1;
    public static Image[] boom, stones, player, ep1, smoke, bonus;
    
    public static void init() {
        Game.println("Initializing Images:");
/*        player1 = new javax.microedition.lcdui.Image[2][];
        pct = new javax.microedition.lcdui.Image[2];
        pct[0] = getImage("PCT0");
        pct[1] = getImage("PCT1");
        player1[0] = pct;
        plt = new javax.microedition.lcdui.Image[2];
        plt[0] = getImage("PLT0");
        plt[1] = getImage("PLT1");
        player1[1] = plt;
        */
        
        rock1 = getImage("BR0");
        ms1 = getImage("MS");
        boss1 = getImage("boss");
//        shot1 = getImage("B1");
        shot1 = getImage("1");
        shot2 = getImage("2");
        shot3 = getImage("3");
        me1 = getImage("ME");
        le1 = getImage("LE");
        target1 = getImage("T1");
        missile1 = getImage("mini");
        sqrt = getImage("ST");
        sqrf = getImage("SF");
        menu1 = getImage("MN");
        sea1 = getImage("SEA1");
        grass1 = getImage("GRASS1");
        pab1 = getImage("PAB");
        font = getImage("font");
        fontb = getImage("fontb");
        energybar = getImage("ebar");
        
        boom = getImages("BU", 5);
        stones = getImages("K", 2);
        ep1 = getImages("EP", 2);
        smoke = getImages("SM", 4);
        bonus = getImages("X", 3);
         
        player = getImages("P", 5);
        
        player1 = new javax.microedition.lcdui.Image[5][];
        for (int ind = 0; ind<4; ind++) {
            player1[ind] = getImages("P"+ind, 2);
//                       player1[ind] = new javax.microedition.lcdui.Image[2];
//            for (int fr = 0; fr<2; fr++) {
//                player1[ind][fr] = getImage("P"+ind+""+fr);
//            }
        }
            
        Game.println("Initializing Images COMPLETED");
    } 
    
    
    
}
