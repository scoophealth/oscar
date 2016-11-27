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
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "functionalCentreAdmission")
@Entity
public class FunctionalCentreAdmission extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
 
	private Integer demographicNo;
	private String functionalCentreId;
	
	@Temporal(TemporalType.DATE)
	private Date referralDate;
	
	@Temporal(TemporalType.DATE)
	private Date admissionDate;
	
	@Temporal(TemporalType.DATE)
	private Date serviceInitiationDate;
	
	@Temporal(TemporalType.DATE)
	private Date dischargeDate = null;
	
	private boolean discharged = false;
	private String providerNo;
	
	@Temporal(TemporalType.DATE)
	private Date updateDate;
	
	private String dischargeReason;
	
	public FunctionalCentreAdmission() {
		updateDate = new Date();
		discharged = false;		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getFunctionalCentreId() {
    	return functionalCentreId;
    }

	public void setFunctionalCentreId(String functionalCentreId) {
    	this.functionalCentreId = functionalCentreId;
    }

	public Date getReferralDate() {
    	return referralDate;
    }

	public void setReferralDate(Date referralDate) {
    	this.referralDate = referralDate;
    }

	public Date getAdmissionDate() {
    	return admissionDate;
    }

	public void setAdmissionDate(Date admissionDate) {
    	this.admissionDate = admissionDate;
    }

	public Date getServiceInitiationDate() {
    	return serviceInitiationDate;
    }	
	
	public void setServiceInitiationDate(Date serviceInitiationDate) {
    	this.serviceInitiationDate = serviceInitiationDate;
    }
/*
	public void setServiceInitiationDate(String serviceInitiationDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(serviceInitiationDate!=null && serviceInitiationDate.length()>=10)
				this.serviceInitiationDate = formatter.parse(serviceInitiationDate.substring(0, 10));
		} catch (ParseException ex) {
			MiscUtils.getLogger().error("Error", ex);
		}
    }
	*/
	public Date getDischargeDate() {
    	return dischargeDate;
    }

	public void setDischargeDate(Date dischargeDate) {
    	this.dischargeDate = dischargeDate;
    }

	public GregorianCalendar getAdmissionCalendar()
	{
		GregorianCalendar cal=new GregorianCalendar();
		cal.setTime(admissionDate);
		return(cal);
	}
	
	public GregorianCalendar getDischargeCalendar()
	{
		if (dischargeDate==null) return(null);
		
		GregorianCalendar cal=new GregorianCalendar();
		cal.setTime(dischargeDate);
		return(cal);
	}
	
	public boolean isDischarged() {
    	return discharged;
    }

	public void setDischarged(boolean discharged) {
    	this.discharged = discharged;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getUpdateDate() {
    	return updateDate;
    }

	public void setUpdateDate(Date updateDate) {
    	this.updateDate = updateDate;
    }

	public String getDischargeReason() {
    	return dischargeReason;
    }

	public void setDischargeReason(String dischargeReason) {
    	this.dischargeReason = dischargeReason;
    }
	
	
}
