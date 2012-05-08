package tradeworld;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import tradeworld.RoadNetwork.RoadPoint;

/**
 *
 * @author Kotuc
 */
public class PathFinding {

    private RoadNetwork network;

    public PathFinding(RoadNetwork network) {
        this.network = network;
    }

    public List<RoadPoint> aStar(RoadPoint start, RoadPoint dest) {
//        System.out.println("aStar "+start+" "+dest);
        Set<RoadPoint> solved = new HashSet<RoadPoint>();
        Set<RoadPoint> visited = new HashSet<RoadPoint>();
        AStarHeuristics heuristics = new AStarHeuristics(dest);
        Queue<RoadPoint> queue = new PriorityQueue<RoadPoint>(11, heuristics);
        queue.add(start);
        heuristics.shortestDist.put(start, 0.0);
        while (!queue.isEmpty()) {
            RoadPoint poll = queue.poll();
//            System.out.println("Poll "+poll);
            {   // expand poll
                for (RoadPoint next : poll.incidents) {
                    if (!visited.contains(next)) {
                        heuristics.explore(poll, next);
                        queue.add(next);
                        visited.add(next);
                    }
                }
            }
            solved.add(poll);
            if (poll.equals(dest)) {
                // can finish
                // build path

//                System.out.println("BackPath: ");// + heuristics.ancestors.toString());

                List<RoadPoint> path = new LinkedList<RoadPoint>();
                RoadPoint last = dest;
                while (last != null) {
//                    System.out.println("last " + last);
                    path.add(last);
                    last = heuristics.ancestors.get(last);
                }
//                System.out.println("Path length " + path.size());
                Collections.reverse(path);
                return path;
            }
        }
        // no path found
        return null;
    }

    class AStarHeuristics implements Comparator<RoadPoint> {

        private RoadPoint dest;
        private Map<RoadPoint, Double> shortestDist = new HashMap<RoadPoint, Double>();
        private Map<RoadPoint, RoadPoint> ancestors = new HashMap<RoadPoint, RoadPoint>();

        public AStarHeuristics(RoadPoint dest) {
            this.dest = dest;
            
        }

        public void explore(RoadPoint from, RoadPoint explore) {
            double edgeValue = explore.getPos().distance(from.getPos());
            if (edgeValue < 0) {
                throw new AssertionError(" edge value < 0 breaks invatriant ");
            }
            final double newDist = shortestDist(from) + edgeValue;
            if (newDist < shortestDist(explore)) {
                // replace
                // TODO remove old!!!
                shortestDist.put(explore, newDist);
                // TODO replace ancestor
                ancestors.put(explore, from);
//                System.out.println("Updated "+explore+" from "+from+" newDist "+newDist);
//                ancestors.put(from, explore);
                // TODO if solved was updated means error - invarian break

                // TODO update queue (comparable thing)

            }
        }

        public int compare(RoadPoint o1, RoadPoint o2) {
            if (shortestDist(o1) < shortestDist(o2)) {
                return -1;
            } else {
                return 1;
            }
        }

        private double potential(RoadPoint of) {
            return dest.getPos().distance(of.getPos());
        }

        double shortestDist(RoadPoint point) {
            if (shortestDist.containsKey(point)) {
                return shortestDist.get(point);
            } else {
                return Double.POSITIVE_INFINITY;
//                return 100000;
            }
        }
    }
}
