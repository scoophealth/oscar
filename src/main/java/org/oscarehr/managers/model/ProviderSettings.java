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
package org.oscarehr.managers.model;

import java.util.ArrayList;
import java.util.Collection;


public class ProviderSettings {

	private String rxAddress;
	private String rxCity;
	private String rxProvince;
	private String rxPostal;
	private String rxPhone;
	private String faxNumber;
	private String newTicklerWarningWindow;
	private String workloadManagement;
	private String ticklerWarningProvider;
	
	private boolean useCobaltOnLogin;

	private int startHour = 8;
	private int endHour = 18;
	private int period = 15;
	private String groupNo;
	private int appointmentScreenLinkNameDisplayLength=3;
	private boolean hideOldEchartLinkInAppointment=false;
	
	private Collection<String> appointmentScreenForms = new ArrayList<String>();	
	private Collection<Integer> appointmentScreenEforms = new ArrayList<Integer>();
	private Collection<org.oscarehr.managers.model.QuickLink> appointmentScreenQuickLinks = new ArrayList<org.oscarehr.managers.model.QuickLink>();
	
	private String defaultServiceType = "no";
	private String defaultDxCode;
	private boolean defaultDoNotDeleteBilling;
	
	private boolean useRx3 = true;
	private boolean showPatientDob;
	private boolean printQrCodeOnPrescription;
	
	private boolean eRxEnabled;
	private boolean eRxTrainingMode;
	
	private String eRxUsername;
	private String eRxPassword;
	private String eRxFacility;
	private String eRxURL;
	
	private String signature;
	private String rxDefaultQuantity;
	private String rxPageSize;
	private String rxInteractionWarningLevel = "0";
	
	private String defaultHcType = "";
	private String defaultSex = "";
	
	private String consultationTimePeriodWarning;
	private String consultationTeamWarning = "";
	private String consultationPasteFormat;
	
	private boolean documentBrowserInDocumentReport;
	private boolean documentBrowserInMasterFile;
	
	private boolean cppSingleLine;
	private String cmeNoteDate = "A";
	private boolean cmeNoteFormat;
	private String quickChartSize;
	private String encounterWindowWidth;
	private String encounterWindowHeight;
	private boolean encounterWindowMaximize;
	
	private String favoriteFormGroup = "";
	
	private boolean disableCommentOnAck;
	private boolean defaultPmm;
	
	private String olisDefaultReportingLab = "";
	private String olisDefaultExcludeReportingLab = "";
	private String myDrugRefId;
	private boolean useMyMeds;
	
	
	
	
	
