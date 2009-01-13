package com.quatro.web.lookup;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.quatro.model.LookupTableDefValue;

public class LookupListForm extends ActionForm{
	List lookups;
    String openerForm;
    String codeName;
    String descName;
    String keywordName;
    String tableId;
    String parentCode;
    String grandParentCode;
    LookupTableDefValue tableDef;
    
	public List getLookups() {
		return lookups;
	}

	public void setLookups(List lookups) {
		this.lookups = lookups;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getDescName() {
		return descName;
	}

	public void setDescName(String descName) {
		this.descName = descName;
	}

	public String getOpenerForm() {
		return openerForm;
	}

	public void setOpenerForm(String openerForm) {
		this.openerForm = openerForm;
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

	public LookupTableDefValue getTableDef() {
		return tableDef;
	}

	public void setTableDef(LookupTableDefValue tableDef) {
		this.tableDef = tableDef;
	}

	public String getGrandParentCode() {
		return grandParentCode;
	}

	public void setGrandParentCode(String grandParentCode) {
		this.grandParentCode = grandParentCode;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
