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


package uk.ac.liv.prng;

//import edu.cornell.lassp.houle.RngPack.RandomSeedable;

import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
//import edu.cornell.lassp.houle.RngPack.RanMT;


public class MT extends PRNGFactory {

  public RandomEngine create() {
     return new MersenneTwister64();
  }

  public RandomEngine create( long seed ) {
    return new MersenneTwister64((int) seed);
  }

  public String getDescription() {
    return "64-bit Mersenne Twister (Matsumoto and Nishimura)";
  }

}
