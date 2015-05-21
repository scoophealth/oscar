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

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class HtmlEditForm extends ActionForm {
    private FormFile uploadFile = null;
    private String fid = "";
    private String formName = "";
    private String formSubject = "";
    private String formFileName = "";
    private String formHtml = "";
    private boolean showLatestFormOnly = false;
    private boolean patientIndependent = false;
    private String roleType = "";
    private String programNo = "";
    private boolean restrictByProgram =false; 
    
    public HtmlEditForm() {
    }
    
    public FormFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(FormFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getFid() {
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
    
    public String getFormSubject() {
        return formSubject;
    }
    
    public void setFormSubject(String formSubject) {
        this.formSubject = formSubject;
    }
    
    public void setFormFileName(String formFileName) {
        this.formFileName = formFileName;
    }
    
    public String getFormFileName() {
        return formFileName;
    }
    
    public String getFormHtml() {
        return formHtml;
    }
    
    public void setFormHtml(String formHtml) {
        this.formHtml = formHtml;
    }
    
    public boolean isShowLatestFormOnly() {
        return showLatestFormOnly;
    }

    public void setShowLatestFormOnly(boolean showLatestFormOnly) {
        this.showLatestFormOnly = showLatestFormOnly;
    }
    
    public boolean isPatientIndependent() {
        return patientIndependent;
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
    
    
}
