package tradeworld.graphics;

import tradeworld.Goods;
import tradeworld.Payload;

/**
 *
 * @author Kotuc
 */
public class Payload3D extends Model3D {

    private final Payload payload;

    public Payload3D(Payload payload) {
        this.payload = payload;
    }

    @Override
    public void refresh() {
        for (Goods goods : payload.getGoods()) {

        }
    }



}
