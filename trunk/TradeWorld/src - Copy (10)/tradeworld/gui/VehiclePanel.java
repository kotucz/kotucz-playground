/*
 * VehiclePanel.java
 *
 * Created on 6.10.2009, 20:38:45
 */
package tradeworld.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.vecmath.Point3d;
import tradeworld.Goods;
import tradeworld.Player;
import tradeworld.Truck;
import tradeworld.Vehicle;

/**
 *
 * @author Petr Dluhoš
 */
public class VehiclePanel extends SuperPanel {

    /**
     * Test main method
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Vehicle Panel");


        Player player1 = new Player("Petr", null, 0, null);
        Player player2 = new Player("Joan", null, 0, null);

        Truck truck = new Truck(player1, Vehicle.Type.STEAM_TRUCK, new Point3d());
        truck.getPayload().addGoods(new Goods(Goods.Type.FURNITURE, 3, player1));
        truck.getPayload().addGoods(new Goods(Goods.Type.WOOD, 1, player1));
        truck.getPayload().addGoods(new Goods(Goods.Type.WOOD, 1, player2));


        VehiclePanel panel = new VehiclePanel(truck);

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    private final Vehicle vehicle;
    private final PayloadPanel payloadPanel;

    /** Creates new form VehiclePanel */
    public VehiclePanel(Vehicle vehicle) {
        initComponents();
        this.vehicle = vehicle;
        this.payloadPanel = new PayloadPanel(vehicle.getPayload());
        this.add(payloadPanel, BorderLayout.CENTER);
        update();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void update() {

        setName(vehicle.toString());
        payloadPanel.update();

//        jListPayload.removeAll();
//
//        jListPayload.setModel(new javax.swing.AbstractListModel() {
//
//            public int getSize() {
//                return vehicle.getPayload().getNumberOfGoodsTypes();
//            }
//
//            public Object getElementAt(int i) {
//                Goods goods = vehicle.getPayload().getGoodsAt(i);
//                return (goods.getType().toString() + " " + goods.getQuantity() + " (" + goods.getOwner().getName() + ")");
//            }
//        });


    }
}
