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

package test.uk.ac.liv.util;

import uk.ac.liv.util.FixedLengthQueue;

import junit.framework.*;

/**
 * @author Jinzhong Niu
 * @version $Revision$
 */

public class FixedLengthQueueTest extends TestCase {

  /**
   * @uml.property name="testQueue"
   * @uml.associationEnd
   */
	FixedLengthQueue testQueue;

  public FixedLengthQueueTest( String name ) {
    super(name);
  }

  public void setUp() {
    testQueue = new FixedLengthQueue(5);
  }

  public void testPredefinedData() {
    for ( int i = 0; i < 10; i++ ) {
      testQueue.newData(i);
      System.out.print(testQueue.count());
      System.out.println(testQueue);
      
    }
    assertTrue(Math.abs(testQueue.getMean() - 7) < 0.000000001);
  }
  
  public static void main( String[] args ) {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {
    return new TestSuite(FixedLengthQueueTest.class);
  }

}