/*
 * Figure.java
 *
 * Created on 13. kvìten 2006, 17:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mythicalduty.figures;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;
import mythicalduty.figures.*;
import mythicalduty.attacks.*;
/**
 *
 * @author PC
 */
public abstract class Figure {
    
// fields to save
    public int hitpoints;
    public int maxhitpoints;
// fields to save    
    public int experience = 950;
    public int skilllevel = 0;
    
    public int energy = 0;
    public int energyNeeded = 0;
    
    String name;
    
    int [] levelExpNeed = new int [] {0, 1000, 3000, 6000, 10000, 15000, 21000};
    
    public static final int GOOD = 2, EVIL = 1;
    
    public int side = EVIL;
    
    int id = -1;

    public Loc getLoc() {
        return loc;
    }
    
    public void setLoc(int a, int b) {
        loc.a = a;
        loc.b = b;
    }
    
    private Loc loc = new Loc(5, 5);
            
    /** Creates a new instance of Figure */
    protected Figure() {
        maxhitpoints = 100;
        hitpoints = 80;
        
/*        attacks = new Attack[1];
        attacks[0] = new FireBall(this);
  */    
//        addAttack(Attack.getHeal());
//        addAttack(Attack.getFireball());
    }
    
    FigureAnimation anim = new FigureAnimation(this);
//    HeroIcon icon = new HeroIcon(this);
//    StatusAnimation hpstat = new StatusAnimation(this);
    
    public FigureAnimation getAnim() {
        return this.anim;
    }
       
    public void damage(int dmg) {
        this.hitpoints -= dmg;
        if (dmg>0) Game.fight.addAnim(new FloatingString(""+dmg/*+"DMG"*/, 0x00FF0000, getPos()));
        else Game.fight.addAnim(new FloatingString("MISSED", 0x00FF0000, getPos()));
        stun(2000*dmg);
        if (!alive()) kill();
    }
    
    public void heal(int hp) {
        this.hitpoints += hp;                              
        if (hp>0) Game.fight.addAnim(new FloatingString(""+hp+"HP", 0x0000FF00, getPos()));
//      else Game.fight.addAnimation(new FloatingString("MISSED", 0x00FF0000, getPos()));
        stun(-2000*hp);
        if (this.hitpoints>maxhitpoints) hitpoints = maxhitpoints;
    }
    
    public void addExp(int exp) {
      if (!alive()) return;
        this.experience += exp;
//      if (exp>0) Game.fight.anims.addAnimation(new FloatingString(""+exp+"EXP", 0x00FFFFFF, getPos()));
        if (experience>=levelExpNeed[skilllevel+1]) addLevel();
    }
    
    protected void addLevel() {
        if (experience<levelExpNeed[skilllevel+1]) return;
        this.maxhitpoints += 6;
        this.hitpoints = maxhitpoints;
        this.energy = energyNeeded;
        this.skilllevel++;
        Game.fight.addAnim(new LevelUpAnimation(this));
    }
    /*
    void addEnergy(int en) {
        this.energy += en;
        energy = (energyNeeded>energy)?energy:energyNeeded;
    }*/
    
    public void addEnergy() {
///        MySystem.println("energy++;");
        this.energy += Settings.gameSpeed;
        if (weak()) energy -= 5;
        if (energyNeeded<energy) {
            this.stunOut -= energy - energyNeeded;
            energy = energyNeeded;
        };
        //energy = (energyNeeded>energy)?energy:energyNeeded;
    }
    
    public boolean alive () {
        return hitpoints>0;
    }

//  the time until figure is stunned  
    private int stunOut;
    public void stun(int ms) {
        if (MySystem.getTime()<stunOut) stunOut+=ms;
            else stunOut = (int)MySystem.getTime()+ms;
    }
    
    public boolean weak() {
        return (MySystem.getTime()<stunOut)||((3*hitpoints<maxhitpoints)&&(!ready()));
    }
    
    public boolean ready = true;
    
    public boolean ready() {
            return ready&&(energy>=energyNeeded)&&alive();
    }
      
