package kotucz.village.transport;

import com.jme3.material.Material;
import kotucz.village.build.Building;
import kotucz.village.game.Player;
import kotucz.village.tiles.Pos;

import javax.vecmath.Point3d;

/**
 *
 * @author Petr Dluhoš
 */
public class Storage {

    private final Payload payload;


    public Storage(int capacity) {
        this.payload = new Payload(capacity);

    }

//    public Storage(Player owner, Point3d pos, int capacity) {
//                          super(null, null);
////        super(Building.Type.STORAGE, owner, vector);
//        payload = new Payload(capacity);
//        payload.addGoods(new Goods(Goods.Type.PETROL, 20, owner)); //TODO: To remove - onlz for testing
//        payload.addGoods(new Goods(Goods.Type.WOOD, 20, owner)); //TODO: To remove - onlz for testing
//
////        panel = new StoragePanel(this);
//
//    }
    
    public Payload getPayload() {
        return payload;
    }
}
