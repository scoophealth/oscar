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
