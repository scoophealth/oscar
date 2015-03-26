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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;

import java.util.List;

/**
 *
 * @author rjonasz
 */
@Entity
@Table(name = "consultationServices")
public class ConsultationServices extends AbstractModel<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer serviceId;
    private String serviceDesc;
    private String active;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable( name="serviceSpecialists", joinColumns=@JoinColumn(name="serviceId"), inverseJoinColumns = @JoinColumn(name="specId") )
    private List<ProfessionalSpecialist> specialists;

    public ConsultationServices() {}
    
    public ConsultationServices(String serviceDesc) {
    	setServiceDesc(serviceDesc);
    }

    @Override
    public Integer getId() {
	    return(serviceId);
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceId != null ? serviceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsultationServices)) {
            return false;
        }
        ConsultationServices other = (ConsultationServices) object;
        if ((this.serviceId == null && other.serviceId != null) || (this.serviceId != null && !this.serviceId.equals(other.serviceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.oscarehr.common.model.ConsultationServices[serviceId=" + serviceId + "]";
    }

    /**
     * @return the specialists
     */
    public List<ProfessionalSpecialist> getSpecialists() {
        return specialists;
    }

    /**
     * @param specialists the specialists to set
     */
    public void setSpecialists(List<ProfessionalSpecialist> specialists) {
        this.specialists = specialists;
    }

}
