/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2004 Steve Phelps
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

package uk.ac.liv.auction.ec.gp.func;

import java.io.Serializable;

import ec.util.*;

import uk.ac.liv.auction.agent.*;
import uk.ac.liv.auction.core.*;

import uk.ac.liv.ai.learning.MimicryLearner;

import uk.ac.liv.ec.gp.*;

import uk.ac.liv.util.*;

/** 
 * @author Steve Phelps
 * @version $Revision$
 *
 */

public class GPTradingStrategy extends FixedQuantityStrategyImpl
    implements GPObject, Cloneable, Serializable, Resetable {


	protected GPGenericIndividual gpIndividual;

	protected MimicryLearner momentumLearner;
	
	protected double currentMargin;
	
  private CummulativeStatCounter priceStats = 
  	new CummulativeStatCounter("priceStats");
  
  public static final String P_LEARNER = "learner";


  public void setup( ParameterDatabase parameters, Parameter base ) {
  	
  	momentumLearner = (MimicryLearner)
  		parameters.getInstanceForParameterEq(base.push(P_LEARNER), null,
  																					MimicryLearner.class);
  	if ( momentumLearner instanceof Parameterizable ) {
  		((Parameterizable) momentumLearner).setup(parameters, 
  																									base.push(P_LEARNER));
  	}
    
    momentumLearner.setOutputLevel(currentMargin=0.5);  		
  }
  
  public void setGPIndividual( GPGenericIndividual individual ) {
  	this.gpIndividual = individual;
  }
  
  public void setAgent( AbstractTraderAgent agent ) {
    this.agent = agent;
  }

  public AbstractTraderAgent getAgent() {
    return agent;
  }


  protected double getPrivateValue() {
    return agent.getPrivateValue(auction);
  }

  public Auction getAuction() {
    return auction;
  }

  public MarketQuote getQuote() {
    return auction.getQuote();
  }

  public boolean modifyShout( Shout.MutableShout shout ) { 
  	super.modifyShout(shout);
    Number result = gpIndividual.evaluateNumberTree(0);
    double price;
    if ( !gpIndividual.misbehaved() ) {
      price = result.doubleValue();
    } else {
      return false;
    }
    if ( price < 0 || 
    			Double.isInfinite(price) || Double.isNaN(price)) {      
      gpIndividual.illegalResult();
      return false;
    }
    shout.setPrice(price);    
    priceStats.newData(price - agent.getPrivateValue(auction));    
    return true;
  }

  public void endOfRound( Auction auction ) {
    // Do nothing
  }

  public double getLastProfit() {
    return agent.getLastProfit();
  }
  
  public boolean lastShoutAccepted() {
  	return agent.lastShoutAccepted();
  }


  public CummulativeStatCounter getPriceStats() {
    return priceStats;
  }

  public void reset() {
    priceStats.reset();
    ((Resetable) momentumLearner).reset();
    gpIndividual.reset();
    momentumLearner.setOutputLevel(currentMargin=0.5);
    super.reset();    
  }
  
  public boolean misbehaved() {
  	return gpIndividual.misbehaved();
  }
  
  public GPGenericIndividual getGPIndividual() {
  	return gpIndividual;
  }

  public Object protoClone() {
    GPTradingStrategy copy = null;
    try {
      copy = (GPTradingStrategy) super.protoClone();
      copy.priceStats = (CummulativeStatCounter) priceStats.clone();  
      copy.gpIndividual = (GPGenericIndividual) gpIndividual.shallowClone();
      copy.gpIndividual.setGPObject(copy);
    } catch ( CloneNotSupportedException e ) {
      e.printStackTrace();
      throw new Error(e.getMessage());
    }
    return copy;
  }

  public void setMargin( double margin ) {
  	this.currentMargin = margin;  
  }
  
  public double markedUpPrice() {  	
  	double price;
  	if ( agent.isBuyer() ) {
      price = agent.getPrivateValue(auction) * (1 - currentMargin);
    } else {
      price = agent.getPrivateValue(auction) * (1 + currentMargin);
    }    
  	return price;
  }

  public void adjustMargin( double targetMargin ) {
    momentumLearner.train(targetMargin);   
    currentMargin = momentumLearner.act();
  }
  
}

