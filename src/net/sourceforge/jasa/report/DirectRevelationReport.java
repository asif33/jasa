/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2009 Steve Phelps
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

package net.sourceforge.jasa.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.jasa.agent.AbstractTradingAgent;
import net.sourceforge.jasa.market.DuplicateShoutException;
import net.sourceforge.jasa.market.FourHeapOrderBook;
import net.sourceforge.jasa.market.Order;
import net.sourceforge.jasa.market.MarketFacade;
import net.sourceforge.jasa.sim.util.Resetable;

import org.apache.log4j.Logger;

/**
 * This class computes the hypothetical market state when all agents bid at
 * their private valuation, ie when all agents bid truthfully. This can be used,
 * for example, for equilibrium calculations.
 * 
 * @author Steve Phelps
 * @version $Revision$
 */

public abstract class DirectRevelationReport extends AbstractMarketStatsReport
    implements Resetable, Serializable {

	/**
	 * The market state after forced direct revelation.
	 */
	protected FourHeapOrderBook shoutEngine = new FourHeapOrderBook();

	/**
	 * The truthful shouts of all traders in the market.
	 */
	protected ArrayList shouts;

	static Logger logger = Logger.getLogger(DirectRevelationReport.class);

	public DirectRevelationReport(MarketFacade auction) {
		super(auction);
		shouts = new ArrayList();
	}

	public DirectRevelationReport() {
		this(null);
	}

	public void setAuction(MarketFacade auction) {
		this.auction = auction;
	}

	public void calculate() {
		initialise();
		simulateDirectRevelation();
	}

	/**
	 * Update the market state with a truthful shout from each trader.
	 */
	protected void simulateDirectRevelation() {
		Iterator traders = auction.getTraderIterator();
		while (traders.hasNext()) {
			AbstractTradingAgent trader = (AbstractTradingAgent) traders.next();
			int quantity = trader.determineQuantity(auction);
			double value = trader.getValuation(auction);
			boolean isBid = trader.isBuyer(auction);
			Order shout = new Order(trader, quantity, value, isBid);
			shouts.add(shout);
			enumerateTruthfulShout(shout);
		}
	}

	public void initialise() {
		shouts.clear();
		shoutEngine.reset();
	}

	public void reset() {
		initialise();
	}

	/**
	 * Process a truthful shout from an agent
	 * 
	 * @param shout
	 *          The truthful shout
	 */
	protected void enumerateTruthfulShout(Order shout) {
		try {
			shoutEngine.add(shout);
		} catch (DuplicateShoutException e) {
			logger.error(e.getMessage());
			throw new Error(e);
		}
	}

}
