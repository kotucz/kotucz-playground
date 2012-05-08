/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tradeworld.chains;

import tradeworld.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * IMMUTABLE
 * @author Petr Dluhoš
 */
public class ProductionChain {

    private final List<Product> inputs = new LinkedList<Product>();
    private final List<Product> outputs = new LinkedList<Product>();
    private int speed;


    public ProductionChain() {

        // This is only an example for testing, products should be in constructor parameters
      //  inputs.add(new Product(Goods.Type.WOOD, 1));
      //  outputs.add(new Product(Goods.Type.BOARD, 2));

     

        

    }

    public List<Product> getInputs() {

        return Collections.unmodifiableList(inputs);
    }

    public List<Product> getOutputs() {

        return Collections.unmodifiableList(outputs);
    }

    void addInput(Product input) {

        inputs.add(input);
    }

    void addOutput(Product output) {

        outputs.add(output);
    }

    public int getSpeed() {

        return speed;
    }

    void setSpeed(int speed) {

        this.speed = speed;
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
