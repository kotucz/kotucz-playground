package tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Kotuc
 */
public class DownloaderPanel extends JPanel {
    
    private JPanel panel = new JPanel();
    
    public DownloaderPanel() {
        
        this.setPreferredSize(new Dimension(400, 300));
        this.setLayout(new BorderLayout());
                
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);                       
                
        JScrollPane spane = new JScrollPane();
//        spane.setPreferredSize(new Dimension(400, 300));
        spane.setBackground(Color.BLUE);
        spane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spane.setViewportView(panel);
                       
        add(spane, BorderLayout.CENTER);
            
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
    
    public void add(DownPanel downpanel) {
        panel.add(downpanel, 0);
    }
    
    public void write(String text) {
        JLabel label = new JLabel(text);
        label.setBackground(Color.YELLOW);
        panel.add(label, 0);                
    }
    

}
