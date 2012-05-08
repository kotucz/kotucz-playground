/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tradeworld;

import java.util.LinkedList;
import java.util.List;

/**
 * Place, where vehicles load and unload goods)
 * @author Petr Dluhoš
 */
public abstract class Depot extends Building{

    private final List<Storage> storages = new LinkedList<Storage>();

    public Depot(Type type, Player owner, Pos pos) {
        super(type, owner, pos);
        

    }

}
