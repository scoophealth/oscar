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

import org.apache.struts.action.ActionForm;

public class QuatroReportSaveTemplateForm extends ActionForm{
	private String optSaveAs;
	private String txtDescription;
	private String txtTitle;
	private String chkPrivate;
	private String msg;
	private String optSaveAsSelected;

	public String getChkPrivate() {
		return chkPrivate;
	}
	public void setChkPrivate(String chkPrivate) {
		this.chkPrivate = chkPrivate;
	}
	public String getOptSaveAs() {
		return optSaveAs;
	}
	public void setOptSaveAs(String optSaveAs) {
		this.optSaveAs = optSaveAs;
	}
	public String getOptSaveAsSelected() {
		return optSaveAsSelected;
	}
	public void setOptSaveAsSelected(String optSaveAsSelected) {
		this.optSaveAsSelected = optSaveAsSelected;
	}
	public String getTxtDescription() {
		return txtDescription;
	}
	public void setTxtDescription(String txtDescription) {
		this.txtDescription = txtDescription;
	}
	public String getTxtTitle() {
		return txtTitle;
	}
	public void setTxtTitle(String txtTitle) {
		this.txtTitle = txtTitle;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
