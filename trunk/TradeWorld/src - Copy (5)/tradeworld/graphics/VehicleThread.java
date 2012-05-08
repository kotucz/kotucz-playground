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

    private List<Vehicle3D> vehicles = new LinkedList<Vehicle3D>();
    
    public VehicleThread() {
    }

    public void run() {
        while (true) {
            for (Vehicle3D vehicle : vehicles) {
                vehicle.act();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(VehicleThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void add(Vehicle3D vehicle) {
        vehicles.add(vehicle);
    }

}
