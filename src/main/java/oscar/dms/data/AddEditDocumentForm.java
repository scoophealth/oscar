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


package oscar.dms.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class AddEditDocumentForm extends ActionForm {
    private String function = "";
    private String functionId = "";
    private String docType = "";
    private String docClass = "";
    private String docSubClass = "";
    private String docDesc = "";
    private String docCreator = "";
    private String responsibleId = "";
    private String source = "";
    private String sourceFacility = "";
    private FormFile docFile;

    private FormFile filedata;
    
    private String docPublic = "";
    private String mode = "";
    private String observationDate = "";
    private String reviewerId = "";
    private String reviewDateTime = "";
    private String contentDateTime = "";
    private boolean reviewDoc = false;
    private String html = "";
    
    private String appointmentNo = "0";
    
    private boolean restrictToProgram = false;
    
    public AddEditDocumentForm() {
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocClass() {
        return docClass;
    }

    public void setDocClass(String docClass) {
        this.docClass = docClass;
    }

    public String getDocSubClass() {
        return docSubClass;
    }

    public void setDocSubClass(String docSubClass) {
        this.docSubClass = docSubClass;
    }

    public String getDocDesc() {
        return docDesc;
    }

    public void setDocDesc(String docDesc) {
        this.docDesc = docDesc;
    }

    public String getDocCreator() {
        return docCreator;
    }

    public void setDocCreator(String docCreator) {
        this.docCreator = docCreator;
    }

    public String getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(String responsibleId) {
        this.responsibleId = responsibleId;
    }

    public String getSource() {
	return source;
    }
    
    public void setSource(String source) {
	this.source = source;
    }
    
	public String getSourceFacility() {
		return sourceFacility;
	}
	
	public void setSourceFacility(String sourceFacility) {
		this.sourceFacility = sourceFacility;
	}
    
    public FormFile getDocFile() {
        return docFile;
    }

    public void setDocFile(FormFile docFile) {
        this.docFile = docFile;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDocPublic() {
        return docPublic;
    }

    public void setDocPublic(String docPublic) {
        this.docPublic = docPublic;
    }

    public String getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(String observationDate) {
        this.observationDate = observationDate;
    }
    
    public String getReviewerId() {
	return reviewerId;
    }
    
    public void setReviewerId(String reviewerId) {
	this.reviewerId = reviewerId;
    }
    
    public String getReviewDateTime() {
	return reviewDateTime;
    }
    
    public void setReviewDateTime(String reviewDateTime) {
	this.reviewDateTime = reviewDateTime;
    }
    
    public String getContentDateTime() {
	return contentDateTime;
    }
    
    public void setContentDateTime(String contentDateTime) {
	this.contentDateTime = contentDateTime;
    }
    
    public boolean getReviewDoc() {
	return reviewDoc;
    }
    
    public void setReviewDoc(boolean reviewDoc) {
	this.reviewDoc = reviewDoc;
    }
    
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public FormFile getFiledata() {
        return filedata;
    }

    public void setFiledata(FormFile Filedata) {
        this.filedata = Filedata;
    }

	public String getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(String appointment) {
		this.appointmentNo = appointment;
	}

	public boolean isRestrictToProgram() {
		return restrictToProgram;
	}

	public void setRestrictToProgram(boolean restrictToProgram) {
		this.restrictToProgram = restrictToProgram;
	}
    
    
}
