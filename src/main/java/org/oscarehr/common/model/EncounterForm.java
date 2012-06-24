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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "encounterForm")
public class EncounterForm extends AbstractModel<String> implements Serializable {

	/**
	 * This comparator sorts By FormName but puts BC form names first
	 */
	public static final Comparator<EncounterForm> BC_FIRST_COMPARATOR = new Comparator<EncounterForm>() {
		public int compare(EncounterForm o1, EncounterForm o2) {
			if (o1.formName.startsWith("BC") && o2.formName.startsWith("BC")) return (o1.formName.compareTo(o2.formName));
			else if (o1.formName.startsWith("BC")) return (-1);
			else if (o2.formName.startsWith("BC")) return (1);
			else return (o1.formName.compareTo(o2.formName));
		}
	};

	/**
	 * This comparator sorts EncounterForm ascending based on the formName
	 */
	public static final Comparator<EncounterForm> FORM_NAME_COMPARATOR = new Comparator<EncounterForm>() {
		public int compare(EncounterForm o1, EncounterForm o2) {
			return (o1.formName.compareTo(o2.formName));
		}
	};

	@Id
	@Column(name = "form_value")
	private String formValue;

	@Column(name = "form_name")
	private String formName;

	@Column(name = "form_table")
	private String formTable;

	// yes the column name is wrong, if hidden=0 it means do not display, if hidden>0 it means to show it and it is the order in which they should be displayed
	@Column(name = "hidden")
	private int displayOrder;

	@Override
	public String getId() {
		return (formValue);
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormTable() {
		return formTable;
	}

	public void setFormTable(String formTable) {
		this.formTable = formTable;
	}

	public String getFormValue() {
		return formValue;
	}

	public void setFormValue(String formValue) {
		this.formValue = formValue;
	}

	public int getDisplayOrder() {
    	return displayOrder;
    }

	public void setDisplayOrder(int displayOrder) {
    	this.displayOrder = displayOrder;
    }
	
	public boolean isHidden()
	{
		return(displayOrder==0);
	}
}
