/*
 * JASA Java Auction Simulator API
 * Copyright (C) Steve Phelps
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

package uk.ac.liv.util.io;

import java.util.Iterator;

import java.io.*;


/**
 * A class for writing data to CSV (comma-separated variables) text files.
 *
 * @author Steve Phelps
 */

public class CSVWriter implements Serializable {

  PrintStream out;
  int numColumns;
  int currentColumn = 0;
  char seperator;
  static final char DEFAULT_SEPERATOR = '\t';

  public CSVWriter( OutputStream out, int numColumns, char seperator ) {
    this.out = new PrintStream(out);
    this.numColumns = numColumns;
    this.seperator = seperator;
  }

  public CSVWriter( OutputStream out, int numColumns ) {
    this(out, numColumns, DEFAULT_SEPERATOR);
  }

  public void newData( Iterator i ) {
    while ( i.hasNext() ) {
      newData(i.next());
    }
  }

  public void newData( Object[] data ) {
    for( int i=0; i<data.length; i++ ) {
      newData(data[i]);
    }
  }

  public void newData( Object data ) {
    out.print(data);
    currentColumn++;
    if ( currentColumn < numColumns ) {
      out.print(seperator);
    } else {
      out.println();
      currentColumn = 0;
    }
  }

  public void newData( int data ) {
    newData(new Integer(data));
  }

  public void newData( long data ) {
    newData(new Long(data));
  }

  public void newData( boolean data ) {
    if ( data ) {
      newData(1);
    } else {
      newData(0);
    }
  }

  public void newData( double data ) {
    newData(new Double(data));
  }

  public void flush() {
    out.flush();
  }

  public void close() {
    out.close();
  }

  private void writeObject(java.io.ObjectOutputStream out)
     throws IOException {
  }

  private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException {
  }

}