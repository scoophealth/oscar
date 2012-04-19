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
import java.sql.Time;
import java.util.Date;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class OscarAppointment implements Serializable {
    private Integer appointment_no;
    private String  provider_no;
    private Date    appointment_date;
    private Time    start_time;
    private Time    end_time;
    private String  name;
    private Integer demographic_no;
    private Integer program_id;
    private String  notes;
    private String  reason;
    private String  location;
    private String  resources;
    private String  type;
    private String  style;
    private String  billing;
    private String  status;
    private Date    createdatetime;
    private Date    updatedatetime;
    private String  creator;
    private String  lastupdateuser;
    private String  remarks;

    public Integer getAppointment_no() {
        return this.appointment_no;
    }

    public String getProvider_no() {
        return this.provider_no;
    }

    public Date getAppointment_date() {
        return this.appointment_date;
    }

    public Time getStart_time() {
        return this.start_time;
    }

    public Time getEnd_time() {
        return this.end_time;
    }

    public String getName() {
        return this.name;
    }

    public Integer getDemographic_no() {
        return this.demographic_no;
    }

    public Integer getProgram_id() {
        return this.program_id;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getReason() {
        return this.reason;
    }

    public String getLocation() {
        return this.location;
    }

    public String getResources() {
        return this.resources;
    }

    public String getType() {
        return this.type;
    }

    public String getStyle() {
        return this.style;
    }

    public String getBilling() {
        return this.billing;
    }

    public String getStatus() {
        return this.status;
    }

    public Date getCreatedatetime() {
        return this.createdatetime;
    }

    public Date getUpdatedatetime() {
        return this.updatedatetime;
    }

    public String getCreator() {
        return this.creator;
    }

    public String getLastupdateuser() {
        return this.lastupdateuser;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setAppointment_no(Integer appointment_no) {
        this.appointment_no = appointment_no;
    }

    public void setProvider_no(String provider_no) {
        this.provider_no = provider_no;
    }

    public void setAppointment_date(Date appointment_date) {
        this.appointment_date = appointment_date;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDemographic_no(Integer demographic_no) {
        this.demographic_no = demographic_no;
    }

    public void setProgram_id(Integer program_id) {
        this.program_id = program_id;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedatetime(Date createdatetime) {
        this.createdatetime = createdatetime;
    }

    public void setUpdatedatetime(Date updatedatetime) {
        this.updatedatetime = updatedatetime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setLastupdateuser(String lastupdateuser) {
        this.lastupdateuser = lastupdateuser;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
