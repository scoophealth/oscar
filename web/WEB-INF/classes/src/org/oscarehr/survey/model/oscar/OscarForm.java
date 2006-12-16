package org.oscarehr.survey.model.oscar;

public class OscarForm {
	public static final short STATUS_ACTIVE		= 1;
	public static final short STATUS_INACTIVE	= 2;
	
	private Long formId;
    private String description;
	private String surveyData;
	private short status;
	private long version;
	
	
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public String getSurveyData() {
		return surveyData;
	}
	public void setSurveyData(String surveyData) {
		this.surveyData = surveyData;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
}
