/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BuildingsPanel.java
 *
 * Created on 11.10.2009, 3:01:26
 */

package tradeworld.gui;

/**
 *
 * @author Koste
 */
public class BuildingsPanel extends javax.swing.JPanel {

    /** Creates new form BuildingsPanel */
    public BuildingsPanel() {
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

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/buildings.png"))); // NOI18N
        jButton1.setText("Lumberjack camp");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/buildings.png"))); // NOI18N
        jButton2.setText("Woodmill");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/buildings.png"))); // NOI18N
        jButton3.setText("Furniture factory");
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/buildings.png"))); // NOI18N
        jButton4.setText("Storage");
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(137, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    // End of variables declaration//GEN-END:variables

}
