/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2002 Steve Phelps
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

import junit.framework.*;

import uk.ac.liv.auction.agent.*;
import uk.ac.liv.auction.core.*;


public class AdaptiveStrategyTest extends TestCase {

  TestStrategy strategy;

  static final int NUM_ROUNDS = 10;

  public AdaptiveStrategyTest(String name) {
    super(name);
  }

  public void setUp() {
    strategy = new TestStrategy();
    strategy.setQuantity(1);
  }

  public void testActionsAndRewards() {
    RoundRobinAuction auction = new RoundRobinAuction("test auction");
    Auctioneer auctioneer = new ContinuousDoubleAuctioneer(auction);
    auction.setAuctioneer(auctioneer);
    auction.setMaximumRounds(NUM_ROUNDS);
    ZITraderAgent agent = new ZITraderAgent(10, 100, false);
    agent.setStrategy(strategy);
    auction.register(agent);
    auction.run();
    System.out.println("Number of actions = " + strategy.actions);
    System.out.println("Number of rewards = " + strategy.rewards);
    assertTrue(strategy.actions==NUM_ROUNDS);
    assertTrue(strategy.rewards==NUM_ROUNDS-1);
  }

  public static void main( String[] args ) {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {
    return new TestSuite(AdaptiveStrategyTest.class);
  }

}

class TestStrategy extends AdaptiveStrategy {

  public int actions = 0;

  public int rewards = 0;

  public int act() {
    return actions++;
  }

  public void calculateReward( Auction auction ) {
    rewards++;
  }

}
