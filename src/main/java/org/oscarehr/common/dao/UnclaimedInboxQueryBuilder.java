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
package org.oscarehr.common.dao;

class UnclaimedInboxQueryBuilder {
	
	private boolean mixLabsAndDocs;
	private boolean isPaged;
	private int page;
	private int pageSize;

	public String buildQuery() {
	    String whereSubclauseForUnclamedLabs = "(provRt.provider_no = 0 OR provRt.provider_no = -1 OR provRt.provider_no IS NULL)";
	    StringBuilder buf = new StringBuilder();
	    buf.append(
			  "(SELECT provRt.id, provRt.lab_no as document_no, hl7ti.result_status as status, provRt.lab_type as doctype, hl7ti.last_name, hl7ti.first_name, hl7ti.health_no as hin, hl7ti.sex, demo.demographic_no as module_id, hl7ti.obr_date as observationdate "
			+ "FROM hl7TextInfo AS hl7ti "
			+ "	LEFT JOIN patientLabRouting AS patRt ON hl7ti.lab_no = patRt.lab_no " 
			+ "		JOIN providerLabRouting AS provRt ON hl7ti.lab_no = provRt.lab_no"
			+ "			LEFT JOIN demographic AS demo ON patRt.demographic_no = demo.demographic_no WHERE "
			+ whereSubclauseForUnclamedLabs + " AND provRt.lab_type != 'DOC')"
		);
	    
	    if (mixLabsAndDocs) {
	    	buf.append(
			" UNION ALL "
			+ "(SELECT provRt.id, provRt.lab_no, provRt.status, provRt.lab_type as doctype, demo.last_name, demo.first_name, demo.hin, demo.sex, demo.demographic_no as module_id, doc.observationdate"
			+ " FROM providerLabRouting AS provRt "
				+ "	JOIN ctl_document AS ctlDoc ON provRt.lab_no = ctlDoc.document_no "
				+ "		JOIN document AS doc ON provRt.lab_no = doc.document_no "
				+ "			LEFT JOIN demographic AS demo ON ctlDoc.module_id = demo.demographic_no WHERE "
				+ whereSubclauseForUnclamedLabs + " AND provRt.lab_type = 'DOC')"
			);
	    }
	    
	    buf.append(" ORDER BY id DESC ");
	    if (isPaged) {
	    	buf.append("	LIMIT " + (page * pageSize) + "," + pageSize);
	    }
	    
	    return buf.toString();
	}

	public boolean isMixLabsAndDocs() {
		return mixLabsAndDocs;
	}

	public void setMixLabsAndDocs(boolean mixLabsAndDocs) {
		this.mixLabsAndDocs = mixLabsAndDocs;
	}

	public boolean isPaged() {
		return isPaged;
	}

	public void setPaged(boolean isPaged) {
		this.isPaged = isPaged;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
