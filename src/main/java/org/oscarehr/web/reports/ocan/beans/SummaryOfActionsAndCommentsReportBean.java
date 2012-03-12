package org.oscarehr.web.reports.ocan.beans;

import java.util.ArrayList;
import java.util.List;

public class SummaryOfActionsAndCommentsReportBean {

	private List<SummaryOfActionsAndCommentsDomainBean> unmetNeeds = new ArrayList<SummaryOfActionsAndCommentsDomainBean>();
	private List<SummaryOfActionsAndCommentsDomainBean> metNeeds = new ArrayList<SummaryOfActionsAndCommentsDomainBean>();
	private List<SummaryOfActionsAndCommentsDomainBean> noNeeds = new ArrayList<SummaryOfActionsAndCommentsDomainBean>();
	private List<SummaryOfActionsAndCommentsDomainBean> unknown = new ArrayList<SummaryOfActionsAndCommentsDomainBean>();

	public SummaryOfActionsAndCommentsReportBean() {

	}

	public List<SummaryOfActionsAndCommentsDomainBean> getUnmetNeeds() {
		return unmetNeeds;
	}

	public void setUnmetNeeds(List<SummaryOfActionsAndCommentsDomainBean> unmetNeeds) {
		this.unmetNeeds = unmetNeeds;
	}

	public List<SummaryOfActionsAndCommentsDomainBean> getMetNeeds() {
		return metNeeds;
	}

	public void setMetNeeds(List<SummaryOfActionsAndCommentsDomainBean> metNeeds) {
		this.metNeeds = metNeeds;
	}

	public List<SummaryOfActionsAndCommentsDomainBean> getNoNeeds() {
		return noNeeds;
	}

	public void setNoNeeds(List<SummaryOfActionsAndCommentsDomainBean> noNeeds) {
		this.noNeeds = noNeeds;
	}

	public List<SummaryOfActionsAndCommentsDomainBean> getUnknown() {
		return unknown;
	}

	public void setUnknown(List<SummaryOfActionsAndCommentsDomainBean> unknown) {
		this.unknown = unknown;
	}



}
