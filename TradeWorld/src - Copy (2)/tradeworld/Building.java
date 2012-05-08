package tradeworld;

/**
 *
 * @author Kotuc
 */
public abstract class Building {

    private Player owner;
    private final Pos pos;
    private double width, height;
    private Type type;

    public Building(Type type, Player owner, Pos pos) {
        this.type = type;
        this.owner = owner;
        this.pos = pos;

    }


    public Type getType() {

        return type;
    }

    public Player getOwner() {

        return owner;
    }

    public enum Type {
        WOODMILL;
    }

}
