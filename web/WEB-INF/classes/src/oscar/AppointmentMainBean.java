// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster Unviersity
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar;

import oscar.oscarDB.*;
import oscar.util.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.*;

public class AppointmentMainBean {

  private DBPreparedHandler dbPH=null;
  private UtilDict toFile=null;
  private UtilDict dbSQL=null;
  private UtilDict requestUtilDict=null;
  private String targetType=null;
  private boolean bDoConfigure = false;

  public UtilDict getTargets() {return toFile;}
  public UtilDict getRequestUtilDict() {return requestUtilDict;}
  public boolean getBDoConfigure() {return bDoConfigure;}
  public void setBDoConfigure() { bDoConfigure = false;}

  public void doConfigure( String[]dbParams,String[][]dbOperation ) throws Exception {
    bDoConfigure = true;
    if(dbPH!=null) dbPH=null;

    dbSQL=new UtilDict();
    dbSQL.setDef(dbOperation);
    dbPH=new DBPreparedHandler(dbParams[0],dbParams[1],dbParams[2],dbParams[3]);
  }

  public void doConfigure( String[]dbParams,String[][]dbOperation,String[][]controlToFile) throws Exception {
    bDoConfigure = true;
    if(dbPH!=null) dbPH=null;

    toFile=new UtilDict();
    toFile.setDef(controlToFile);
    dbSQL=new UtilDict();
    dbSQL.setDef(dbOperation);
//    makeDBPreparedHandler(dbParams, dbOperation);
    dbPH=new DBPreparedHandler(dbParams[0],dbParams[1],dbParams[2],dbParams[3]);
  }
  public void doCommand(HttpServletRequest request) {
  	//wrap the request object into a Dict help object
  	requestUtilDict=new UtilDict();
  	requestUtilDict.setDef(request);
  }

  public String whereTo() {
  	targetType=requestUtilDict.getDef("displaymode","");
  	return toFile.getDef(targetType,"");
  }
  public String whereTo(String displaymode) {
  	targetType=requestUtilDict.getDef(displaymode,"");
  	return toFile.getDef(targetType,"");
  }

  public int queryExecuteUpdate(String[] param, String dboperation) throws Exception{
	  String sqlExec = dbSQL.getDef(dboperation,"");
  	return (dbPH.queryExecuteUpdate(sqlExec, param));
  }
  public int queryExecuteUpdate(String aKeyword, String dboperation) throws Exception{
	  String sqlExec = dbSQL.getDef(dboperation,"");
	  String[] param =new String[] {aKeyword};
  	return (dbPH.queryExecuteUpdate(sqlExec, param));
  }
  public int queryExecuteUpdate(String[] param,Date[] dtParam, int[] intparam, String dboperation) throws Exception{
	  String sqlExec = dbSQL.getDef(dboperation,"");
     return (dbPH.queryExecuteUpdate(sqlExec, param, dtParam,intparam));
  }
  public int queryExecuteUpdate(String[] param, int[] intparam, String dboperation) throws Exception{
	  String sqlExec = dbSQL.getDef(dboperation,"");
  	return (dbPH.queryExecuteUpdate(sqlExec, param, intparam));
  }
  public int queryExecuteUpdate(int[] intparam, String[] param, String dboperation) throws Exception{
	  String sqlExec = dbSQL.getDef(dboperation,"");
  	return (dbPH.queryExecuteUpdate(sqlExec, intparam, param));
  }
  public int queryExecuteUpdate(String[] param, int intparam, String dboperation) throws Exception{
	  String sqlExec = dbSQL.getDef(dboperation,"");
	  int [] intKeyword =new int [] {intparam};
  	return (dbPH.queryExecuteUpdate(sqlExec, param, intKeyword));
  }
  public int queryExecuteUpdate(int[] intparam, String dboperation) throws Exception{
	  String sqlExec = dbSQL.getDef(dboperation,"");
  	return (dbPH.queryExecuteUpdate(sqlExec, intparam));
  }

