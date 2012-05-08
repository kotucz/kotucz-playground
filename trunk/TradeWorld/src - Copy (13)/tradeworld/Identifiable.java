package tradeworld;

/**
 *
 * @author Kotuc
 */
public interface Identifiable {

    ObjectId<? extends Identifiable> getId();

}
