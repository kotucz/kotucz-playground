package arachnoid2;

import javax.vecmath.Tuple3d;

/**
 *
 * @author Kotuc
 */
public class VectorNd {

//    List<Joint> joints;
    protected double[] values;

    public VectorNd() {
    }

    public VectorNd(int N) {
        values = new double[N];
    }

    public VectorNd sub(VectorNd vec2) {
        if (this.dim()!=vec2.dim()) {
            throw new IllegalArgumentException("Different dimensions "+this.dim()+" "+vec2.dim());
        }
        VectorNd res = new VectorNd(values.length);
        for (int i = 0; i < values.length; i++) {
            res.values[i] = this.values[i] - vec2.values[i];
        }
        return res;
    }

    public VectorNd add(VectorNd vec2) {
        if (this.dim()!=vec2.dim()) {
            throw new IllegalArgumentException("Different dimensions "+this.dim()+" "+vec2.dim());
        }
        VectorNd res = new VectorNd(values.length);
        for (int i = 0; i < values.length; i++) {
            res.values[i] = this.values[i] + vec2.values[i];
        }
        return res;
    }

//    public void applyValues(List<Site> sites) {
//        for (int i = 0; i < values.length; i++) {
//            sites.get(i).setAngle(values[i]);
//        }
//    }

    public int dim() {
        return values.length;
    }

    public double lengthSquared() {
        double squareSum = 0;
        for (int i = 0; i < values.length; i++) {
            squareSum += this.values[i] * this.values[i];
        }
        return squareSum;
    }

    void setTuple(Tuple3d tuple, int tupleid) {
        values[3 * tupleid + 0] = tuple.x;
        values[3 * tupleid + 1] = tuple.y;
        values[3 * tupleid + 2] = tuple.z;
    }

    void getTuple(Tuple3d tuple, int tupleid) {
        tuple.x = values[3 * tupleid + 0];
        tuple.y = values[3 * tupleid + 1];
        tuple.z = values[3 * tupleid + 2];
    }
}
