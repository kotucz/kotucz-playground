/*
 * MainFrame.java
 *
 * Created on 27. cerven 2006, 22:33
 */
package tluda;

/**
 *
 * @author  PC
 */
public class MainFrame extends javax.swing.JFrame implements Runnable{
    
    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jFileChooser1 = new javax.swing.JFileChooser();
        jButton1 = new javax.swing.JButton();
        textArea1 = new java.awt.TextArea();
        jButton2 = new javax.swing.JButton();
        progressBar1 = new javax.swing.JProgressBar();
        filenameField = new javax.swing.JTextField();
        fileAddressField = new javax.swing.JTextField();
        addressField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("tludA Downloader");
        jButton1.setText("find link");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Download vid");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        filenameField.setText("D:\\serials\\");

            fileAddressField.setText("adresa videa");

            addressField.setText("http://www.mia-movies.com/");

            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancelButtonActionPerformed(evt);
                }
            });

            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(filenameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                        .add(textArea1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                            .add(progressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(cancelButton))
                        .add(layout.createSequentialGroup()
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(fileAddressField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, addressField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButton1)
                        .add(addressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButton2)
                        .add(fileAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(filenameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(textArea1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(cancelButton)
                        .add(progressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );
            pack();
        }// </editor-fold>//GEN-END:initComponents

    private boolean canceled = false;
    
//    private void setCanceled(boolean flag) {
//        
//    }
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        canceled = true;
        cancelButton.setEnabled(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new Thread () {
            public void run() {
                canceled = false;
                cancelButton.setEnabled(true);
                downloader.downloadFile(fileAddressField.getText(), filenameField.getText());
            }
        }.start();
            
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        String content = downloader.loadPage(addressField.getText());
        
        String pageLink = downloader.findLinkToPage(addressField.getText(), content);
        
        println("required link: >"+pageLink+"<");
        
        addressField.setText(pageLink);
        
        content = downloader.loadPage(pageLink);
        
        String vidLink = downloader.findLinkToVid(content);
        
        println("movie link: >"+vidLink+"<");
        
        fileAddressField.setText(vidLink);
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    public void run () {
        
        println("Downloadin process will start in one minute...");
        
        println("\nto abort it press cancel in lower right corner");
        
        for (int i = 0; i<100; i++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            if (canceled) {
                println("\nPROCESS CANCELED");
                setProgress(0);
                return;
            }
            
            setProgress(i);
        }

        String[] addrs = {
            "http://www.mia-movies.com/",
            "http://www.slutsvideos.com/",
            "http://www.nastyalien.com/",
            "http://www.hornytiger.com/",
            "http://www.whorevideos.com/"
        };

        for (int i = 0; i<addrs.length; i++) {
            println("downloading form "+addrs[i]+":");
            
            if (canceled) {
                println("\nPROCESS CANCELED");
                setProgress(0);
                return;
            };
            
            String content = downloader.loadPage(addrs[i]);

            String pageLink = downloader.findLinkToPage(addrs[i], content);

            println("required link: >"+pageLink+"<");

            content = downloader.loadPage(pageLink);

            String vidLink = downloader.findLinkToVid(content);

            println("movie link: >"+vidLink+"<\n");

            if (canceled) {
                println("\nPROCESS CANCELED");
                setProgress(0);
                return;
            };
            
            downloader.downloadFile(vidLink, filenameField.getText());

            println(vidLink+" downloaded successfully");
            
            println("-------------------------------------------\n");
            
        }

        println("DOWNLOAD FINISHED SUUCCESSFULLY");
        println("program will exit");
        
        terminate();
        
    }
    
    private void terminate() {
        setVisible(false);
        System.exit(0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame mf = new MainFrame(); 
                mf.setVisible(true);
                new Thread(mf).start();
            }
        });
        
        
    }
    
    
    public void println(String s) {
        textArea1.append(s+"\n");
    }
    
    Downloader downloader = new Downloader();
    
    public void setProgress(int i) {
       progressBar1.setValue(i);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField fileAddressField;
    private javax.swing.JTextField filenameField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JProgressBar progressBar1;
    private java.awt.TextArea textArea1;
    // End of variables declaration//GEN-END:variables
    
}
