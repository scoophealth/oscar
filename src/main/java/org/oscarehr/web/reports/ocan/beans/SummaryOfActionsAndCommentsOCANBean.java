/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


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
