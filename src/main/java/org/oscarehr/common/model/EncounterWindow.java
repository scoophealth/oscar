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
package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="encounterWindow")
public class EncounterWindow extends AbstractModel<String>{

	@Id
	@Column(name="provider_no")
	private String providerNo;
	
	private int rowOneSize;
	
	private int rowTwoSize;
	
	private int presBoxSize;
	
	private int rowThreeSize;

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public int getRowOneSize() {
		return rowOneSize;
	}

	public void setRowOneSize(int rowOneSize) {
		this.rowOneSize = rowOneSize;
	}

	public int getRowTwoSize() {
		return rowTwoSize;
	}

	public void setRowTwoSize(int rowTwoSize) {
		this.rowTwoSize = rowTwoSize;
	}

	public int getPresBoxSize() {
		return presBoxSize;
	}

	public void setPresBoxSize(int presBoxSize) {
		this.presBoxSize = presBoxSize;
	}

	public int getRowThreeSize() {
		return rowThreeSize;
	}

	public void setRowThreeSize(int rowThreeSize) {
		this.rowThreeSize = rowThreeSize;
	}
	
	public String getId() {
		return getProviderNo();
	}
	
	public void setId(String id) {
		setProviderNo(id);
	}
	
}
