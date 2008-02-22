package com.quatro.web.report;

import org.apache.struts.action.ActionForm;

public class QuatroReportSaveTemplateForm extends ActionForm{
	private String optSaveAs;
	private String txtDescription;
	private String txtTitle;
	private String chkPrivate;
	private String msg;

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
