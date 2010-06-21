package oscar.oscarLab.ca.all.pageUtil;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class OruR01UploadForm extends ActionForm {

	private Integer professionalSpecialistId=null;
	private String clientFirstName=null;
	private String clientLastName=null;
	private String clientHealthNumber=null;
	private String clientBirthDay=null;
	private String clientGender=null;
	private String dataName=null;
	private String textData=null;
	private FormFile uploadFile = null;
	
	public FormFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public Integer getProfessionalSpecialistId() {
    	return professionalSpecialistId;
    }

	public void setProfessionalSpecialistId(Integer professionalSpecialistId) {
    	this.professionalSpecialistId = professionalSpecialistId;
    }

	public String getClientFirstName() {
    	return clientFirstName;
    }

	public void setClientFirstName(String clientFirstName) {
    	this.clientFirstName = clientFirstName;
    }

	public String getClientLastName() {
    	return clientLastName;
    }

	public void setClientLastName(String clientLastName) {
    	this.clientLastName = clientLastName;
    }

	public String getClientHealthNumber() {
    	return clientHealthNumber;
    }

	public void setClientHealthNumber(String clientHealthNumber) {
    	this.clientHealthNumber = clientHealthNumber;
    }

	public String getClientBirthDay() {
    	return clientBirthDay;
    }

	public void setClientBirthDay(String clientBirthDay) {
    	this.clientBirthDay = clientBirthDay;
    }

	public String getClientGender() {
    	return clientGender;
    }

	public void setClientGender(String clientGender) {
    	this.clientGender = clientGender;
    }

	public String getDataName() {
    	return dataName;
    }

	public void setDataName(String dataName) {
    	this.dataName = dataName;
    }

	public String getTextData() {
    	return textData;
    }

	public void setTextData(String textData) {
    	this.textData = textData;
    }

	@Override
    public String toString()
	{
		return(ToStringBuilder.reflectionToString(this));
	}
}