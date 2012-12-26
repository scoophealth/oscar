/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.caseload;

public enum CaseloadCategory {
	Demographic("Demographic", "cl_search_demographic_query", "last_name"),
	Age("Age", "cl_search_demographic_query", "age"),
	Sex("Sex", "cl_search_demographic_query", "sex"),
	LastAppt("Last Appt", "cl_search_last_appt", "appointment_date" ),
	NextAppt("Next Appt", "cl_search_next_appt", "appointment_date" ),
	ApptsLYTD("Appts LYTD", "cl_search_num_appts", "count"),
	Lab("Lab", "cl_search_new_labs", "count"),
	Doc("Doc", "cl_search_new_docs", "count"),
	Tickler("Tickler", "cl_search_new_ticklers", "count"),
	Msg("Msg", "cl_search_new_msgs", "count"),
	BMI("BMI", "cl_search_measurement", "dataField"),
	BP("BP", "cl_search_measurement", "dataField"),
	WT("WT", "cl_search_measurement", "dataField"),
	SMK("SMK", "cl_search_measurement", "dataField"),
	A1C("A1C", "cl_search_measurement", "dataField"),
	ACR("ACR", "cl_search_measurement", "dataField"),
	SCR("SCR", "cl_search_measurement", "dataField"),
	LDL("LDL", "cl_search_measurement", "dataField"),
	HDL("HDL", "cl_search_measurement", "dataField"),
	TCHD("TCHD", "cl_search_measurement", "dataField"),
	EGFR("EGFR", "cl_search_measurement", "dataField"),
	EYEE("EYEE", "cl_search_measurement", "dataField"),
	LastEncounterDate("LastEncounterDate", "cl_search_lastencdate", "update_date"),
	LastEncounterType("LastEncounterType", "cl_search_lastenctype", "encounter_type"),
	CashAdmissionDate("[CASH]AdmissionDate", "cl_search_cashaddate", "referral_date"),
	Access1AdmissionDate("[ACCESS1]AdmissionDate", "cl_search_access1addate", "referral_date");
	
	/** The label for this category. */
	private String label;
	
	/** The database query for this category. */
	private String query;

	/** The field used to sort this query. */
	private String field;
	
	/** Retrieves the labels associated with this category. */
	public String getLabel() { return label; }
	
	/** Retrieve the query associated with this category. */
	public String getQuery() {
		return query;
	}

	/** Retrieve the sort field associated with this category. */
	public Object getField() {
		return field;
	}
	
	/** Create a category with a label and an associated database query. */
	private CaseloadCategory(String label, String query, String field) { 
		this.label = label;
		this.query = query;
		this.field = field;
	}
	
	/** Retrieves a caseload category. */
	public static CaseloadCategory getCategory(String label) {
		for (CaseloadCategory cg : CaseloadCategory.values()) {
			if (cg.label.equals(label)) { return cg; }			
		}
		return null;
	}

	/** Indicates if this category is a measurement */
	public boolean isMeasurement() {
		switch(this) {			
			case BMI: 
			case BP: 
			case WT: 
			case SMK: 
			case A1C: 
			case ACR: 
			case SCR: 
			case LDL: 
			case HDL: 
			case TCHD: 
			case EGFR: 
			case EYEE: 
				return true;
			default:
				return false;
		}
	}
}
