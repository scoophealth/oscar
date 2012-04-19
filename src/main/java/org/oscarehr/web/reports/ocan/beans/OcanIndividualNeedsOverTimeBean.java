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
