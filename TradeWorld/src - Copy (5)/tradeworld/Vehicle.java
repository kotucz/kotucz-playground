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
    protected final Payload payload;
    private Type type;
    private String name;
    private Player owner;

    public Vehicle(Player owner, int maxPayload) {
        name = toString();
        this.owner = owner;
        payload = new Payload(maxPayload);
    }

    public String getName() {

        return name;
    }

    public Player getOwner() {

        return owner;
    }

    public Payload getPayload() {

        return payload;
    }

    public enum Type {

        SKODA120, LIGHT_TRUCK, STEAM_TRUCK;
        private Goods.Type fuelType;
        
    }
}
