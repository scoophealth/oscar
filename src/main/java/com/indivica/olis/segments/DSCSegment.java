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
