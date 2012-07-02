package kotucz.village.transport.paths;

import kotucz.village.transport.paths.Dijkstra;
import kotucz.village.transport.paths.PathGraph;

import java.util.Comparator;

/**
* @author Kotuc
*/
class AStar<T> extends Dijkstra<T> implements Comparator<T> {

    final T dest;

    public AStar(PathGraph<T> graph, T start, T dest) {
        super(graph, start);
        this.dest = dest;

//            queue = new PriorityQueue<T>(11, this);
    }

    @Override
    double heuristicDistance(T through) {
        // TODO take into account the dest
        return super.heuristicDistance(through);    //To change body of overridden methods use File | Settings | File Templates.
    }

    //
//    public int compare(T o1, T o2) {
//        if (heuristicDistance(o1) < heuristicDistance(o2)) {
//            return -1;
//        } else {
//            return 1;
//        }
//    }

//        private double potential(T of) {
//            return dest.getPosVector().distance(of.getPosVector());
//        }



//    double heuristicDistance(T through) {
//        // TODO
//
//
//        if (distFromStart.containsKey(through)) {
//            return distFromStart.get(through) + graph.estimatedMinimumDistance(through, dest);
//        } else {
//            return Double.POSITIVE_INFINITY;
////                return 100000;
//        }
//    }

}
