/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradeworld;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Point3d;

/**
 * Place, where vehicles load and unload goods)
 * @author Petr Dluhoš
 */
public class Depot extends Building {

    private final List<Storage> storages = new LinkedList<Storage>();
    private final List<Vehicle> vehicles = new LinkedList<Vehicle>();
    private int progress;
    private WorkingState workingState;
    private static final int LOADING_SPEED = 200;
    private static final long FUEL_EFFICIENCY = 2000; //TODO : should be different for different types of fuel
    private Vehicle operatedVehicle;

    public Depot(Type type, Player owner, Point3d upperLeft) {
        super(type, owner, upperLeft);

        workingState = WorkingState.WAITING_FOR_REQUEST;
        progress = 0;

    }

    private Storage findFreeStorage(int spaceRequired) {

        for (Storage storage : storages) {
            if (storage.getPayload().getFreeVolume() >= spaceRequired) {
                return storage;
            }

        }
        return null;
    }

    public boolean requestLoadVehicle(Vehicle vehicle, Goods.Type type, Player owner) {

        if (vehicles.contains(vehicle)) {

            if (WorkingState.WAITING_FOR_REQUEST.equals(workingState)) {


                boolean done = false;

                for (Storage storage : storages) {
                 //   System.out.println("Prohledavam skladiste");
                    Goods targetGoods = storage.getPayload().findGoodsPile(type, owner);
                    if (targetGoods != null) {
                        operatedVehicle = vehicle;
                        targetGoods = new Goods(type, 1, owner);
                        storage.getPayload().removeGoods(targetGoods);
                        operatedVehicle.load(targetGoods);

                        done = true;
                        break;
                    }
                }
                if (done) {
                 //   System.out.println("Nalezeno zbozi.");
                    progress = 0;
                    workingState = WorkingState.WORKING;
                }
                return false;
            }
            if (WorkingState.DONE.equals(workingState)) {

                if (vehicle.equals(operatedVehicle)) {
                    workingState = WorkingState.WAITING_FOR_REQUEST;
                    operatedVehicle = null;
                    return true;
                } else {
                    return false;
                }
            }


        } else {
            throw new IllegalArgumentException("Vehicle is trying to load not being in the depot's 'vehicles' list.");
        }
        return false;
    }

    public boolean requestUnloadVehicle(Vehicle vehicle, Goods.Type type, Player owner) {

        if (vehicles.contains(vehicle)) {

            if (WorkingState.WAITING_FOR_REQUEST.equals(workingState)) {


                Storage storage = findFreeStorage(1);
                if (storage != null) {
                    operatedVehicle = vehicle;
                    Goods targetGoods = new Goods(type, 1, owner);
                    storage.getPayload().addGoods(targetGoods);
                    operatedVehicle.unload(targetGoods);

                    progress = 0;
                    workingState = WorkingState.WORKING;
                }
                return false;
            }
            if (WorkingState.DONE.equals(workingState)) {

                if (vehicle.equals(operatedVehicle)) {
                    workingState = WorkingState.WAITING_FOR_REQUEST;
                    operatedVehicle = null;
                    return true;
                } else {
                    return false;
                }
            }


        } else {
            throw new IllegalArgumentException("Vehicle is trying to unload not being in the depot's 'vehicles' list.");
        }
        return false;
    }

    private void tankVehicle(Vehicle vehicle) {

        for (Storage storage : storages) {

            Goods targetGoods = storage.getPayload().findGoodsPile(vehicle.getFuelType(), getOwner());//TODO : Should be vehicle's owner?);
            if (targetGoods != null) {
                
                targetGoods = new Goods(vehicle.getFuelType(), 1, getOwner());
                storage.getPayload().removeGoods(targetGoods);
                vehicle.addFuel(FUEL_EFFICIENCY);
                break;
            }
        }
    }

    public void addVehicle(Vehicle vehicle) {

        vehicles.add(vehicle);
        System.out.println("Fuel : " + vehicle.getFuel());
        if (vehicle.getFuel() < vehicle.getMaxFuel() - FUEL_EFFICIENCY) {
            tankVehicle(vehicle);
        }

    }

    public void removeVehicle(Vehicle vehicle) {

        if (vehicles.contains(vehicle)) {
            vehicles.remove(vehicle);
        } else {
            throw new IllegalArgumentException("Cannot remove vehicle which is not in the depot's 'vehicles' list.");
        }

    }

    public List<Vehicle> getVehicles() {

        return vehicles;
    }

    public Pos getEntrance() {
        Pos point = new Pos(getType().getWidthx() / 2.0 - 1.0, -getType().getWidthy() / 2.0, 0);
        point.add(getPos());
        return point;
    }

    public void addStorage(Storage storage) {

        //System.out.println("" + getPos().distanceTo(storage.getPos()));
        if (getPos().distanceTo(storage.getPos()) <= Building.STORAGE_RADIUS) {
            storages.add(storage);
            Collections.sort(storages, new Comparator<Storage>() {

                public int compare(Storage o1, Storage o2) {
                    double dst1 = getPos().distanceTo(o1.getPos());
                    double dst2 = getPos().distanceTo(o2.getPos());
                    if (o1.equals(o2)) {
                        return 0;
                    } else {
                        if (dst1 == dst2) {
                            return o1.getName().compareTo(o2.getName());
                        } else {
                            if (dst1 < dst2) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }
                }
            });

        } else {

            throw new IllegalArgumentException("Storage is out of STORAGE_RADIUS.");
        }
    }

    public void act() {
        doStep();
    }

    private void doStep() {

        if (WorkingState.WORKING.equals(workingState)) {
            if (progress >= LOADING_SPEED) {
                workingState = WorkingState.DONE;
            } else {
                progress++;
                System.out.println("" + progress);
            }
        }
    }

    public enum WorkingState {

        WORKING,
        WAITING_FOR_REQUEST,
        DONE;
    }
}
