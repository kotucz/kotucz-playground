/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradeworld.graphics;

import tradeworld.GameAction;
import tradeworld.World;

/**
 *
 * @author Kotuc
 */
public class BuildRoad implements GameAction {

    private static final long serialVersionUID = 532890802305L;
    private int x, y;

    public BuildRoad(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean validate(World world) {
        return true;
    }

    public void perform(World world) {
        world.getRoadNetwork().addPoint(x, y);
        System.out.println("road built " + x + ", " + y);
    }

    @Override
    public String toString() {
        return "Build road "+x+", "+y;
    }



}
