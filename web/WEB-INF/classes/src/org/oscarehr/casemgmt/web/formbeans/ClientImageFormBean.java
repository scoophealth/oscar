package org.oscarehr.casemgmt.web.formbeans;


import java.io.File;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ClientImageFormBean extends ActionForm {
	private FormFile imagefile;

	public FormFile getImagefile() {
		return imagefile;
	}

	public void setImagefile(FormFile imagefile) {
		this.imagefile = imagefile;
	}
}
