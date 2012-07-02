package kotucz.village.transport.paths;

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
