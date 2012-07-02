package kotucz.village.transport;

/**
 * @author Kotuc
 */
public interface PathGraph<T> {

    // TODO crete class Edge (T, dist)
    Iterable<Edge<T>> getNexts(T poll);

//    double getEdgeDistance(T from, T to);

//    double estimatedMinimumDistance(T from, T to);
}
