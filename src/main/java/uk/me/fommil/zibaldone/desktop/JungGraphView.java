/*
 * Created 24-May-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.zibaldone.desktop;

import uk.me.fommil.zibaldone.control.JungMainController;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.layout.ObservableCachingLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import lombok.Setter;
import lombok.extern.java.Log;
import uk.me.fommil.utils.Convenience;
import uk.me.fommil.utils.Convenience.Loop;
import uk.me.fommil.zibaldone.Bunch;
import uk.me.fommil.zibaldone.Note;
import uk.me.fommil.zibaldone.control.Listeners.ClusterId;
import uk.me.fommil.zibaldone.control.Listeners.ClustersChangedListener;
import uk.me.fommil.zibaldone.control.Weight;

/**
 * Draws the network graph of the {@link Note}s and {@link Bunch}s
 * using JUNG.
 * 
 * @see JungMainController
 * @author Samuel Halliday
 */
@Log
public class JungGraphView extends JPanel implements ClustersChangedListener {

    private final VisualizationViewer<Note, Weight> graphVisualiser;

    @Setter
    private JungMainController controller;

    // TODO: make a JDialog which can be resized
    private final JPopupMenu popup = new JPopupMenu();

    /**
     * The JUNG mouse handling framework was developed
     * to allow different plugins to function under different user-selected
     * "modes", which is detrimental to the Zibaldone user experience.
     * <p>
     * Here we go back to the highest abstraction in the JUNG mouse handling
     * logic and provide a lightweight "modeless" experience that should be
     * feasible to extend to provide multi-touch support.
     * <p>
     * http://stackoverflow.com/questions/369301
     * TODO: touchpad swipe gestures = scrolling up/down/left/right
     * TODO: touchpad pinching = zoom
     */
    private class ModelessGraphMouse extends PluggableGraphMouse {

        private final GraphMousePlugin picker = new PickingGraphMousePlugin<Note, Weight>();

        private int pickedBefore;

        public ModelessGraphMouse() {
            add(picker);
        }

//        private Note lastMouseOverNote;
//
//        @Override
//        public void mouseMoved(MouseEvent e) {
//            super.mouseMoved(e);
//
//            // TODO: require a mouse click for touchscreens
//            // HACK: this essentially implements mouseEntered/mouseExited on vertices
//            Note note = graphVisualiser.getPickSupport().getVertex(getGraphLayout(), e.getX(), e.getY());
//            if (note != lastMouseOverNote) {
//                lastMouseOverNote = note;
//                if (note != null) {
//                    JungGraphView.this.mouseEntered(note);
//                } else {
//                    JungGraphView.this.mouseExited(note);
//                }
//            }
//        }
        @Override
        public void mousePressed(MouseEvent e) {
            pickedBefore = graphVisualiser.getPickedVertexState().getPicked().hashCode();
            super.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            Set<Note> picked = graphVisualiser.getPickedVertexState().getPicked();

            if (!picked.isEmpty() && picked.hashCode() != pickedBefore) {
                selectNotes(picked);
            }
        }
    }

    public JungGraphView() {
        super(new BorderLayout());
        // the JUNG API needs a Graph instance to instantiate many visual objects
        Graph<Note, Weight> dummy = new UndirectedSparseGraph<Note, Weight>();

        // TODO: often get relaxEdges exceptions when using SpringLayout

        Layout<Note, Weight> delegateLayout = new FRLayout<Note, Weight>(dummy);
//        Layout<Note, Weight> delegateLayout = new SpringLayout<Note, Weight>(dummy, Weight.TRANSFORMER);
        Layout<Note, Weight> graphLayout = new AggregateLayout<Note, Weight>(delegateLayout);
        graphVisualiser = new VisualizationViewer<Note, Weight>(graphLayout);
        graphVisualiser.setBackground(Color.WHITE);

        // TODO: don't draw edges (removes problem of edge selection)

        // TODO: custom vertex icon, not painted circle
        graphVisualiser.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<Note>(
                graphVisualiser.getPickedVertexState(),
                Color.red, Color.yellow));

        graphVisualiser.setGraphMouse(new ModelessGraphMouse());

        // TODO: popup Note/Bunch Component, not tooltip
//        graphVisualiser.setVertexToolTipTransformer(new ToStringLabeller<Note>());

