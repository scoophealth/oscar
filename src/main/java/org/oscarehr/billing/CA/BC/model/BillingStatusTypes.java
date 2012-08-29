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
package org.oscarehr.billing.CA.BC.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "billingstatus_types")
public class BillingStatusTypes extends AbstractModel<Character> {

	@Id
	@Column(name = "billingstatus", unique = true, nullable = false, length = 1)
	private char id;
	@Column(name = "displayName", length = 20)
	private String displayName;
	@Column(name = "displayNameExt", length = 50)
	private String displayNameExt;
	@Column(name = "sortOrder", nullable = false)
	private int sortOrder;

	public BillingStatusTypes() {
	}

	public BillingStatusTypes(char billingstatus, int sortOrder) {
		this.id = billingstatus;
		this.sortOrder = sortOrder;
	}

	public BillingStatusTypes(char billingstatus, String displayName, String displayNameExt, int sortOrder) {
		this.id = billingstatus;
		this.displayName = displayName;
		this.displayNameExt = displayNameExt;
		this.sortOrder = sortOrder;
	}

	public Character getId() {
		return this.id;
	}

	public void setId(Character billingstatus) {
		this.id = billingstatus;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayNameExt() {
		return this.displayNameExt;
	}

	public void setDisplayNameExt(String displayNameExt) {
		this.displayNameExt = displayNameExt;
	}

	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
