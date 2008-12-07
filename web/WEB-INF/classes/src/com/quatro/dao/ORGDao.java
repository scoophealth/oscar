package com.quatro.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.LookupTableDefValue;
import com.quatro.dao.LookupDao;

public class ORGDao extends HibernateDaoSupport {

	/* Column property mappings defined by the generic idx 
	 *  1 - Code 2 - Description 3 Active 
	 *  4 - Display Order, 5 - ParentCode 6 - Buf1 7 - CodeTree 8 - codecsv
	 */
	
	private LookupDao lookupDao;
	
	public void setLookupDao(LookupDao lookupDao) {
		this.lookupDao = lookupDao;
	}

	public LookupTableDefValue GetLookupTableDef(String tableId)
	{
		return lookupDao.GetLookupTableDef(tableId);
	}
	
	public List LoadCodeList(String tableId, boolean activeOnly, String code, String codeDesc)
	{
	   return LoadCodeList(tableId,activeOnly,"",code,codeDesc);
	}

	public List LoadFieldDefList(String tableId) 
	{
		return lookupDao.LoadFieldDefList(tableId);
	}

	public List LoadCodeList(String tableId,boolean activeOnly,  String parentCode,String code, String codeDesc)
	{
	   return lookupDao.LoadCodeList(tableId, activeOnly, parentCode,code, codeDesc);
	}
	
	public void delete(String orgcd) {
		
		try {
			//getSession().delete(persistentInstance);
			
			getHibernateTemplate().bulkUpdate("delete LstOrgcd q where q.code=?", orgcd);
		
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
}
