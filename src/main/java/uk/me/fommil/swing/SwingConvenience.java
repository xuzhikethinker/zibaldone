/*
 * Created 27-Jun-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.swing;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import java.awt.Component;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.StatUtils;
import uk.me.fommil.zibaldone.desktop.Mainscreen;

/**
 * Convenience methods for Swing UIs.
 * 
 * @author Samuel Halliday
 */
@Log
public final class SwingConvenience {

    /**
     * Much cleaner API than {@link JOptionPane#showMessageDialog(Component, Object, String, int)}.
     *
     * @param parent
     * @param warning
     */
    public static void warning(@Nullable final Component parent, final String warning) {
        Preconditions.checkNotNull(warning);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(parent, warning, "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Packs and then shows a popup menu at the mouse location, contained
     * within a Component.
     * 
     * @param popup
     * @param owner
     */
    public static void popupAtMouse(JPopupMenu popup, Component owner) {
        Preconditions.checkNotNull(popup);
        Preconditions.checkNotNull(owner);
        popup.pack();
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouse, owner);
        // http://stackoverflow.com/questions/766956
        popup.show(owner, mouse.x, mouse.y);
    }

    /**
     * @param title
     * @param component
     * @param modal
     * @param listener
     * @param parent  
     */
    public static void showAsDialog(String title, Component component, boolean modal, Component parent, @Nullable WindowListener listener) {
        JDialog dialog = new JDialog(findParentFrame(parent));
        dialog.setTitle(Preconditions.checkNotNull(title));
        dialog.setModal(modal);
        dialog.add(Preconditions.checkNotNull(component));
        if (listener != null) {
            dialog.addWindowListener(listener);
        }
        dialog.pack();

        relocateDialogAtMouse(dialog);

        dialog.setVisible(true);
    }

    /**
     * @param dialog
     */
    public static void relocateDialogAtMouse(JDialog dialog) {
        Preconditions.checkNotNull(dialog);
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        dialog.setLocation(mouse);
    }

    /**
     * @param points
     * @return the average of the given points
     */
    public static Point2D average(Collection<Point2D> points) {
        Preconditions.checkNotNull(points);
        Preconditions.checkArgument(!points.isEmpty());

        List<Double> xs = Lists.newArrayList();
        List<Double> ys = Lists.newArrayList();

        for (Point2D point : points) {
            xs.add(point.getX());
            ys.add(point.getY());
        }
        double x = StatUtils.mean(Doubles.toArray(xs));
        double y = StatUtils.mean(Doubles.toArray(ys));
        return new Point2D.Double(x, y);
    }

    /**
     * @param window
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void enableOSXFullscreen(Window window) {
        Preconditions.checkNotNull(window);
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (ClassNotFoundException e1) {
        } catch (Exception e) {
            log.log(Level.WARNING, "OS X Fullscreen FAIL", e);
        }
    }

    /**
     * @param component
     * @return
     */
    public static Frame findParentFrame(@Nullable Component component) {
        if (component == null || component instanceof Frame) {
            if (component != null) {
                log.info("parent was " + component.getName());
            }
            return (Frame) component;
        }
        return findParentFrame(component.getParent());
    }

    /**
     * @param parent
     */
    public static void setUncaughtExceptionHandlerPopup(final JFrame parent) {
        Preconditions.checkNotNull(parent);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                JOptionPane.showMessageDialog(
                        parent, "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
