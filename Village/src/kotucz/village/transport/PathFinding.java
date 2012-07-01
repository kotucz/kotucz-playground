package kotucz.village.transport;

import com.google.common.base.Preconditions;

import java.util.*;

/**
 * @author Kotuc
 */
public final  class PathFinding {

//    private PathNetwork network;
//
//    public PathFinding(PathNetwork network) {
//        this.network = network;
//    }

    private PathFinding() {
    }

    public static <T> List<T> findPath(PathGraph<T> graph, T start, T dest) {
        Preconditions.checkNotNull(start, "start");
        Preconditions.checkNotNull(dest, "dest");

        System.out.println("findPath " + start + " " + dest);

        Dijkstra dijkstra = new Dijkstra<T>(graph, start);

        dijkstra.search();

        return dijkstra.reconstructPath(dest);

    }

}
