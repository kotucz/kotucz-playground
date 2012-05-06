/*
 * DownPanel.java
 *
 * Created on 20. prosinec 2007, 14:03
 */
package tools;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;

/**
 *
 * @author  Kotuc
 */
public class DownPanel extends javax.swing.JPanel {

    private Download download;

    /** Creates new form DownPanel
     * @param download 
     */
    public DownPanel(Download download) {

        if (download == null) {
            throw new NullPointerException("download");
        }

        this.download = download;

        myInitComponents();
//        initComponents();
    }

    private void myInitComponents() {

        setBorder(new BevelBorder(BevelBorder.RAISED));

        setLayout(new BorderLayout());

        progressBar = new JProgressBar();
        fileLabel = new JLabel();
        progressLabel = new JLabel();

        add(fileLabel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.CENTER);
        add(progressLabel, BorderLayout.SOUTH);

        JButton butt = new JButton("detail");
        butt.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                showDetails();
            }

        });
        add(butt, BorderLayout.EAST);
    }
    private static JFrame jFrame = new JFrame("Test");
    private static DownloadInfoPanel dip = new DownloadInfoPanel();

    static {
        jFrame.add(dip);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void showDetails() {
        dip.show(download);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    static String units(long dataSize) {
        int tire = 0;
//        dataSize/=8;
        while (dataSize > 20000) {
            dataSize /= 1024;
            tire++;
        }
        String[] marks = new String[]{"B", "KB", "MB", "GB", "TB"};
        return "" + dataSize + marks[tire];
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        fileLabel = new javax.swing.JLabel();
        progressLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());
        add(progressBar, java.awt.BorderLayout.CENTER);

        fileLabel.setText("jLabel1");
        add(fileLabel, java.awt.BorderLayout.PAGE_START);

        progressLabel.setText("jLabel1");
        add(progressLabel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void repaint() {
        if (progressBar != null) {
            progressBar.setValue(download.getProgress());
        }
        if (fileLabel != null) {
            fileLabel.setText(download.getTargetFile().getName());
        }
        if (progressLabel != null) {
            if (download.completed()) {
                progressLabel.setText("" + units(download.getTotalSize()) + " completed");
            } else {
                progressLabel.setText("" + units(download.getCurrentSize()) + " of " + units(download.getTotalSize())
                        + "; speed: " + units(download.getSpeedBytesPerSecond()) + "/s");
            }
        }
        super.repaint();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fileLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    // End of variables declaration//GEN-END:variables
}
