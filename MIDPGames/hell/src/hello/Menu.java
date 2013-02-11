/*
 * Menu.java
 *
 * Created on 17. záøí 2006, 11:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.*;


/**
 *
 * @author PC
 */
public class Menu extends HCanvas implements Runnable{
        
    /** Creates a new instance of Menu */
    public Menu() {

    }
    
    int menuchoice = 0;
    
    public void keyPressed(int key) {
        int ga = getGameAction(key);
                
        switch (screen) {
            case PAB:
                menuchoice = 0;
            case ABOUT:
                screen = MENU;
            break;
            case MENU: 
                if (UP==ga) if (menuchoice>0) menuchoice--;
                if (DOWN==ga) if (menuchoice<menus.length-1) menuchoice++;

                  if (FIRE==ga) {
                      String comm = menus[menuchoice];
                      if (Text.contin==comm) {
                          Hell.play(); // continue
                      } else if (Text.newg==comm) {
                          Hell.showMenu(LEVEL_SELECT);
//                          screen = LEVEL_SELECT;
//                          flushGraphics(); // level select
                      } else if (Text.about==comm) {
                          Hell.showMenu(ABOUT);
//                          screen = ABOUT;
//                          flushGraphics(); // about screen
                      } else if (Text.exitg==comm) {
                          Hell.exit(); // quit game
                      }
                      
                  }
                
//                if (FIRE==ga) switch (menuchoice) {
//                    
//                    case 0: Hell.play(); break;// continue
//                    case 1: screen = LEVEL_SELECT; flushGraphics(); break;// continue
//                    case 3: screen = ABOUT; flushGraphics(); break;
//                    case 4: Hell.exit(); break;
//                }

                
            break;
            case LEVEL_SELECT: 
                if (UP==ga) if (FlyCanvas.level>1) FlyCanvas.level--;
                if (DOWN==ga) if (FlyCanvas.level<FlyCanvas.NUM_LEVELS) FlyCanvas.level++;
                if (FIRE==ga) {
                
                    Hell.loadLevel(FlyCanvas.level);
                    
                    //screen = BRIEFING; flushGraphics();
                    Hell.showMenu(BRIEFING);
                    //Hell.play();
                }
                
            break;            
            case BRIEFING: 
                if (FIRE==ga) {
                    Hell.play();
                }
                
            break; 
        }
        repaint();
    }
       
    int loadi = 0;
    
    public void paint(Graphics g) {
        switch (screen) {
            case INTRO:
                g.setColor(0xFF000000);
                g.fillRect(0, 0, width, height);
                g.setColor(0xFFFFFFFF);
                g.drawString("INTRO1", width/2, height/2, g.BASELINE|g.HCENTER);
//                g.drawImage(Image.intro1, width/2, height/2, g.HCENTER|g.VCENTER);
            break;
            case LOGO:
                g.setColor(0xFFFFFFFF);
                g.fillRect(0, 0, width, height);
//                g.setColor(0xFFFFFFFF);
//                g.drawString("NYTOO", width/2, height/2, g.BASELINE|g.HCENTER);
                g.drawImage(HImage.n2, width/2, height/2, g.HCENTER|g.VCENTER);
            break;
            case LOAD:
                g.setColor(0xFF000000);
                g.fillRect(0, 0, width, height);
                g.setColor(0xFFFFFF00);
                Text.write(g, Text.loading, Text.CENTER, height/2-20, Text.BOLD);
//                g.drawImage(HImage.loading, width/2, height/2-20, g.HCENTER|g.VCENTER);
//                g.drawString("LOADING", width/2, height/2-20, g.HCENTER|g.BASELINE);
                for (int i = 0; i<loadi; i++) 
                    g.drawImage(HImage.sqrt, width/2-15+15*i, height/2, g.HCENTER|g.VCENTER);
                for (int i = loadi; i<3; i++) 
                    g.drawImage(HImage.sqrf, width/2-15+15*i, height/2, g.HCENTER|g.VCENTER);
            break;
            case MENU:
                drawMenu(g);
                break;
            case LEVEL_SELECT:
                drawLS(g);
                break;
            case BRIEFING:
                drawBF(g);
                break;
            case PAB:
                g.setColor(0xFF000000);
                g.fillRect(0, 0, width, height);
                g.drawImage(HImage.pab1, width/2, height/2, g.HCENTER|g.VCENTER);
            break;
            case ABOUT:
                drawAbout(g);
            break;
        }
        
        
        
    }
       
    
    
    
    int screen = LOGO;
    
    /**
     *  PAB .. press any button
     */
    public static final int 
            INTRO = 1,
            MENU = 2,
            PAB = 3,
            LOGO = 4,
            LOAD = 5,
            ABOUT = 6,
            LEVEL_SELECT = 7,
            BRIEFING = 8;
    
