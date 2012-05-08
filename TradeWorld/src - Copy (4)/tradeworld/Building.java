package tradeworld;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kotuc
 */
public abstract class Building {

    public static final int STORAGE_RADIUS = 10;
    private Player owner;
    private final Pos pos;
    private double width, height;
    private Type type;
    private String name;

    public Building(Type type, Player owner, Pos pos) {
        this.type = type;
        this.owner = owner;
        this.pos = pos;
        this.name = this.type.toString(); //TODO: Name should not be this

    }


    public Type getType() {

        return type;
    }

    public Player getOwner() {

        return owner;
    }

    public Pos getPos() {

        return pos;
    }

    public String getName() {

        return name;
    }

    public enum Type {
        LUMBEJACK_CAMP,
        WOODMILL,
        FURNITURE_FACTORY,
        STORAGE;

        

    }



}
