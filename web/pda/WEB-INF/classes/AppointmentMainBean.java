  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar;

import java.sql.*;
import javax.servlet.http.HttpServletRequest;

public class AppointmentMainBean {
  //set up a DBHandler and to forward the request to the right target
  //get request object by a DBHandler and a Dict, a Dict for the target
	
  private DBHandler dbH=null;
  private Dict targets=null;
  private Dict requestDict=null;
  private Dict queryDict=null;
  private String targetType=null; //view file name
  //String dboperation = null; //sql selector
  private boolean bDoConfigure = false;

  // default constructor
  public AppointmentMainBean() {}
  
  public Dict getTargets() {return targets;}
  public Dict getRequestDict() {return requestDict;}
  public boolean getBDoConfigure() {return bDoConfigure;}
  public void setBDoConfigure() { bDoConfigure = false;}
  
  public void doConfigure( String[]dbParams,String[][]dbQueries,String[][]responseTargets)
    throws SQLException {  //initialize this bean and then create a DBHandler bean

    bDoConfigure = true;
    if(dbH!=null) dbH=null; //return; // only do this once per session
    
    targets=new Dict();
    queryDict=new Dict();

    targets.setDef(responseTargets); // store response targets in the Dict
    queryDict.setDef(dbQueries); 

    dbH=makeDBHandler(dbParams, dbQueries);
  }
  private DBHandler makeDBHandler(String[]dbParams, String[][]dbQueries) throws SQLException {
    String dbDriver=dbParams[0];
    String dbName=dbParams[1];
    //set dbUser, dbPwd or leave as null if not provided
    String dbUser=dbParams[2];
    String dbPwd=dbParams[3];
    
    String []qNames=Misc.column(0,dbQueries); //help class to get column 0 for name
    String []qSqlStrings=Misc.column(1,dbQueries);
    //    System.out.println("qNames: "+qNames[0] +" "+qNames[1]+" sql: "+qSqlStrings[0]);

    return new DBHandler(dbDriver,dbName,dbUser,dbPwd,qNames,qSqlStrings);
  } //DBHandler is created, ready to run queries
  
  public void doCommand(HttpServletRequest request) {
  	//wrap the request object into a Dict help object
  	requestDict=new Dict();
  	requestDict.setDef(request);
  	setupRequestDict(); ////check through parameter name assumptions
  }
  private void setupRequestDict() {
  	//here we can check the request and complain if we don't like it
  }

  public String whereTo() {
  	targetType=requestDict.getDef("displaymode","");
  	//targetType={"day","week","month","add","search", "update","delete"));
  	return targets.getDef(targetType,"");
  }
  public String whereTo(String displaymode) {
  	targetType=requestDict.getDef(displaymode,"");
  	//targetType={"day","week","month","add","search", "update","delete"));
  	return targets.getDef(targetType,"");
  }

  //the following codes show the functions of a client bean
  public void queryExecute(String[] param) throws Exception{
	  // called from receiving page, not forwarding page.
	  String sqlExec = queryDict.getDef("dboperation","");
  	dbH.queryExecute(sqlExec, param);
    System.out.println("finish "+sqlExec);
  }
  public void queryExecute(String aKeyword) throws Exception{
	  // called from receiving page, not forwarding page.
	  // to reuse the above func
	  String sqlExec = queryDict.getDef("dboperation","");
	  String[] param =new String[] {aKeyword};
  	dbH.queryExecute(sqlExec, param);
  }
  public void queryExecute(String[] param, String dboperation) throws Exception{
	  // called from receiving page, not forwarding page.
	  String sqlExec = queryDict.getDef(dboperation,"");
  	dbH.queryExecute(sqlExec, param);
    System.out.println("finish "+sqlExec);
  }
  public void queryExecute(String aKeyword, String dboperation) throws Exception{
	  // called from receiving page, not forwarding page.
	  // to reuse the above func
	  String sqlExec = queryDict.getDef(dboperation,"");
	  String[] param =new String[] {aKeyword};
  	dbH.queryExecute(sqlExec, param);
  }
  
