/*
 * Copyright Samuel Halliday 2010
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.utils;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.logging.Level;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * Workarounds for bugs in Java or third party libraries.
 *
 * @author Samuel Halliday
 */
@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Workarounds {

    /**
     * Sadly, a wrapper for the File method of this name. Swallows security exceptions, which
     * can erroneously appear (on OS X at least) despite the policy file saying otherwise and which
     * are probably not fatal at any rate.
     *
     * @param file
     * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6997203">Java Bug 6997203</a>
     */
    @SuppressWarnings("CallToThreadYield")
    public static void deleteOnExit(File file) {
        Preconditions.checkNotNull(file);
        try {
            file.deleteOnExit();
        } catch (Exception e1) {
            log.log(Level.INFO, "{0} delete denied, retrying - it might be Java bug #6997203.", file);
            try {
                System.gc();
                Thread.yield();
                file.deleteOnExit();
            } catch (Exception e2) {
                log.log(Level.WARNING, file + " delete denied a second time.", e2);
            }
        }
    }
}
