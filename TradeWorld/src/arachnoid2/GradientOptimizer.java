/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arachnoid2;

/**
 *
 * @author Kotuc
 */
public class GradientOptimizer {

    Valuation valuation;

    public GradientOptimizer(Valuation valuation) {
        this.valuation = valuation;
    }

    void gradientDescentIteration(VectorNd dconf) {
        final double d = 0.01;
        final double lam = 0.1;

        double ss0 = valuation.evaluate(dconf);

        VectorNd grad = new VectorNd(dconf.dim());
        for (int i = 0; i < grad.dim(); i++) {
            dconf.values[i] += d;
            grad.values[i] = valuation.evaluate(dconf) - ss0;
            dconf.values[i] -= d;
        }

        for (int i = 0; i < grad.dim(); i++) {
            dconf.values[i] += -grad.values[i] * lam;
        }

    }
}
