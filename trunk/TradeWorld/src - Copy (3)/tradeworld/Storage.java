/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tradeworld;

/**
 *
 * @author Petr Dluhoš
 */
public class Storage extends Building {
    
    private final Payload payload;

    public Storage(Type type, Player owner, Pos pos, int capacity) {

        super(type, owner, pos);
        payload = new Payload(capacity);

    }

}
