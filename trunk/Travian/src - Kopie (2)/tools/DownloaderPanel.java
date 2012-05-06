package tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Kotuc
 */
public class DownloaderPanel extends JPanel {

    private ParallelGroup horizontalGroup;
    private SequentialGroup verticalGroup;
    
    public DownloaderPanel() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
            horizontalGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        );
        layout.setVerticalGroup(
            verticalGroup = layout.createSequentialGroup().addContainerGap()
        );
        
//        setPreferredSize(new Dimension(400, 300));
        
        JScrollPane spane = new JScrollPane();
        spane.setPreferredSize(new Dimension(400, 300));
        spane.setBackground(Color.BLUE);
        spane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spane.setViewportView(panel);
        
                       
        add(spane);
            
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                repaint();
                for (Component component : panel.getComponents()) {
                    component.repaint();
                }

            }
        }, 500, 500);
    }

    private JPanel panel = new JPanel();
    
    public void add(DownPanel downpanel) {
        panel.add(downpanel, 0);
        horizontalGroup.addComponent(downpanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE);
        verticalGroup.addComponent(downpanel);
    }
    
    public void write(String text) {
        JLabel label = new JLabel(text);
        label.setBackground(Color.YELLOW);
//        panel.add(label, 0);        
        horizontalGroup.addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE);
        verticalGroup.addComponent(label);
    }
    

}
