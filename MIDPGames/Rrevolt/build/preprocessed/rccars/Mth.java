package rccars;

public class Mth {

    public static final double SQRT3 = 1.732050807568877294;

    public static double atan(double x) {
        boolean signChange = false;
        boolean invert = false;
        int sp = 0;
        double x2, a;
        // check up the sign change
        if (x < 0) {
            x = -x;
            signChange = true;
        }
        // check up the invertation
        if (x > 1) {
            x = 1 / x;
            invert = true;
        }
        // process shrinking the domain until x<PI/12
        while (x > Math.PI / 12) {
            sp++;
            a = x + SQRT3;
            a = 1 / a;
            x = x * SQRT3;
            x = x - 1;
            x = x * a;
        }
        // calculation core
        x2 = x * x;
        a = x2 + 1.4087812;
        a = 0.55913709 / a;
        a = a + 0.60310579;
        a = a - (x2 * 0.05160454);
        a = a * x;
        // process until sp=0
        while (sp > 0) {
            a = a + Math.PI / 6;
            sp--;
        }
        // invertation took place
        if (invert) {
            a = Math.PI / 2 - a;
        }
        // sign change took place
        if (signChange) {
            a = -a;
        }
        return a;
    }

    public static double atan2(double y, double x) {
        if (y == 0 && x == 0) {
            return 0;
        }
        if (x > 0) {
            return atan(y / x);
        }
        if (x < 0) {
            if (y < 0) {
                return -(Math.PI - atan(y / x));
            } else {
                return Math.PI - atan(-y / x);
            }
        }
        if (y > 0) {
            return Math.PI / 2;
        } else {
            return -Math.PI / 2;
        }
    }
    
    public static float distSqr(float x1, float y1, float x2, float y2) {
        return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
    }
            
}
