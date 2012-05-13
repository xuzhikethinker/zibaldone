/*
 * Created 08-May-2012
 * 
 * Copyright Samuel Halliday 2012
 * PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
package uk.me.fommil.zibaldone;

/**
 * Provides a metric between {@link Note} instances to be used in user interfaces
 * and machine learning algorithms.
 * 
 * @author Samuel Halliday
 */
public interface Relator {

    /**
     * Defines a metric between the parameters which is 0 if the objects are
     * equal.
     * 
     * @param a
     * @param b
     * @return
     */
    public double relate(Note a, Note b);
}