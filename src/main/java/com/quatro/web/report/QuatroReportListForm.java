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
package com.quatro.web.report;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class QuatroReportListForm extends ActionForm{
	List reportGroups;
	String provider;
	String chkDel;
	
	public List getReportGroups() {
		return reportGroups;
	}

	public void setReportGroups(List reportGroups) {
		this.reportGroups = reportGroups;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getChkDel() {
		return chkDel;
	}

	public void setChkDel(String chkDel) {
		this.chkDel = chkDel;
	}
	
}
