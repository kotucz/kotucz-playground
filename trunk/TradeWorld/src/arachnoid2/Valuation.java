package arachnoid2;

/**
 *
 * @author Kotuc
 */
public interface Valuation {

    /**
     *
     * @param vector
     * @return the value of function in the vector
     */
    double evaluate(VectorNd vector);

}