	public String getRxAddress() {
		return rxAddress;
	}
	public void setRxAddress(String rxAddress) {
		this.rxAddress = rxAddress;
	}
	public String getRxCity() {
		return rxCity;
	}
	public void setRxCity(String rxCity) {
		this.rxCity = rxCity;
	}
	public String getRxProvince() {
		return rxProvince;
	}
	public void setRxProvince(String rxProvince) {
		this.rxProvince = rxProvince;
	}
	public String getRxPostal() {
		return rxPostal;
	}
	public void setRxPostal(String rxPostal) {
		this.rxPostal = rxPostal;
	}
	public String getRxPhone() {
		return rxPhone;
	}
	public void setRxPhone(String rxPhone) {
		this.rxPhone = rxPhone;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getNewTicklerWarningWindow() {
		return newTicklerWarningWindow;
	}
	public void setNewTicklerWarningWindow(String newTicklerWarningWindow) {
		this.newTicklerWarningWindow = newTicklerWarningWindow;
	}
	public String getWorkloadManagement() {
		return workloadManagement;
	}
	public void setWorkloadManagement(String workloadManagement) {
		this.workloadManagement = workloadManagement;
	}
	public String getTicklerWarningProvider() {
		return ticklerWarningProvider;
	}
	public void setTicklerWarningProvider(String ticklerWarningProvider) {
		this.ticklerWarningProvider = ticklerWarningProvider;
	}
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public int getAppointmentScreenLinkNameDisplayLength() {
		return appointmentScreenLinkNameDisplayLength;
	}
	public void setAppointmentScreenLinkNameDisplayLength(int appointmentScreenLinkNameDisplayLength) {
		this.appointmentScreenLinkNameDisplayLength = appointmentScreenLinkNameDisplayLength;
	}
	public boolean isHideOldEchartLinkInAppointment() {
		return hideOldEchartLinkInAppointment;
	}
	public void setHideOldEchartLinkInAppointment(boolean hideOldEchartLinkInAppointment) {
		this.hideOldEchartLinkInAppointment = hideOldEchartLinkInAppointment;
	}
	public Collection<String> getAppointmentScreenForms() {
		return appointmentScreenForms;
	}
	public void setAppointmentScreenForms(Collection<String> appointmentScreenForms) {
		this.appointmentScreenForms = appointmentScreenForms;
	}
	public Collection<Integer> getAppointmentScreenEforms() {
		return appointmentScreenEforms;
	}
	public void setAppointmentScreenEforms(Collection<Integer> appointmentScreenEforms) {
		this.appointmentScreenEforms = appointmentScreenEforms;
	}
	public Collection<org.oscarehr.managers.model.QuickLink> getAppointmentScreenQuickLinks() {
		return appointmentScreenQuickLinks;
	}
	public void setAppointmentScreenQuickLinks(Collection<org.oscarehr.managers.model.QuickLink> appointmentScreenQuickLinks) {
		this.appointmentScreenQuickLinks = appointmentScreenQuickLinks;
	}
	public String getDefaultServiceType() {
		return defaultServiceType;
	}
	public void setDefaultServiceType(String defaultServiceType) {
		this.defaultServiceType = defaultServiceType;
	}
	public String getDefaultDxCode() {
		return defaultDxCode;
	}
	public void setDefaultDxCode(String defaultDxCode) {
		this.defaultDxCode = defaultDxCode;
	}
	public boolean isDefaultDoNotDeleteBilling() {
		return defaultDoNotDeleteBilling;
	}
	public void setDefaultDoNotDeleteBilling(boolean defaultDoNotDeleteBilling) {
		this.defaultDoNotDeleteBilling = defaultDoNotDeleteBilling;
	}
	public boolean isUseRx3() {
		return useRx3;
	}
	public void setUseRx3(boolean useRx3) {
		this.useRx3 = useRx3;
	}
	public boolean isShowPatientDob() {
		return showPatientDob;
	}
	public void setShowPatientDob(boolean showPatientDob) {
		this.showPatientDob = showPatientDob;
	}
	public boolean isPrintQrCodeOnPrescription() {
		return printQrCodeOnPrescription;
	}
	public void setPrintQrCodeOnPrescription(boolean printQrCodeOnPrescription) {
		this.printQrCodeOnPrescription = printQrCodeOnPrescription;
	}
	public boolean iseRxEnabled() {
		return eRxEnabled;
	}
	public void seteRxEnabled(boolean eRxEnabled) {
		this.eRxEnabled = eRxEnabled;
	}
	public boolean iseRxTrainingMode() {
		return eRxTrainingMode;
	}
	public void seteRxTrainingMode(boolean eRxTrainingMode) {
		this.eRxTrainingMode = eRxTrainingMode;
	}
	public String geteRxUsername() {
		return eRxUsername;
	}
	public void seteRxUsername(String eRxUsername) {
		this.eRxUsername = eRxUsername;
	}
	public String geteRxPassword() {
		return eRxPassword;
	}
	public void seteRxPassword(String eRxPassword) {
		this.eRxPassword = eRxPassword;
	}
	public String geteRxFacility() {
		return eRxFacility;
	}
	public void seteRxFacility(String eRxFacility) {
		this.eRxFacility = eRxFacility;
	}
	public String geteRxURL() {
		return eRxURL;
	}
	public void seteRxURL(String eRxURL) {
		this.eRxURL = eRxURL;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getRxDefaultQuantity() {
		return rxDefaultQuantity;
	}
	public void setRxDefaultQuantity(String rxDefaultQuantity) {
		this.rxDefaultQuantity = rxDefaultQuantity;
	}
	public String getRxPageSize() {
		return rxPageSize;
	}
	public void setRxPageSize(String rxPageSize) {
		this.rxPageSize = rxPageSize;
	}
	public String getRxInteractionWarningLevel() {
		return rxInteractionWarningLevel;
	}
	public void setRxInteractionWarningLevel(String rxInteractionWarningLevel) {
		this.rxInteractionWarningLevel = rxInteractionWarningLevel;
	}
	public String getDefaultHcType() {
		return defaultHcType;
	}
	public void setDefaultHcType(String defaultHcType) {
		this.defaultHcType = defaultHcType;
	}
	public String getDefaultSex() {
		return defaultSex;
	}
	public void setDefaultSex(String defaultSex) {
		this.defaultSex = defaultSex;
	}
	public String getConsultationTimePeriodWarning() {
		return consultationTimePeriodWarning;
	}
	public void setConsultationTimePeriodWarning(String consultationTimePeriodWarning) {
		this.consultationTimePeriodWarning = consultationTimePeriodWarning;
	}
	public String getConsultationTeamWarning() {
		return consultationTeamWarning;
	}
	public void setConsultationTeamWarning(String consultationTeamWarning) {
		this.consultationTeamWarning = consultationTeamWarning;
	}
	public String getConsultationPasteFormat() {
		return consultationPasteFormat;
	}
	public void setConsultationPasteFormat(String consultationPasteFormat) {
		this.consultationPasteFormat = consultationPasteFormat;
	}
	public boolean isDocumentBrowserInDocumentReport() {
		return documentBrowserInDocumentReport;
	}
	public void setDocumentBrowserInDocumentReport(boolean documentBrowserInDocumentReport) {
		this.documentBrowserInDocumentReport = documentBrowserInDocumentReport;
	}
	public boolean isDocumentBrowserInMasterFile() {
		return documentBrowserInMasterFile;
	}
	public void setDocumentBrowserInMasterFile(boolean documentBrowserInMasterFile) {
		this.documentBrowserInMasterFile = documentBrowserInMasterFile;
	}
	public boolean isCppSingleLine() {
		return cppSingleLine;
	}
	public void setCppSingleLine(boolean cppSingleLine) {
		this.cppSingleLine = cppSingleLine;
	}
	public String getCmeNoteDate() {
		return cmeNoteDate;
	}
	public void setCmeNoteDate(String cmeNoteDate) {
		this.cmeNoteDate = cmeNoteDate;
	}
	public boolean isCmeNoteFormat() {
		return cmeNoteFormat;
	}
	public void setCmeNoteFormat(boolean cmeNoteFormat) {
		this.cmeNoteFormat = cmeNoteFormat;
	}
	public String getQuickChartSize() {
		return quickChartSize;
	}
	public void setQuickChartSize(String quickChartSize) {
		this.quickChartSize = quickChartSize;
	}
	public String getEncounterWindowWidth() {
		return encounterWindowWidth;
	}
	public void setEncounterWindowWidth(String encounterWindowWidth) {
		this.encounterWindowWidth = encounterWindowWidth;
	}
	public String getEncounterWindowHeight() {
		return encounterWindowHeight;
	}
	public void setEncounterWindowHeight(String encounterWindowHeight) {
		this.encounterWindowHeight = encounterWindowHeight;
	}
	public boolean isEncounterWindowMaximize() {
		return encounterWindowMaximize;
	}
	public void setEncounterWindowMaximize(boolean encounterWindowMaximize) {
		this.encounterWindowMaximize = encounterWindowMaximize;
	}
	public String getFavoriteFormGroup() {
		return favoriteFormGroup;
	}
	public void setFavoriteFormGroup(String favoriteFormGroup) {
		this.favoriteFormGroup = favoriteFormGroup;
	}
	public boolean isDisableCommentOnAck() {
		return disableCommentOnAck;
	}
	public void setDisableCommentOnAck(boolean disableCommentOnAck) {
		this.disableCommentOnAck = disableCommentOnAck;
	}
	public boolean isDefaultPmm() {
		return defaultPmm;
	}
	public void setDefaultPmm(boolean defaultPmm) {
		this.defaultPmm = defaultPmm;
	}
	public String getOlisDefaultReportingLab() {
		return olisDefaultReportingLab;
	}
	public void setOlisDefaultReportingLab(String olisDefaultReportingLab) {
		this.olisDefaultReportingLab = olisDefaultReportingLab;
	}
	public String getOlisDefaultExcludeReportingLab() {
		return olisDefaultExcludeReportingLab;
	}
	public void setOlisDefaultExcludeReportingLab(String olisDefaultExcludeReportingLab) {
		this.olisDefaultExcludeReportingLab = olisDefaultExcludeReportingLab;
	}
	public String getMyDrugRefId() {
		return myDrugRefId;
	}
	public void setMyDrugRefId(String myDrugRefId) {
		this.myDrugRefId = myDrugRefId;
	}
	
	public boolean isUseMyMeds() {
		return useMyMeds;
	}
	public void setUseMyMeds(boolean useMyMeds) {
		this.useMyMeds = useMyMeds;
	}
	public boolean isUseCobaltOnLogin() {
		return useCobaltOnLogin;
	}
	public void setUseCobaltOnLogin(boolean useCobaltOnLogin) {
		this.useCobaltOnLogin = useCobaltOnLogin;
	}
	
	
	
	

}
