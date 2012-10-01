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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quickList")
public class QuickList extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	private String quickListName;

	private String createdByProvider;

	private String dxResearchCode;

	private String codingSystem;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getQuickListName() {
    	return quickListName;
    }

	public void setQuickListName(String quickListName) {
    	this.quickListName = quickListName;
    }

	public String getCreatedByProvider() {
    	return createdByProvider;
    }

	public void setCreatedByProvider(String createdByProvider) {
    	this.createdByProvider = createdByProvider;
    }

	public String getDxResearchCode() {
    	return dxResearchCode;
    }

	public void setDxResearchCode(String dxResearchCode) {
    	this.dxResearchCode = dxResearchCode;
    }

	public String getCodingSystem() {
    	return codingSystem;
    }

	public void setCodingSystem(String codingSystem) {
    	this.codingSystem = codingSystem;
    }


}
