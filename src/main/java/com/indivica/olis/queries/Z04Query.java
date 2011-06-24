package com.indivica.olis.queries;

import java.util.LinkedList;
import java.util.List;

import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.OBR4;
import com.indivica.olis.parameters.OBX3;
import com.indivica.olis.parameters.QRD7;
import com.indivica.olis.parameters.ZRP1;

/**
 * Z04 - Retrieve Laboratory Information Updates for Practitioner
 * @author jen
 *
 */
public class Z04Query implements Query {

	private OBR22 startEndTimestamp = new OBR22(); // mandatory
	private QRD7 quantityLimitedRequest = null;
	private ZRP1 requestingHic = new ZRP1(); // mandatory
	private List<OBR4> testRequestCodeList = new LinkedList<OBR4>();
	private List<OBX3> testResultCodeList = new LinkedList<OBX3>();
	
	@Override
	public String getQueryHL7String() {
		String query = "";
		
		if (startEndTimestamp != null)
			query += startEndTimestamp.toOlisString() + "~";
		
		if (quantityLimitedRequest != null)
			query += quantityLimitedRequest.toOlisString() + "~";
		
		if (requestingHic != null)
			query += requestingHic.toOlisString() + "~";
		
		for (OBR4 testRequestCode : testRequestCodeList)
			query += testRequestCode.toOlisString() + "~";
	
		for (OBX3 testResultCode : testResultCodeList)
			query += testResultCode.toOlisString() + "~";
		
		return query;
	}

	public void setStartEndTimestamp(OBR22 startEndTimestamp) {
    	this.startEndTimestamp = startEndTimestamp;
    }

	public void setQuantityLimitedRequest(QRD7 quantityLimitedRequest) {
    	this.quantityLimitedRequest = quantityLimitedRequest;
    }

	public void setRequestingHic(ZRP1 requestingHic) {
    	this.requestingHic = requestingHic;
    }

	public void setTestRequestCodeList(List<OBR4> testRequestCodeList) {
    	this.testRequestCodeList = testRequestCodeList;
    }

	public void setTestResultCodeList(List<OBX3> testResultCodeList) {
    	this.testResultCodeList = testResultCodeList;
    }

	@Override
	public QueryType getQueryType() {
		return QueryType.Z04;
	}

}
