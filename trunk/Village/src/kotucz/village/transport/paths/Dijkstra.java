package kotucz.village.transport.paths;

import com.google.common.base.Preconditions;

import java.util.*;

/**
* @author Kotuc
*/
class Dijkstra<T> implements Comparator<T> {

    final T start;
    final Set<T> closed = new HashSet<T>();
//    final Set<T> opened = new HashSet<T>();
    // opened are in the queue
//    final Queue<T> queue = new PriorityQueue<T>(11, this);
    final Queue<T> queue = new LinkedList<T>();


    // holds shortet calculated distances
    final Map<T, Double> distFromStart = new HashMap<T, Double>();
//        final Map<T, Double> heuristicDist = new HashMap<T, Double>();
    final Map<T, T> commingFrom = new HashMap<T, T>();
    final PathGraph<T> graph;

    Dijkstra(PathGraph<T> graph, T start) {
        Preconditions.checkNotNull(this.graph = graph, "graph");
        Preconditions.checkNotNull(this.start = start, "start");
    }


    public void search() {

        System.out.println("dijkstra from " + start);

        distFromStart.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            // invariant : this has the shortest distance from strart among opened points
            T current = queue.poll();
//            System.out.println("Current "+current);
//            System.out.println("Current "+closed);

            if (closed.contains(current)) {
//                    System.out.println("found "+current+" "+next);
                continue;
            }

            closed.add(current);

            // expand poll

            for (Edge<T> edge : graph.getNexts(current)) {

                explore(current, edge.to, edge.dist);

//                queue.add(edge);
//                opened.add(next);
            }
        }

    }

    List<T> reconstructPath(T dest) {
        // can finish
        // build path

//                System.out.println("BackPath: ");// + heuristics.commingFrom.toString());

        List<T> path = new LinkedList<T>();
        T last = dest;
        while (last != null) {
//                    System.out.println("last " + last);
            path.add(last);
            last = commingFrom.get(last);
        }
//                System.out.println("Path length " + path.size());
        Collections.reverse(path);
        return path;
    }

    void explore(T from, T explore, double dist) {
//        System.out.println("explore "+from+" "+explore);
//            double edgeValue = explore.getPosVector().distance(from.getPosVector());
//        double edgeValue = graph.getEdgeDistance(from, explore);
        final double edgeValue = dist;
        if (edgeValue < 0) {
            throw new AssertionError(" edge value < 0 breaks algorithm invatriant ");
        }
        final double newDist = distFromStart.get(from) + edgeValue;
        if (!distFromStart.containsKey(explore) || newDist < distFromStart.get(explore)) {

            // TODO decrease key would be better
            // remove with old h dist
            queue.remove(explore);

            distFromStart.put(explore, newDist);
            // replace ancestor
            commingFrom.put(explore, from);

            // add with new h dist
            queue.add(explore);


        }
    }

    public int compare(T o1, T o2) {
        if (heuristicDistance(o1) < heuristicDistance(o2)) {
            return -1;
        } else {
            return 1;
        }
    }


    double heuristicDistance(T through) {
        if (distFromStart.containsKey(through)) {
            return distFromStart.get(through);
        } else {
            System.err.println("Should not be here?");
            return Double.POSITIVE_INFINITY;
//                return 100000;
        }
    }

}
