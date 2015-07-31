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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.Date;

public class EncounterTemplateTo1 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String encounterTemplateName;

	private Date createdDate;

	private String encounterTemplateValue;

	private String creatorProviderNo;

	public String getEncounterTemplateName() {
		return encounterTemplateName;
	}

	public void setEncounterTemplateName(String encounterTemplateName) {
		this.encounterTemplateName = encounterTemplateName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getEncounterTemplateValue() {
		return encounterTemplateValue;
	}

	public void setEncounterTemplateValue(String encounterTemplateValue) {
		this.encounterTemplateValue = encounterTemplateValue;
	}

	public String getCreatorProviderNo() {
		return creatorProviderNo;
	}

	public void setCreatorProviderNo(String creatorProviderNo) {
		this.creatorProviderNo = creatorProviderNo;
	}

	

}
