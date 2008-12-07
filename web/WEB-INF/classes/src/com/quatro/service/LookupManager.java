package com.quatro.service;

import java.sql.SQLException;
import java.util.List;

import com.quatro.dao.LookupDao;
import com.quatro.model.LookupTableDefValue;
import com.quatro.model.LookupCodeValue;

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
