package kotucz.village.transport;

/**
 * @author Kotuc
 */
public interface PathGraph<T> {
    Iterable<T> getNexts(T poll);

    double getEdgeDistance(T from, T to);

//    double estimatedMinimumDistance(T from, T to);
}
