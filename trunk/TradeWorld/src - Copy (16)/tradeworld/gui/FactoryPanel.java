/*
 * FactoryPanel.java
 *
 * Created on 5.10.2009, 22:01:50
 */
package tradeworld.gui;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import tradeworld.Building;
import tradeworld.Factory;
import tradeworld.Player;
import tradeworld.chains.ProductionChain;

/**
 *
 * @author Petr Dluhoš
 */
public class FactoryPanel extends SuperPanel {

    /**
     * Test main method
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Factory frame");

        FactoryPanel panel = new FactoryPanel(new Factory(Building.Type.WOODMILL, new Player("Petr", null, 0, null), null));

        panel.update();

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    private final Factory factory;

    /** Creates new form FactoryPanel
     * @param factory
     */
    public FactoryPanel(Factory factory) {
        initComponents();
        this.factory = factory;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jListProductionChains = new javax.swing.JList();

        jListProductionChains.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jListProductionChains);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jListProductionChains;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public void update() {

        this.setName(factory.toString());

//        jListProductionChains.removeAll();
        DefaultListModel defaultListModel = new DefaultListModel();
        for (ProductionChain productionChain : factory.getProductionChains()) {
            defaultListModel.addElement(productionChain);
        }
        jListProductionChains.setModel(defaultListModel);

//        jListProductionChains.setModel(new javax.swing.AbstractListModel() {
//
//            public int getSize() {
//                return factory.getProductionChains().size();
//            }
//
//            public Object getElementAt(int i) {
//                return factory.getProductionChains().toArray()[i];
//            }
//        });


    }
}