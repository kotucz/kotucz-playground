/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefence;

/**
 *
 * @author Kotuc
 */
public class Templates {

    static final Tower TOWER1 = new Tower() {

        {
            shootRange = 100;
            img = getImage("tower1_16.png");
        }
    };
    static final Tower TOWER2 = new Tower() {

        {
            shootRange = 200;
            img = getImage("tower1_16.png");
        }
    };
    static final Creep CREEP1 = new Creep() {

        {
            speed = 50;
            img = getImage("creep1_16.png");
        }
    };
    static final Bullet BULLET1 = new Bullet() {

        {
            buildTime = 8;
            speed = 100;
            img = getImage("bullet1_16.png");
        }
    };
    
    
    

}
