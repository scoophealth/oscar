package com.quatro.service;


import java.util.List;

import com.quatro.dao.ORGDao;
import com.quatro.model.LookupTableDefValue;

public class ORGManager {
    private ORGDao orgDao=null;
	
    public ORGDao getOrgDao() {
		return orgDao;
	}

	public void setOrgDao(ORGDao orgDao) {
		this.orgDao = orgDao;
	}
	
    public LookupTableDefValue GetLookupTableDef(String tableId){
        return orgDao.GetLookupTableDef(tableId);
    }
	
    public List LoadCodeList(String tableId, boolean activeOnly, String code, String codeDesc) {
        return orgDao.LoadCodeList(tableId, activeOnly, code, codeDesc);
	}
	    
}
