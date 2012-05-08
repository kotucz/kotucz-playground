package tradeworld;

/**
 *
 * @author Kotuc
 */
public class Goods {

    private final Type type;
    private int quantity;

    public Goods(Type type) {
        this.type = type;
    }

    public enum Type {
        WOOD,
        BOARD,
        FURNITURE;
    }

}
