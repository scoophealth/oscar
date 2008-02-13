package com.quatro.dao;

import java.util.ArrayList;
import java.util.List;
import com.quatro.model.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QuatroLookupDao extends HibernateDaoSupport {

	public List LoadCodeList(String tableIdName, boolean activeOnly, String code, String codeDesc)
	{
	   ArrayList paramList = new ArrayList();
	   String sSQL="FROM LookupCodeValue s where s.prefix= ? ORDER BY s.description";		
	   paramList.add(tableIdName);
	   Object params[] = paramList.toArray(new Object[paramList.size()]);
	   return getHibernateTemplate().find(sSQL ,params);
	}

	public LookupTableDefValue GetLookupTableDef(String tableId)
	{
		ArrayList paramList = new ArrayList();
	    String sSQL="FROM LookupTableDefValue s where s.tableId= ?";		
	    paramList.add(tableId);
	    Object params[] = paramList.toArray(new Object[paramList.size()]);
	    try{
	      return (LookupTableDefValue)getHibernateTemplate().find(sSQL ,params).get(0);
	    }catch(Exception ex){
	    	return null;
	    }
	}
}
