package tradeworld;

import javax.vecmath.Point3d;
import tradeworld.graphics.Building3D;
import tradeworld.gui.SuperPanel;

/**
 *
 * @author Kotuc
 */
public abstract class Building extends WorldObject {

    public static final int STORAGE_RADIUS = 10;
    private final Type type;
    protected SuperPanel panel;
    private final BuildingId buildingId;

    public SuperPanel getPanel() {
        return panel;
    }

    public Building(Type type, Player owner, Point3d center) {
        this.type = type;
        this.owner = owner;
        this.setPos(center);
        this.name = this.type.toString(); // TODO: Name should not be this
//        this.model = new Building3D(type.getWidthx(), type.getWidthy(), 1, "factory2.png");
//        this.model = new Building3D();
//        this.model.setPos(pos);
//        this.model.refresh();
        this.buildingId = new BuildingId((int) center.x + 12487 * (int) center.y);
        this.model = new Building3D(this);
    }

    public BuildingId getBuildingId() {
        return buildingId;
    }

//    public Building3D getModel() {
//        return model;
//    }
    public Type getType() {
        return type;
    }

     public boolean isInRange(Building building) {

        if (this.getPos().distance(building.getPos()) <= Building.STORAGE_RADIUS) {
            return true;
        } else {
            return false;
        }
    }

    public enum Type {

        LUMBERJACK_CAMP(2, 2, "lumberjack_camp.png", 4000), //drevorubecky srub
        WOODMILL(3, 2, "woodmill.png", 6000), //pila
        FURNITURE_FACTORY(4, 3, "furniture_factory.png", 12000), //vyrobna nabytku
        STORAGE(2, 2, "storage.png", 4000), //skladiste
        TRUCK_DEPOT1(2, 2, "car_depot1.png", 4000);       //depo pro auta velikosti 1
        final int widthx;
        final int widthy;
        final String iconName;
        final long price;

        private Type(int widthx, int widthy, String iconName, long price) {
            this.widthx = widthx;
            this.widthy = widthy;
            this.iconName = iconName;
            this.price = price;
        }

        public int getWidthx() {
            return widthx;
        }

        public int getWidthy() {
            return widthy;
        }

        public String getIconName() {
            return iconName;
        }

        public long getPrice() {
            return price;
        }
    }

    @Override
    public String toString() {
        return getType() + " (" + getOwner() + ")";
    }
}
