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


package uk.ac.liv.auction.stats;

import uk.ac.liv.auction.core.*;

import java.io.Serializable;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * <p>
 * A class to calculate the true equilibrium price and quantity
 * ranges for a given auction.
 * </p>
 *
 * @author Steve Phelps
 * @version $Revision$
 */

public class EquilibriumReport extends DirectRevelationReport
    implements Serializable {

  /**
   * The minimum equilibrium price.
   */
  protected double minPrice;

  /**
   * The maximum equilibrium price.
   */
  protected double  maxPrice;

  /**
   * Do any equilbria exist?
   */
  protected boolean equilibriaFound = false;
  
  protected List matchedShouts;
  
  protected int quantity;
  
  public static final ReportVariable VAR_EXISTS =
    new ReportVariable("equilibria.exists", "Does an equilibrium exist?");
  
  public static final ReportVariable VAR_MINPRICE =
    new ReportVariable("equilibria.minprice", "Minimum equilibrium price");
  
  public static final ReportVariable VAR_MAXPRICE =
    new ReportVariable("equilibria.maxprice", "Maximum equilibrium price");
  
  public static final ReportVariable VAR_QUANTITY =
    new ReportVariable("equilibria.quantity", "Equilibrium quantity");
  

  static Logger logger = Logger.getLogger(EquilibriumReport.class);


  public EquilibriumReport( RoundRobinAuction auction ) {
    super(auction);
  }

  public EquilibriumReport() {
    super();
  }

  public void recalculate() {
    reset();
    calculate();
  }

  public void calculate() {
    super.calculate();
    Shout hiAsk = shoutEngine.getHighestMatchedAsk();
    Shout loBid = shoutEngine.getLowestMatchedBid();
    if ( hiAsk == null || loBid == null ) {
      equilibriaFound = false;
    } else {
      calculateEquilibriaPriceRange();
      equilibriaFound = true;
      matchedShouts = shoutEngine.getMatchedShouts();
      calculateEquilibriaQuantity();
    }    
  }
  
  protected void calculateEquilibriaQuantity() {
    quantity = 0;
    Iterator i = matchedShouts.iterator();
    while ( i.hasNext() ) {
      Shout bid = (Shout) i.next();
      Shout ask = (Shout) i.next();
      quantity += ask.getQuantity();
    }
  }

  protected void calculateEquilibriaPriceRange() {

    minPrice = Shout.maxPrice(shoutEngine.getHighestMatchedAsk(),
                               shoutEngine.getHighestUnmatchedBid());

    maxPrice = Shout.minPrice(shoutEngine.getLowestUnmatchedAsk(),
                               shoutEngine.getLowestMatchedBid());

    assert minPrice <= maxPrice;
  }


  public void initialise() {
    super.initialise();
    quantity = 0;
  }

  public double getMinPrice() {
    return minPrice;
  }

  public double getMaxPrice() {
    return maxPrice;
  }
  
  public int getQuantity() {
    return quantity;
  }


  public boolean equilibriaExists() {
    return equilibriaFound;
  }

  public double calculateMidEquilibriumPrice() {
    return (getMinPrice() + getMaxPrice()) / 2;
  }

  public String toString() {
    return "(" + getClass() + " equilibriaFound:" + equilibriaFound +
           " minPrice:" + minPrice + " maxPrice:" + maxPrice + ")";
  }

  public void produceUserOutput() {
    logger.info("");
    logger.info("Equilibrium analysis report");
    logger.info("---------------------------");
    logger.info("");
    logger.info("\tEquilibria Found?\t" + equilibriaFound);
    logger.info("\n\tquantity:\t" + quantity + "\n");
    logger.info("\n\tprice:\n\t\tmin:\t" + minPrice + "\tmax:\t" + maxPrice);
    logger.info("");
  }
  
  public Map getVariables() {
    HashMap reportVars = new HashMap();
    reportVars.put(VAR_EXISTS, new Boolean(equilibriaFound));
    reportVars.put(VAR_QUANTITY, new Long(quantity));
    reportVars.put(VAR_MINPRICE, new Double(minPrice));
    reportVars.put(VAR_MAXPRICE, new Double(maxPrice));
    return reportVars;
  }

}
