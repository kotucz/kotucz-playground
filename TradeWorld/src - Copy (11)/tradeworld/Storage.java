/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradeworld;

import javax.vecmath.Point3d;
import tradeworld.gui.StoragePanel;

/**
 *
 * @author Petr Dluhoš
 */
public class Storage extends Building {

    private final Payload payload;

    public Storage(Player owner, Point3d pos, int capacity) {

        super(Building.Type.STORAGE, owner, pos);
        payload = new Payload(capacity);
        payload.addGoods(new Goods(Goods.Type.PETROL, 20, owner)); //TODO: To remove - onlz for testing

        panel = new StoragePanel(this);

    }

    @Override
    public void act() {
        // does nothing
//        ((StoragePanel)panel).update();
//        panel.repaint();
    }

    public Payload getPayload() {

        return payload;
    }
}
