package com.quatro.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.MyDateFormat;
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarDB.DBPreparedHandlerParam;

import com.quatro.model.FieldDefValue;
import com.quatro.model.LookupCodeValue;
import com.quatro.model.LookupTableDefValue;
import com.quatro.util.Utility;
public class LookupDao extends HibernateDaoSupport {

	public List LoadCodeList(String tableId, boolean activeOnly, String code, String codeDesc)
	{
/*
	   ArrayList paramList = new ArrayList();
	   String sSQL="from LookupCodeValue s where s.prefix= ? order by s.orderByIndex,s.parentCode, s.description";		
	   paramList.add(tableIdName);
	   Object params[] = paramList.toArray(new Object[paramList.size()]);
	   return getHibernateTemplate().find(sSQL ,params);
*/	   
       Criteria criteria = getSession().createCriteria(LookupCodeValue.class);
       criteria.add(Restrictions.eq("prefix", tableId));
       if(activeOnly) criteria.add(Restrictions.eq("active",true));
	   if(!Utility.IsEmpty(code)) criteria.add(Restrictions.eq("code",code));
	   if(!Utility.IsEmpty(codeDesc)) criteria.add(Restrictions.ilike("description", "%" + codeDesc + "%"));
	   criteria.addOrder(Order.asc("orderByIndex"));
	   criteria.addOrder(Order.asc("parentCode"));
	   criteria.addOrder(Order.asc("description"));
	   List lst = criteria.list();
	   return lst;
	}
	
	public LookupCodeValue GetCode(String tableId,String code)
	{
        Criteria criteria = getSession().createCriteria(LookupCodeValue.class);
        criteria.add(Restrictions.eq("prefix", tableId));
		criteria.add(Restrictions.eq("code",code));
		List lst = criteria.list();
		LookupCodeValue lkv = null;
		if (lst.size()>0) 
		{
			lkv = (LookupCodeValue) lst.get(0);
		}
		return lkv;
	}

	public List LoadCodeList(String tableIdName,boolean activeOnly,  String parentCode,String code, String codeDesc)
	{
        Criteria criteria = getSession().createCriteria(LookupCodeValue.class);
        criteria.add(Restrictions.eq("prefix", tableIdName));
        if (activeOnly) {
        	criteria.add(Restrictions.eq("active",true));
        }
	   if (!Utility.IsEmpty(parentCode)) {
		   	criteria.add(Restrictions.eq("parentCode",parentCode));
	   }
	   if (!Utility.IsEmpty(code)) {
		   	criteria.add(Restrictions.eq("code",code));
	   }
	   if (!Utility.IsEmpty(codeDesc)) {
		   	criteria.add(Restrictions.ilike("code","%" + codeDesc + "%"));
	   }
	   criteria.addOrder( Order.asc("orderByIndex"));
	   criteria.addOrder( Order.asc("parentCode"));
	   criteria.addOrder( Order.asc("description"));
	   
	   return criteria.list();
	}

