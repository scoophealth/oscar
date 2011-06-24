package com.indivica.olis.queries;

import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.QRD7;

/**
 * Z08 - Retrieve Test Results Reportable to Cancer Care Ontario
 * @author jen
 *
 */
public class Z08Query implements Query {

	private OBR22 startEndTimestamp = new OBR22(); // mandatory
	private QRD7 quantityLimitedRequest = null;
	
	@Override
	public String getQueryHL7String() {
		String query = "";

		if (startEndTimestamp != null)
			query += startEndTimestamp.toOlisString() + "~";

		if (quantityLimitedRequest != null)
			query += quantityLimitedRequest.toOlisString() + "~";

		return query;
	}

	public void setStartEndTimestamp(OBR22 startEndTimestamp) {
    	this.startEndTimestamp = startEndTimestamp;
    }

	public void setQuantityLimitedRequest(QRD7 quantityLimitedRequest) {
    	this.quantityLimitedRequest = quantityLimitedRequest;
    }

	@Override
	public QueryType getQueryType() {
		return QueryType.Z08;
	}

}
