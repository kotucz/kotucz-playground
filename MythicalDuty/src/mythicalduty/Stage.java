/*
 * Stage.java
 *
 * Created on 29. kvìten 2006, 18:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mythicalduty;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.figures.*;

/**
 *
 * @author PC
 */
public class Stage extends Animation {

    int asize = 10 , bsize = 10;
    private int upperfund = 50;
    
    static Image[] tileImgs;

    public Stage() {
        initImages();
    }
    
    /** Creates a new instance of Stage */
    public Stage(int id) {
        initImages();
//        tiles = new int[asize][bsize];
//        tiles[0][0] = 2;
//        tiles[9][9] = 2;
//        tiles[9][0] = 2;
        tiles = maps[id];
//                new int[][] {
//            {3, 3, 3, 3, 3,  3, 1, 0, 0, 0},
//            {3, 3, 3, 3, 3,  0, 1, 0, 0, 0},
//            {3, 3, 3, 3, 3,  0, 1, 0, 0, 0},
//            {3, 3, 3, 3, 3,  1, 0, 0, 4, 0},
//            {3, 3, 0, 0, 0,  1, 0, 0, 0, 0},
//            
//            {3, 0, 0, 0, 0,  1, 0, 0, 0, 0},
//            {3, 4, 0, 0, 1,  0, 0, 0, 0, 0},
//            {3, 0, 0, 1, 1,  0, 0, 0, 0, 0},
//            {3, 0, 1, 1, 0,  0, 0, 0, 0, 0},
//            {3, 0, 1, 0, 0,  0, 0, 0, 0, 0}
//        };
//        this.pos = new Pos(0, -upperfund);
        addExit(5, 5, 1, 1, 1);
        addExit(0, 6, 1, 2, 2);
    }
    
    public void initImages() {
        if (tileImgs!=null) return;
        MySystem.print("Initializing tile imgs .. ");
        int N = 6;
        tileImgs = new Image[N];
        for (int i = 0; i<N; i++) tileImgs[i] = MySystem.createImage("/tiles/"+i+".png");
        MySystem.println(" OK");
    }
    
    int[][] tiles;
    
    static int[][][] maps = new int[][][] {
        {
            {0, 0, 0, 3, 3,  3, 1, 0, 0, 0},
            {0, 0, 3, 3, 3,  0, 1, 0, 0, 0},
            {0, 0, 3, 3, 3,  0, 1, 0, 0, 0},
            {3, 3, 3, 3, 3,  1, 0, 0, 4, 0},
            {3, 3, 0, 0, 0,  1, 0, 0, 0, 0},
            
            {3, 0, 0, 0, 0,  1, 0, 0, 0, 0},
            {3, 4, 0, 0, 1,  0, 0, 0, 0, 0},
            {3, 0, 0, 1, 1,  0, 0, 0, 0, 0},
            {3, 0, 1, 1, 0,  0, 0, 4, 0, 3},
            {3, 0, 1, 0, 0,  0, 0, 0, 0, 3}
        },
        {
            {3, 3, 3, 3, 3,  3, 1, 0, 0, 0},
            {3, 3, 3, 3, 3,  0, 1, 0, 0, 0},
            {3, 3, 3, 3, 3,  0, 1, 0, 0, 0},
            {3, 3, 3, 3, 3,  1, 0, 0, 4, 0},
            {3, 3, 0, 0, 0,  1, 0, 0, 0, 0},
            
            {3, 0, 0, 0, 0,  1, 0, 0, 0, 0},
            {3, 4, 0, 0, 1,  0, 0, 0, 0, 0},
            {3, 0, 0, 1, 1,  0, 0, 0, 0, 0},
            {3, 0, 1, 1, 0,  0, 0, 0, 0, 0},
            {3, 0, 1, 0, 0,  0, 0, 0, 0, 0}
        },
        {
            {3, 0, 3, 3, 3,  0, 0},
            {3, 0, 0, 0, 0,  0, 0},
            {3, 0, 0, 0, 0,  0, 0},
            {0, 0, 1, 0, 0,  1, 0},
            {0, 3, 0, 0, 0,  0, 0},
            
            {0, 0, 0, 0, 0,  0, 0},
            {0, 0, 0, 1, 0,  0, 0}
        }
    };

    
    public Pos getPos(Loc loc) {
        return new Pos(getX(loc.a, loc.b), getY(loc.a, loc.b));
    }
    
    Image floor;
    
    public void paint(Graphics g) {
//     if (floor==null) refresh();
//     g.drawImage(floor, getX(), getY(), g.TOP|g.HCENTER); 
//       MySystem.error("tiles:"+ tiles); 
       for (int a = 0; a<tiles.length; a++) {
           for (int b = 0; b<tiles[a].length; b++) {
               int cx = getX() + getX(a, b);
               int by = getY() + getY(a, b);// + floor.getHeight()/2
               
               try {
                   g.drawImage(tileImgs[tiles[a][b]], cx, by, g.BOTTOM|g.HCENTER);
               } catch (Exception ex) {
//                   MySystem.error(""+tileImgs[tiles[a][b]]);
//                   ex.printStackTrace();
               }
                             
/*               
               g.drawImage(Game.createImage("/tiles/mt1.png"), cx, by, g.BOTTOM|g.HCENTER); 
               
               g.setColor(0x00FF00FF);
               g.drawRect(cx, by, 2, 2); 
               if (tiles[a][b] == 2) {
                  g.drawImage(Game.createImage("/tiles/mt3.png"), cx, by, g.BOTTOM|g.HCENTER);  
                  g.setColor(0x00000000);
                  g.drawRect(cx-5, by-20, 10, 20);
                
               }
*/           } 
        }
        
       for (int i = 0; i<exits.length; i++) {
           int a=exits[i][0], b=exits[i][1];
           g.drawImage(tileImgs[5], getX()+getX(a, b), getY()+getY(a, b), g.BOTTOM|g.HCENTER);
       }
       
 //      if (cursor!=null) {
 //           g.setColor(0x00FFFF22);
 //           g.drawRect(getX()+getX(cursor.a, cursor.b)-16, getY()+getY(cursor.a, cursor.b)-32, 32, 24);
 //      }
    }

