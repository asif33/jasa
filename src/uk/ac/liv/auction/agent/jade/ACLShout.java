/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2003 Steve Phelps
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


package uk.ac.liv.auction.agent.jade;

import uk.ac.liv.auction.core.Shout;

import jade.content.Concept;

import jade.core.Agent;
import jade.core.AID;

public class ACLShout implements Concept  {

  String agentName;
  Shout jasaShout;

  public ACLShout() {
    jasaShout = new Shout();
  }

  public ACLShout( Shout jasaShout ) {
    this.jasaShout = jasaShout;
  }


  public void setAgentName( String agentName ) {
    this.agentName = agentName;
    muteJasaShout();
    ((ShoutWithMutableId) jasaShout).setJADEAgent(new AID(agentName, true), null);

  }

  public String getAgentName() {
    return agentName;
  }

  public double getPrice() {
    return jasaShout.getPrice();
  }

  public int getQuantity() {
    return jasaShout.getQuantity();
  }

  public boolean getIsBid() {
    return jasaShout.isBid();
  }

  public int getId() {
    return jasaShout.getId();
  }

  public void setPrice( double price ) {
    jasaShout.setPrice(price);
  }

  public void setQuantity( int quantity ) {
    jasaShout.setQuantity(quantity);
  }

  public void setIsBid( boolean isBid ) {
    jasaShout.setIsBid(isBid);
  }


  public void setId(int id) {
    muteJasaShout();
    ((ShoutWithMutableId) jasaShout).setId(id);
  }

  public Shout jasaShout() {
    return jasaShout;
  }


  public void muteJasaShout() {
    ShoutWithMutableId muted = new ShoutWithMutableId();
    muted.copyFrom(jasaShout);
    jasaShout = muted;
  }


}

class ShoutWithMutableId extends Shout {

  public void setId( int id ) {
    this.id = id;
  }

  public void setJADEAgent( AID aid, Agent sender ) {
    setAgent( new JASATraderAgentProxy(aid, sender) );
  }

}
