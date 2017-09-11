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


package oscar.eform.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import oscar.util.StringBuilderUtils;
import oscar.util.UtilDateUtilities;

public class EFormBase {
    protected final String imageMarker = "${oscar_image_path}";
	protected final String jsMarker = "${oscar_javascript_path}";
	protected final String signatureMarker = "${oscar_signature_code}";
	protected final String sourceMarker = "${source}";
    protected String fdid;
    protected String fid;
    protected String formName;
    protected String formSubject;
    protected String formHtml;
    protected String formFileName;
    protected String formCreator;
    protected String demographicNo;
    protected String providerNo;
    protected String formDate;
    protected String formTime;
    protected boolean showLatestFormOnly=false;
    protected boolean patientIndependent=false;
    protected String roleType;
    protected String programNo;
    protected boolean restrictByProgram = false;
    protected Boolean disableUpdate = false;
    
    protected ArrayList<String> updateFields = new ArrayList<String>();

    public EFormBase() {

    }

    public EFormBase(String fid, String formName, String formSubject,
            String formFileName, String formHtml, String roleType) {
        this.fid = fid;
        this.formName = formName;
        this.formSubject = formSubject;
        this.formHtml = formHtml;
        this.formFileName = formFileName;
        this.roleType = roleType;
        dateTimeStamp();
    }

    public EFormBase(String fid, String formName, String formSubject,
            String formFileName, String formHtml, boolean showLatestFormOnly, boolean patientIndependent, String roleType, String programNo, boolean restrictByProgram) {
        this.fid = fid;
        this.formName = formName;
        this.formSubject = formSubject;
        this.formHtml = formHtml;
        this.formFileName = formFileName;
        this.showLatestFormOnly = showLatestFormOnly;
        this.patientIndependent = patientIndependent;
        this.roleType = roleType;
        this.programNo = programNo;
        this.restrictByProgram = restrictByProgram;
        dateTimeStamp();
    }

    public void setImagePath() {
        Properties prop = oscar.OscarProperties.getInstance();
        String projHome = prop.getProperty("project_home");
        String output = "../eform/displayImage.do?imagefile=";
        StringBuilder html = new StringBuilder(formHtml);
        int pointer = StringBuilderUtils.indexOfIgnoreCase(html, imageMarker, 0);
        while (pointer >= 0) {
            html = html.replace(pointer, pointer+imageMarker.length(), output);
            pointer = StringBuilderUtils.indexOfIgnoreCase(html, imageMarker, 0);
        }
        formHtml = html.toString();
    }

    //------------getters/setters----
    public String getFormTime() {
        return formTime;
    }
    public void setFormTime(String formTime) {
        this.formTime = formTime;
    }
    public String getFormDate() {
        return formDate;
    }
    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }
    public java.lang.String getFid() {
        return fid;
    }
    public void setFid(String fid) {
        this.fid = fid;
    }
    public String getFormName() {
        return formName;
    }
    public void setFormName(String formName) {
        this.formName = formName;
    }
    public String getRoleType() {
        return roleType;
    }
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
    public String getFormHtml() {
        return formHtml;
    }
    public void setFormHtml(String formHtml) {
        this.formHtml = formHtml;
    }
    public String getDemographicNo() {
        return demographicNo;
    }
    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }
    public String getFormSubject() {
        if (formSubject == null) {
            return "";
        }
        return formSubject;
    }
    public void setFormSubject(String formSubject) {
        this.formSubject = formSubject;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public void setFormFileName(String formFileName) {
        this.formFileName = formFileName;
    }

    public String getFormFileName() {
        return formFileName;
    }

    private void dateTimeStamp() {
       formDate = UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd");
       formTime = UtilDateUtilities.DateToString(new Date(), "HH:mm:ss");
    }

    public void setFormCreator(String formCreator) {
        this.formCreator = formCreator;
    }

    public String getFormCreator() {
        return this.formCreator;
    }

    public boolean isShowLatestFormOnly() {
        return this.showLatestFormOnly;
    }

    public void setShowLatestFormOnly(boolean showLatestFormOnly) {
        this.showLatestFormOnly = showLatestFormOnly;
    }

    public boolean isPatientIndependent() {
        return this.patientIndependent;
    }

    public void setPatientIndependent(boolean patientIndependent) {
        this.patientIndependent = patientIndependent;
    }

	public String getProgramNo() {
		return programNo;
	}

	public void setProgramNo(String programNo) {
		this.programNo = programNo;
	}

	public boolean isRestrictByProgram() {
		return restrictByProgram;
	}

	public void setRestrictByProgram(boolean restrictByProgram) {
		this.restrictByProgram = restrictByProgram;
	}

	public Boolean getDisableUpdate() {
		return disableUpdate;
	}

	public void setDisableUpdate(Boolean disableUpdate) {
		this.disableUpdate = disableUpdate;
	}
    
}
