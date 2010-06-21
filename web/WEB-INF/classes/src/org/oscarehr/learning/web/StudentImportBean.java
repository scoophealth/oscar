package org.oscarehr.learning.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class StudentImportBean extends ActionForm {
	FormFile file;
	
	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}
	
}
