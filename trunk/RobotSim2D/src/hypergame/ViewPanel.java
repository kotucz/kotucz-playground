/*
 * ViewPanel.java
 *
 * Created on 10.11.2010, 20:26:12
 */
package hypergame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class ViewPanel extends javax.swing.JPanel {

    private Game game;

    /** Creates new form ViewPanel
     * @param game 
     */
    public ViewPanel(Game game) {
        this.game = game;
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

        setName("Form"); // NOI18N
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        int x = evt.getX();
        int y = evt.getY();
        System.out.println("goto "+x+" "+y);
    }//GEN-LAST:event_formMouseClicked

    @Override
    public void paint(Graphics g) {
        game.paint((Graphics2D)g);
    }

    public static void main(String[] args) {

        final Game game = new Game();

        final ViewPanel viewPanel = new ViewPanel(game);

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(700, 600);
        frame.add(viewPanel);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);


            }
        });
//        frame.addComponentListener(new ComponentAdapter() {
//
//            Point oldloc = null;
//
//            @Override
//            public void componentMoved(ComponentEvent e) {
//                Point nloc = e.getComponent().getLocation();
//                if (oldloc != null) {
//                    float vx = -(nloc.x - oldloc.x) * 0.1f;
//                    float vy = (nloc.y - oldloc.y) * 0.1f;
//                    game.shake(vx, vy);
//                }
//                oldloc = nloc;
//            }
//        });

        new Thread() {

            @Override
            public void run() {
                while (true) {

                    game.update();
                    viewPanel.repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ViewPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
