package tradeworld.war;

/**
 *
 * @author Kotuc
 */
public class Health {

    private int health = 100;

    public void damage(int points) {
        this.health -= points;
    }

    public boolean isAlive() {
        return (health > 0);
    }

    @Override
    public String toString() {
        return this.health+"HP";
    }



}
