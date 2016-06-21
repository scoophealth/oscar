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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author rjonasz
 */
@Entity
@Table(name="resident_oscarMsg")
public class ResidentOscarMsg extends AbstractModel<Long>implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String supervisor_no;
    private String resident_no;
    private Integer demographic_no;
    private Boolean complete;
    private Integer appointment_no;
    private Long note_id;
    
    @Temporal( TemporalType.TIMESTAMP )
    private Date create_time;
    
    @Temporal( TemporalType.TIMESTAMP )
    private Date complete_time;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResidentOscarMsg)) {
            return false;
        }
        ResidentOscarMsg other = (ResidentOscarMsg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * @return the supervisor_no
     */
    public String getSupervisor_no() {
        return supervisor_no;
    }

    /**
     * @param supervisor_no the supervisor_no to set
     */
    public void setSupervisor_no(String supervisor_no) {
        this.supervisor_no = supervisor_no;
    }

    /**
     * @return the resident_no
     */
    public String getResident_no() {
        return resident_no;
    }

    /**
     * @param resident_no the resident_no to set
     */
    public void setResident_no(String resident_no) {
        this.resident_no = resident_no;
    }

    /**
     * @return the complete
     */
    public Boolean getComplete() {
        return complete;
    }

    /**
     * @param complete the complete to set
     */
    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    /**
     * @return the create_time
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * @param create_time the create_time to set
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    /**
     * @return the complete_time
     */
    public Date getComplete_time() {
        return complete_time;
    }

    /**
     * @param complete_time the complete_time to set
     */
    public void setComplete_time(Date complete_time) {
        this.complete_time = complete_time;
    }

    /**
     * @return the demographic_no
     */
    public Integer getDemographic_no() {
        return demographic_no;
    }

    /**
     * @param demographic_no the demographic_no to set
     */
    public void setDemographic_no(Integer demographic_no) {
        this.demographic_no = demographic_no;
    }

    /**
     * @return the appointment_no
     */
    public Integer getAppointment_no() {
        return appointment_no;
    }

    /**
     * @param appointment_no the appointment_no to set
     */
    public void setAppointment_no(Integer appointment_no) {
        this.appointment_no = appointment_no;
    }

    /**
     * @return the note_id
     */
    public Long getNote_id() {
        return note_id;
    }

    /**
     * @param note_id the note_id to set
     */
    public void setNote_id(Long note_id) {
        this.note_id = note_id;
    }

}
