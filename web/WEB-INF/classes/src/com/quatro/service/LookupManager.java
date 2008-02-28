package com.quatro.service;

import java.util.List;
import com.quatro.dao.LookupDao;
import com.quatro.model.*;
import java.sql.*;

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
	public void SaveCodeValue(boolean isNew, LookupTableDefValue tableDef, List fieldDefList) throws SQLException
	{
		lookupDao.SaveCodeValue(isNew, tableDef, fieldDefList);
	}
}
