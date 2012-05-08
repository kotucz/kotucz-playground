package tradeworld.graphics;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
class VehicleThread implements Runnable {

    private List<Vehicle> vehicles = new LinkedList<Vehicle>();
    

    public VehicleThread() {
    }

    public void run() {
        while (true) {
            for (Vehicle vehicle : vehicles) {
                if (!vehicle.navigate(vehicle.dest, 0.01)) {
                    vehicle.refresh();
                } else {
                    vehicle.dest.x = Math.random() * 20;
                    vehicle.dest.y = Math.random() * 20;
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(VehicleThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void add(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

}
