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
package com.quatro.service;

import java.sql.SQLException;
import java.util.List;

import com.quatro.dao.LookupDao;
import com.quatro.model.LookupCodeValue;
import com.quatro.model.LookupTableDefValue;

public class LookupManager {
    private LookupDao lookupDao=null;

	public List LoadCodeList(String tableId, boolean activeOnly, String code, String codeDesc) {
        return lookupDao.LoadCodeList(tableId, activeOnly, code, codeDesc);
	}

	public List LoadCodeList(String tableId, boolean activeOnly, String parentCode, String code, String codeDesc) {
        return lookupDao.LoadCodeList(tableId, activeOnly,parentCode, code, codeDesc);
	}

    public LookupTableDefValue GetLookupTableDef(String tableId){
        return lookupDao.GetLookupTableDef(tableId);
    }
	
    public LookupCodeValue GetLookupCode(String tableId, String code){
        return lookupDao.GetCode(tableId, code);
    }

    public LookupDao getLookupDao() {
		return lookupDao;
	}

	public void setLookupDao(LookupDao lookupDao) {
		this.lookupDao = lookupDao;
	}
	
	public List LoadFieldDefList(String tableId)
	{
		return lookupDao.LoadFieldDefList(tableId);
	}
	public List GetCodeFieldValues(LookupTableDefValue tableDef, String code)
	{
		return lookupDao.GetCodeFieldValues(tableDef, code);
	}
	public List GetCodeFieldValues(LookupTableDefValue tableDef)
	{
		return lookupDao.GetCodeFieldValues(tableDef);
	}
	public String SaveCodeValue(boolean isNew, LookupTableDefValue tableDef, List fieldDefList) throws SQLException
	{
		return lookupDao.SaveCodeValue(isNew, tableDef, fieldDefList);
	}
	
	public int getCountOfActiveClient(String orgCd) throws SQLException{
   	    return lookupDao.getCountOfActiveClient(orgCd); 
	}

}
