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

package uk.ac.liv.auction.electricity;

import java.util.Iterator;
import java.util.List;

import ec.util.Parameter;
import ec.util.ParameterDatabase;

import java.io.Serializable;

import uk.ac.liv.util.Debug;

import uk.ac.liv.auction.core.RoundRobinAuction;

import uk.ac.liv.auction.stats.*;

import uk.ac.liv.auction.agent.*;


/**
 * @author Steve Phelps
 */

public class ElectricityStats implements Serializable, Cloneable, MarketStats {

  RoundRobinAuction auction;

  public double rCon, rCap;

  public double pBCE = 0, pSCE = 0;

  public double pBA, pSA;

  public double mPB, mPS;

  public double eA;

  public double numSellers;
  public double numBuyers;

  int buyerCap, sellerCap;

  public EquilibriaStats standardStats = null;

  double equilibPrice;


  public ElectricityStats( RoundRobinAuction auction ) {
    this.auction = auction;
    calculate();
  }

  /**
   * @deprecated
   */
  public ElectricityStats( long minPrice, long maxPrice, RoundRobinAuction auction ) {
    this(auction);
  }

  /**
   * @deprecated
   */
  public void setPriceRange( long minPrice, long maxPrice ) {
  }

  public void setup( ParameterDatabase parameters, Parameter base ) {
  }

  public void setAuction( RoundRobinAuction auction ) {
    this.auction = auction;
    standardStats.setAuction(auction);
  }


  public void recalculate() {
    calculate(false);
  }

  public void calculate() {
    calculate(true);
  }

  protected void calculateEquilibria() {
    if ( standardStats == null ) {
      standardStats = new EquilibriaStats(auction);
      standardStats.calculate();
    } else {
      standardStats.recalculate();
    }
  }

  protected void zeroTotals() {
    sellerCap = 0;
    buyerCap = 0;
    pBA = 0;
    pSA = 0;
    numBuyers = 0;
    numSellers = 0;
  }

  protected void zeroEquilibriumTotals() {
    pSCE = 0;
    pBCE = 0;
  }

  public double calculateEquilibriumPrice() {
    return (standardStats.getMinPrice() + standardStats.getMaxPrice()) / 2;
  }

  public double equilibriumProfits( AbstractTraderAgent trader ) {
    double surplus = 0;
    if ( trader.isSeller() ) {
      surplus = equilibPrice - trader.getPrivateValue();
    } else {
      surplus = trader.getPrivateValue() - equilibPrice;
    }
    return auction.getAge() * equilibQuant(trader, equilibPrice) * surplus;
  }

  protected double getProfits( AbstractTraderAgent trader ) {
    return ((ElectricityTrader) trader).getProfits();
  }

  protected double getCapacity( AbstractTraderAgent trader ) {
    return ((ElectricityTrader) trader).getCapacity();
  }

  protected void calculate( boolean equilibrium ) {
    zeroTotals();
    if ( equilibrium ) {
      calculateEquilibria();
      zeroEquilibriumTotals();
    }
    equilibPrice = calculateEquilibriumPrice();
    Iterator i = auction.getTraderIterator();
    while ( i.hasNext() ) {
      AbstractTraderAgent trader = (AbstractTraderAgent) i.next();
      if ( trader.isSeller() ) {
        numSellers++;
        sellerCap += getCapacity(trader);
        pSA += getProfits(trader);
        if ( equilibrium ) {
          pSCE += equilibriumProfits(trader);
        }
      } else {
        numBuyers++;
        buyerCap += getCapacity(trader);
        pBA += getProfits(trader);
        if ( equilibrium ) {
          pBCE += equilibriumProfits(trader);
        }
      }
    }
    rCon = numSellers / numBuyers;
    rCap = (double) buyerCap / (double) sellerCap;
    mPB = (pBA - pBCE) / pBCE;
    mPS = (pSA - pSCE) / pSCE;
    eA = (pBA + pSA) / (pBCE + pSCE) * 100;
  }

  public double equilibQuant( AbstractTraderAgent t, double price ) {
    double privateValue = t.getPrivateValue();
    if ( t.isBuyer() ) {
      if ( price > privateValue ) {
        return 0;
      } else {
        return ((ElectricityTrader) t).getCapacity();
      }
    } else {
      if ( price > privateValue ) {
        return ((ElectricityTrader) t).getCapacity();
      } else {
        return 0;
      }
    }
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public ElectricityStats newCopy() {
    Object copy = null;
    try {
      copy = this.clone();
    } catch ( CloneNotSupportedException e ) {
    }
    return (ElectricityStats) copy;
  }

  public String toString() {
    return "(" + getClass() + "\n\trCon:" + rCon + "\n\trCap:" + rCap
      + "\n\tmPB:" + mPB + "\n\tmPS:" + mPS + "\n\tpBA:" + pBA
      + "\n\tpSA:" + pSA + "\n\tpBCE:" + pBCE + "\n\tpSCE:" + pSCE
      + "\n\teA:" + eA
      + "\n\tstandardStats:" + standardStats
      + "\n)";
  }

}

class ElectricityMetaStats extends MetaMarketStats {

  public ElectricityMetaStats( double min, double max, RoundRobinAuction auction  ) {
    super( (long) min, (long) max, auction);
  }

  public int quantity( AbstractTraderAgent agent ) {
    return ((ElectricityTrader) agent).getCapacity();
  }

}