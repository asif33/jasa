/*
 * JASA Java Auction Simulator API
 * Copyright (C) Steve Phelps
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

package test.uk.ac.liv.auction.agent;

import uk.ac.liv.auction.core.*;

import uk.ac.liv.auction.agent.AbstractTraderAgent;
import uk.ac.liv.auction.agent.RoundRobinTrader;

import junit.framework.TestCase;


public class TestTrader extends AbstractTraderAgent {

  public Shout lastWinningShout = null;
  public double lastWinningPrice;
  public int lastWinningQuantity;
  public Shout[] shouts;
  int currentShoutIndex = 0;
  TestCase test;

  public TestTrader( TestCase test, int stock, long funds ) {
    super(stock, funds);
    this.test = test;
  }

  public void informOfSeller( Shout winningShout, RoundRobinTrader seller,
                              double price, int quantity ) {
    System.out.println(this + ": winning shout " + winningShout + " at price " + price + " and quantity " + quantity);
    lastWinningShout = winningShout;
    lastWinningPrice = price;
  }

  public void requestShout( RoundRobinAuction auction ) {
    if ( currentShoutIndex >= shouts.length ) {
      auction.remove(this);
      return;
    }
    if ( currentShoutIndex > 0 ) {
      auction.removeShout(shouts[currentShoutIndex-1]);
    }
    try {
      auction.newShout(shouts[currentShoutIndex++]);
    } catch ( AuctionException e ) {
      e.printStackTrace();
      test.fail();
    }
  }

}