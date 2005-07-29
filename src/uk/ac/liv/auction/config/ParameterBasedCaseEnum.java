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
package uk.ac.liv.auction.config;

import org.apache.log4j.Logger;

import uk.ac.liv.util.Parameterizable;

import ec.util.Parameter;
import ec.util.ParameterDatabase;

/**
 * A type of CaseEnum that can automatically generate a list of cases out
 * of specified Class based on the given parameters.
 * 
 * <i>base</i>.name: name
 * <i>base</i>.case: class name of the template ParameterBasedCase
 * <i>base</i>.parameters: a list of ','-separated parameters, each
 *    corresponding to a case.
 * 
 * @author Jinzhong Niu
 * @version $Revision$
 */

public class ParameterBasedCaseEnum extends CaseEnum {
    
	static Logger logger = Logger.getLogger(ParameterBasedCaseEnum.class);
    
    private ParameterBasedCase cases[];
    
    private int i;

    private static final String P_NAME = "name";
    private static final String P_CASE = "case";
    private static final String P_PARAMETERS = "parameters";
    
    
    public void setup(ParameterDatabase pdb, Parameter base) {
        
        setName(pdb.getString(base.push(P_NAME)));        
        
        Class c = pdb.getInstanceForParameterEq(base
				.push(P_CASE), null, ParameterBasedCase.class).getClass();
        
        String s = pdb.getString(base.push(P_PARAMETERS));
        String params[] = s.split(",");
        
        cases = new ParameterBasedCase[params.length];
        for (int i=0; i<cases.length; i++) {
            try {
                cases[i] = (ParameterBasedCase)c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                logger.error(e.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
            
            if (cases[i] instanceof Parameterizable) {
                ((Parameterizable)cases[i]).setup(pdb, base.push(P_CASE));
            }
            cases[i].setParameter(params[i].trim());
        }
        
        reset();
    }
    
    public void reset() {
        i = 0;
    }

    /* 
     * @see uk.ac.liv.auction.config.CaseEnum#moreCases()
     */
    public boolean moreCases() {
		return i<cases.length;
    }

    /* 
     * @see uk.ac.liv.auction.config.CaseEnum#nextCase()
     */
    public Case nextCase() {
        assert moreCases();
        
        return cases[i++];
    }
}