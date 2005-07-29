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
package uk.ac.liv.auction.config.strategy;


import uk.ac.liv.auction.config.Case;
import ec.util.ParameterDatabase;
import ec.util.Parameter;

/**
 * @author Jinzhong Niu
 * @version $Revision$
 */
public class TT  extends Case {
	
	private static final String ClassName = "uk.ac.liv.auction.agent.TruthTellingStrategy";

	public String toString() {
		return "TT";
	}
	
	public void apply(ParameterDatabase pdb, Parameter base) {
		pdb.set(base, ClassName);		
	}
}