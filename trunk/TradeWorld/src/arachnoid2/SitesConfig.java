package arachnoid2;

import java.util.List;

/**
 *
 * @author Kotuc
 */
public class SitesConfig extends VectorNd {

    public SitesConfig() {
    }

    public SitesConfig(int nsites) {
        super(3*nsites);
    }

    public SitesConfig(List<Site> sites) {
        storeSites(sites);
    }

    public void storeSites(List<Site> sites) {
        values = new double[sites.size() * 3];
        int i = 0;
        for (Site site : sites) {
            this.setTuple(site.getPoint(), i);
            i++;
        }
    }
//    public Vector3d[] sub(SitesConfig config2) {
//        Vector3d[] res = new Vector3d[values.length];
//        for (int i = 0; i < values.length; i++) {
//            res[i] = new Vector3d(values[i]);
//            res[i].sub(config2.values[i]);
//        }
//        return res;
//    }
////    public void applyValues(List<Site> sites) {
////        for (int i = 0; i < values.length; i++) {
////            sites.get(i).setAngle(values[i]);
////        }
////    }
}
