/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.transport;

import com.jme3.material.Material;
import kotucz.village.build.Building;
import kotucz.village.game.Player;
import kotucz.village.tiles.Pos;

/**
 * Place, where vehicles load and unload goods)
 *
 * @author Petr Dluhoš
 */
public interface Depot  {

    //    private static final long FUEL_EFFICIENCY = 2000; //TODO : should be different for different types of fuel
//    private Vehicle operatedVehicle;


    //    Storage localStorage;

    //    public Depot(Goods.Type type, Player owner, Point3d upperLeft) {
//    public Depot(Pos pos, Player owner, Material mat) {
//        super(pos, mat, owner);
//        super(type, owner, upperLeft);

        //        localStorage = new Storage(100);
//        localStorage.getType().addGoods(new Goods(Goods.Type.PETROL, 20, owner)); //TODO: To remove - onlz for testing
//        localStorage.getType().addGoods(new Goods(Goods.Type.WOOD, 20, owner)); //TODO: To remove - onlz for testing



//        panel = new DepotPanel(this);

//    }

//    public void buildVehicle(Vehicle.Type type) {
////        Vehicle vehicle = new Truck(this.getOwner(), type, this.getEntrance());
////        getWorld().putVehicle(vehicle);
//    }

//    private Storage findFreeStorage(int spaceRequired) {
//
//        for (Storage storage : storages) {
//            if (storage.getType().getFreeVolume() >= spaceRequired) {
//                return storage;
//            }
//
//        }
//        return null;
//    }

    //    private void tankVehicle(Vehicle vehicle) {
//
//        for (Storage storage : storages) {
//
//            Goods targetGoods = storage.getType().findGoodsPile(vehicle.getFuelType(), getOwner());//TODO : Should be vehicle's owner?);
//            if (targetGoods != null) {
//
//                targetGoods = new Goods(vehicle.getFuelType(), 1, getOwner());
//                storage.getType().removeGoods(targetGoods);
//                vehicle.addFuel(FUEL_EFFICIENCY);
//                break;
//            }
//        }
//    }

//    public void putVehicle(Vehicle vehicle) {
//
//        vehicles.add(vehicle);
//        System.out.println("Fuel : " + vehicle.getFuel());
//        if (vehicle.getFuel() < vehicle.getMaxFuel() - FUEL_EFFICIENCY) {
//            tankVehicle(vehicle);
//        }
//
//    }

//    public void removeVehicle(Vehicle vehicle) {
//
//        if (vehicles.contains(vehicle)) {
//            vehicles.remove(vehicle);
//        } else {
//            throw new IllegalArgumentException("Cannot remove vehicle which is not in the depot's 'vehicles' list.");
//        }
//
//    }

//    public List<Vehicle> getVehicles() {
//
//        return vehicles;
//    }

    //    public Pos getEntrance() {
////        Pos point = new Pos(getType().getWidthx() / 2.0 - 1.0, -getType().getWidthy() / 2.0, 0);
//        Pos point = new Pos(0, 0);
////        point.add(getPosVector());
//        return point;
//    }

//    public void addStorage(Storage storage) {

        //System.out.println("" + getPosVector().distanceTo(storage.getPosVector()));
//        if (getPosVector().distance(storage.getPosVector()) <= Building.STORAGE_RADIUS) {
//            storages.add(storage);
//            Collections.sort(storages, new Comparator<Storage>() {
//
//                public int compare(Storage o1, Storage o2) {
//                    double dst1 = getPosVector().distance(o1.getPosVector());
//                    double dst2 = getPosVector().distance(o2.getPosVector());
//                    if (o1.equals(o2)) {
//                        return 0;
//                    } else {
//                        if (dst1 == dst2) {
//                            return o1.getName().compareTo(o2.getName());
//                        } else {
//                            if (dst1 < dst2) {
//                                return -1;
//                            } else {
//                                return 1;
//                            }
//                        }
//                    }
//                }
//            });
//
//        } else {
//
//            throw new IllegalArgumentException("Storage is out of STORAGE_RADIUS.");
//        }
//    }

//    public boolean isEmpty() {
//        return vehicles.isEmpty();
//    }

//    public void act() {
//        doStep();
//    }


    Pos getEntrancePos();

    boolean requestUnloadVehicle(Vehicle vehicle, GoodsType type);

    boolean requestLoadVehicle(Vehicle vehicle, GoodsType type);
}
