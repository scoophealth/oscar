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
