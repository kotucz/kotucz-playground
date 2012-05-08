/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tradeworld;

/**
 *
 * @author Petr Dluhoš
 */
public class Product {

    private final Goods.Type goodsType;
    private final int ratio;            //ratio of production (with respect to other products)

    public Product(Goods.Type goodsType, int ratio) {

        this.goodsType = goodsType;
        this.ratio = ratio;
    }

    public Goods.Type getGoodsType() {

        return goodsType;
    }

    public int getRatio() {

        return ratio;
    }


}
