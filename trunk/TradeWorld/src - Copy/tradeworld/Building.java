package tradeworld;

/**
 *
 * @author Kotuc
 */
public abstract class Building {

    private Player owner;
    private final Pos pos = new Pos();
    private double width, height;

    public enum Type {
        WOODMILL;
    }

}
