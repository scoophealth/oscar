/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.model;

import java.util.Date;

/**
 *
 * 
   create table flowsheet_customization(
      id int(10) NOT NULL auto_increment primary key,  
      flowsheet varchar(40),
      action varchar(10),
      measurement varchar(255),
      payload text,
      provider_no varchar(6),
      demographic_no int(10),
      create_date datetime,
      archived char(1) default '0',
      archived_date datetime
      
     
    ) ; 
    
 * 
 * 
 * @author jaygallagher
 */
public class FlowSheetCustomization {
    public static final String ADD = "add";
    public static final String DELETE = "delete";
    public static final String UPDATE = "update";
    
    private long    id;
    private String flowsheet = null;
    private String measurement = null;
    private String payload = null;
    private String action = null;
    private String providerNo = null;
    private String demographicNo = null;
    private boolean archived = false;
    private Date createDate = null;
    private Date archivedDate = null;

    
    public String toString(){
        return   " id:"+id+ " flowsheet:" +flowsheet+ " measurement:" + measurement + " payload:" + payload +  " action:"  + action +  " providerNo:"  +providerNo +" demographicNo:"+ demographicNo+" createDate:"+createDate+" archived:"+archived;
    
    }
        
    public String getFlowsheet() {
        return flowsheet;
    }

    public void setFlowsheet(String flowsheet) {
        this.flowsheet = flowsheet;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public String getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }

}
