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

package org.oscarehr.casemgmt.web.formbeans;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.common.model.EncounterWindow;

public class CaseManagementViewFormBean extends ActionForm {
	private String demographicNo;
	private String providerNo;
	private String issues[];
	private String note_view = "summary";
	private String prescipt_view="current";
	private String tab;
	private String vlCountry="";
	private String rootCompURL="";
	private String hideActiveIssue="true"; 
	private CaseManagementCPP cpp=new CaseManagementCPP();

    private EncounterWindow ectWin = new EncounterWindow();
	public static final String[] tabs = {"Current Issues","Client History","Allergies","Prescriptions","Reminders","Ticklers","Search"};
	private FormFile imageFile;
	
	private String searchStartDate;
	private String searchEndDate;
	private int searchRoleId;
	private String searchProviderNo;
	private int searchProgramId;
	private String searchText;
	private String searchEncounterType;
	
	private String note_sort = null;
	private String filter_provider = "";
    private String filter_providers[];
    private String filter_roles[];
	
	private long formId;
	
	
	private int noteId;
	private String password;
        
        public EncounterWindow getEctWin() {
            return this.ectWin;
        }
        
        public void setEctWin(EncounterWindow ectWin) {
            this.ectWin = ectWin;
        }
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNote_sort() {
		return note_sort;
	}

	public void setNote_sort(String note_sort) {
		this.note_sort = note_sort;
	}

	public CaseManagementViewFormBean() {
		tab = tabs[0];
	}
	
	public FormFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(FormFile imageFile) {
		this.imageFile = imageFile;
	}

	public CaseManagementCPP getCpp(){
		return this.cpp;
	}
	
	public void setCpp(CaseManagementCPP cpp){
		this.cpp=cpp;
	}
	
	public String getVlCountry() {
		return vlCountry;
	}
	public void setVlCountry(String vlCountry) {
		this.vlCountry = vlCountry;
	}
	public String getRootCompURL() {
		return rootCompURL;
	}
	public void setRootCompURL(String rootCompURL) {
		this.rootCompURL = rootCompURL;
	}
	public String getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String[] getIssues() {
		return issues;
	}
	public void setIssues(String[] issues) {
		this.issues = issues;
	}
	public String getNote_view() {
		return note_view;
	}
	public void setNote_view(String note_view) {
		this.note_view = note_view;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}

	public String getHideActiveIssue()
	{
		return hideActiveIssue;
	}

	public void setHideActiveIssue(String hideActiveIssue)
	{
		this.hideActiveIssue = hideActiveIssue;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getPrescipt_view() {
		return prescipt_view;
	}

	public void setPrescipt_view(String prescipt_view) {
		this.prescipt_view = prescipt_view;
	}

	public String getSearchEncounterType() {
		return searchEncounterType;
	}

	public String getSearchEndDate() {
		return searchEndDate;
	}

	public int getSearchProgramId() {
		return searchProgramId;
	}

	public String getSearchProviderNo() {
		return searchProviderNo;
	}

	public int getSearchRoleId() {
		return searchRoleId;
	}

	public String getSearchStartDate() {
		return searchStartDate;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchEncounterType(String searchEncounterType) {
		this.searchEncounterType = searchEncounterType;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public void setSearchProgramId(int searchProgramId) {
		this.searchProgramId = searchProgramId;
	}

	public void setSearchProviderNo(String searchProviderNo) {
		this.searchProviderNo = searchProviderNo;
	}

	public void setSearchRoleId(int searchRoleId) {
		this.searchRoleId = searchRoleId;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
        
        public String[] getFilter_providers() {
            return this.filter_providers;
        }
        
        public void setFilter_providers( String[] provs ) {
            this.filter_providers = provs;
        }

	public String getFilter_provider() {
		return filter_provider;
	}

	public void setFilter_provider(String filter_provider) {
		this.filter_provider = filter_provider;
	}
        
        public String[] getFilter_roles() {
		return filter_roles;
	}

	public void setFilter_roles(String[] filter_roles) {
		this.filter_roles = filter_roles;
	}

	public long getFormId() {
		return formId;
	}

	public void setFormId(long formId) {
		this.formId = formId;
	}
}
