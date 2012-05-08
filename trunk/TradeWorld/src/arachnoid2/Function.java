package arachnoid2;

/**
 *
 * @author Kotuc
 */
public class Function implements Valuation {

    StaticModel smodel;
    SitesConfig goal;

    public Function(StaticModel smodel, SitesConfig goal) {
        this.smodel = smodel;
        this.goal = goal;
    }

    public double evaluate(VectorNd config) {
        smodel.setJoints(config);

        SitesConfig res = smodel.getSitesConfig();
        VectorNd sub = goal.sub(res);

//        double ss = 0; // square sum
//        for (int i = 0; i < sub.length; i++) {
//            ss += sub[i].lengthSquared();
//        }
        return sub.lengthSquared();
    }



//    public double evaluateSquareSum(JointsConfig config) {
//        smodel.set(config);
//
//        SitesConfig res = smodel.getSitesConfig();
//        VectorNd sub = goal.sub(res);
//
////        double ss = 0; // square sum
////        for (int i = 0; i < sub.length; i++) {
////            ss += sub[i].lengthSquared();
////        }
//        return sub.lengthSquared();
//    }
//    void gradientDescentIteration(SitesConfig goal) {
//        final double d = 0.01;
//        final double lam = 0.1;
//
//        JointsConfig jointsConfig0 = smodel.getJointsConfig();
////        SitesConfig sitesConfig0 = smodel.getSitesConfig();
//        double ss0 = evaluateSquareSum(goal, jointsConfig0);
//
//        JointsConfig dconf = smodel.getJointsConfig();
//        VectorNd grad = new VectorNd(smodel.joints.size());
//        for (int i = 0; i < grad.dim(); i++) {
//            dconf.values[i] += d;
//            double evaluateSquareSum = evaluateSquareSum(goal, dconf);
//            grad.values[i] = evaluateSquareSum - ss0;
//            dconf.values[i] -= d;
//        }
//
//        for (int i = 0; i < grad.dim(); i++) {
//            jointsConfig0.values[i] += -grad.values[i] * lam;
//        }
//
//        smodel.set(jointsConfig0);
//    }
}
