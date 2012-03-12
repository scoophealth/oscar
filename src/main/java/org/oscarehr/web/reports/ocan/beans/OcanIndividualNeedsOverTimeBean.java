package org.oscarehr.web.reports.ocan.beans;

import java.util.Date;

import org.jfree.chart.JFreeChart;

public class OcanIndividualNeedsOverTimeBean {
	private String consumerName;
	private String staffName;
	private Date reportDate;

	private boolean showUnmetNeeds;
	private boolean showMetNeeds;
	private boolean showNoNeeds;
	private boolean showUnknownNeeds;

	private JFreeChart unmetNeedsChart;
	private JFreeChart metNeedsChart;
	private JFreeChart noNeedsChart;
	private JFreeChart unknownNeedsChart;


	public String getConsumerName() {
		return consumerName;
	}
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public boolean isShowUnmetNeeds() {
		return showUnmetNeeds;
	}
	public void setShowUnmetNeeds(boolean showUnmetNeeds) {
		this.showUnmetNeeds = showUnmetNeeds;
	}
	public boolean isShowMetNeeds() {
		return showMetNeeds;
	}
	public void setShowMetNeeds(boolean showMetNeeds) {
		this.showMetNeeds = showMetNeeds;
	}
	public boolean isShowNoNeeds() {
		return showNoNeeds;
	}
	public void setShowNoNeeds(boolean showNoNeeds) {
		this.showNoNeeds = showNoNeeds;
	}
	public boolean isShowUnknownNeeds() {
		return showUnknownNeeds;
	}
	public void setShowUnknownNeeds(boolean showUnknownNeeds) {
		this.showUnknownNeeds = showUnknownNeeds;
	}
	public JFreeChart getUnmetNeedsChart() {
		return unmetNeedsChart;
	}
	public void setUnmetNeedsChart(JFreeChart unmetNeedsChart) {
		this.unmetNeedsChart = unmetNeedsChart;
	}
	public JFreeChart getMetNeedsChart() {
		return metNeedsChart;
	}
	public void setMetNeedsChart(JFreeChart metNeedsChart) {
		this.metNeedsChart = metNeedsChart;
	}
	public JFreeChart getNoNeedsChart() {
		return noNeedsChart;
	}
	public void setNoNeedsChart(JFreeChart noNeedsChart) {
		this.noNeedsChart = noNeedsChart;
	}
	public JFreeChart getUnknownNeedsChart() {
		return unknownNeedsChart;
	}
	public void setUnknownNeedsChart(JFreeChart unknownNeedsChart) {
		this.unknownNeedsChart = unknownNeedsChart;
	}




}