    public Pos getPos() {
        return getStage().getPos(this.loc);
    }
    
    
    public Stage getStage() {
        return Game.fight.getStage();
    }
    
//    Pos getPos() {
//        return new Pos(anim.pos);
//    }
    
//  abstract void setPos(int id) ;
        
    Image deadImage = Game.createImage("/figures/dead.png");
    
    void kill() {
        anim.images = null;
        anim.image = deadImage;
    }

    public void addAttack(Attack a) {
        a.attacker = this;
        
        Attack[] as = attacks;
        attacks = new Attack[attacks.length+1];
        System.arraycopy(as, 0, attacks, 0, as.length);
        attacks[as.length]=a;
    }
    
    public Attack[] attacks() {
        return attacks;
    }
      
    public Attack[] attacks = new Attack[0];
    
    public int attackId=0;
    
    public void prevAttack() {
        attackId--;
        if (attackId<0) attackId=attacks.length-1;
    }
    
    public void nextAttack() {
        attackId++; 
        if (attackId>=attacks.length) attackId=0;
    }
    
    
    
/*    
    Figure nextFigure, prevFigure;
    Figure next() {
        if ((nextFigure!=null)&&nextFigure.alive()) return this.nextFigure;
        else return nextFigure.next();
    }

    Figure prev() {
        if ((prevFigure!=null)&&prevFigure.alive()) return this.prevFigure;
        else return prevFigure.next();
    }
*/
    /*
    boolean attackable(Attack a) {
        if (a==null) return false;
        if ((a.targetType&this.side)!=0) return true;
        else return false;
    } 
  */  
//    
//    public void load(int id) {
//        String[] lines = MySystem.loadTexts("/figures/figures.txt");
//        
//        Figure f=null;
//        String[] param; 
//        int line=0;
//        try{
//        for (line = 0; line<lines.length; line++) {
//            param = MySystem.getWords(lines[line]);
//// all params are for the last taken figure
//            if (param.length==0) continue;
//            if (param[0].equals("figure")) {
//                f = new Figure();
//                MySystem.println("figure "+Integer.parseInt(param[1]));
//           
//            } else if (param[0].equals("name")) {
//// sets name
//                f.name = param[1];
//            
//            } else if (param[0].equals("side")) {
//                if (param[1].equals("good")) f.side=GOOD;
//                else if (param[1].equals("evil")) f.side=EVIL;
//                else MySystem.error("error parsing figures.txt. line: "+line+" invalid side "+param[1]);
//            
//            } else if (param[0].equals("attack")) {
//                f.addAttack(new mythicalduty.attacks.FireBall(f));
// //  todo add attack by param[1]
//                 
//            }
//            
//        }
//        } catch (Exception ex) {
//            MySystem.error("figures.txt parsing error. line: "+line);
//            ex.printStackTrace();
//        }
//    }

    public static  EvilFigure createNightmare () {
//        return new Nightmare();
        EvilFigure ef = new EvilFigure();
        ef.name = "Mike";
        ef.maxhitpoints=50;
        ef.hitpoints=48;
        ef.addAttack(Attack.getFireball());
        ef.anim.loadImages("/figures/nightmarefront", 2);
        return ef;

    }

    public static EvilFigure createAlien () {
        EvilFigure ef = new EvilFigure();
        ef.name = "Ricky";
        ef.maxhitpoints=70;
        ef.hitpoints=70;
        ef.addAttack(Attack.getFireball());
        ef.anim.loadImages("/figures/alien", 1);
        return ef;
    }
    
    public static EvilFigure createMud () {
        EvilFigure ef = new EvilFigure();
        ef.name = "Pulos";
        ef.maxhitpoints=30;
        ef.hitpoints=30;
        ef.addAttack(Attack.getMudSpit());
        ef.anim.loadImages("/figures/mud", 2);
        return ef;
    }

    public static GoodFigure createHero () {
        GoodFigure gf = new GoodFigure();
        gf.name = "Gianna";

        gf.maxhitpoints=100;
        gf.hitpoints=80;

        gf.addAttack(Attack.getFireball());
        gf.addAttack(Attack.getMudSpit());
        gf.addAttack(Attack.getHeal());
        gf.anim.loadImages("/figures/heroback", 2);

        return gf;
    }
    
}

