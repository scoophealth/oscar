/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */
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
