package oscar.dms.data;

import org.apache.struts.action.ActionForm;

public class ChangeDocStatusForm extends ActionForm {
	
	
	private String docTypeD = "";
	private String docTypeP = "";
	private String statusD = "";
	private String statusP = "";
	
	public ChangeDocStatusForm() {
    }
	
	public String getDocTypeD() {
        return docTypeD;
	}
	
	public String getDocTypeP() {
        return docTypeP;
	}
	
	
	public String getStatusD() {
		 return statusD;
	 }
	
	public String getStatusP() {
		 return statusP;
	 }
	
	public void setDocTypeD(String docTypeD) {
        this.docTypeD = docTypeD;
	}
	
	public void setDocTypeP(String docTypeP) {
        this.docTypeP = docTypeP;
	}
	
	public void setStatusD(String statusD) {
		 this.statusD = statusD;
	 }
	
	public void setStatusP(String statusP) {
		 this.statusP = statusP;
	 }
	
	 
	
}
