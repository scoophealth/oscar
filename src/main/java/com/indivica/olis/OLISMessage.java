package com.indivica.olis;

import com.indivica.olis.queries.Query;
import com.indivica.olis.segments.DSCSegment;
import com.indivica.olis.segments.MSHSegment;
import com.indivica.olis.segments.SPRSegment;
import com.indivica.olis.segments.ZSHSegment;

public class OLISMessage {

	private MSHSegment mshSegment;
	private ZSHSegment zshSegment;
	private SPRSegment sprSegment;
	private DSCSegment dscSegment = null;
	
	public OLISMessage(Query query) {
		mshSegment = new MSHSegment(query.getQueryType());
		zshSegment = new ZSHSegment();
		sprSegment = new SPRSegment(query.getQueryType(), query);
	}
	
	public OLISMessage(Query query, String continuationPointer) {
		mshSegment = new MSHSegment(query.getQueryType());
		zshSegment = new ZSHSegment();
		sprSegment = new SPRSegment(query.getQueryType(), query);
		dscSegment = new DSCSegment(continuationPointer);
	}	
	
	public String getTransactionId() {
		return mshSegment.getUuidString();
	}
	
	public String getOlisHL7String() {
		String output = "";
		
		output += mshSegment.getSegmentHL7String() + "\r";
		output += zshSegment.getSegmentHL7String() + "\r";
		output += sprSegment.getSegmentHL7String();
		
		if (dscSegment != null)
			output += "\r" + dscSegment.getSegmentHL7String();
		
		return output;
	}
	
}
