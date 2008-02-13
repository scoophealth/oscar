package com.quatro.service;

import java.util.List;
import com.quatro.dao.QuatroLookupDao;
import com.quatro.model.*;

public class QuatroLookupManager {
    private QuatroLookupDao lookupDao=null;

	public List LoadCodeList(String tableId, boolean activeOnly, String code, String codeDesc) {
        return lookupDao.LoadCodeList(tableId, activeOnly, code, codeDesc);
	}

    public LookupTableDefValue GetLookupTableDef(String tableId){
        return lookupDao.GetLookupTableDef(tableId);
    }
	
	public QuatroLookupDao getLookupDao() {
		return lookupDao;
	}

	public void setLookupDao(QuatroLookupDao lookupDao) {
		this.lookupDao = lookupDao;
	}
	
}
