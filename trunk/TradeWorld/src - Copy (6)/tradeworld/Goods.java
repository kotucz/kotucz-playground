package tradeworld;

/**
 *
 * @author Kotuc
 */
public class Goods {

    private final Type type;
    private int quantity;
    private Player owner;

    public Goods(Type type, int quantity, Player owner) {
        this.type = type;
        this.quantity = quantity;
        this.owner = owner;
    }

    public Type getType() {

        return type;
    }

    public int getQuantity() {

        return quantity;
    }

    public Player getOwner() {

        return owner;
    }

    /**
     * Increases quantity of the goods.
     * @param quantity Amount to add.
     */
    public void increaseQuantity(int quantity) {

        if (quantity > 0) {
            this.quantity += quantity;
        } else {
            throw new IllegalArgumentException("Quantity should be a positive integer.");
        }
    }

    /**
     * Decreases quantity of the goods.
     * @param quantity Amount to subtract.
     */
    public void decreaseQuantity(int quantity) {
        if (quantity > 0) {
            if (quantity <= this.quantity) {
                this.quantity -= quantity;
            } else {
                throw new IllegalArgumentException("Quantity should be a positive integer.");
            }
        } else {
            throw new IllegalArgumentException("There is not anough quantity to decrease.");
        }

    }

    /**
     * Merges 2 piles of goods into this one.
     * @param goods Pile of goods to add
     */
    public void addGoods(Goods goods) {

        if (type == goods.type) {
            increaseQuantity(goods.getQuantity());
        } else {
            throw new IllegalArgumentException("The type of goods being added does not match.");
        }
    }

    /**
     * Idicates whether two goods have the same type and owner.
     * @param goods Goods to compare with.
     * @return True if the two goods have the same type and owner
     */
    public boolean isSamePile(Goods goods) {
        
        return (this.type.equals(goods.type) && (this.owner.equals(goods.owner)));
    }

    public enum Type {
        WOOD,           //drevo
        BOARD,          //prkna
        FURNITURE;      //nabytek
    }

}
