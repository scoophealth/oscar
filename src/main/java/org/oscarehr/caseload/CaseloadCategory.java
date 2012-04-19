/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
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
	EYEE("EYEE", "cl_search_measurement", "dataField");	
	
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
