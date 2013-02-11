/*
 * ShotAttack.java
 *
 * Created on 25. èerven 2006, 13:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mythicalduty.attacks;

import mythicalduty.*;
import kotuc.midp.*;

/**
 *
 * @author PC
 */
public class ShotAttack extends Attack {
    
    String imagesName;
    int bangImagesCount;
    int shotImagesCount;
    
    
    /** Creates a new instance of ShotAttack */
    public ShotAttack() {
    }
    
        
    public void animate() {
        ShotAttackAnimation saa = new ShotAttackAnimation(this);       
        
        saa.setTimeout(700);
        
        saa.loadImages("/attacks/"+imagesName+"S", shotImagesCount);

        Game.fight.addAnim(saa);    
    }
    
    public void proceed() {
        super.proceed();
        Bang b = new Bang(target.getPos());
        b.loadImages("/attacks/"+imagesName+"B", bangImagesCount);
        Game.fight.addAnim(b);
    }
    
    protected void calculate() {
        int tw = (target.weak())?1:0;
        int aw = (target.weak())?1:0;
        damageToEnemy = (MySystem.random(5+3*tw-2*aw+attacker.skilllevel-target.skilllevel)==0)?(0):(attacker.skilllevel*2+MySystem.random(6)+3*tw);
        experienceBonus = 10*(damageToEnemy);//target.skilllevel; 
        energyUsed = 4000+500*MySystem.random(4);
    }
    
    public void setImages(String iname, int shotpoc, int bangpoc) {
        imagesName = iname;
        shotImagesCount = shotpoc;
        bangImagesCount = bangpoc;
    }
    
}
