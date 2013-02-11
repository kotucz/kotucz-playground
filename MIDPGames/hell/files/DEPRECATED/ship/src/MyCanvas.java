import javax.microedition.midlet.*; 
import javax.microedition.lcdui.*; 
import java.io.IOException;
import javax.microedition.lcdui.game.Sprite;




public class MyCanvas extends Canvas implements Runnable // Rozsirime tridu
 {


  public boolean stop = false;   // pro zastaveni behu vlakna

  int rw,rh;
  ship mid;
  int klavesa;
  int interval;

  int vrtule=0;
  int delic_vrtule;
  int naklon=0;
  int lod_x;
  int lod_y;
  Image[] lod_zd;
  Image[] vrtule_moje_zd;
  Sprite[] lod;
  Sprite[][] vrtule_moje;

  int zmena_x;
  int zmena_y;
  int maximalka=4;
    
  int lod_up;
  int lod_do;
  int lod_pr;
  int lod_le;
  
  Image cizi_vrtule1;
  Image cizi_vrtule2;
  Image nepritel;


  public MyCanvas(ship mid)  
    {

      setFullScreenMode(true);
      rw = getWidth();
      rh = getHeight();
      this.mid = mid;

lod_zd = new Image[4];
lod = new Sprite[5];
vrtule_moje_zd = new Image[6];
vrtule_moje = new Sprite[5][2];


lod_x = rw/2;
lod_y = rh-50;


     try 
     { 
     
       lod_zd[0] = Image.createImage("/normal.png");
       lod_zd[1] = Image.createImage("/vpred.png");
       lod_zd[2] = Image.createImage("/vzad.png");
       lod_zd[3] = Image.createImage("/naklon.png");
       
       lod[0] = new Sprite(lod_zd[0],20,29);
       lod[1] = new Sprite(lod_zd[3],18,31);
       lod[2] = new Sprite(lod_zd[3],18,31);
       lod[3] = new Sprite(lod_zd[1],20,23);
       lod[4] = new Sprite(lod_zd[2],22,34);
       
 
       vrtule_moje_zd[0] = Image.createImage("/n_vrtule1.png");
       vrtule_moje_zd[1] = Image.createImage("/n_vrtule2.png");
       vrtule_moje_zd[2] = Image.createImage("/v_vrtule1.png");
       vrtule_moje_zd[3] = Image.createImage("/v_vrtule2.png");
       vrtule_moje_zd[4] = Image.createImage("/z_vrtule1.png");
       vrtule_moje_zd[5] = Image.createImage("/z_vrtule1.png");
       
       vrtule_moje[0][0] = new Sprite(vrtule_moje_zd[4],41,37);
       vrtule_moje[0][1] = new Sprite(vrtule_moje_zd[5],41,37);
       vrtule_moje[1][0] = new Sprite(vrtule_moje_zd[0],30,36);
       vrtule_moje[1][1] = new Sprite(vrtule_moje_zd[1],30,36);
       vrtule_moje[2][0] = new Sprite(vrtule_moje_zd[0],30,36);
       vrtule_moje[2][1] = new Sprite(vrtule_moje_zd[1],30,36);
       vrtule_moje[3][0] = new Sprite(vrtule_moje_zd[2],41,20);
       vrtule_moje[3][1] = new Sprite(vrtule_moje_zd[3],41,20);
       vrtule_moje[4][0] = new Sprite(vrtule_moje_zd[4],41,37);
       vrtule_moje[4][1] = new Sprite(vrtule_moje_zd[5],41,37);

       lod[2].setTransform(Sprite.TRANS_MIRROR);
       vrtule_moje[2][0].setTransform(Sprite.TRANS_MIRROR);
       vrtule_moje[2][1].setTransform(Sprite.TRANS_MIRROR);
       vrtule_moje[0][1].setTransform(Sprite.TRANS_MIRROR);
       vrtule_moje[4][1].setTransform(Sprite.TRANS_MIRROR);
          
       cizi_vrtule1 = Image.createImage("/vrtule_01.png");
       cizi_vrtule2 = Image.createImage("/vrtule_02.png");
       nepritel = Image.createImage("/protivnik.png");


     } catch (IOException e) { }   
    }



  public void paint(Graphics g) 
    {
    
    

//       g.setColor(200,200,255);
     g.setColor(255,255,255);
      
      g.fillRect(0,0,rw,rh);    //vykresli pozadí

      g.drawImage(nepritel, rw/2, 10, Graphics.HCENTER|Graphics.TOP);          
      if(vrtule==0){
      g.drawImage(cizi_vrtule1,rw/2-22,55,Graphics.VCENTER|Graphics.HCENTER);
      g.drawImage(cizi_vrtule1,rw/2+22,55,Graphics.VCENTER|Graphics.HCENTER);}
      if(vrtule==1){
      g.drawImage(cizi_vrtule2,rw/2-22,55,Graphics.VCENTER|Graphics.HCENTER);
      g.drawImage(cizi_vrtule2,rw/2+22,55,Graphics.VCENTER|Graphics.HCENTER);}
      
      
  lod[naklon].setPosition(lod_x-(lod[naklon].getWidth()/2),lod_y-(lod[naklon].getHeight()/2));
	lod[naklon].paint(g);
	vrtule_moje[naklon][vrtule].setPosition(lod_x-(vrtule_moje[naklon][vrtule].getWidth()/2),lod_y-(vrtule_moje[naklon][vrtule].getHeight()/2));
	vrtule_moje[naklon][vrtule].paint(g);

}



  public void run()
    {

      while (!stop)  // dokud neni stop true bez 
       {
       
        pohyb();
        vyrovnani();
        rotovac_vrtule();
        
        repaint();

        try { Thread.sleep(80); } catch (java.lang.InterruptedException ie) { }   

       }

    }

  public void pohyb()
    {
    lod_x = lod_x + zmena_x;
    lod_y = lod_y + zmena_y;
    
    if((lod_up==1)&&(zmena_y>-maximalka)) zmena_y = zmena_y -1;
    if((lod_do==1)&&(zmena_y<+maximalka)) zmena_y = zmena_y +1;
    if((lod_le==1)&&(zmena_x>-maximalka)) zmena_x = zmena_x -1;
    if((lod_pr==1)&&(zmena_x<+maximalka)) zmena_x = zmena_x +1;
    
    if((zmena_x==0)&&(zmena_y==0)) naklon=0;    //normal
    if(zmena_x<0)                  naklon=1;    //doleva
    if(zmena_x>0)                  naklon=2;    //doprava
    if((zmena_y<0)&&(zmena_x==0))  naklon=3;    //dopredu
    if((zmena_y>0)&&(zmena_x==0))  naklon=4;    //dozadu
    
    
    
    }

  public void vyrovnani()     //at nevyletava z mapy
    {
    if((lod_up==0)&&(zmena_y<0)) zmena_y = zmena_y +1;
    if((lod_do==0)&&(zmena_y>0)) zmena_y = zmena_y -1;
    if((lod_le==0)&&(zmena_x<0)) zmena_x = zmena_x +1;
    if((lod_pr==0)&&(zmena_x>0)) zmena_x = zmena_x -1;
   
  if(lod_y>rh-30) lod_do=0;
  if(lod_y<  +30) lod_up=0;
  if(lod_x>rh-30) lod_pr=0;
  if(lod_x<  +30) lod_le=0;
    
    }

  public void rotovac_vrtule()
    {
    delic_vrtule = delic_vrtule+1;
    if(delic_vrtule==1) delic_vrtule=0;
    if(delic_vrtule==0){
      vrtule = vrtule +1;
      if(vrtule==2) vrtule=0;}
    
    }

  protected void keyPressed(int keyCode)
   {
	  klavesa = keyCode;
  if(klavesa==48) {mid.destroyApp(false); mid.notifyDestroyed();}
  if((klavesa==50)&&(lod_y<rh-30)) lod_do=1;
  if((klavesa==56)&&(lod_y>  +30)) lod_up=1;
  if((klavesa==54)&&(lod_x<rh-30)) lod_pr=1;
  if((klavesa==52)&&(lod_x>  +30)) lod_le=1;
  
    			}


  protected void keyReleased(int keyCode)
   {
	  klavesa = keyCode;
  if(klavesa==56) lod_up=0;
  if(klavesa==50) lod_do=0;
  if(klavesa==52) lod_le=0;
  if(klavesa==54) lod_pr=0;
  
    			}
}
