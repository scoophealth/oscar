/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="dxresearch")
public class Dxresearch  extends AbstractModel<Integer> implements java.io.Serializable {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="dxresearch_no")
    private Integer dxresearchNo;
	@Column(name="demographic_no")
    private Integer demographicNo;
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
    private Date startDate;
	@Column(name="update_date")
	@Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private Character status;
    @Column(name="dxresearch_code")
    private String dxresearchCode;
    @Column(name="coding_system")
    private String codingSystem;
    private byte association;

    private String providerNo;
    
    public Dxresearch() {
    }


    public Dxresearch(byte association) {
        this.association = association;
    }
    public Dxresearch(Integer demographicNo, Date startDate, Date updateDate, Character status, String dxresearchCode, String codingSystem, byte association, String providerNo) {
       this.demographicNo = demographicNo;
       this.startDate = startDate;
       this.updateDate = updateDate;
       this.status = status;
       this.dxresearchCode = dxresearchCode;
       this.codingSystem = codingSystem;
       this.association = association;
       this.providerNo = providerNo;
    }

    public Integer getId() {
    	return getDxresearchNo();
    }

    public Integer getDxresearchNo() {
        return this.dxresearchNo;
    }

    public void setDxresearchNo(Integer dxresearchNo) {
        this.dxresearchNo = dxresearchNo;
    }
    public Integer getDemographicNo() {
        return this.demographicNo;
    }

    public void setDemographicNo(Integer demographicNo) {
        this.demographicNo = demographicNo;
    }
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Character getStatus() {
        return this.status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
    public String getDxresearchCode() {
        return this.dxresearchCode;
    }

    public void setDxresearchCode(String dxresearchCode) {
        this.dxresearchCode = dxresearchCode;
    }
    public String getCodingSystem() {
        return this.codingSystem;
    }

    public void setCodingSystem(String codingSystem) {
        this.codingSystem = codingSystem;
    }
    public byte getAssociation() {
        return this.association;
    }

    public void setAssociation(byte association) {
        this.association = association;
    }
    
    

    public String getProviderNo() {
		return providerNo;
	}


	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}


	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dxresearch other = (Dxresearch) obj;
        if (this.dxresearchNo != other.dxresearchNo && (this.dxresearchNo == null || !this.dxresearchNo.equals(other.dxresearchNo))) {
            return false;
        }
        if (this.demographicNo != other.demographicNo && (this.demographicNo == null || !this.demographicNo.equals(other.demographicNo))) {
            return false;
        }
        if (this.startDate != other.startDate && (this.startDate == null || !this.startDate.equals(other.startDate))) {
            return false;
        }
        if (this.updateDate != other.updateDate && (this.updateDate == null || !this.updateDate.equals(other.updateDate))) {
            return false;
        }
        if ((this.dxresearchCode == null) ? (other.dxresearchCode != null) : !this.dxresearchCode.equals(other.dxresearchCode)) {
            return false;
        }
        if ((this.codingSystem == null) ? (other.codingSystem != null) : !this.codingSystem.equals(other.codingSystem)) {
            return false;
        }
        
        if ((this.providerNo == null) ? (other.providerNo != null) : !this.providerNo.equals(other.providerNo)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.dxresearchNo != null ? this.dxresearchNo.hashCode() : 0);
        hash = 67 * hash + (this.demographicNo != null ? this.demographicNo.hashCode() : 0);
        hash = 67 * hash + (this.startDate != null ? this.startDate.hashCode() : 0);
        hash = 67 * hash + (this.updateDate != null ? this.updateDate.hashCode() : 0);
        hash = 67 * hash + (this.dxresearchCode != null ? this.dxresearchCode.hashCode() : 0);
        hash = 67 * hash + (this.codingSystem != null ? this.codingSystem.hashCode() : 0);
        hash = 67 * hash + (this.providerNo != null ? this.providerNo.hashCode() : 0);
        return hash;
    }


    @PrePersist
	@PreUpdate
	protected void jpaUpdateDate() {
		this.updateDate = new Date();
	}

}
