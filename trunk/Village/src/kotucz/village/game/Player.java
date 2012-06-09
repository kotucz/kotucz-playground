package kotucz.village.game;

import java.awt.*;
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
//    private final World world;
//    private final List<Building> buildings = new LinkedList<Building>();
//    private final List<Vehicle> vehicles = new LinkedList<Vehicle>();
    private final PlayerId id;

    public Player(String name, Color color, long cash) {
        this.name = name;
        this.color = color;
        this.cash = cash;
//        this.world = world;
        this.id = new PlayerId(color.getRGB());
    }

    public void pay(long amount) {
        this.cash -= amount;
    }

    public void earn(long amount) {
        this.cash += amount;
    }

    public long getCash() {
        return cash;
    }

    public String getName() {
        return name;
    }

    public PlayerId getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }
}
