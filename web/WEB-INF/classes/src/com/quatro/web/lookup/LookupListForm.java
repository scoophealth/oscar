package com.quatro.web.lookup;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class LookupListForm extends ActionForm{
	List lookups;
    String openerFormName;
    String openerCodeElementName;
    String openerDescElementName;
    String keywordName;
    String tableId;
    
	public List getLookups() {
		return lookups;
	}

	public void setLookups(List lookups) {
		this.lookups = lookups;
	}

	public String getOpenerCodeElementName() {
		return openerCodeElementName;
	}

	public void setOpenerCodeElementName(String openerCodeElementName) {
		this.openerCodeElementName = openerCodeElementName;
	}

	public String getOpenerDescElementName() {
		return openerDescElementName;
	}

	public void setOpenerDescElementName(String openerDescElementName) {
		this.openerDescElementName = openerDescElementName;
	}

	public String getOpenerFormName() {
		return openerFormName;
	}

	public void setOpenerFormName(String openerFormName) {
		this.openerFormName = openerFormName;
	}

	public String getKeywordName() {
		return keywordName;
	}

	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
}
