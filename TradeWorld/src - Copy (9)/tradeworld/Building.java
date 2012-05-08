package tradeworld;

import javax.swing.JPanel;
import javax.vecmath.Point3d;
import tradeworld.graphics.Building3D;

/**
 *
 * @author Kotuc
 */
public abstract class Building {

    public static final int STORAGE_RADIUS = 10;
    private Player owner;
    private final Pos pos;
    private Type type;
    private String name;
    private Building3D model;
    private World world;
    protected JPanel panel;

    public JPanel getPanel() {
        return panel;
    }

    void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public Building(Type type, Player owner, Point3d center) {
        this.type = type;
        this.owner = owner;
        this.pos = new Pos(center.x, center.y, center.z);
        this.name = this.type.toString(); //TODO: Name should not be this
        this.model = new Building3D(type.getWidthx(), type.getWidthy(), 1);
        this.model.setPos(pos);
        this.model.refresh();
    }

    Building3D getModel() {
        return model;
    }

    public Type getType() {

        return type;
    }

    public Player getOwner() {

        return owner;
    }

    public Pos getPos() {

        return pos;
    }

    public String getName() {

        return name;
    }

    public void act() {
    }

    public boolean isInRange(Building building) {

        if (this.getPos().distanceTo(building.getPos()) <= Building.STORAGE_RADIUS) {
            return true;
        } else {
            return false;
        }
    }

    public enum Type {

        LUMBERJACK_CAMP(2, 2, "lumberjack_camp.png"), //drevorubecky srub
        WOODMILL(3, 2, "woodmill.png"), //pila
        FURNITURE_FACTORY(4, 3, "furniture_factory.png"), //vyrobna nabytku
        STORAGE(2, 2, "storage.png"), //skladiste
        TRUCK_DEPOT1(2, 2, "car_depot1.png");       //depo pro auta velikosti 1
        final int widthx;
        final int widthy;
        final String iconName;

        private Type(int widthx, int widthy, String iconName) {
            this.widthx = widthx;
            this.widthy = widthy;
            this.iconName = iconName;
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
    }
}
