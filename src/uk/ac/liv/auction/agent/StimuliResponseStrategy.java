/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2005 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package uk.ac.liv.auction.agent;

import java.io.Serializable;

import uk.ac.liv.auction.core.Auction;

import uk.ac.liv.ai.learning.*;

import uk.ac.liv.util.Prototypeable;
import uk.ac.liv.util.Parameterizable;
import uk.ac.liv.util.Resetable;

import ec.util.ParameterDatabase;
import ec.util.Parameter;


/**
 * <p>
 * A trading strategy that uses a stimuli-response learning algorithm,
 * such as the Roth-Erev algorithm, to adapt its trading behaviour in
 * successive auction rounds by using the agent's profits in the last
 * round as a reward signal.
 * </p>
 *
 * </p><p><b>Parameters</b><br>
 *
 * <table>
 *
 * <tr><td valign=top><i>base</i><tt>.learner</tt><br>
 * <font size=-1>classname, inherits StimuliResponseLearner</font></td>
 * <td valign=top>(the learning algorithm to use)</td></tr>
 *
 * </table>
 *
 * @author Steve Phelps
 * @version $Revision$
 */

public class StimuliResponseStrategy extends DiscreteLearnerStrategy 
							 	implements Serializable {

  /**
   * The learning algorithm to use.
   */
  protected StimuliResponseLearner learner;

  public static final String P_LEARNER = "learner";

  public StimuliResponseStrategy( AbstractTradingAgent agent ) {
    super(agent);
  }

  public StimuliResponseStrategy() {
    super();
  }


  public void setup( ParameterDatabase parameters, Parameter base ) {

    super.setup(parameters, base);

    Parameter learnerParameter = base.push(P_LEARNER);
    learner = (StimuliResponseLearner)
      parameters.getInstanceForParameter(learnerParameter, null,
                                          StimuliResponseLearner.class);

    ((Parameterizable) learner).setup(parameters, learnerParameter);
  }

  public Object protoClone() {
  	StimuliResponseStrategy clonedStrategy;
  	try {
  		clonedStrategy = (StimuliResponseStrategy) clone();
  		clonedStrategy.learner =
				(StimuliResponseLearner) ((Prototypeable) this.learner).protoClone();
  	} catch ( CloneNotSupportedException e ) {
  		throw new Error(e);
  	}
    return clonedStrategy;
  }

  public int act() {
    return learner.act();
  }

  public void learn( Auction auction ) {
    learner.reward(agent.getLastProfit());
  }

  public void reset() {
    super.reset();
    ((Resetable) learner).reset();
  }

  public Learner getLearner() {
    return learner;
  }

  public void setLearner( Learner learner ) {
    this.learner = (StimuliResponseLearner) learner;
  }

  public String toString() {
    return "(" + getClass() + " markupscale:" + markupScale + " learner:" +
              learner + " quantity:" + quantity + ")";
  }

}
