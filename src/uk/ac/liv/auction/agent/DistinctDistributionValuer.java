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

import uk.ac.liv.auction.event.AuctionEvent;
import uk.ac.liv.auction.event.AuctionOpenEvent;
import uk.ac.liv.prng.GlobalPRNG;

import cern.jet.random.Uniform;

import ec.util.Parameter;
import ec.util.ParameterDatabase;

/**
 * 
 * @author Steve Phelps
 * @version $Revision$
 */
public class DistinctDistributionValuer extends AbstractRandomValuer {

  protected double minValueMin;
  
  protected double minValueMax;
  
  protected double rangeMin;
  
  protected double rangeMax;
  
  protected double minValue;
  
  protected double range;
  
  public static final String P_MINVALUEMIN = "minvaluemin";
  public static final String P_MINVALUEMAX = "minvaluemax";
  public static final String P_RANGEMIN = "rangemin";
  public static final String P_RANGEMAX = "rangemax";
  
  
  public void setup( ParameterDatabase parameters, Parameter base ) {
    minValueMin = parameters.getDouble(base.push(P_MINVALUEMIN), null, 0);
    minValueMax = parameters.getDouble(base.push(P_MINVALUEMAX), null, minValueMin);
    rangeMin = parameters.getDouble(base.push(P_RANGEMIN),  null, 0);
    rangeMax = parameters.getDouble(base.push(P_RANGEMAX), null, rangeMax);
    initialise();
  }

  public void initialise() {
    Uniform minValueDist = new Uniform(minValueMin, minValueMax, GlobalPRNG.getInstance());
    Uniform rangeDist = new Uniform(rangeMin, rangeMax, GlobalPRNG.getInstance());
    minValue = minValueDist.nextDouble();
    range = rangeDist.nextDouble();
    distribution = new Uniform(minValue, minValue+range, GlobalPRNG.getInstance()); 
    drawRandomValue();
  }
  
  public void eventOccurred( AuctionEvent event ) {
    super.eventOccurred(event);
    if ( event instanceof AuctionOpenEvent ) {
      drawRandomValue();
    }
  }


}