  public ResultSet queryResults(String[] aKeyword, String dboperation) throws Exception{
	 	String sqlQuery =null;

	  ResultSet rs =null;
	  if(aKeyword[0].equals("*")) {
	  	sqlQuery = dbSQL.getDef("search*","");
    	rs = dbPH.queryResults(sqlQuery);
	  } else {
	  	sqlQuery = dbSQL.getDef(dboperation,"");
    	rs = dbPH.queryResults(sqlQuery, aKeyword);
	  }
     // System.out.println("sqlQuery=" + sqlQuery);
  	return rs;
  }

  public Object[] queryResultsCaisi(String[] aKeyword, String dboperation) throws Exception{
	String sqlQuery =null;
	Object[] rs =null;
	  if(aKeyword[0].equals("*")) {
	  	sqlQuery = dbSQL.getDef("search*","");
	  	rs = dbPH.queryResultsCaisi(sqlQuery);
	  } else {
	  	sqlQuery = dbSQL.getDef(dboperation,"");
	  	rs = dbPH.queryResultsCaisi(sqlQuery, aKeyword);
	  }
	return rs;
  }
  public Object[] queryResultsCaisi(String aKeyword, String dboperation) throws Exception{
	  String sqlQuery = null;
	  Object[] rs =null;
	  if(aKeyword.equals("*")) {
	  	sqlQuery = dbSQL.getDef("search*","");
    	rs = dbPH.queryResultsCaisi(sqlQuery);
	  } else {
	  	sqlQuery = dbSQL.getDef(dboperation,"");
    	rs = dbPH.queryResultsCaisi(sqlQuery, aKeyword);
	  }
  	return rs;
  }
  public Object[] queryResultsCaisi(int aKeyword, String dboperation) throws Exception{
	  String sqlQuery = null;
  	sqlQuery = dbSQL.getDef(dboperation,"");
  	return dbPH.queryResultsCaisi(sqlQuery, aKeyword);
  }
  public Object[] queryResultsCaisi(String dboperation) throws Exception {
      String sqlQuery = dbSQL.getDef(dboperation);
      return dbPH.queryResultsCaisi(sqlQuery);
    }
  
  public ResultSet queryResults(String aKeyword, String dboperation) throws Exception{
	  String sqlQuery = null;
	  ResultSet rs =null;
	  if(aKeyword.equals("*")) {
	  	sqlQuery = dbSQL.getDef("search*","");
    	rs = dbPH.queryResults(sqlQuery);
	  } else {
	  	sqlQuery = dbSQL.getDef(dboperation,"");
    	rs = dbPH.queryResults(sqlQuery, aKeyword);
	  }
  	return rs;
  }
  public ResultSet queryResults(int aKeyword, String dboperation) throws Exception{
	  String sqlQuery = null;
	  ResultSet rs =null;
  	sqlQuery = dbSQL.getDef(dboperation,"");
   	rs = dbPH.queryResults(sqlQuery, aKeyword);
  	return rs;
  }
  public ResultSet queryResults(String[] aKeyword, int[] nKeyword, String dboperation) throws Exception{
    String sqlQuery = null;
    ResultSet rs =null;
  	sqlQuery = dbSQL.getDef(dboperation,"");
   	rs = dbPH.queryResults(sqlQuery, aKeyword, nKeyword);
  	return rs;
  }

    public ResultSet queryResults(int[] parameters, String dboperation) throws Exception{
      String sqlQuery = dbSQL.getDef(dboperation);
      return dbPH.queryResults(sqlQuery, parameters);
    }
    /* This method is called by querys that dont need to set a PreparedStatement */
    public ResultSet queryResults(String dboperation) throws Exception {
      String sqlQuery = dbSQL.getDef(dboperation);
      return dbPH.queryResults(sqlQuery);
    }

  // Don't forget to clean up!
  public void closePstmtConn() throws SQLException {
    dbPH.closePstmt();
    dbPH.closeConn();
  }
 
}
