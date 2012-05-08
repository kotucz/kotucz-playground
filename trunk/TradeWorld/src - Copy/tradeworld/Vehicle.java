package tradeworld;

/**
 *
 * @author Kotuc
 */
public abstract class Vehicle {

    /**
     * Fuel amount in tank.
     */
    private double fuel;
    private Pos pos = new Pos();
    protected final Payload payload = new Payload();
    private Type type;

    public enum Type {

        SKODA120, LIGHT_TRUCK, STEAM_TRUCK;
        private Goods.Type fuelType;
        
    }
}
