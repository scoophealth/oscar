/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class ReportDateValue extends BaseObject{
	private String sessionId;
	private Date startDate;
	private Date endDate;
	private Date asOfDate;
	private String startDate_S;
	private String endDate_S;
	private String asOfDate_S;

	public ReportDateValue() {		
	}

	public Date getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}

	public String getAsOfDate_S() {
		return asOfDate_S;
	}

	public void setAsOfDate_S(String asOfDate_S) {
		this.asOfDate_S = asOfDate_S;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndDate_S() {
		return endDate_S;
	}

	public void setEndDate_S(String endDate_S) {
		this.endDate_S = endDate_S;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStartDate_S() {
		return startDate_S;
	}

	public void setStartDate_S(String startDate_S) {
		this.startDate_S = startDate_S;
	}

		
}
