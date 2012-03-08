package oscar.dms.data;

import org.apache.struts.action.ActionForm;

public class AddDocumentTypeForm extends ActionForm  {
	
	private String function = "";
	private String docType = "";
	
	
	public AddDocumentTypeForm() {
    }
	
	 public String getFunction() {
	        return function;
	 }
	 
	 public String getDocType() {
	        return docType;
	 }
	 
	
	 public void  setFunction(String function) {
	        this.function = function;
	 }
	 
	 public void setDocType(String docType) {
	        this.docType = docType;
	 }
	 
	
}
