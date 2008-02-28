package com.quatro.web.lookup;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class LookupCodeEditForm extends ActionForm{
	List codeFields;
	com.quatro.model.LookupTableDefValue tableDef;
	boolean newCode;
	String errMsg;
	
	public List getCodeFields() {
		return codeFields;
	}
	public void setCodeFields(List codeFields) {
		this.codeFields = codeFields;
	}
	public boolean isNewCode() {
		return newCode;
	}
	public void setNewCode(boolean newCode) {
		this.newCode = newCode;
	}
	public com.quatro.model.LookupTableDefValue getTableDef() {
		return tableDef;
	}
	public void setTableDef(com.quatro.model.LookupTableDefValue tableDef) {
		this.tableDef = tableDef;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
