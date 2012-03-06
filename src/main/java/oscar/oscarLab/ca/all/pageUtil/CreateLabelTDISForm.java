package oscar.oscarLab.ca.all.pageUtil;

import org.apache.struts.action.ActionForm;

public class CreateLabelTDISForm extends ActionForm {
	
	private String lab_no;
	private String accessionNum;
	private String label;
	
	public CreateLabelTDISForm(){
		
	}
	
	public String getLab_no() {
		return lab_no;
	}
	public void setLab_no(String lab_no) {
		this.lab_no = lab_no;
	}
	public String getAccessionNum() {
		return accessionNum;
	}
	public void setAccessionNum(String accessionNum) {
		this.accessionNum = accessionNum;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	

}
