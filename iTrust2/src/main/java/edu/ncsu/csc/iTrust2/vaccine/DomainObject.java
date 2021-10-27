package edu.ncsu.csc.iTrust2.vaccine;

import java.io.Serializable;

/**
 * This class is used to provide a common superclass that the `Service` methods
 * can use.
 *
 * @author sarasophiamasood
 *
 */
public abstract class DomainObject {

    /**
     * Returns the ID of the object.
     *
     * @return object ID
     */
    public abstract Serializable getId ();

}
