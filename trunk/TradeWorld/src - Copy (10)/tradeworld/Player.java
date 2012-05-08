package tradeworld;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class Player {

    private final String name;
    private final Color color;
    private long cash;
    private final World world;
    private final List<Building> buildings = new LinkedList<Building>();
    private final List<Vehicle> vehicles = new LinkedList<Vehicle>();

    public Player(String name, Color color, long cash, World world) {
        this.name = name;
        this.color = color;
        this.cash = cash;
        this.world = world;
    }


    public String getName() {

        return name;
    }

    @Override
    public String toString() {
        return name;
    }





}
