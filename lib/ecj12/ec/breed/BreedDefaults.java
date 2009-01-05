package ec.breed;
import ec.util.Parameter;
import ec.*;

/* 
 * BreedDefaults.java
 * 
 * Created: Thu Jan 20 17:12:49 2000
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public final class BreedDefaults implements DefaultsForm 
    {
    public static final String P_BREED = "breed";

    /** Returns the default base. */
    public static final Parameter base()
        {
        return new Parameter(P_BREED);
        }
    }