    void refresh() {
//        floor = Image.createImage(htw*(asize+bsize), upperfund+hth*(asize+bsize));
//        Graphics g = floor.getGraphics();
// a to SouthEast, b to SouthWest        
//        for (int a = 0; a<tiles.length; a++) {
//           for (int b = 0; b<tiles[a].length; b++) {
//               int cx = floor.getWidth()/2 + getX(a, b);
//               int by = upperfund + getY(a, b);// + floor.getHeight()/2
//               
//               g.drawImage(tileImgs[tiles[a][b]], cx, by, g.BOTTOM|g.HCENTER);
//                             
///*               
//               g.drawImage(Game.createImage("/tiles/mt1.png"), cx, by, g.BOTTOM|g.HCENTER); 
//               
//               g.setColor(0x00FF00FF);
//               g.drawRect(cx, by, 2, 2); 
//               if (tiles[a][b] == 2) {
//                  g.drawImage(Game.createImage("/tiles/mt3.png"), cx, by, g.BOTTOM|g.HCENTER);  
//                  g.setColor(0x00000000);
//                  g.drawRect(cx-5, by-20, 10, 20);
//                
//               }
//*/           } 
//        }
    }    

    int[][] exits = new int[0][];
    
    public void addExit(int fa, int fb, int ds, int da, int db) {
        MySystem.println("new Exit:"+fa+" "+fb+" "+ds+" "+da+" "+db);
        int[][] xs = exits;
        exits = new int[exits.length+1][];
        System.arraycopy(xs, 0, exits, 0, xs.length);
        exits[xs.length] = new int[5];
        exits[xs.length][0] = fa;
        exits[xs.length][1] = fb;
        exits[xs.length][2] = ds;
        exits[xs.length][3] = da;
        exits[xs.length][4] = db;
    }
    
    public boolean travelerEntered(Loc loc) {
               
        for (int i = 0; i<exits.length; i++) {
            if (exits[i][0]==loc.a&&exits[i][1]==loc.b) Traveling.setLocation(exits[i][2], exits[i][3], exits[i][4]);     
 //           return false;
        }
                    
        int v = get(loc);
        if (v < 0) return false;
        if (v < 2) return true;
        
        if (v == 4) {
            Game.startFight();
            return true;
        }
        
        return false;
    }
    
    public int get(Loc loc) {
        try {
            return (tiles[loc.a][loc.b]);
        } catch (Exception e) {
            return -1;
        }
    }
    
    
    
//    Loc cursor;

    public Loc cursorLoc(int x, int y) {
        x-=getX();
        y-=getY()-hth-4;
        return new Loc( (x/htw+y/hth)/2, (y/hth-x/hth)/2 );
    }

    //  half tile width/height    
    static int htw = 16, hth = 12; 
    
    static int getX(int a, int b) {
        return (a-b)*htw;
    }
    
    static int getY(int a, int b) {
        return (a+b)*hth;
    }

    
//    
//    void setCursor(int a, int b) {
//        if (cursor==null) cursor = new Loc();
//        cursor.a = a;
//        cursor.b = b;
//    }
//    
      
//    void load(String[] lines, int id) {
//        Game.showLoadingScreen();
//        MySystem.print("loadingStage "+id+" .. ");
//        int line=-1;
//        int[] ints=null;
//        while (lines[++line]!=null&&line<lines.length) {
//            String s = lines[line];
//            
//            if (s.startsWith("stage ")) {
//              ints = MySystem.getInts(s);
////            MySystem.print("ID:"+  ints[0]);  
//            if (id==ints[0]) {
//                MySystem.println("stage "+id+" :");
//                    
//                this.asize = ints[1];
//                this.bsize = ints[2];
//                
//                tiles = new int[asize][bsize];
//                
//                line++;
///*
//                for (int a = 0; a<asize; a++) {
//                s = lines[line];
//                ints = MySystem.getInts(s);
//                    for (int b = 0; b<bsize; b++) {
////                        if (b==0) tiles[a] = new int[bsize];
//                        tiles[a][b]=ints[b];
//                    }
//                line++;
//                }
//*/
//                
//                MySystem.println("tiles: ");
//                
//                for (int b = 0; b<bsize; b++) {
//                s = lines[line];
//                if (!s.startsWith("b")) MySystem.error("error parsing stage file. line: "+line+" invalid number of b lines. should be: "+bsize);
//                ints = MySystem.getInts(s);
//                MySystem.println("ints.length"+ints.length);
//                    for (int a = 0; a<asize; a++) {
////                        if (b==0) tiles[a] = new int[bsize];
//                        tiles[a][b]=ints[a];
//                    }
//                line++;
//                }
//                
//                MySystem.println("exits: ");
//                s=lines[line];
//                if (!s.startsWith("exit")) MySystem.error("error parsing stage file. line: "+line+" invalid number of b lines. should be: "+bsize);  
//                
//                while (s.startsWith("exit")) {
//                    s = lines[line];
//                    if (s==null) break;
//                    ints = MySystem.getInts(s);
//                    addExit(ints[0], ints[1], ints[2], ints[3], ints[4]);
//                    line++;
//                }
//                
//            }
//            }
            
//        }        
//        MySystem.println("OK");
//    }
}