  public int queryExecuteUpdate(String[] param, String dboperation) throws Exception{
	  // called from receiving page, not forwarding page.
	  String sqlExec = queryDict.getDef(dboperation,"");
  	return (dbH.queryExecuteUpdate(sqlExec, param));
  }
  public int queryExecuteUpdate(String aKeyword, String dboperation) throws Exception{
	  // called from receiving page, not forwarding page.
	  // to reuse the above func
	  String sqlExec = queryDict.getDef(dboperation,"");
	  String[] param =new String[] {aKeyword};
  	return (dbH.queryExecuteUpdate(sqlExec, param));
  }
  public int queryExecuteUpdate(String[] param, int[] intparam, String dboperation) throws Exception{
	  String sqlExec = queryDict.getDef(dboperation,"");
  	return (dbH.queryExecuteUpdate(sqlExec, param, intparam));
  }
  public int queryExecuteUpdate(int[] intparam, String[] param, String dboperation) throws Exception{
	  String sqlExec = queryDict.getDef(dboperation,"");
  	return (dbH.queryExecuteUpdate(sqlExec, intparam, param));
  }
  public int queryExecuteUpdate(String[] param, int intparam, String dboperation) throws Exception{
	  String sqlExec = queryDict.getDef(dboperation,"");
	  int [] intKeyword =new int [] {intparam};
  	return (dbH.queryExecuteUpdate(sqlExec, param, intKeyword));
  }
  public int queryExecuteUpdate(int[] intparam, String dboperation) throws Exception{
	  String sqlExec = queryDict.getDef(dboperation,"");
  	return (dbH.queryExecuteUpdate(sqlExec, intparam));
  }

  public ResultSet queryResults(String[] aKeyword, String dboperation) throws Exception{
	 	String sqlQuery =null;
	  ResultSet rs =null;
	  //if search *, then use no aKeyword to query the result
	  if(aKeyword[0].equals("*")) {
	  	sqlQuery = queryDict.getDef("search*","");//(dboperation,""); temp use "search*" super command
    	rs = dbH.queryResults(sqlQuery); 
	  } else {
	  	//System.out.println("sql from appointmentmainbean.java---------------: "+aKeyword[1]+" dboperation: "+dboperation);
	  	sqlQuery = queryDict.getDef(dboperation,"");
	  	//System.out.println("sql from appointmentmainbean.java---------------: "+sqlQuery);
    	rs = dbH.queryResults(sqlQuery, aKeyword);
	  }
  	return rs;
  }
 
  public ResultSet queryResults(String aKeyword, String dboperation) throws Exception{
	  String sqlQuery = null;
	  ResultSet rs =null;
	  //if search *, then use no aKeyword to query the result
	  if(aKeyword.equals("*")) {
	  	sqlQuery = queryDict.getDef("search*","");//(dboperation,""); temp use "search*" super command
    	rs = dbH.queryResults(sqlQuery);
	  } else {
	  	sqlQuery = queryDict.getDef(dboperation,"");
    	rs = dbH.queryResults(sqlQuery, aKeyword);
	  }
  	return rs;
  }
 
  public ResultSet queryResults(int aKeyword, String dboperation) throws Exception{
	  String sqlQuery = null;
	  ResultSet rs =null;
	  //if search *, then use no aKeyword to query the result
  	sqlQuery = queryDict.getDef(dboperation,"");
   	rs = dbH.queryResults(sqlQuery, aKeyword);
  	return rs;
  }
  public ResultSet queryResults(String[] aKeyword, int[] nKeyword, String dboperation) throws Exception{
	  String sqlQuery = null;
	  ResultSet rs =null;
	  //if search *, then use no aKeyword to query the result
  	sqlQuery = queryDict.getDef(dboperation,"");
   	rs = dbH.queryResults(sqlQuery, aKeyword, nKeyword);
  	return rs;
  }
   
  // Don't forget to clean up!
  public void closePstmtConn() throws SQLException {
    dbH.closePstmt();
    dbH.closeConn();
  }
 
}