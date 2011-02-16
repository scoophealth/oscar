/*
 * Copyright (c) 2010. Department of Family Medicine, McMaster University. All Rights Reserved.
 * 
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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.PostLoad;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CollectionOfElements;

import oscar.OscarProperties;

@Entity
public class ProviderPreference extends AbstractModel<String> implements Serializable {

	@Id
	private String providerNo;
	private Integer startHour=8;
	private Integer endHour=18;
	private Integer everyMin=15;
	private String myGroupNo = null;	
	private String colourTemplate="deepblue";
	private String newTicklerWarningWindow="disabled";
	private String defaultServiceType="no";
	private String defaultCaisiPmm="disabled";
	private String defaultNewOscarCme="disabled";
	private boolean printQrCodeOnPrescriptions=Boolean.valueOf(OscarProperties.getInstance().getProperty("QR_CODE_ENABLED_PROVIDER_DEFAULT"));
	private int appointmentScreenFormNameDisplayLength=3;
	
	@CollectionOfElements(targetElement = String.class)
	@JoinTable(name = "ProviderPreferenceAppointmentScreenForm",joinColumns = @JoinColumn(name = "providerNo"))
	@Column(name="appointmentScreenForm")
	private Collection<String> appointmentScreenForms=new HashSet<String>();
	
	@CollectionOfElements(targetElement = Integer.class)
	@JoinTable(name = "ProviderPreferenceAppointmentScreenEForm",joinColumns = @JoinColumn(name = "providerNo"))
	@Column(name="appointmentScreenEForm")
	private Collection<Integer> appointmentScreenEForms=new HashSet<Integer>();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated=new Date();
	
	@PostLoad
	protected void hibernatePreFetchCollectionsFix() {
		// forces eager fetching which can't be done normally as hibernate doesn't allow mulitple collection eager fetching
		appointmentScreenForms.size();
		appointmentScreenEForms.size();
	}
	
	@PreUpdate
	protected void jpaUpdateLastUpdateTime() {
		lastUpdated = new Date();
	}

	@Override
    public String getId() {
	    return(providerNo);
    }

	public String getProviderNo() {
    	return providerNo;
    }

	/**
	 * The providerNo is also the primarykey, as such it should not
	 * be changed on a persisted record (only set it before persistence, i.e. upon creation)
	 */
	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Integer getStartHour() {
    	return startHour;
    }

	public void setStartHour(Integer startHour) {
    	this.startHour = startHour;
    }

	public Integer getEndHour() {
    	return endHour;
    }

	public void setEndHour(Integer endHour) {
    	this.endHour = endHour;
    }

	public Integer getEveryMin() {
    	return everyMin;
    }

	public void setEveryMin(Integer everyMin) {
    	this.everyMin = everyMin;
    }

	public String getMyGroupNo() {
    	return myGroupNo;
    }

	public void setMyGroupNo(String myGroupNo) {
    	this.myGroupNo = myGroupNo;
    }

	public String getColourTemplate() {
    	return colourTemplate;
    }

	public void setColourTemplate(String colourTemplate) {
    	this.colourTemplate = colourTemplate;
    }

	public String getNewTicklerWarningWindow() {
    	return newTicklerWarningWindow;
    }

	public void setNewTicklerWarningWindow(String newTicklerWarningWindow) {
    	this.newTicklerWarningWindow = newTicklerWarningWindow;
    }

	public String getDefaultServiceType() {
    	return defaultServiceType;
    }

	public void setDefaultServiceType(String defaultServiceType) {
    	this.defaultServiceType = defaultServiceType;
    }

	public String getDefaultCaisiPmm() {
    	return defaultCaisiPmm;
    }

	public void setDefaultCaisiPmm(String defaultCaisiPmm) {
    	this.defaultCaisiPmm = defaultCaisiPmm;
    }

	public String getDefaultNewOscarCme() {
    	return defaultNewOscarCme;
    }

	public void setDefaultNewOscarCme(String defaultNewOscarCme) {
    	this.defaultNewOscarCme = defaultNewOscarCme;
    }

	public boolean isPrintQrCodeOnPrescriptions() {
    	return printQrCodeOnPrescriptions;
    }

	public void setPrintQrCodeOnPrescriptions(boolean printQrCodeOnPrescriptions) {
    	this.printQrCodeOnPrescriptions = printQrCodeOnPrescriptions;
    }

	public Date getLastUpdated() {
    	return lastUpdated;
    }

	public Collection<String> getAppointmentScreenForms() {
    	return appointmentScreenForms;
    }

	public Collection<Integer> getAppointmentScreenEForms() {
    	return appointmentScreenEForms;
    }

	public int getAppointmentScreenFormNameDisplayLength() {
    	return appointmentScreenFormNameDisplayLength;
    }

	public void setAppointmentScreenFormNameDisplayLength(int appointmentScreenFormNameDisplayLength) {
    	this.appointmentScreenFormNameDisplayLength = appointmentScreenFormNameDisplayLength;
    }

}
