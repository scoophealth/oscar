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

    @Override  //must have same hashcode, but oh well
    public boolean equals(Object object2) {
        DSGuidelineProviderMapping mapping2 = (DSGuidelineProviderMapping) object2;
        if (mapping2.getProviderNo().equals(this.getProviderNo()) && mapping2.getGuidelineUUID().equals(this.getGuidelineUUID())) {
            return true;
        }
        return false;
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
