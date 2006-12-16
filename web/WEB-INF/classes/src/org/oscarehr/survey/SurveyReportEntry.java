package org.oscarehr.survey;

public class SurveyReportEntry {

	private int pageNumber;
	private int sectionId;
	private int questionId;
	private int operation;

	public SurveyReportEntry() {
		
	}
	
	public int getOperation() {
		return operation;
	}


	public int getPageNumber() {
		return pageNumber;
	}


	public int getQuestionId() {
		return questionId;
	}


	public int getSectionId() {
		return sectionId;
	}


	public void setOperation(int operation) {
		this.operation = operation;
	}


	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}


	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}


	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

}
