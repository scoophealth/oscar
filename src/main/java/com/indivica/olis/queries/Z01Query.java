/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.queries;

import java.util.LinkedList;
import java.util.List;

import com.indivica.olis.parameters.OBR16;
import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.OBR25;
import com.indivica.olis.parameters.OBR28;
import com.indivica.olis.parameters.OBR4;
import com.indivica.olis.parameters.OBR7;
import com.indivica.olis.parameters.OBX3;
import com.indivica.olis.parameters.ORC4;
import com.indivica.olis.parameters.PID3;
import com.indivica.olis.parameters.PV117;
import com.indivica.olis.parameters.PV17;
import com.indivica.olis.parameters.QRD7;
import com.indivica.olis.parameters.ZBE4;
import com.indivica.olis.parameters.ZBE6;
import com.indivica.olis.parameters.ZBR2;
import com.indivica.olis.parameters.ZBR3;
import com.indivica.olis.parameters.ZBR4;
import com.indivica.olis.parameters.ZBR6;
import com.indivica.olis.parameters.ZPD1;
import com.indivica.olis.parameters.ZPD3;
import com.indivica.olis.parameters.ZRP1;

/**
 * Z01 - Retrieve Laboratory Information for Patient
 * @author jen
 *
 */
public class Z01Query extends Query {

	private OBR22 startEndTimestamp = null;
	private OBR7 earliestLatestObservationDateTime = null;
	private QRD7 quantityLimitedRequest = null;
	private ZRP1 requestingHic = new ZRP1(); // mandatory
	private ZPD1 consentToViewBlockedInformation = null;
	private ZPD3 patientConsentBlockAllIndicator = null;
	private ZBR3 specimenCollector = null;
	private ZBR6 performingLaboratory = null;
	private ZBE6 excludePerformingLaboratory = null;
	private ZBR4 reportingLaboratory = null;
	private ZBE4 excludeReportingLaboratory = null;
	private PID3 patientIdentifier = new PID3(); // mandatory
	private OBR16 orderingPractitioner = null;
	private OBR28 copiedToPractitioner = null;
	private PV17 attendingPractitioner = null;
	private PV117 admittingPractitioner = null;
	private ZBR2 testResultPlacer = null;
	private List<OBR4> testRequestCodeList = new LinkedList<OBR4>();
	private ORC4 placerGroupNumber = null;
	private List<OBR25> testRequestStatusList = new LinkedList<OBR25>();
	private List<OBX3> testResultCodeList = new LinkedList<OBX3>();
	
	@Override
	public String getQueryHL7String() {
		String query = "";
		
		if (startEndTimestamp != null)
			query += startEndTimestamp.toOlisString() + "~";
		
		if (earliestLatestObservationDateTime != null)
			query += earliestLatestObservationDateTime.toOlisString() + "~";
		
		if (quantityLimitedRequest != null)
			query += quantityLimitedRequest.toOlisString() + "~";
		
		if (requestingHic != null)
			query += requestingHic.toOlisString() + "~";
		
		if (consentToViewBlockedInformation != null)
			query += consentToViewBlockedInformation.toOlisString() + "~";
		
		if (patientConsentBlockAllIndicator != null)
			query += patientConsentBlockAllIndicator.toOlisString() + "~";
		
		if (specimenCollector != null)
			query += specimenCollector.toOlisString() + "~";
		
		if (performingLaboratory != null)
			query += performingLaboratory.toOlisString() + "~";
		
		if (excludePerformingLaboratory != null)
			query += excludePerformingLaboratory.toOlisString() + "~";
		
		if (reportingLaboratory != null)
			query += reportingLaboratory.toOlisString() + "~";
		
		if (excludeReportingLaboratory != null)
			query += excludeReportingLaboratory.toOlisString() + "~";
		
		if (patientIdentifier != null)
			query += patientIdentifier.toOlisString() + "~";
		
		if (orderingPractitioner != null)
			query += orderingPractitioner.toOlisString() + "~";
		
		if (copiedToPractitioner != null)
			query += copiedToPractitioner.toOlisString() + "~";
		
		if (attendingPractitioner != null)
			query += attendingPractitioner.toOlisString() + "~";
		
		if (admittingPractitioner != null)
			query += admittingPractitioner.toOlisString() + "~";
		
		if (testResultPlacer != null)
			query += testResultPlacer.toOlisString() + "~";
		
		for (OBR4 testRequestCode : testRequestCodeList) {
			query += testRequestCode.toOlisString() + "~";
		}
		
		if (placerGroupNumber != null)
			query += placerGroupNumber.toOlisString() + "~";
		
		for (OBR25 testRequestStatus : testRequestStatusList) {
			query += testRequestStatus.toOlisString() + "~";
		}
		
		for (OBX3 testResultCode : testResultCodeList) {
			query += testResultCode.toOlisString() + "~";
		}
		
		if(query.endsWith("~")) {
			query = query.substring(0,query.length()-1);
		}
		
		return query;
		
	}

