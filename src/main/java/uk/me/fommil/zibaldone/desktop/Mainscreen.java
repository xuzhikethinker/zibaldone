/*
 * Created 24-May-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.zibaldone.desktop;

import uk.me.fommil.zibaldone.control.GraphController;
import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorManager;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.persistence.EntityManagerFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import lombok.BoundSetter;
import lombok.extern.java.Log;
import org.jdesktop.swingx.combobox.MapComboBoxModel;
import uk.me.fommil.beans.editors.DatePropertyEditor;
import uk.me.fommil.beans.editors.FilePropertyEditor;
import uk.me.fommil.persistence.CrudDao;
import uk.me.fommil.swing.SwingConvenience;
import uk.me.fommil.zibaldone.Importer;
import uk.me.fommil.zibaldone.control.BunchController;
import uk.me.fommil.zibaldone.control.ImporterController;
import uk.me.fommil.zibaldone.control.Settings;
import uk.me.fommil.zibaldone.control.TagController;

/**
 * @author Samuel Halliday
 */
@Log
public final class Mainscreen extends JFrame implements PropertyChangeListener {

    public static void main(String args[]) throws Exception {
        PropertyEditorManager.registerEditor(File.class, FilePropertyEditor.class);
        PropertyEditorManager.registerEditor(Date.class, DatePropertyEditor.class);

        EntityManagerFactory emf = CrudDao.createEntityManagerFactory("ZibaldonePU");

        Settings settings = Settings.loadAutoSavingInstance(new File("zibaldone.xml"));

        GraphController graphController = new GraphController(emf, settings);
        TagController tagController = new TagController(settings);
        BunchController bunchController = new BunchController(emf, settings);
        ImporterController importerController = new ImporterController(emf, settings);
        importerController.addTagListener(graphController);
        importerController.addNoteListener(graphController);
        tagController.addTagListener(graphController);

        // TODO: Swing-based Log capture and view
        Mainscreen main = new Mainscreen();
        main.setTagController(tagController);
        main.setGraphController(graphController);
        main.setBunchController(bunchController);
        main.setImporterController(importerController);
        main.setSettings(settings);
        main.setVisible(true);

        importerController.loadDb();
        bunchController.loadDb();

        // TODO: JXTaskPaneContainer Layout http://netbeans.org/bugzilla/show_bug.cgi?id=215528
        // TODO: add the 'null' importer        
        // TODO: animated settings panel
        // TODO: icons for the toolbar buttons
        // TODO: menu entries
        // TODO: use simplericity for a better OS X experience
    }

    @BoundSetter
    private GraphController graphController;

    @BoundSetter
    private TagController tagController;

    @BoundSetter
    private BunchController bunchController;

    @BoundSetter
    private ImporterController importerController;

