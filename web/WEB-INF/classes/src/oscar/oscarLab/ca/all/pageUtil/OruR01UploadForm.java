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
	private String subject=null;
	private String textMessage=null;
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

	public String getSubject() {
    	return subject;
    }

	public void setSubject(String subject) {
    	this.subject = subject;
    }

	public String getTextMessage() {
    	return textMessage;
    }

	public void setTextMessage(String textMessage) {
    	this.textMessage = textMessage;
    }

	@Override
    public String toString()
	{
		return(ToStringBuilder.reflectionToString(this));
	}
}