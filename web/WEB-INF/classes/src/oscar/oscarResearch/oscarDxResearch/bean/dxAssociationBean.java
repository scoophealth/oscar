package oscar.oscarResearch.oscarDxResearch.bean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class dxAssociationBean extends ActionForm {

	FormFile file;
	boolean replace = true;
	
	
	public boolean isReplace() {
		return replace;
	}

	public void setReplace(boolean replace) {
		this.replace = replace;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}
	
	
}
