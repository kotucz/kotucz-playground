package kotuc.chaos.flowers;

import javax.vecmath.*;
import kotuc.chaos.Entity;

public class Seed extends PlantPart {

    /**
     * Method Seed
     *
     *
     * @param part
     */
    public Seed(Branch part) {
        this.depth = part.depth + 1;
        this.plant = part.plant;
        this.directionAngle = plant.genom.getGrowDirectionAngle(Genom.SEED) + part.directionAngle;
        this.elevationAngle = plant.genom.getGrowElevationAngle(Genom.SEED);

        this.pos = part.endpos;
        endpos = new Point3d();

        size = 0;
        maxSize = plant.genom.getGrowLength(Genom.SEED);
        System.out.println(maxSize);
        plant.addPart(this);
    }

    public void grow() {
        this.plant.energy--;
        if (this.plant.energy > 12) {

            if ((maxSize > 1) && (size == maxSize - 1)) {

                nodeActivity();
//                System.out.println("mnozim se");
            }

            if (size < maxSize) {

                this.plant.energy -= 11;

//                System.out.println("rostu");
                size++;

            }

        }




    }

    public void nodeActivity() {

        Plant pp = new Plant(this.plant);

        for (Entity entity : plant.getLocation().getEntities()) {
            if (entity.getPos().distance(pp.getPos()) < 2.0) {
                return; // no born
            }
        }

        plant.getLocation().addEntity(pp);
        pp.energy = this.size * 10;

    }
}
