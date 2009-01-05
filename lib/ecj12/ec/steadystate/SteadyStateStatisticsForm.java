package ec.steadystate;
import ec.*;

/* 
 * SteadyStateStatisticsForm.java
 * 
 * Created: Fri Nov  9 20:45:26 EST 2001
 * By: Sean Luke
 */

/**
 * This interface defines the hooks for SteadyStateEvolutionState objects
 * to update themselves on.  Note that the the only methods in common
 * with the standard statistics are initialization and final. This is an
 * optional interface: SteadyStateEvolutionState will complain, but
 * will permit Statistics objects that don't adhere to it, though they will
 * only have their initialization and final statistics methods called!
 *
 * @author Sean Luke
 * @version 1.0 
 */

public interface SteadyStateStatisticsForm 
    {
    /** Called immediately before population initialization occurs. */
    public void preInitializationStatistics(final EvolutionState state);
    /** Called immediately after population initialization occurs. */
    public void postInitializationStatistics(final EvolutionState state);
    /** Called immediately before the initial generation is evaluated. */
    public void preInitialEvaluationStatistics(final SteadyStateEvolutionState state);
    /** Called immediately after the initial generation is evaluated. */
    public void postInitialEvaluationStatistics(final SteadyStateEvolutionState state);
    /** Called each time new individuals are bred during the steady-state
        process.  You can look up the individuals in state.newIndividuals[] */
    public void individualsBredStatistics(SteadyStateEvolutionState state);
    /** Called each time new individuals are evaluated during the steady-state
        process.  You can look up the individuals in state.newIndividuals[] */
    public void individualsEvaluatedStatistics(SteadyStateEvolutionState state);
    /** Called each time one full pseudogeneration of individuals has been bred and evaluated.
        If the first total number of individuals is much larger than the pseudogeneration, then this may
        only get called once for the initial generation. */
    public void nextPseudogenerationStatistics(SteadyStateEvolutionState state);
    /** Called immediately after the run has completed.  <i>result</i>
        is either <tt>state.R_FAILURE</tt>, indicating that an ideal individual
        was not found, or <tt>state.R_SUCCESS</tt>, indicating that an ideal
        individual <i>was</i> found. */
    public void finalStatistics(final EvolutionState state, final int result);
    }
