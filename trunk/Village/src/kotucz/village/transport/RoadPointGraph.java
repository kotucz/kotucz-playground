package kotucz.village.transport;

import kotucz.village.transport.paths.Edge;
import kotucz.village.transport.paths.PathGraph;

/**
* @author Kotuc
*/
class RoadPointGraph implements PathGraph<RoadPoint> {

    @Override
    public Iterable<Edge<RoadPoint>> getNexts(RoadPoint poll) {
        return poll.getEdges();
    }

//    @Override
//    public double getEdgeDistance(RoadPoint from, RoadPoint to) {
//        if (from.getNexts().contains(to)) {
//          return 1.0;
//
//        } else {
//            return Double.POSITIVE_INFINITY;
//        }
//    }

//    @Override
//    public double estimatedMinimumDistance(RoadPoint from, RoadPoint to) {
//        throw new UnsupportedOperationException("TODO for astar");
////        return 0;
//    }

}
