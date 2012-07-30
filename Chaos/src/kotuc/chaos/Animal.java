package kotuc.chaos;
//import java.awt.*;

import javax.media.j3d.*;

public class Animal extends PhysicEntity {

    final double VELOCITY = 0.01;

    public Animal() {
        BranchGroup bg = new BranchGroup();

        String[] fNames = new String[3];
        fNames[0] = "models/hand1.obj";
        fNames[1] = "models/hand2.obj";
        fNames[2] = "models/hand3.obj";

        Appearance handAppear = new Appearance();
        handAppear.setMaterial(new Material());
        handMorph = new MorphLoader(fNames, handAppear);
        bg.addChild(handMorph);

        this.refreshWeights();

        addChild(bg);
    }
    Morph handMorph;

    public void setStrength(int i) {
    }

    public void refreshWeights() {
        double[] w = {0.2, 0.0, 0.8};
        handMorph.setWeights(w);
    }

    public String toString() {
        return "Animal" + this.hitpoints + "HP\n";
    }

    @Override
    public void doEveryFrame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
