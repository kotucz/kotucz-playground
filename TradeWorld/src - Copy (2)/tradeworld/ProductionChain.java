/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tradeworld;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Dluhoš
 */
public class ProductionChain {

    private final List<Product> inputs = new LinkedList<Product>();
    private final List<Product> outputs = new LinkedList<Product>();


    public ProductionChain() {

        // This is only an example for testing, products should be in constructor parameters
        inputs.add(new Product(Goods.Type.WOOD, 1));
        outputs.add(new Product(Goods.Type.BOARD, 2));

    }

    public Collection<Product> getInputs() {

        return inputs;
    }

    public Collection<Product> getOutputs() {

        return outputs;
    }

    public String toString() {

        String text = "";
        for (Product product : inputs) {
            text += ("" + product.getRatio() + " " + product.getGoodsType().toString() + " ");
        }
        text += " -> ";
        for (Product product : outputs) {
            text += ("" + product.getRatio() + " " + product.getGoodsType().toString() + " ");
        }

        return text;
    }

}