    @BoundSetter
    private Settings settings;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Preconditions.checkNotNull(evt);
        String property = evt.getPropertyName();
        log.fine("Changed " + property);
        // TODO: remove older listeners
        if ("graphController".equals(property)) {
            graphController.addClusterListener(jungGraphView);
            jungGraphView.setGraph(graphController.getGraph());
        } else if ("tagController".equals(property)) {
            tagController.addTagListener(tagSelectView);
            tagSelectView.setTagController(tagController);
        } else if ("bunchController".equals(property)) {
            bunchController.addBunchListener(jungGraphView);
            bunchController.addBunchListener(bunchMenu);
            jungGraphView.setBunchController(bunchController);
            bunchMenu.setBunchController(bunchController);
        } else if ("importerController".equals(property)) {
            importerController.addTagListener(tagSelectView);
            // TODO: dynamic lookup of available importers
            Map<String, Class<Importer>> importers = importerController.getImporterImplementations();
            ComboBoxModel importerChoices = new MapComboBoxModel<String, Class<Importer>>(importers);
            importerSelector.setModel(importerChoices);
        } else if ("settings".equals(property)) {
            for (UUID uuid : settings.getImporters().keySet()) {
                addImporter(uuid, true);
            }
            search.setText(settings.getSearch());
        }
    }

    public Mainscreen() {
        super();
        rootPane.putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
        initComponents();
        settingsPanel.setVisible(false);
        addPropertyChangeListener(this);
    }

    private void addImporter(UUID uuid, boolean used) {
        ImporterView importerView = new ImporterView();
        importerView.setImporterController(importerController);
        importerView.setUuid(uuid);
        importerView.setCollapsed(used);
        importersPanel.add(importerView);
        importersPanel.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tagDialog = new javax.swing.JDialog();
        tagSelectView = new uk.me.fommil.zibaldone.desktop.TagsView();
        bunchMenu = new uk.me.fommil.zibaldone.desktop.BunchMenu();
        javax.swing.JToolBar jToolBar = new javax.swing.JToolBar();
        search = new org.jdesktop.swingx.JXSearchField();
        javax.swing.JButton tagsButton = new javax.swing.JButton();
        bunchesButton = new javax.swing.JButton();
        javax.swing.JToggleButton jButtonLayout = new javax.swing.JToggleButton();
        javax.swing.Box.Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(1000, 0));
        settingsButton = new javax.swing.JToggleButton();
        jungGraphView = new uk.me.fommil.zibaldone.desktop.JungGraphView();
        settingsPanel = new javax.swing.JTabbedPane();
        javax.swing.JPanel jImportersPanel = new javax.swing.JPanel();
        javax.swing.JToolBar jToolBar1 = new javax.swing.JToolBar();
        org.jdesktop.swingx.JXButton jAddImporterButton = new org.jdesktop.swingx.JXButton();
        importerSelector = new javax.swing.JComboBox();
        javax.swing.Box.Filler filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(500, 32767));
        org.jdesktop.swingx.JXButton jReloadImportersButton = new org.jdesktop.swingx.JXButton();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        importersPanel = new org.jdesktop.swingx.JXTaskPaneContainer();
        javax.swing.JPanel jAdvancedPanel = new javax.swing.JPanel();
        javax.swing.JPanel jSynonymsPanel = new javax.swing.JPanel();

        tagDialog.setTitle("Tags");
        tagDialog.setAlwaysOnTop(true);
        tagDialog.setMinimumSize(new java.awt.Dimension(300, 300));
        tagDialog.setResizable(false);

        tagSelectView.setSelectable(true);
        tagDialog.getContentPane().add(tagSelectView, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Zibaldone");
        setMinimumSize(new java.awt.Dimension(900, 600));
        setSize(new java.awt.Dimension(800, 600));

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);
        jToolBar.setPreferredSize(new java.awt.Dimension(480, 42));

        search.setMaximumSize(new java.awt.Dimension(1000, 2147483647));
        search.setMinimumSize(new java.awt.Dimension(100, 28));
        search.setPrompt("Search titles, tags and contents");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        jToolBar.add(search);

        tagsButton.setText("Tags");
        tagsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagsButtonActionPerformed(evt);
            }
        });
        jToolBar.add(tagsButton);

        bunchesButton.setText("Bunches");
        bunchesButton.setFocusable(false);
        bunchesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bunchesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bunchesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bunchesButtonActionPerformed(evt);
            }
        });
        jToolBar.add(bunchesButton);

        jButtonLayout.setText("Relators");
        jButtonLayout.setFocusable(false);
        jButtonLayout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLayout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonLayout);
        jToolBar.add(filler1);

        settingsButton.setText("Settings");
        settingsButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                settingsButtonStateChanged(evt);
            }
        });
        jToolBar.add(settingsButton);

        getContentPane().add(jToolBar, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(jungGraphView, java.awt.BorderLayout.CENTER);

        settingsPanel.setPreferredSize(new java.awt.Dimension(320, 480));

        jImportersPanel.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);

        jAddImporterButton.setText("+");
        jAddImporterButton.setFocusable(false);
        jAddImporterButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jAddImporterButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jAddImporterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddImporterButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(jAddImporterButton);
        jToolBar1.add(importerSelector);
        jToolBar1.add(filler2);

        jReloadImportersButton.setText("Reload All");
        jReloadImportersButton.setFocusable(false);
        jReloadImportersButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jReloadImportersButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jReloadImportersButton);

        jImportersPanel.add(jToolBar1, java.awt.BorderLayout.SOUTH);

        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportView(null);
        jScrollPane1.setViewportView(importersPanel);

        jImportersPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        settingsPanel.addTab("Importers", jImportersPanel);

        jAdvancedPanel.setLayout(new java.awt.BorderLayout());
        settingsPanel.addTab("Relators", jAdvancedPanel);

        jSynonymsPanel.setLayout(new java.awt.BorderLayout());
        settingsPanel.addTab("Synonyms", jSynonymsPanel);

        getContentPane().add(settingsPanel, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void settingsButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_settingsButtonStateChanged
        settingsPanel.setVisible(settingsButton.isSelected());
    }//GEN-LAST:event_settingsButtonStateChanged

    @SuppressWarnings("unchecked")
    private void jAddImporterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddImporterButtonActionPerformed
        String name = (String) importerSelector.getSelectedItem();
        Entry<UUID, Importer> pair = importerController.newImporter(name);
        settings.getImporters().put(pair.getKey(), pair.getValue());
        addImporter(pair.getKey(), false);
    }//GEN-LAST:event_jAddImporterButtonActionPerformed

    private void tagsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagsButtonActionPerformed
        tagDialog.setVisible(true);
    }//GEN-LAST:event_tagsButtonActionPerformed

    private void bunchesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bunchesButtonActionPerformed
        SwingConvenience.popupAtMouse(bunchMenu, this);
    }//GEN-LAST:event_bunchesButtonActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        graphController.searchChanged(search.getText());
    }//GEN-LAST:event_searchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    uk.me.fommil.zibaldone.desktop.BunchMenu bunchMenu;
    javax.swing.JButton bunchesButton;
    javax.swing.JComboBox importerSelector;
    org.jdesktop.swingx.JXTaskPaneContainer importersPanel;
    uk.me.fommil.zibaldone.desktop.JungGraphView jungGraphView;
    org.jdesktop.swingx.JXSearchField search;
    javax.swing.JToggleButton settingsButton;
    javax.swing.JTabbedPane settingsPanel;
    javax.swing.JDialog tagDialog;
    uk.me.fommil.zibaldone.desktop.TagsView tagSelectView;
    // End of variables declaration//GEN-END:variables
}
