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

package uk.ac.liv.util;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * <p>
 * A utility class for cummulative tracking of stats for a series
 * of doubles.  Moments are incremented dynamically, rather than keeping
 * the actual cases in memory.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <p>
 * <code>
 * CummulativeStatCounter series1 = new CummulativeStatCounter("series1");<br>
 * series1.newData(4.5);<br>
 * series1.newData(5.6);<br>
 * series1.newData(9.0);<br>
 * System.out.println("Standard deviation of series1 = " + series1.getStdDev());<br>
 * series1.newData(5.56);<br>
 * series1.newData(12);<br>
 * System.out.println("And now the standard deviation = " + series1.getStdDev());<br>
 * </code>
 * </p>
 *
 * @author Steve Phelps
 * @version $Revision$
 */

public class CummulativeDistribution implements 
    Serializable, Cloneable, Resetable, Distribution {

  /**
   * The number of data in the series so far.
   */
  protected int n;

  /**
   * The cummulative total of all numbers in the series so far.
   */
  protected double total;

  /**
   * The square of the total.
   */
  protected double totalSq;

  /**
   * The minimum so far.
   */
  protected double min;

  /**
   * The maximum so far.
   */
  protected double max;

  /**
   * The name of this series.
   */
  protected String varName;

  static Logger logger = Logger.getLogger(CummulativeDistribution.class);

  public CummulativeDistribution( String varName ) {
    this.varName = varName;
    initialise();
  }

  public CummulativeDistribution() {
    this("");
  }

  public void initialise() {
    n = 0;
    total = 0;
    min = Double.POSITIVE_INFINITY;
    max = Double.NEGATIVE_INFINITY;
    totalSq = 0;
  }

  /**
   * Add a new datum to the series.
   */
  public void newData( double i ) {
    n++;
    total += i;
    totalSq += i*i;
    if ( i > max ) {
      max = i;
    }
    if ( i < min ) {
      min = i;
    }
  }

  /**
   * Get the number of items in the series.
   */
  public int getN() {
    return n;
  }

  /**
   * Get the mean of the data.
   */
  public double getMean() {
    return total / n;
  }

  /**
   * Get the variance of the data about origin.
   */
  public double getVariance( double origin ) {
    return Math.abs(totalSq/n - origin*origin);
  }

  /**
   * Get the standard deviation of the data about origin.
   */
  public double getStdDev( double origin ) {
    return Math.sqrt(getVariance(origin));
  }

  /**
   * Get the variance about the mean.
   */
  public double getVariance() {
    return getVariance(getMean());
  }

  /**
   * Get the standard deviation from the mean.
   */
  public double getStdDev() {
    return getStdDev(getMean());
  }

  /**
   * Get the coefficient of variable about origin.
   */
  public double getVarCoef( double origin ) {
    return 100 * getStdDev(origin) / origin;
  }

  /**
   * Get the minimum datum.
   */
  public double getMin() {
    return min;
  }

  /**
   * Get the maximum datum.
   */
  public double getMax() {
    return max;
  }
  
  /**
   * Get the total of the data
   */
  public double getTotal() {
  	return total;
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public void reset() {
    initialise();
  }

  public String getName() {
    return varName;
  }

  public String toString() {
    return "(" + getClass() + " varName:" + varName + " n:" + n + " mean:" +
             getMean() + " stdev:" + getStdDev() + " min:" + min + " max:" +
             max + ")";
  }

  public void log() {
    logger.info(getName());
    logger.info("\tn:\t" + getN());
    logger.info("\tmin:\t" + getMin());
    logger.info("\tmax:\t" + getMax());
    logger.info("\tmean:\t" + getMean());
    logger.info("\tstdev:\t" + getStdDev());
  }
  
  public double getTrimmedMean( double p ) {
    throw new Error("method not implemented");
  }

}