	public void setStartEndTimestamp(OBR22 startEndTimestamp) {
    	this.startEndTimestamp = startEndTimestamp;
    }

	public void setEarliestLatestObservationDateTime(OBR7 earliestLatestObservationDateTime) {
    	this.earliestLatestObservationDateTime = earliestLatestObservationDateTime;
    }

	public void setQuantityLimitedRequest(QRD7 quantityLimitedRequest) {
    	this.quantityLimitedRequest = quantityLimitedRequest;
    }

	public void setRequestingHic(ZRP1 requestingHic) {
    	this.requestingHic = requestingHic;
    }

	public void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation) {
    	this.consentToViewBlockedInformation = consentToViewBlockedInformation;
    }

	public void setPatientConsentBlockAllIndicator(ZPD3 patientConsentBlockAllIndicator) {
    	this.patientConsentBlockAllIndicator = patientConsentBlockAllIndicator;
    }

	public void setSpecimenCollector(ZBR3 specimenCollector) {
    	this.specimenCollector = specimenCollector;
    }

	public void setPerformingLaboratory(ZBR6 performingLaboratory) {
    	this.performingLaboratory = performingLaboratory;
    }

	public void setExcludePerformingLaboratory(ZBE6 excludePerformingLaboratory) {
    	this.excludePerformingLaboratory = excludePerformingLaboratory;
    }

	public void setReportingLaboratory(ZBR4 reportingLaboratory) {
    	this.reportingLaboratory = reportingLaboratory;
    }

	public void setExcludeReportingLaboratory(ZBE4 excludeReportingLaboratory) {
    	this.excludeReportingLaboratory = excludeReportingLaboratory;
    }

	public void setPatientIdentifier(PID3 patientIdentifier) {
    	this.patientIdentifier = patientIdentifier;
    }

	public void setOrderingPractitioner(OBR16 orderingPractitioner) {
    	this.orderingPractitioner = orderingPractitioner;
    }

	public void setCopiedToPractitioner(OBR28 copiedToPractitioner) {
    	this.copiedToPractitioner = copiedToPractitioner;
    }

	public void setAttendingPractitioner(PV17 attendingPractitioner) {
    	this.attendingPractitioner = attendingPractitioner;
    }

	public void setAdmittingPractitioner(PV117 admittingPractitioner) {
    	this.admittingPractitioner = admittingPractitioner;
    }

	public void setTestResultPlacer(ZBR2 testResultPlacer) {
    	this.testResultPlacer = testResultPlacer;
    }

	public void setPlacerGroupNumber(ORC4 placerGroupNumber) {
    	this.placerGroupNumber = placerGroupNumber;
    }
	
	public void addToTestRequestCodeList(OBR4 testRequestCode) {
		this.testRequestCodeList.add(testRequestCode);
	}
	
	public void addToTestRequestStatusList(OBR25 testRequestStatus) {
		this.testRequestStatusList.add(testRequestStatus);
	}
	
	public void addToTestResultCodeList(OBX3 testResultCode) {
		this.testResultCodeList.add(testResultCode);
	}

	@Override
    public QueryType getQueryType() {
	   return QueryType.Z01;
    }
	
}
