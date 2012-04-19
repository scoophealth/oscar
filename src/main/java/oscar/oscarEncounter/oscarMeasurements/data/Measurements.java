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


package oscar.oscarEncounter.oscarMeasurements.data;

import java.util.Date;

public class Measurements {
    private Long    id;
    private String  type;
    private Long    demographicNo;
    private String  providerNo;
    private String  dataField;
    private String  measuringInstruction;
    private String  comments;
    private Date    dateObserved;
    private Date    dateEntered;
    
    /** Creates a new instance of Measurements */
    public Measurements(Long demographicNo) {
	this.setDemographicNo(demographicNo);
    }
    
    public Measurements(Long demographicNo, String providerNo) {
        this.setDemographicNo(demographicNo);
        this.setProviderNo(providerNo);
    }
    
    public Measurements() {}
    
    
    
    public Long getId() {
	return this.id;
    }
    public void setId(Long id) {
	this.id = id;
    }
    
    public String getType() {
	return this.type;
    }
    public void setType(String type) {
	this.type = type;
    }
    
    public Long getDemographicNo() {
	return this.demographicNo;
    }
    public void setDemographicNo(Long demographicNo) {
	this.demographicNo = demographicNo;
    }
    
    public String getProviderNo() {
	return this.providerNo;
    }
    public void setProviderNo(String providerNo) {
	this.providerNo = providerNo;
    }
    
    public String getDataField() {
	return this.dataField;
    }
    public void setDataField(String dataField) {
	this.dataField = dataField;
    }
    
    public String getMeasuringInstruction() {
	return this.measuringInstruction;
    }
    public void setMeasuringInstruction(String measuringInstruction) {
	this.measuringInstruction = measuringInstruction;
    }
    
    public String getComments() {
	return this.comments;
    }
    public void setComments(String comments) {
	this.comments = comments;
    }
    
    public Date getDateObserved() {
	return this.dateObserved;
    }
    public void setDateObserved(Date dateObserved) {
	this.dateObserved = dateObserved;
    }
    
    public Date getDateEntered() {
	return this.dateEntered;
    }
    public void setDateEntered(Date dateEntered) {
	this.dateEntered = dateEntered;
    }
}
