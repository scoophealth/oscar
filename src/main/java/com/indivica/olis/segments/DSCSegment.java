/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.segments;

/**
 * When OLIS responds with a continuation pointer we can use the DSC segment
 * as well as the entire identical query to get the rest of the results.
 * @author jen
 *
 */
public class DSCSegment implements Segment {

	private String continuationPointer;
	
	public DSCSegment(String continuationPointer) {
		this.continuationPointer = continuationPointer;
	}
	
	@Override
	public String getSegmentHL7String() {
		return "DSC|" + continuationPointer;
	}

}
