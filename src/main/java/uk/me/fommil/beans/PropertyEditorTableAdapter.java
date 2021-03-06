/*
 * Created 01-Jun-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.beans;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Adapts a {@link PropertyEditor} into {@link TableCellEditor} and {@link TableCellRenderer}.
 * 
 * @author Samuel Halliday
 */
public class PropertyEditorTableAdapter extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private final PropertyEditor editor;

    /**
     * @param klass
     * @return
     */
    public static PropertyEditorTableAdapter forClass(Class<?> klass) {
        PropertyEditor editor = PropertyEditorManager.findEditor(klass);
        if (editor == null || !editor.supportsCustomEditor()) {
            return null;
        }
        return new PropertyEditorTableAdapter(editor);
    }

    /**
     * @param editor
     */
    public PropertyEditorTableAdapter(PropertyEditor editor) {
        super();
        this.editor = Preconditions.checkNotNull(editor);
        Preconditions.checkArgument(editor.supportsCustomEditor());
        editor.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editor.setValue(value);
        Component editorComponent = editor.getCustomEditor();
        Preconditions.checkState(editorComponent != null);
        return editorComponent;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    @Override
    public Object getCellEditorValue() {
        return editor.getValue();
    }
}