package org.oscarehr.survey.model.oscar;

public class OscarFormData {
	private Long id;
	private long instanceId;
	private long pageNumber;
	private long sectionId;
	private long questionId;
	private String value;
	private String key;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(long pageNumber) {
		this.pageNumber = pageNumber;
	}
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public long getSectionId() {
		return sectionId;
	}
	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
