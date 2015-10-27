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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

/**
 *
 * @author apavel
 */
@Entity
@Table(name="dsGuidelineProviderMap")
public class DSGuidelineProviderMapping extends AbstractModel<Integer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="mapid")
    private Integer id;
	
	@Column(name="provider_no",nullable=false)
    private String providerNo;
	
	@Column(name="guideline_uuid",length=60,nullable=false)
    private String guidelineUUID;

    public DSGuidelineProviderMapping() {
        
    }

    public DSGuidelineProviderMapping(String guidelineUUID, String providerNo) {
        this.guidelineUUID = guidelineUUID;
        this.providerNo = providerNo;
    }

   

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((guidelineUUID == null) ? 0 : guidelineUUID.hashCode());
		result = prime * result + ((providerNo == null) ? 0 : providerNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		DSGuidelineProviderMapping other = (DSGuidelineProviderMapping) obj;
		if (guidelineUUID == null) {
			if (other.guidelineUUID != null) return false;
		} else if (!guidelineUUID.equals(other.guidelineUUID)) return false;
		if (providerNo == null) {
			if (other.providerNo != null) return false;
		} else if (!providerNo.equals(other.providerNo)) return false;
		return true;
	}

	/**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the providerNo
     */
    public String getProviderNo() {
        return providerNo;
    }

    /**
     * @param providerNo the providerNo to set
     */
    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    /**
     * @return the guidelineUUID
     */
    public String getGuidelineUUID() {
        return guidelineUUID;
    }

    /**
     * @param guidelineUUID the guidelineUUID to set
     */
    public void setGuidelineUUID(String guidelineUUID) {
        this.guidelineUUID = guidelineUUID;
    }

}
