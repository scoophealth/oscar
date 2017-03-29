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
import javax.persistence.*;


/**
 * The persistent class for the preventionsBilling database table.
 * 
 */
@Entity
@Table(name="preventionsBilling")
public class PreventionsBilling extends AbstractModel<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=50)
	private String billingDxCode = "";

	@Column(nullable=false, length=50)
	private String billingServiceCodeAndUnit = "";

	@Column(nullable=false, length=50)
	private String billingType = "";

	@Column(nullable=false, length=50)
	private String preventionType = "";

	@Column(nullable=false, length=10)
	private String sliCode = "";

	@Column(nullable=false, length=50)
	private String visitLocation = "";

	@Column(nullable=false, length=50)
	private String visitType = "";

	public PreventionsBilling() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBillingDxCode() {
		return this.billingDxCode;
	}

	public void setBillingDxCode(String billingDxCode) {
		this.billingDxCode = billingDxCode;
	}

	public String getBillingServiceCodeAndUnit() {
		return this.billingServiceCodeAndUnit;
	}

	public void setBillingServiceCodeAndUnit(String billingServiceCodeAndUnit) {
		this.billingServiceCodeAndUnit = billingServiceCodeAndUnit;
	}

	public String getBillingType() {
		return this.billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getPreventionType() {
		return this.preventionType;
	}

	public void setPreventionType(String preventionType) {
		this.preventionType = preventionType;
	}

	public String getSliCode() {
		return this.sliCode;
	}

	public void setSliCode(String sliCode) {
		this.sliCode = sliCode;
	}

	public String getVisitLocation() {
		return this.visitLocation;
	}

	public void setVisitLocation(String visitLocation) {
		this.visitLocation = visitLocation;
	}

	public String getVisitType() {
		return this.visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

}