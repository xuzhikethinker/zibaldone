/*
 * Created 19-Jul-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.zibaldone.control;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import lombok.ListenerSupport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import uk.me.fommil.zibaldone.Bunch;
import uk.me.fommil.zibaldone.Note;
import uk.me.fommil.zibaldone.control.Listeners.BunchListener;
import uk.me.fommil.zibaldone.control.TagController.TagChoice;
import uk.me.fommil.zibaldone.persistence.BunchDao;

/**
 * Controller for {@link Bunch} instances.
 * <p>
 * TODO: is this redundant and simply delegating to the DAO?
 * 
 * @author Samuel Halliday
 */
@Log
@RequiredArgsConstructor
@ListenerSupport(BunchListener.class)
public class BunchController {

    @NonNull
    private final EntityManagerFactory emf;

    @NonNull
    private final Settings settings;

    public Collection<Bunch> getBunches() {
        BunchDao dao = new BunchDao(emf);
        return dao.readAll();
    }

    public Bunch getBunch(Long bunchId) {
        BunchDao dao = new BunchDao(emf);
        Bunch bunch = dao.read(bunchId);
        return bunch;
    }

    /**
     * @param notes 
     */
    public void newBunch(Set<Note> notes) {
        Preconditions.checkNotNull(notes);
        Preconditions.checkArgument(!notes.isEmpty());

        Bunch bunch = new Bunch();
        bunch.setName("New Bunch");
        bunch.setNotes(notes);
        BunchDao dao = new BunchDao(emf);
        dao.create(bunch);

        fireBunchAdded(bunch);
        fireBunchSelectionChanged(bunch, TagChoice.SHOW);
    }

    public void removeBunch(Bunch bunch) {
        Preconditions.checkNotNull(bunch);

        BunchDao dao = new BunchDao(emf);
        dao.delete(bunch);

        fireBunchRemoved(bunch);
    }

    /**
     * @param bunch 
     */
    public void updateBunch(Bunch bunch) {
        Preconditions.checkNotNull(bunch);

        BunchDao dao = new BunchDao(emf);
        Bunch update = dao.update(bunch);

        fireBunchUpdated(update);
    }

    /**
     * @param bunch
     * @param choice 
     */
    public void selectBunch(Bunch bunch, TagChoice choice) {
        boolean change;
        switch (choice) {
            case HIDE:
                change = settings.getSelectedBunches().remove(bunch.getId());
                break;
            case SHOW:
                change = settings.getSelectedBunches().add(bunch.getId());
                break;
            default:
                throw new UnsupportedOperationException("only HIDE and SHOW supported");
        }
        if (change) {
            fireBunchSelectionChanged(bunch, choice);
        }
    }
}