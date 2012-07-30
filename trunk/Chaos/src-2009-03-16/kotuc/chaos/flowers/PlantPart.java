package kotuc.chaos.flowers;

import javax.vecmath.*;

public abstract class PlantPart {

    public double directionAngle;
    public double elevationAngle;
    public Point3d pos;
    public Point3d endpos;
    public int size;
    public int maxSize;
    public Plant plant;
    //public int type;
    public int depth;

    /**
     * Method PlantPart
     *
     *
     */
    public PlantPart() {
    }

    public PlantPart(Plant plant) {
        this.plant = plant;
        directionAngle = plant.genom.getGrowDirectionAngle(Genom.BRANCH);
        elevationAngle = 0.0;//plant.genom.getGrowElevationAngle(Genom.BRANCH);
        pos = new Point3d();
        endpos = new Point3d();
        size = 0;
        maxSize = plant.genom.getGrowLength(Genom.BRANCH);
//	    type = Genom.BRANCH;
        depth = 0;
        plant.addPart(this);
    }

    public PlantPart(PlantPart part, int type) {
//		this.type = type;
        this.depth = part.depth + 1;
        this.plant = part.plant;
        this.directionAngle = plant.genom.getGrowDirectionAngle(type) + part.directionAngle;
        this.elevationAngle = plant.genom.getGrowElevationAngle(type);// + part.elevationAngle;

        this.pos = part.endpos;
//	    this.pos = (Point3d)part.endpos.clone();
        endpos = new Point3d();
        //endpos();
        size = 0;
        if ((type == Genom.BRANCH) && (plant.genom.getGrowRatioUpdepth() < this.depth)) {
            maxSize = (int) ((double) part.maxSize * plant.genom.getGrowRatio(type));
        } else {
            maxSize = plant.genom.getGrowLength(type);
        }
        System.out.println(maxSize);
        plant.addPart(this);
    }

    public abstract void grow();/* {
    if (this.type == Genom.LEAF) plant.energy+=this.size;
    if (this.plant.energy > 12) {

    if ((maxSize > 1) && (size  == maxSize / 2)) {
    //endpos();
    nodeActivity();
    System.out.println("delim se");
    };
    if (size < maxSize) {

    plant.energy--;
    if (this.type == Genom.SEED) this.plant.energy -= 10;

    System.out.println("rostu");
    size++;

    };
    };



    }*/


    public  void nodeActivity() {} /*{
    if (this.type == Genom.SEED) {
    Plant pp = new Plant(this.plant);
    pp.energy = this.size * 10;

    }

    if (this.type != Genom.BRANCH) return;
    if (plant.genom.getGrowPeak() == true) {
    PlantPart pp = new Branch(this);
    pp.elevationAngle = 0;
    //	pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.BRANCH));
    //	if (plant.genom.getChildCount(Genom.BRANCH) > 1) pp.elevationAngle+= Math.toRadians(10);

    }
    for (int i = 1; i <= plant.genom.getChildCount(Genom.BRANCH); i++) {
    PlantPart pp = new PlantPart(this, Genom.BRANCH);
    pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.BRANCH));
    //		pp.elevationAngle += Math.toRadians(plant.genom.getGrowBranchElevationAngle());
    if (plant.genom.getChildCount(Genom.BRANCH) > 1) pp.elevationAngle+= Math.toRadians(10);
    }

    if (depth < plant.genom.getGrowLeavesDepth()) {

    for (int i = 1; i <= plant.genom.getChildCount(Genom.LEAF); i++) {
    PlantPart pp = new PlantPart(this, Genom.LEAF);
    pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.LEAF));
    //pp.elevationAngle += Math.toRadians(10);
    pp.type = Genom.LEAF;
    }
    return;
    }

    for (int i = 1; i <= plant.genom.getChildCount(Genom.SEED); i++) {
    PlantPart pp = new PlantPart(this, Genom.SEED);
    pp.directionAngle += Math.toRadians((i - 1) * 360 / plant.genom.getChildCount(Genom.SEED));
    //pp.elevationAngle += Math.toRadians(10);
    pp.type = Genom.SEED;
    }


    }*/


    public Point3d endpos() {
        this.endpos.x = pos.x + Math.cos(directionAngle) * size * 0.1 * Math.sin(elevationAngle);
        this.endpos.y = pos.y + Math.sin(directionAngle) * size * 0.1 * Math.sin(elevationAngle);
        this.endpos.z = pos.z + Math.cos(elevationAngle) * size * 0.1;

        return endpos;
    }

    public String toString() {
        return "ENERGY : " + plant.energy;
    }
}
