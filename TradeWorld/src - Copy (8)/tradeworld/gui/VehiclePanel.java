/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VehiclePanel.java
 *
 * Created on 6.10.2009, 20:38:45
 */
package tradeworld.gui;

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
public class VehiclePanel extends javax.swing.JPanel {

    /**
     * Test main method
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Vehicle Panel");

        VehiclePanel panel = new VehiclePanel();
        Player player1 = new Player("Petr", null, 0, null);
        Player player2 = new Player("Joan", null, 0, null);

        Truck truck = new Truck(player1, Vehicle.Type.STEAM_TRUCK, new Point3d());
        truck.getPayload().addGoods(new Goods(Goods.Type.FURNITURE, 3, player1));
        truck.getPayload().addGoods(new Goods(Goods.Type.WOOD, 1, player1));
        truck.getPayload().addGoods(new Goods(Goods.Type.WOOD, 1, player2));

        panel.setVehicle(truck);
        panel.update();

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    /** Creates new form VehiclePanel */
    public VehiclePanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListPayload = new javax.swing.JList();

        jLabelName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelName.setText("jLabelName");

        jListPayload.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jListPayload);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelName;
    private javax.swing.JList jListPayload;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    private Vehicle vehicle;

    public void setVehicle(Vehicle vehicle) {

        this.vehicle = vehicle;
    }

    public void update() {

        jLabelName.setText(vehicle.getName() + " (" + vehicle.getOwner().getName() + ")");

        jListPayload.removeAll();

        jListPayload.setModel(new javax.swing.AbstractListModel() {

            public int getSize() {
                return vehicle.getPayload().getNumberOfGoodsTypes();
            }

            public Object getElementAt(int i) {
                Goods goods = vehicle.getPayload().getGoodsAt(i);
                return (goods.getType().toString() + " " + goods.getQuantity() + " (" + goods.getOwner().getName() + ")");
            }
        });


    }
}