        add(graphVisualiser, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                graphVisualiser.setSize(getSize());
                getGraphLayout().setSize(getSize());
                
                // TODO: reposition clusters on window resize
            }
        });
    }

    /**
     * @param graph 
     */
    public void setGraph(ObservableGraph<Note, Weight> graph) {
        // TODO: should this not be set from the controller?
        Preconditions.checkNotNull(graph);
        //        this.graph.removeGraphEventListener(this);
        getGraphLayout().setGraph(graph);
        //        graph.addGraphEventListener(this);
    }

    private AggregateLayout<Note, Weight> getGraphLayout() {
        ObservableCachingLayout<Note, Weight> layout = (ObservableCachingLayout<Note, Weight>) graphVisualiser.getGraphLayout();
        return (AggregateLayout<Note, Weight>) layout.getDelegate();
    }

    private Graph<Note, Weight> subGraph(Set<Note> cluster) {
        // the GraphCollapser supposedly provides this functionality, but is unreliable
        // Graph<Note, Weight> graph = graphVisualiser.getGraphLayout().getGraph();
        // GraphCollapser collapser = new GraphCollapser(graph);
        // return collapser.getClusterGraph(graph, cluster);
        final Graph<Note, Weight> graph = graphVisualiser.getGraphLayout().getGraph();
        final UndirectedSparseGraph<Note, Weight> subGraph = new UndirectedSparseGraph<Note, Weight>();
        for (Note note : cluster) {
            Preconditions.checkState(graph.containsVertex(note), note);
            subGraph.addVertex(note);
        }
        Convenience.upperOuter(cluster, new Loop<Note>() {
            @Override
            public void action(Note first, Note second) {
                Weight weight = graph.findEdge(first, second);
                if (weight != null) {
                    subGraph.addEdge(weight, first, second);
                }
            }
        });
        return subGraph;
    }

    private void selectNotes(final Set<Note> notes) {
        assert notes != null;
        assert notes.size() > 0;

        popup.removeAll();

        // TODO: selected notes shouldn't move

        // TODO: only go through active bunches
        for (Bunch bunch : controller.getBunches()) {
            if (Convenience.isSubset(notes, bunch.getNotes())) {
                BunchView view = new BunchView();
                view.setBunch(bunch);
                popup.add(view);
                popup();
                return;
            }
        }

        if (notes.size() == 1) {
            Note note = Iterables.getOnlyElement(notes);
            NoteView noteView = new NoteView();
            noteView.setNote(note);
            popup.add(noteView);
            popup();
            return;
        }

        JMenuItem newBunch = new JMenuItem("New Bunch", KeyEvent.VK_N);
        newBunch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bunch bunch = controller.newBunch(notes);
                BunchView view = new BunchView();
                view.setBunch(bunch);
                popup.removeAll();
                popup.add(view);
                popup();
            }
        });
        popup.add(newBunch);

        Collection<Bunch> bunches = controller.getBunches();
        if (!bunches.isEmpty() && bunches.size() < 10) {
            popup.add(new JSeparator());
            for (Bunch bunch : bunches) {
                popup.add(new JMenuItem("Add to \"" + bunch.getName() + "\""));
            }
        }

        if (notes.size() < 10) {
            popup.add(new JSeparator());
            for (Note note : notes) {
                JMenuItem entry = new JMenuItem(note.getTitle());
                entry.setEnabled(false);
                entry.setFont(entry.getFont().deriveFont(10f));
                popup.add(entry);
            }
        }
        popup();
    }

    private void popup() {
        popup.pack();
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouse, this);
        // http://stackoverflow.com/questions/766956
        popup.show(this, mouse.x, mouse.y);
    }

    private final Map<ClusterId, Layout<Note, Weight>> clusters = Maps.newHashMap();

    @Override
    public void clusterAdded(ClusterId id, Set<Note> cluster) {
        Preconditions.checkNotNull(id);

        AggregateLayout<Note, Weight> graphLayout = getGraphLayout();
        Graph<Note, Weight> subGraph = subGraph(cluster);
        Layout<Note, Weight> subLayout = new CircleLayout<Note, Weight>(subGraph);

        subLayout.setInitializer(graphLayout);

        int size = Math.min(50, cluster.size() * 5);
        subLayout.setSize(new Dimension(size, size));

        // TODO: smarter positioning of clusters
        Random random = new Random();
        Point2D subCentered = new Point(random.nextInt(getSize().width), random.nextInt(getSize().height));
        graphLayout.put(subLayout, subCentered);
        clusters.put(id, subLayout);
        graphVisualiser.repaint();
    }

    @Override
    public void clusterRemoved(ClusterId id) {
        Preconditions.checkNotNull(id);
        Preconditions.checkArgument(clusters.containsKey(id));

        Layout<Note, Weight> subLayout = clusters.get(id);
        AggregateLayout<Note, Weight> graphLayout = getGraphLayout();
        graphLayout.remove(subLayout);

        clusters.remove(id);
        graphVisualiser.repaint();
    }

    @Override
    public void clusterUpdated(ClusterId id, Set<Note> cluster) {
        Preconditions.checkNotNull(id);
        Preconditions.checkArgument(clusters.containsKey(id));

        Layout<Note, Weight> subLayout = clusters.get(id);
        int size = Math.min(50, cluster.size() * 5);
        subLayout.setSize(new Dimension(size, size));

        Graph<Note, Weight> subGraph = subGraph(cluster);
        subLayout.setGraph(subGraph);
        graphVisualiser.repaint();
    }
}
