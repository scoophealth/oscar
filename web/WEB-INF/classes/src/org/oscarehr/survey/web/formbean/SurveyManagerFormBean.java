package org.oscarehr.survey.web.formbean;

import org.apache.struts.upload.FormFile;

public class SurveyManagerFormBean {
	private String page;
	private int section;
	private String questionType;
	private String questionTypeData;
	private int numAnswers;
	private String dateFormat;
	private FormFile importFile;
	private int templateId;
	
	
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public FormFile getImportFile() {
		return importFile;
	}

	public void setImportFile(FormFile importFile) {
		this.importFile = importFile;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getNumAnswers() {
		return numAnswers;
	}

	public void setNumAnswers(int numAnswers) {
		this.numAnswers = numAnswers;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getQuestionTypeData() {
		return questionTypeData;
	}

	public void setQuestionTypeData(String questionTypeData) {
		this.questionTypeData = questionTypeData;
	}
	
	
}
