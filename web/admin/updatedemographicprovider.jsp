<!--  
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
-->

<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin")) response.sendRedirect("../logout.jsp");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="updatedpBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="namevector" class="java.util.Vector" scope="page" />
<jsp:useBean id="novector" class="java.util.Vector" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"update_residentmultiple", "update demographiccust  set cust2 = ? where cust2 = ? and demographic_no in " }, 
{"update_nursemultiple", "update demographiccust  set cust1 = ? where cust1 = ? and demographic_no in " }, 
{"update_provider", "update demographic set provider_no = ? where provider_no = ? " }, 
{"select_demoname", "select d.demographic_no from demographic d, demographiccust c where c.cust2=? and d.demographic_no=c.demographic_no and d.last_name REGEXP ? " }, 
{"search_provider", "select provider_no, last_name, first_name from provider where provider_type='doctor' order by ?"}, 

{"select_demoname1", "select d.demographic_no from demographic d, demographiccust c where c.cust1=? and d.demographic_no=c.demographic_no and d.last_name REGEXP ? " }, 

{"update_residentsingle", "update demographiccust  set cust2 = ? where cust2 = ? and demographic_no in (?) " }, 
{"update_nursesingle", "update demographiccust  set cust1 = ? where cust1 = ? and demographic_no in (?) " }, 
  };
  String[][] responseTargets=new String[][] {  };
  updatedpBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<html>
<head><title> UPDATE PATIENT PROVIDERS </title></head>
<meta http-equiv="Cache-Control" content="no-cache" >
<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database
function setfocus() {
	this.focus();
}
function setregexp() {
	var exp = "^[" +document.ADDAPPT.last_name_from.value + "-" +document.ADDAPPT.last_name_to.value + "]" ;
	document.ADDAPPT.regexp.value = exp ;
//	alert(document.ADDAPPT.regexp.value);
}
function setregexp1() {
	var exp = "^[" +document.ADDAPPT1.last_name_from.value + "-" +document.ADDAPPT1.last_name_to.value + "]" ;
	document.ADDAPPT1.regexp.value = exp ;
//	alert(document.ADDAPPT1.regexp.value);
}
// stop javascript -->
</script>

<%
  ResultSet rsgroup =null;
  rsgroup = updatedpBean.queryResults("last_name", "search_provider");
 	while (rsgroup.next()) { 
 	  namevector.add(rsgroup.getString("provider_no"));
 	  namevector.add(rsgroup.getString("last_name")+", "+rsgroup.getString("first_name"));
 	}
%>
<body  background="../images/gray_bg.jpg" bgproperties="fixed"  onLoad="setfocus()" topmargin="0"  leftmargin="0" rightmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="<%=deepcolor%>"><th><font face="Helvetica">UPDATE PATIENT PROVIDER</font></th></tr>
</table>
<%
  if(request.getParameter("update")!=null && request.getParameter("update").equals(" Go ") ) {
    String [] param1 = new String[2] ;
    param1[0] = request.getParameter("oldcust2") ;
    param1[1] = request.getParameter("regexp") ;
    rsgroup = updatedpBean.queryResults(param1, "select_demoname");
    while (rsgroup.next()) { 
 	    novector.add(rsgroup.getString("demographic_no"));
    }
    int nosize = novector.size();
    int rowsAffected = 0;
    if(nosize != 0) { 
      String [] param = new String[nosize+2] ;
      param[0] = request.getParameter("newcust2") ;
      param[1] = request.getParameter("oldcust2") ;
 	    StringBuffer sbtemp = new StringBuffer("?") ;
      param[0+2] = (String) novector.get(0);

 	    if(nosize>1) {
 	      for(int i=1; i<nosize; i++) {
 	        sbtemp = sbtemp.append(",?");
          param[i+2] = (String) novector.get(i);
 	      }
      }
 	    String instrdemo = sbtemp.toString();
      dbQueries[0][1] = dbQueries[0][1] + "("+ instrdemo +")" ; 
//      System.out.println( dbQueries[0][1] );    
      updatedpBean.doConfigure(dbParams,dbQueries,responseTargets);
  	  rowsAffected = updatedpBean.queryExecuteUpdate(param, "update_residentmultiple");
  	}
    out.print( (rowsAffected==1?(rowsAffected+" record has "):(rowsAffected+" record(s) have ")) + "been updated. <br> "); 
  }

  if(request.getParameter("update")!=null && request.getParameter("update").equals(" Submit ") ) {
    String [] param1 = new String[2] ;
    param1[0] = request.getParameter("oldcust2") ;
    param1[1] = request.getParameter("regexp") ;
    rsgroup = updatedpBean.queryResults(param1, "select_demoname1");

    while (rsgroup.next()) { 
 	    novector.add(rsgroup.getString("demographic_no"));
    }
    int nosize = novector.size();
    int rowsAffected = 0;

    if(nosize != 0) { 
      String [] param = new String[nosize+2] ;
      param[0] = request.getParameter("newcust2") ;
      param[1] = request.getParameter("oldcust2") ;

 	    StringBuffer sbtemp = new StringBuffer("?") ;
      param[0+2] = (String) novector.get(0);

 	    if(nosize>1) {
 	      for(int i=1; i<nosize; i++) {
 	        sbtemp = sbtemp.append(",?");
          param[i+2] = (String) novector.get(i);
 	      }
      }
 	    String instrdemo = sbtemp.toString();
      dbQueries[1][1] += "("+ instrdemo +")" ; 
//    System.out.println( dbQueries[1][1] );    
      updatedpBean.doConfigure(dbParams,dbQueries,responseTargets);
  	  rowsAffected = updatedpBean.queryExecuteUpdate(param, "update_nursemultiple");
  	}
    out.print( (rowsAffected==1?(rowsAffected+" record has "):(rowsAffected+" record(s) have ")) + "been updated. <br> "); 
  }