	public LookupTableDefValue GetLookupTableDef(String tableId)
	{
		ArrayList paramList = new ArrayList();

		String sSQL="from LookupTableDefValue s where s.tableId= ?";		
	    paramList.add(tableId);
	    Object params[] = paramList.toArray(new Object[paramList.size()]);
	    try{
	      return (LookupTableDefValue)getHibernateTemplate().find(sSQL ,params).get(0);
	    }catch(Exception ex){
	    	return null;
	    }
	}
	public List LoadFieldDefList(String tableId) 
	{
		String sSql = "from FieldDefValue s where s.tableId=? order by s.fieldIndex ";
		ArrayList paramList = new ArrayList();
	    paramList.add(tableId);
	    Object params[] = paramList.toArray(new Object[paramList.size()]);
		return getHibernateTemplate().find(sSql,params);
	}
	public List GetCodeFieldValues(LookupTableDefValue tableDef, String code)
	{
		String tableName = tableDef.getTableName();
		List fs = LoadFieldDefList(tableDef.getTableId());
		String idFieldName = ((FieldDefValue) fs.get(0)).getFieldSQL();
		
		String sql = "select ";
		for(int i=0; i<fs.size(); i++) {
			FieldDefValue fdv = (FieldDefValue) fs.get(i);
			if (i==0) {
				sql += fdv.getFieldSQL();
			}
			else
			{
				sql += "," + fdv.getFieldSQL();
			}
		}
		sql += " from " + tableName;
		sql += " where " + idFieldName + "='" + code + "'"; 
		try {
			DBPreparedHandler db = new DBPreparedHandler();
			ResultSet rs = db.queryResults(sql);
			if (rs.next()) {
				for(int i=0; i< fs.size(); i++) 
				{
					FieldDefValue fdv = (FieldDefValue) fs.get(i);
					String val = db.getString(rs, i+1);
					fdv.setVal(val);
					if (!Utility.IsEmpty(fdv.getLookupTable()))
					{
						LookupCodeValue lkv = GetCode(fdv.getLookupTable(),val);
						if (lkv != null) fdv.setValDesc(lkv.getDescription());
					}
				}
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.getStackTrace());
		}
		return fs;
	}
	public List GetCodeFieldValues(LookupTableDefValue tableDef)
	{
		String tableName = tableDef.getTableName();
		List fs = LoadFieldDefList(tableDef.getTableId());
		ArrayList codes = new ArrayList();
		String sql = "select ";
		for(int i=0; i<fs.size(); i++) {
			FieldDefValue fdv = (FieldDefValue) fs.get(i);
			if (i==0) {
				sql += fdv.getFieldSQL();
			}
			else
			{
				sql += "," + fdv.getFieldSQL();
			}
		}
		sql += " from " + tableName;
		try {
			DBPreparedHandler db = new DBPreparedHandler();
			ResultSet rs = db.queryResults(sql);
			while (rs.next()) {
				for(int i=0; i< fs.size(); i++) 
				{
					FieldDefValue fdv = (FieldDefValue) fs.get(i);
					String val = db.getString(rs, i+1);
					fdv.setVal(val);
					if (!Utility.IsEmpty(fdv.getLookupTable()))
					{
						LookupCodeValue lkv = GetCode(fdv.getLookupTable(),val);
						if (lkv != null) fdv.setValDesc(lkv.getDescription());
					}
				}
				codes.add(fs);
			}
			rs.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.getStackTrace());
		}
		return codes;
	}
	private int GetNextId(String idFieldName, String tableName) throws SQLException

	{
		String sql = "select max(" + idFieldName + ")";
		sql += " from " + tableName;
		DBPreparedHandler db = new DBPreparedHandler();
		ResultSet rs = db.queryResults(sql);
		int id = 0;
		if (rs.next()) 
			 id = rs.getInt(1);
		return id + 1;
	}
	
	public String SaveCodeValue(boolean isNew, LookupTableDefValue tableDef, List fieldDefList) throws SQLException
	{
		if (isNew) 
		{
			return InsertCodeValue(tableDef, fieldDefList);
		}
		else
			return UpdateCodeValue(tableDef,fieldDefList);
	}
	
	private String InsertCodeValue(LookupTableDefValue tableDef, List fieldDefList) throws SQLException
	{
		String tableName = tableDef.getTableName();
		String idFieldVal = "";

		DBPreparedHandlerParam[] params = new DBPreparedHandlerParam[fieldDefList.size()];
		String phs = "";
		String sql = "insert into  " + tableName + "("; 
		for(int i=0; i< fieldDefList.size(); i++) {
			FieldDefValue fdv = (FieldDefValue) fieldDefList.get(i);
			sql += fdv.getFieldName() + ",";
			phs +="?,"; 
			if (fdv.isAuto())
			{
				idFieldVal = String.valueOf(GetNextId(fdv.getFieldSQL(), tableName));
				fdv.setVal(idFieldVal);
			}
			if ("S".equals(fdv.getFieldType()))
			{
				params[i] = new DBPreparedHandlerParam(fdv.getVal());
			}
			else if ("D".equals(fdv.getFieldType()))
			{
				params[i] = new DBPreparedHandlerParam(MyDateFormat.getSysDate(fdv.getVal()));
			}
			else
			{
				params[i] = new DBPreparedHandlerParam(Integer.valueOf(fdv.getVal()).intValue());
			}
		}
		sql = sql.substring(0,sql.length()-1);
		phs = phs.substring(0,phs.length()-1);
		sql += ") values (" + phs + ")";

		//check the existence of the code 
		LookupCodeValue lkv= GetCode(tableDef.getTableId(), idFieldVal);
		if(lkv != null) 
		{
			throw new SQLException("The Code Already Exist");
		}
		DBPreparedHandler db = new DBPreparedHandler();
		db.queryExecuteUpdate(sql, params);
		return idFieldVal;
	}
	private String UpdateCodeValue(LookupTableDefValue tableDef, List fieldDefList) throws SQLException
	{
		String tableName = tableDef.getTableName();
		String idFieldName = "";
		String idFieldVal = "";

		DBPreparedHandlerParam[] params = new DBPreparedHandlerParam[fieldDefList.size()+1];
		String sql = "update " + tableName + " set ";
		for(int i=0; i< fieldDefList.size(); i++) {
			FieldDefValue fdv = (FieldDefValue) fieldDefList.get(i);
			if (fdv.getGenericIdx()==1) {
				idFieldName = fdv.getFieldSQL();
				idFieldVal = fdv.getVal();
			}
			
			sql += fdv.getFieldName() + "=?,";
			if ("S".equals(fdv.getFieldType()))
			{
				params[i] = new DBPreparedHandlerParam(fdv.getVal());
			}
			else if ("D".equals(fdv.getFieldType()))
			{
				params[i] = new DBPreparedHandlerParam(MyDateFormat.getSysDate(fdv.getVal()));
			}
			else
			{
				params[i] = new DBPreparedHandlerParam(Integer.valueOf(fdv.getVal()).intValue());
			}
		}
		sql = sql.substring(0,sql.length()-1);
		sql += " where " + idFieldName + "=?";
		params[fieldDefList.size()] = params[0];
		DBPreparedHandler db = new DBPreparedHandler();
		db.queryExecuteUpdate(sql, params);
		return idFieldVal;
	}
}
