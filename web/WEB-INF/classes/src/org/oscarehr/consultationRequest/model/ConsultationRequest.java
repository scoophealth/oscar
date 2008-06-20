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
 * Jason Gallagher
 *
 * UserProperty.java
 *
 * Created on December 19, 2007, 4:30 PM
 *
 *
 *
 */

package org.oscarehr.consultationRequest.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Jay Gallagher
 */
public class ConsultationRequest implements Serializable {
   
    private long id;
    
    
    private Date referalDate;//	date	YES			
    private long serviceId;//	int(10)	YES			
    private long specId;//	int(10)	YES			
    private Date appointmentDate;//	date	YES			
    private Date appointmentTime;//	time	YES			
    private String reason;//	text	YES	MUL		
    private String clinicalInfo;//	text	YES			
    private String currentMeds;//	text	YES			
    private String allergies;//	text	YES			
    private String providerNo;//	varchar(6)	YES			
    private long demographicNo;//	int(10)	YES	MUL		
    private String status;//	char(2)	YES			
    private String statusText;//	text	YES			
    private String sendTo;//	varchar(20)	YES			
    //private long id;	//int(10)		PRI		auto_increment
    private String concurrentProblems;//	text	YES			
    private String urgency;//	char(2)	YES			
    private long patientWillBook;//	tinyint(1)	YES		
    
    /** Creates a new instance of UserProperty */
    public ConsultationRequest() {
    }

    
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    
    
    public Date getReferalDate() {
        return referalDate;
    }

    public void setReferalDate(Date referalDate) {
        this.referalDate = referalDate;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getSpecId() {
        return specId;
    }

    public void setSpecId(long specId) {
        this.specId = specId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClinicalInfo() {
        return clinicalInfo;
    }

    public void setClinicalInfo(String clinicalInfo) {
        this.clinicalInfo = clinicalInfo;
    }

    public String getCurrentMeds() {
        return currentMeds;
    }

    public void setCurrentMeds(String currentMeds) {
        this.currentMeds = currentMeds;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }
//
    public long getDemographicNo() {
        return demographicNo;
    }
//
    public void setDemographicNo(long demographicNo) {
        this.demographicNo = demographicNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getConcurrentProblems() {
        return concurrentProblems;
    }

    public void setConcurrentProblems(String concurrentProblems) {
        this.concurrentProblems = concurrentProblems;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public long getPatientWillBook() {
        return patientWillBook;
    }

    public void setPatientWillBook(long patientWillBook) {
        this.patientWillBook = patientWillBook;
    }

    
    
    
}
