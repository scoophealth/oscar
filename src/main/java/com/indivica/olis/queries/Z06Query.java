package com.indivica.olis.queries;

import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.ORC21;
import com.indivica.olis.parameters.QRD7;

/**
 * Z06 - Retrieve Laboratory Information Updates for Ordering Facility 
 * @author jen
 *
 */
public class Z06Query implements Query {

	private OBR22 startEndTimestamp = new OBR22(); // mandatory
	private QRD7 quantityLimitedRequest = null;
	private ORC21 orderingFacilityId = new ORC21(); // mandatory
	
	@Override
	public String getQueryHL7String() {
		String query = "";

		if (startEndTimestamp != null)
			query += startEndTimestamp.toOlisString() + "~";

		if (quantityLimitedRequest != null)
			query += quantityLimitedRequest.toOlisString() + "~";

		if (orderingFacilityId != null)
			query += orderingFacilityId.toOlisString() + "~";
		
		return query;
	}
	
	public void setStartEndTimestamp(OBR22 startEndTimestamp) {
    	this.startEndTimestamp = startEndTimestamp;
    }

	public void setQuantityLimitedRequest(QRD7 quantityLimitedRequest) {
    	this.quantityLimitedRequest = quantityLimitedRequest;
    }

	public void setOrderingFacilityId(ORC21 orderingFacilityId) {
    	this.orderingFacilityId = orderingFacilityId;
    }

	@Override
	public QueryType getQueryType() {
		return QueryType.Z06;
	}

}
