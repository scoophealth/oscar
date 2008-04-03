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

	/* Column property mappings defined by the generic idx 
	 *  1 - Code 2 - Description 3 Active 
	 *  4 - Display Order, aka LineId 5 - ParentCode 6 - Buf1
	 */
	
	public List LoadCodeList(String tableId, boolean activeOnly, String code, String codeDesc)
	{
	   return LoadCodeList(tableId,activeOnly,"",code,codeDesc);
	}
	
	public LookupCodeValue GetCode(String tableId,String code)
	{
		List lst = LoadCodeList(tableId, true, "", code, "");
		LookupCodeValue lkv = null;
		if (lst.size()>0) 
		{
			lkv = (LookupCodeValue) lst.get(0);
		}
		return lkv;
	}

	public List LoadCodeList(String tableId,boolean activeOnly,  String parentCode,String code, String codeDesc)
	{
		LookupTableDefValue tableDef = GetLookupTableDef(tableId);
		List fields = LoadFieldDefList(tableId);
		DBPreparedHandlerParam [] params = new DBPreparedHandlerParam[4];
		String fieldNames [] = new String[6];
		String sSQL="select ";
		for (int i = 1; i <= 6; i++)
		{
			boolean ok = false;
			for (int j = 0; j<fields.size(); j++)
			{
				FieldDefValue fdef = (FieldDefValue)fields.get(j);
				if (fdef.getGenericIdx()== i)
				{
					sSQL += fdef.getFieldSQL() + ",";
					fieldNames[i-1]=fdef.getFieldSQL();
					ok = true;
					break;
				}
			}
			if (!ok) {
				sSQL += " null field" + i + ",";
				fieldNames[i-1] = "field" + i;
			}
		}
		sSQL = sSQL.substring(0,sSQL.length()-1); 
	    sSQL +=" from " + tableDef.getTableName() + " s where 1=1";
	    int i= 0;
        if (activeOnly) {
	    	sSQL += " and " + fieldNames[2] + "=?"; 
	    	params[i++] = new DBPreparedHandlerParam(1);
        }
	   if (!Utility.IsEmpty(parentCode)) {
	    	sSQL += " and " + fieldNames[4] + "=?"; 
	    	params[i++]= new DBPreparedHandlerParam(parentCode);
	   }
	   if (!Utility.IsEmpty(code)) {
	    	sSQL += " and " + fieldNames[0] + "=?"; 
	    	params[i++] = new DBPreparedHandlerParam(code);
	   }
	   if (!Utility.IsEmpty(codeDesc)) {
	    	sSQL += " and " + fieldNames[1] + " like ?"; 
	    	params[i++]= new DBPreparedHandlerParam("%" + codeDesc + "%");
	   }
	   sSQL += " order by 4,5,2";
	   DBPreparedHandlerParam [] pars = new DBPreparedHandlerParam[i];
	   for(int j=0; j<i;j++)
	   {
		   pars[j] = params[j];
	   }
	   
	   DBPreparedHandler db = new DBPreparedHandler();
	   ArrayList list = new ArrayList();
	   try {
		   ResultSet rs = db.queryResults(sSQL,pars);
		   while (rs.next()) {
			   LookupCodeValue lv = new LookupCodeValue();
			   lv.setPrefix(tableId);
			   lv.setCode(rs.getString(1));
			   lv.setDescription(db.getString(rs, 2));
			   lv.setActive(1 == Integer.valueOf("0" + db.getString(rs, 3)));
			   lv.setLineId(Integer.valueOf("0" + db.getString(rs,4)));
			   lv.setParentCode(db.getString(rs, 5));
			   lv.setBuf1(db.getString(rs,6));
			   list.add(lv);
			}
			rs.close();
	   }
	   catch(SQLException e)
	   {
		   System.out.println(e.getStackTrace().toString());
	   }
	   return list;
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