%>

<center>
<table border="0" cellpadding="0" cellspacing="2" width="90%" bgcolor="<%=weakcolor%>" >
<FORM NAME = "ADDAPPT" METHOD="post" ACTION="updatedemographicprovider.jsp" onsubmit="return(setregexp())">
  <tr><td><b>Resident</b></td></tr>
  <tr><td>
  Use 
  <select name="newcust2" >
<%
 	 for(int i=0; i<namevector.size(); i=i+2) {
%>
  <option value="<%=namevector.get(i)%>" ><%=namevector.get(i+1)%></option>
<%
 	 }
%>
</select>

  replace
  <select name="oldcust2" >
<%
 	 for(int i=0; i<namevector.size(); i=i+2) {
%>
  <option value="<%=namevector.get(i)%>" ><%=namevector.get(i+1)%></option>
<%
 	 }
%>
</select><br>
  ON CONDITION <br>
  patient's last name from
  <select name="last_name_from" >
<%
   char cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
  <option value="<%=(char) (cletter+i) %>" ><%=(char) (cletter+i)%></option>
<%
 	 }
%>
</select>
  to
  <select name="last_name_to" >
<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
  <option value="<%=(char) (cletter+i) %>" ><%=(char) (cletter+i)%></option>
<%
 	 }
%>
</select>
<br>
  <INPUT TYPE="hidden" NAME="regexp" VALUE="">
  <INPUT TYPE="submit" NAME="update" VALUE=" Go " >


  </td></tr>
  </form>
</table>

<hr width=90% color='<%=deepcolor%>'>
<!-- for nurse -->
<table border="0" cellpadding="0" cellspacing="2" width="90%" bgcolor="<%=weakcolor%>" >
<FORM NAME = "ADDAPPT1" METHOD="post" ACTION="updatedemographicprovider.jsp" onsubmit="return(setregexp1())">
  <tr><td><b>Nurse</b></td></tr>
  <tr><td>
  Use 
  <select name="newcust2" >
<%
 	 for(int i=0; i<namevector.size(); i=i+2) {
%>
  <option value="<%=namevector.get(i)%>" ><%=namevector.get(i+1)%></option>
<%
 	 }
%>
</select>

  replace
  <select name="oldcust2" >
<%
 	 for(int i=0; i<namevector.size(); i=i+2) {
%>
  <option value="<%=namevector.get(i)%>" ><%=namevector.get(i+1)%></option>
<%
 	 }
%>
</select><br>
  ON CONDITION <br>
  patient's last name from
  <select name="last_name_from" >
<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
  <option value="<%=(char) (cletter+i) %>" ><%=(char) (cletter+i)%></option>
<%
 	 }
%>
</select>
  to
  <select name="last_name_to" >
<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
  <option value="<%=(char) (cletter+i) %>" ><%=(char) (cletter+i)%></option>
<%
 	 }
%>
</select>
<br>
  <INPUT TYPE="hidden" NAME="regexp" VALUE="">
  <INPUT TYPE="submit" NAME="update" VALUE=" Submit " >


  </td></tr>

</table>
<%
    updatedpBean.closePstmtConn();
%>

</center>
</body>
</html>