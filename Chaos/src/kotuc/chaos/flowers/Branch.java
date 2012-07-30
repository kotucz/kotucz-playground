package kotuc.chaos.flowers;

import javax.vecmath.*;

public class Branch extends PlantPart {

    /**
     * Method Branch
     *
     *
     *
     * @param plant
     */
    public Branch(Plant plant) {
        this.plant = plant;
        directionAngle = plant.genom.getGrowDirectionAngle(Genom.BRANCH);
        elevationAngle = 0.0;
        pos = new Point3d();
        endpos = new Point3d();
        size = 0;
        maxSize = plant.genom.getGrowLength(Genom.BRANCH);
        depth = 0;
        plant.addPart(this);
    }

    public Branch(Branch part) {
        this.depth = part.depth + 1;
        this.plant = part.plant;
        this.directionAngle = plant.genom.getGrowDirectionAngle(Genom.BRANCH) + part.directionAngle;
        this.elevationAngle = plant.genom.getGrowElevationAngle(Genom.BRANCH);

        this.pos = part.endpos;
        endpos = new Point3d();

        size = 0;
        if (plant.genom.getGrowRatioUpdepth() < this.depth) {
            maxSize = (int) ((double) part.maxSize * plant.genom.getGrowRatio(Genom.BRANCH));
        } else {
            maxSize = plant.genom.getGrowLength(Genom.BRANCH);
        }
        System.out.println(maxSize);
        plant.addPart(this);
    }

    public void grow() {
        this.plant.energy--;
        if (this.plant.energy > 2) {

            if ((maxSize > 1) && (size == maxSize / 2)) {
                nodeActivity();
//                System.out.println("delim se");
            }
            ;
            if (size < maxSize) {

                plant.energy--;

//                System.out.println("rostu");
                size++;

            }

        }




    }

    @Override
    public void nodeActivity() {
        if (plant.genom.getGrowPeak() == true) {
            Branch pp = new Branch(this);
            pp.elevationAngle = 0;
        //	pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.BRANCH));
        //	if (plant.genom.getChildCount(Genom.BRANCH) > 1) pp.elevationAngle+= Math.toRadians(10);

        }
        for (int i = 1; i <= plant.genom.getChildCount(Genom.BRANCH); i++) {
            Branch pp = new Branch(this);
            pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.BRANCH));
            //		pp.elevationAngle += Math.toRadians(plant.genom.getGrowBranchElevationAngle());
            if (plant.genom.getChildCount(Genom.BRANCH) > 1) {
                pp.elevationAngle += Math.toRadians(10);
            }
        }

        if (depth < plant.genom.getGrowLeavesDepth()) {

            for (int i = 1; i <= plant.genom.getChildCount(Genom.LEAF); i++) {
                Leaf pp = new Leaf(this);
                pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.LEAF));
            //pp.elevationAngle += Math.toRadians(10);
            }
            return;
        }

        for (int i = 1; i <= plant.genom.getChildCount(Genom.SEED); i++) {
            Seed pp = new Seed(this);
            pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.SEED));
        //pp.elevationAngle += Math.toRadians(10);
        }


    }

    public int returnType() {
        return Genom.BRANCH;
    }
}
