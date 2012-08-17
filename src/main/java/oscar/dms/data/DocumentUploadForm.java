/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package oscar.dms.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class DocumentUploadForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private String function = "";
    private String functionId = "";
    private String docType = "";
    private String docDesc = "";
    private String docCreator = "";
    private String responsibleId = "";
    private String source = "";
    private FormFile docFile;

    private FormFile filedata;
    
    private String docPublic = "";
    private String mode = "";
    private String observationDate = "";
    private String reviewerId = "";
    private String reviewDateTime = "";
    private boolean reviewDoc = false;
    private String html = "";
    
    public DocumentUploadForm() {
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
    
}
