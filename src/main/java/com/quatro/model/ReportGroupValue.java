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
import java.util.List;
public class ReportGroupValue {
	int reportGroupId;
	String description;
	String providerNo;
	List reports;
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public int getReportGroupId() {
		return reportGroupId;
	}
	public void setReportGroupId(int id) {
		this.reportGroupId = id;
	}
	public String getReportGroupDesc() {
		return description;
	}
	public void setReportGroupDesc(String description) {
		this.description = description;
	}
	public List getReports() {
		return reports;
	}
	public void setReports(List reports) {
		this.reports = reports;
	}
}
