
/*
 * XOXOPanel.java
 *
 * Created on 5.12.2010, 19:08:15
 */
package xoxox;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author Kotuc
 */
public class XOXOPanel extends javax.swing.JPanel {

    Board board;
    MyAI myai;

    /** Creates new form XOXOPanel */
    public XOXOPanel(Board board) {
        this.board = board;
        initComponents();
        setPreferredSize(new Dimension(20 * 32, 20 * 32));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        System.out.println("Click ");
        int x = evt.getX() / board.gridsize;
        int y = evt.getY() / board.gridsize;
//        if ((evt.getButton()&MouseEvent.BUTTON1)!=0) {
        if ((evt.getButton() == MouseEvent.BUTTON1)) {
            board.apply(new Move(x, y));
        } else if ((evt.getButton() == MouseEvent.BUTTON3)) {
            (myai = new MyAI(board)).play();
        } else if ((evt.getButton() == MouseEvent.BUTTON2)) {
            board.undoMove();
        }
        repaint();
    }//GEN-LAST:event_formMouseClicked

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        board.paint(g);
        if (myai!=null) {
            myai.paint(g);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        Board board = new Board();

        board.load(new BufferedReader(new FileReader(new File("startovni_plan"))));
//        board.load(new BufferedReader(new InputStreamReader(System.in)));

//        board.diags();

//        board.move();

        XOXOPanel xoxoPanel = new XOXOPanel(board);
        JFrame jFrame = new JFrame("Kotuc's XOXO");
        jFrame.add(xoxoPanel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