    public void run() {
        
        long t = System.currentTimeMillis();
        
        screen = LOGO;
                        
        flushGraphics();
         
        
        HImage.init();
        Text.initTexts();
        
        t = 3000 - (System.currentTimeMillis() - t);
             
        if (t>0) {
            try {
                Thread.sleep(t);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        screen = LOAD;
        
//        t = System.currentTimeMillis();
        
        for (loadi = 0; loadi<4; loadi++) {
            
            flushGraphics();
            
            Hell.println("lia "+loadi);
            
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
           
        }
        
        screen = PAB;
        
        flushGraphics();
        
    }
        
    public void drawAbout(Graphics g) {
        g.setColor(0xFF000000);
        g.fillRect(0, 0, width, height);
        
        Text.write(g, Text.hell, Text.CENTER, 20, Text.BOLD);
        
        Text.write(g, Text.nametk, Text.LEFT, 40, Text.PLAIN);
        Text.write(g, Text.nameak, Text.LEFT, 60, Text.PLAIN);
        Text.write(g, Text.nameag, Text.LEFT, 80, Text.PLAIN);
        
//        Text.hell.paint(g, width/2, 20);
//        
//        Text.nametk.paint(g, 20, 40);
//        Text.nameak.paint(g, 20, 60);
//        Text.nameag.paint(g, 20, 80);
//        Text.abc.paint(g, 20, 100);
//        g.drawImage(HImage.pab1, width/2, height/2, g.HCENTER|g.VCENTER);
    }

    public void drawBF(Graphics g) {
        g.setColor(0xFF000000);
        g.fillRect(0, 0, width, height);
        
        Text.write(g, Text.briefing+" "+FlyCanvas.level, Text.CENTER, 20, Text.BOLD);
        
        Text.write(g, "zniè krtka!", Text.LEFT, 40, Text.PLAIN);
        Text.write(g, "nikdo nesmí pøežít!", Text.LEFT, 60, Text.PLAIN);
        Text.write(g, "kdo uteèe vyhraje", Text.LEFT, 80, Text.PLAIN);
        
//        Text.hell.paint(g, width/2, 20);
//        
//        Text.nametk.paint(g, 20, 40);
//        Text.nameak.paint(g, 20, 60);
//        Text.nameag.paint(g, 20, 80);
//        Text.abc.paint(g, 20, 100);
//        g.drawImage(HImage.pab1, width/2, height/2, g.HCENTER|g.VCENTER);
    }
    
    String[] menus;
    
    public void drawMenu(Graphics g) {
                
//                g.setColor(0xFFFF0000);
//                g.fillRect(0, 0, width, height);
//                g.drawImage(HImage.menu1, width/2, height/2, g.HCENTER|g.VCENTER);
//                g.drawImage(HImage.ms1, width/2, height/2-30+choice*20, g.HCENTER|g.VCENTER);
//                Text.abc.paint(g, 10, 10);
        
        g.setColor(0xFF000000);
        g.fillRect(0, 0, width, height);
        
        g.drawImage(HImage.menu1, width/2, height/2, g.HCENTER|g.VCENTER);
    
        if (Hell.fc!=null) menus = new String[] {Text.contin, Text.newg, Text.controls, Text.about, Text.exitg};
        else menus = new String[] {Text.newg, Text.controls, Text.about, Text.exitg};
        
        Text.write(g, "right", Text.RIGHT, height-80,  Text.BOLD);
        Text.write(g, "center", Text.CENTER, height-70,  Text.BOLD);
               
        for (int i = 0; i<menus.length; i++) {
            Text.write(g, menus[i], Text.LEFT, height-menus.length*10+i*10-10, (i==menuchoice)?Text.BOLD:Text.PLAIN);
        }
        
    }
    public void drawLS(Graphics g) {
        
        
//                g.setColor(0xFFFF0000);
//                g.fillRect(0, 0, width, height);
//                g.drawImage(HImage.menu1, width/2, height/2, g.HCENTER|g.VCENTER);
//                g.drawImage(HImage.ms1, width/2, height/2-30+choice*20, g.HCENTER|g.VCENTER);
//                Text.abc.paint(g, 10, 10);
        
        g.setColor(0xFF000000);
        g.fillRect(0, 0, width, height);
        
//        g.drawImage(HImage.menu1, width/2, height/2, g.HCENTER|g.VCENTER);
    
//        menus = new String[8];
        
//        Text.write(g, "right", Text.RIGHT, height-80,  Text.BOLD);
//        Text.write(g, "center", Text.CENTER, height-70,  Text.BOLD);
               
        for (int i = 0; i<FlyCanvas.NUM_LEVELS; i++) {
            boolean locked = FlyCanvas.isLocked(i+1);
            Text.write(g, Text.mission+" "+(i+1)+" "+((locked)?Text.locked:Text.unlocked), Text.LEFT, height-FlyCanvas.NUM_LEVELS*10+i*10-10, ((i+1)==FlyCanvas.level)?Text.BOLD:Text.PLAIN);
        }
        
    }

    
    
    
}
