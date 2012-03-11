package org.oscarehr.web.reports.ocan.beans;

import java.util.Date;

public class SummaryOfActionsAndCommentsOCANBean {

	private String ocanName;
	private Date ocanDate;
	private int assessmentId;
	private int formId;

	private String actions;
	private String byWhom;
	private String reviewDate;
	private String consumerNeedRating;
	private String consumerComments;
	private String staffNeedRating;
	private String staffComments;

	public SummaryOfActionsAndCommentsOCANBean() {

	}

	public String getOcanName() {
		return ocanName;
	}
	public void setOcanName(String ocanName) {
		this.ocanName = ocanName;
	}
	public Date getOcanDate() {
		return ocanDate;
	}
	public void setOcanDate(Date ocanDate) {
		this.ocanDate = ocanDate;
	}
	public int getAssessmentId() {
		return assessmentId;
	}
	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public String getByWhom() {
		return byWhom;
	}
	public void setByWhom(String byWhom) {
		this.byWhom = byWhom;
	}
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public String getConsumerNeedRating() {
		return consumerNeedRating;
	}
	public void setConsumerNeedRating(String consumerNeedRating) {
		this.consumerNeedRating = consumerNeedRating;
	}
	public String getConsumerComments() {
		return consumerComments;
	}
	public void setConsumerComments(String consumerComments) {
		this.consumerComments = consumerComments;
	}
	public String getStaffNeedRating() {
		return staffNeedRating;
	}
	public void setStaffNeedRating(String staffNeedRating) {
		this.staffNeedRating = staffNeedRating;
	}
	public String getStaffComments() {
		return staffComments;
	}
	public void setStaffComments(String staffComments) {
		this.staffComments = staffComments;
	}


}
