/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2003 Steve Phelps
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

package uk.ac.liv.auction.ec.gp;

import uk.ac.liv.auction.agent.Strategy;

import uk.ac.liv.auction.ec.gp.func.GPTradingStrategy;

import java.util.Vector;

/**
 * <p>
 * Use this mixer if you want to have a sub-population of strategies
 * for each agent.
 * </p>
 *
 * @author Steve Phelps
 * @version $Revision$
 */

public class GPMultiPopStrategyMixer extends StrategyMixer {

  public GPMultiPopStrategyMixer() {
    super();
  }

  public Strategy getStrategy( int i, Vector[] group ) {
    GPTradingStrategy strategy = null;
    strategy = (GPTradingStrategy) group[subpopOffset()+i].get(0);
    strategy.setGPContext(problem.getGPContext());
    return strategy;
  }

}