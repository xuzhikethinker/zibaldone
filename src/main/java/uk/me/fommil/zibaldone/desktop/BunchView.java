/*
 * Created 13-Jul-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.zibaldone.desktop;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import lombok.extern.java.Log;
import org.jdesktop.swingx.combobox.MapComboBoxModel;
import uk.me.fommil.swing.SwingConvenience;
import uk.me.fommil.zibaldone.Bunch;
import uk.me.fommil.zibaldone.Note;
import uk.me.fommil.zibaldone.Tag;

/**
 * The underlying Bunch is not edited until getBunch is called.
 * 
 * @author Samuel Halliday
 */
@Log
public class BunchView extends javax.swing.JPanel {

    private Bunch bunch;

    public final Collection<Note> removed = Lists.newArrayList();

    public BunchView() {
        initComponents();
        tags.setWidth(400);
    }

    /**
     * @return the bunch with fields updated by the user input
     */
    public Bunch getBunch() {
        Preconditions.checkState(bunch != null);

        bunch.setTitle(name.getText());
        bunch.setContents(content.getText());

        bunch.getNotes().removeAll(removed);
        removed.clear();

        return bunch;
    }

    /**
     * @param bunch
     */
    public void setBunch(final Bunch bunch) {
        this.bunch = Preconditions.checkNotNull(bunch);

        name.setText(bunch.getTitle());
        content.setText(bunch.getContents());

        updateTagsAndNotes();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popup = new javax.swing.JPopupMenu();
        name = new javax.swing.JTextField();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        tags = new uk.me.fommil.zibaldone.desktop.TagsView();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        content = new javax.swing.JEditorPane();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        notes = new javax.swing.JList();
        javax.swing.JToolBar jToolBar1 = new javax.swing.JToolBar();
        javax.swing.JButton removeNoteButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        name.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        name.setText("Bunch Name");
        name.setBorder(null);
        add(name, java.awt.BorderLayout.PAGE_START);

        jPanel1.setMaximumSize(new java.awt.Dimension(800, 2147483647));
        jPanel1.setLayout(new java.awt.BorderLayout());

        tags.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jPanel2.add(tags);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 300));

        content.setBorder(javax.swing.BorderFactory.createTitledBorder("Comments"));
        jScrollPane1.setViewportView(content);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Notes"));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setMaximumSize(new java.awt.Dimension(250, 32767));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(100, 200));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(250, 200));

        notes.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        notes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(notes);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        removeNoteButton.setText("-");
        removeNoteButton.setFocusable(false);
        removeNoteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeNoteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        removeNoteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeNoteButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(removeNoteButton);

        jPanel3.add(jToolBar1, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel3, java.awt.BorderLayout.EAST);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void notesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_notesMouseClicked
        @SuppressWarnings("unchecked")
        MapComboBoxModel<String, Note> model = (MapComboBoxModel<String, Note>) notes.getModel();
        Object[] selected = notes.getSelectedValues();
        popup.removeAll();
        for (Object object : selected) {
            Note note = model.getValue(object);
            NoteView view = new NoteView();
            view.setNote(note);
            popup.add(view);
        }
        if (selected.length > 0) {
            SwingConvenience.popupAtMouse(popup, this);
        }
    }//GEN-LAST:event_notesMouseClicked

    private void removeNoteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeNoteButtonActionPerformed

        Object[] selected = notes.getSelectedValues();
        @SuppressWarnings("unchecked")
        MapComboBoxModel<String, Note> model = (MapComboBoxModel<String, Note>) notes.getModel();

        for (Object object : selected) {
            Note note = model.getValue(object);
            removed.add(note);
        }

        updateTagsAndNotes();
    }//GEN-LAST:event_removeNoteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JEditorPane content;
    javax.swing.JTextField name;
    javax.swing.JList notes;
    javax.swing.JPopupMenu popup;
    uk.me.fommil.zibaldone.desktop.TagsView tags;
    // End of variables declaration//GEN-END:variables

    private void updateTagsAndNotes() {
        Set<Tag> currentTags = tags.getTags();

        Map<String, Note> noteMap = Maps.newTreeMap();
        Set<Tag> allTags = Sets.newTreeSet();
        for (Note note : bunch.getNotes()) {
            if (removed.contains(note)) {
                continue;
            }
            noteMap.put(note.getTitle(), note);
            allTags.addAll(note.getTags());
        }

        tags.tagsRemoved(Sets.difference(currentTags, allTags));
        tags.tagsAdded(allTags);
        notes.setModel(new MapComboBoxModel<String, Note>(noteMap));
        notes.revalidate();
        notes.repaint();
    }
}
