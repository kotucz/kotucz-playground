package tradeworld;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains goods
 *
 * @author Kotuc
 */
public class Payload {

    
    private final int maxVolume;   //max. volume
    private final List<Goods> goods = new LinkedList<Goods>();

    public Payload(int maxVolume) {
        this.maxVolume = maxVolume;
        


    }

    /**
     * Gets total volume of the payload.
     * @return Total volume of the payload
     */
    public int getVolume() {
        int volume = 0;
        for (Goods goodsPile : this.goods) {
            volume += goodsPile.getQuantity();
          
        }
        return volume;
    }

    /**
     *
     * @return Maximal volume of the payload
     */
    public int getMaxVolume() {

        return maxVolume;
    }

    /**
     *
     * @return Free volume of the payload.
     */
    public int getFreeVolume() {

        return (getMaxVolume() - getVolume());
    }

    /**
     * Adds goods to the payload.
     * @param goods Goods to add
     */
    public void addGoods(Goods goods) {

        if (goods.getQuantity() <= getFreeVolume()) {
            boolean found = false;
            for (Goods goodsPile : this.goods) {
                if (goodsPile.isSamePile(goods)) {
                    goodsPile.addGoods(goods);
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.goods.add(goods);
            }
        } else {
            throw new IllegalStateException("Maximum volume of payload has been overflowed.");
        }
    }

    /**
     * Removes goods from the payload (type and amount specified in param goods)
     * @param goods Goods to remove
     */
    public void removeGoods(Goods goods) {

        for (Iterator<Goods> it = this.goods.iterator(); it.hasNext();) {
            Goods goodsPile = it.next();

            if (goodsPile.isSamePile(goods)) {
                if (goodsPile.getQuantity() >= goods.getQuantity()) {
                    goodsPile.decreaseQuantity(goods.getQuantity());
                    if (goodsPile.getQuantity() == 0) {
                        it.remove();
                    }
                    return;
                } else {
                    throw new IllegalStateException("There is not enough goods to remove.");
                }
            }
        }
        throw new IllegalStateException("There is not such a type of goods to remove.");
    }

    /**
     *
     * @return Number of tzpes of goods
     */
    public int getNumberOfGoodsTypes() {

        return goods.size();
    }

    /**
     *  Finds goods in the payload with the <code> type </code> and <code> owner </code>.
     * @param type Type of are looking for
     * @param owner Owner of goods we are looking for
     * @return Suitable goods or null, if not found
     */
    public Goods findGoodsPile(Goods.Type type, Player owner) {

        for (Goods goodsPile : goods) {
            if (goodsPile.isSamePile(new Goods(type, 0, owner))) {
                return goodsPile;
            }
        }
        return null;
    }

    /**
     * Gets goods at <code> position </code>.
     * @param position Position of required goods
     * @return Goods at <code> position </code>
     */
    public Goods getGoodsAt(int position) {

        if ((position >= 0) && (position <= goods.size())) {
            return goods.get(position);
        } else {
            throw new IndexOutOfBoundsException("There is not any goods at this position.");
        }
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public boolean isEmpty() {
        return goods.isEmpty();
    }


}


