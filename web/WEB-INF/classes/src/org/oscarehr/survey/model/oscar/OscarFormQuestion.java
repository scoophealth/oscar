package org.oscarehr.survey.model.oscar;

public class OscarFormQuestion {

	private Long id;
	private long page;
	private long section;
	private long question;
	private String description;
	private long formId;
	private String formQuestionId;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
	public long getSection() {
		return section;
	}
	public void setSection(long section) {
		this.section = section;
	}
	public long getQuestion() {
		return question;
	}
	public void setQuestion(long question) {
		this.question = question;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getFormId() {
		return formId;
	}
	public void setFormId(long formId) {
		this.formId = formId;
	}
	public String getFormQuestionId() {
		return formQuestionId;
	}
	public void setFormQuestionId(String formQuestionId) {
		this.formQuestionId = formQuestionId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
