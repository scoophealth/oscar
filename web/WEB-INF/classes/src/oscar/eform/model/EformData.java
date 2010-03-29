/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */
package oscar.eform.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class EformData implements Serializable {
    private Integer fdid;
    private Integer fid;
    private String  form_name;
    private String  subject;
    private Integer demographic_no;
    private Boolean status;
    private Date    form_date;
    private Time    form_time;
    private String  form_provider;
    private String  form_data;

    public Integer getFdid() {
        return this.fdid;
    }

    public Integer getFid() {
        return this.fid;
    }

    public String getForm_name() {
        return this.form_name;
    }

    public String getSubject() {
        return this.subject;
    }

    public Integer getDemographic_no() {
        return this.demographic_no;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Date getForm_date() {
        return this.form_date;
    }

    public Time getForm_time() {
        return this.form_time;
    }

    public String getForm_provider() {
        return this.form_provider;
    }

    public String getForm_data() {
        return this.form_data;
    }

    public void setFdid(Integer fdid) {
        this.fdid = fdid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDemographic_no(Integer demographic_no) {
        this.demographic_no = demographic_no;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setForm_date(Date form_date) {
        this.form_date = form_date;
    }

    public void setForm_time(Time form_time) {
        this.form_time = form_time;
    }

    public void setForm_provider(String form_provider) {
        this.form_provider = form_provider;
    }

    public void setForm_data(String form_data) {
        this.form_data = form_data;
    }
}
