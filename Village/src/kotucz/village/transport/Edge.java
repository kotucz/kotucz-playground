package kotucz.village.transport;

/**
 * @author Kotuc
 */
public class Edge<T> {

    final T to;

    final double dist;

    public Edge(T to, double dist) {
        this.to = to;
        this.dist = dist;
    }
}
