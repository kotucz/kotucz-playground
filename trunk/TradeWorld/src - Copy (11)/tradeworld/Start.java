/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tradeworld;

import javax.swing.JFrame;
import javax.swing.JPanel;
import tradeworld.gui.MainPanel;

/**
 *
 * @author Koste
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("Trade World");
//        GameMenuPanel panel = new GameMenuPanel();
        MainPanel panel = new MainPanel(null);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        // TODO code application logic here
    }

    public static void changePanel(JPanel oldPanel, JPanel newPanel){
        frame.remove(oldPanel);
        frame.add(newPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
