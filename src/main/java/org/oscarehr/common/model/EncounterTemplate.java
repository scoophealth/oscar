/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "encountertemplate")
public class EncounterTemplate extends AbstractModel<String> implements Serializable {

	/**
	 * This comparator sorts By FormName but puts BC form names first
	 */
	public static final Comparator<EncounterTemplate> NAME_COMPARATOR = new Comparator<EncounterTemplate>() {
		public int compare(EncounterTemplate o1, EncounterTemplate o2) {
			if (o1.encounterTemplateName.startsWith("BC") && o2.encounterTemplateName.startsWith("BC")) return (o1.encounterTemplateName.compareTo(o2.encounterTemplateName));
			else if (o1.encounterTemplateName.startsWith("BC")) return (-1);
			else if (o2.encounterTemplateName.startsWith("BC")) return (1);
			else return (o1.encounterTemplateName.compareTo(o2.encounterTemplateName));
		}
	};

	@Id
	@Column(name = "encountertemplate_name")
	private String encounterTemplateName;

	@Column(name = "createdatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "encountertemplate_value")
	private String encounterTemplateValue;

	@Column(name = "creator")
	private String creatorProviderNo;

	@Override
	public String getId() {
		return (encounterTemplateName);
	}

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
