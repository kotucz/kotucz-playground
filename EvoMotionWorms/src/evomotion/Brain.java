package evomotion;



import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Tomas
 */
public class Brain {

    private final int limbs = 3;
    private final int frames = 6;
    
    float values[] = new float[3 * 6];

    public Brain() {
        randomvalues();
//        supervalues();
    }

    private void randomvalues() {
        for (int i = 0; i < values.length; i++) {
            values[i] = (float) (Math.random() - Math.random());
        }
    }

    private void supervalues() {
//        double[] vals = new double[]{0.6524393, -0.07643094, -0.07114819, -9.886243E-4, 0.27340478, -0.6093782, -0.3557225, -0.24198897, -0.6301504, -0.115150556, 0.2758763, -0.01976217, -0.20446345, -0.0067717843, -0.5222594, -0.038594984, -0.92861545, 0.27386427};
        // 17.25	Brain
//        double[] vals = new double[]{0.31, 0.08, 0.27, 0.05, -0.25, -0.09, 0.5, 0.12, 0.93,
//            0.19, 0.77, -0.2, -0.71, 0.71, 0.63, -0.13, -0.08, 0.18};
        // 20.6	Brain
        //double[] vals = new double[]{0.31, 0.08, -0.45, 0.05, -0.25, -0.09, 0.5, 0.12, 0.93,
        //    0.19,	0.77,	-0.2,	-0.71,	0.71,	0.63,	-0.13,	-0.08,	0.18};
        //21.61	Brain
        double[] vals = new double[]{0.31, 0.86, 0.26, 0.55, 0.36, -0.63, 0.88,
            0.56, 0, 0.12, -0.39, 0.4, -0.22, 0.23, -0.16 - 0.47, 0.16, -0.5};


        for (int i = 0; i < vals.length; i++) {
            this.values[i] = (float) vals[i];
        }
    }

    float get(int limbid, int frame) {
        return values[limbid + limbs * frame];
    }

    @Override
    public String toString() {
//        String s = "";
//        for (int i = 0; i < values.length; i++) {
//            float f = values[i];
//            s+=f+" ";
//        }
        return "Brain " + Arrays.toString(values);
    }

    void mutate() {
        values[new Random().nextInt(values.length)] = (float) (Math.random() - Math.random());
    }

    static Brain copyOf(Brain orig) {
        Brain brain = new Brain();
        brain.values = Arrays.copyOf(orig.values, orig.values.length);
        return brain;
    }

    static Brain crosover(Brain orig1, Brain orig2) {
        Brain brain = new Brain();
        brain.values = Arrays.copyOf(orig1.values, orig1.values.length);

        int crosspoint = new Random().nextInt(orig1.values.length);
        for (int i = crosspoint; i < orig2.values.length; i++) {
            brain.values[i] = orig2.values[i];
        }

        return brain;
    }
    float score;
}
