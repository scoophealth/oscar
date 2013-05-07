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
package org.oscarehr.research.eaaps;

import oscar.oscarDemographic.pageUtil.DemographicExportHelper;
import cds.OmdCdsDocument;

/**
 * Intermediate export information holder.
 */
class ExportEntry {

	private OmdCdsDocument doc;
	private Integer demographicId;
	private String providerNo;
	private String entryId;
	private String hash;

	public ExportEntry() {
		super();
	}
	
	public ExportEntry(OmdCdsDocument doc, Integer demographicId, String entryId, String hash, String providerNo) {
	    this();
	    this.doc = doc;
	    this.demographicId = demographicId;
	    this.entryId = entryId;
	    this.hash = hash;
	    this.providerNo = providerNo;
    }

	public String getDocContent() {
		return getDoc().xmlText(DemographicExportHelper.getDefaultOptions());
	}
	
	public OmdCdsDocument getDoc() {
		return doc;
	}

	public void setDoc(OmdCdsDocument doc) {
		this.doc = doc;
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

}
