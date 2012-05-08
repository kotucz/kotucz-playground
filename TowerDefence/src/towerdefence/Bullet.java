/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package towerdefence;

/**
 *
 * @author Kotuc
 */
public class Bullet extends Unit {

    private Unit parent;
    private Unit target;

    public Bullet() {
        
    }
    
    public Bullet(Unit parent, Unit target) {
        setParent(parent);
        setTarget(target);
    }

    public void setParent(Unit parent) {
        this.parent = parent;
        setPos(parent.getPos());
    }
    
    public void setTarget(Unit target) {
        this.target = target;
        target.fakeHealth -= 40;
    }
    
    
    @Override
    public void doRound() {
        if ((target==null)||(parent==null)) {
            throw new RuntimeException("parent or target not set!");
        }
        if (dist(target)>5) {
            goTo(target.getPos());
        } else {
            target.health-=40;
            if (target.health<=0) {
                target.destroy();
            }
            this.destroy();
        }
    }
    
    
    
    
    
}
