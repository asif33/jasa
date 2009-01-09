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

package uk.ac.liv.util.io;

import java.util.Iterator;

/**
 * @author Steve Phelps
 * @version $Revision$
 */

public class NullDataWriter implements DataWriter {

	public void newData(Iterator i) {
		// Do nothing
	}

	public void newData(Object[] data) {
		// Do nothing
	}

	public void newData(Object data) {
		// Do nothing
	}

	public void newData(int data) {
		// Do nothing
	}

	public void newData(long data) {
		// Do nothing
	}

	public void newData(double data) {
		// Do nothing
	}

	public void newData(float data) {
		// Do nothing
	}

	public void newData(boolean data) {
		// Do nothing
	}

	public void newData(String data) {
		// Do nothing
	}

	public void newData(Integer data) {
		// Do nothing
	}

	public void newData(Double data) {
		// Do nothing
	}

	public void newData(Long data) {
		// Do nothing
	}

	public void flush() {
		// Do nothing
	}

	public void close() {
		// Do nothing
	}

}
