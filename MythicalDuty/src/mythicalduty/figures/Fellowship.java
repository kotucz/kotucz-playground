/*
 * Fellowship.java
 *
 * Created on 7. èerven 2006, 19:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mythicalduty.figures;

/**
 *
 * @author PC
 */
public class Fellowship {
    
    Figure[] members = new Figure[0];
    
    /** Creates a new instance of Fellowship */
    public Fellowship() {
    }
    
    public void addMember(Figure f) {
        Figure[] m = members;
        members = new Figure[members.length+1];
        System.arraycopy(m, 0, members, 0, m.length);
        members[m.length]=f;
    }
    
}
