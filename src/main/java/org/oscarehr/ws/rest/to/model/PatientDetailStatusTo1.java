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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PatientDetailStatusTo1 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean integratorEnabled = false;
	private boolean integratorOffline = true;
	private boolean integratorAllSynced = true;
	
	private boolean macPHRLoggedIn = false;
	private boolean macPHRIdsSet = false;
	private String macPHRVerificationLevel;

	//from oscar.properties
	private boolean conformanceFeaturesEnabled = false;
	private boolean workflowEnhance = false;
	private boolean showPrimaryCarePhysicianCheck = false;
	private boolean showEmploymentStatus = false;
	private String billregion;
	private String defaultView;
	private String hospitalView;
	
	
	public boolean isIntegratorEnabled() {
		return integratorEnabled;
	}
	public void setIntegratorEnabled(boolean integratorEnabled) {
		this.integratorEnabled = integratorEnabled;
	}
	public boolean isIntegratorOffline() {
		return integratorOffline;
	}
	public void setIntegratorOffline(boolean integratorOffline) {
		this.integratorOffline = integratorOffline;
	}
	public boolean isIntegratorAllSynced() {
		return integratorAllSynced;
	}
	public void setIntegratorAllSynced(boolean integratorAllSynced) {
		this.integratorAllSynced = integratorAllSynced;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isMacPHRLoggedIn() {
		return macPHRLoggedIn;
	}
	public void setMacPHRLoggedIn(boolean macPHRLoggedIn) {
		this.macPHRLoggedIn = macPHRLoggedIn;
	}
	public boolean isMacPHRIdsSet() {
		return macPHRIdsSet;
	}
	public void setMacPHRIdsSet(boolean macPHRIdsSet) {
		this.macPHRIdsSet = macPHRIdsSet;
	}
	public String getMacPHRVerificationLevel() {
		return macPHRVerificationLevel;
	}
	public void setMacPHRVerificationLevel(String macPHRVerificationLevel) {
		this.macPHRVerificationLevel = macPHRVerificationLevel;
	}
	public boolean isConformanceFeaturesEnabled() {
		return conformanceFeaturesEnabled;
	}
	public void setConformanceFeaturesEnabled(boolean conformanceFeaturesEnabled) {
		this.conformanceFeaturesEnabled = conformanceFeaturesEnabled;
	}
	public boolean isWorkflowEnhance() {
		return workflowEnhance;
	}
	public void setWorkflowEnhance(boolean workflowEnhance) {
		this.workflowEnhance = workflowEnhance;
	}
	public boolean isShowPrimaryCarePhysicianCheck() {
		return showPrimaryCarePhysicianCheck;
	}
	public void setShowPrimaryCarePhysicianCheck(boolean showPrimaryCarePhysicianCheck) {
		this.showPrimaryCarePhysicianCheck = showPrimaryCarePhysicianCheck;
	}
	public boolean isShowEmploymentStatus() {
		return showEmploymentStatus;
	}
	public void setShowEmploymentStatus(boolean showEmploymentStatus) {
		this.showEmploymentStatus = showEmploymentStatus;
	}
	public String getBillregion() {
		return billregion;
	}
	public void setBillregion(String billregion) {
		this.billregion = billregion;
	}
	public String getDefaultView() {
		return defaultView;
	}
	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}
	public String getHospitalView() {
		return hospitalView;
	}
	public void setHospitalView(String hospitalView) {
		this.hospitalView = hospitalView;
	}
}
