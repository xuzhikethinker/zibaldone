/*
 * Created 24-May-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.zibaldone.desktop;

import java.awt.Dimension;

/**
 *
 * @author Samuel Halliday
 */
public class Mainscreen extends javax.swing.JFrame {

    /** Creates new form Mainscreen */
    public Mainscreen() {
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

        jToolBar = new javax.swing.JToolBar();
        jButtonRefresh = new javax.swing.JButton();
        jButtonClusters = new javax.swing.JButton();
        jSearchField = new javax.swing.JTextField();
        jCloudButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonSynonyms = new javax.swing.JButton();
        jButtonSources = new javax.swing.JButton();
        noteGraphPanel1 = new uk.me.fommil.zibaldone.desktop.NoteGraphPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);
        jToolBar.setBorderPainted(false);

        jButtonRefresh.setText("Refresh");
        jButtonRefresh.setFocusable(false);
        jButtonRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonRefresh);

        jButtonClusters.setText("Clusters");
        jButtonClusters.setFocusable(false);
        jButtonClusters.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClusters.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonClusters);

        jSearchField.setText("Search");
        jSearchField.setPreferredSize(new java.awt.Dimension(150, 28));
        jSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSearchFieldActionPerformed(evt);
            }
        });
        jToolBar.add(jSearchField);

        jCloudButton.setText("Tags");
        jToolBar.add(jCloudButton);

        jSeparator1.setPreferredSize(new java.awt.Dimension(100, 1));
        jToolBar.add(jSeparator1);

        jButtonSynonyms.setText("Synonyms");
        jToolBar.add(jButtonSynonyms);

        jButtonSources.setText("Sources");
        jButtonSources.setFocusable(false);
        jButtonSources.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSources.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonSources);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jToolBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(noteGraphPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jToolBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(616, Short.MAX_VALUE))
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                    .add(0, 37, Short.MAX_VALUE)
                    .add(noteGraphPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSearchFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Mainscreen().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClusters;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonSources;
    private javax.swing.JButton jButtonSynonyms;
    private javax.swing.JButton jCloudButton;
    private javax.swing.JTextField jSearchField;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar;
    private uk.me.fommil.zibaldone.desktop.NoteGraphPanel noteGraphPanel1;
    // End of variables declaration//GEN-END:variables
}
