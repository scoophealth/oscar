<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%
  //if action is good, then give me the result
	  //param[0]=Integer.parseInt((new GregorianCalendar()).get(Calendar.MILLISECOND) ); //int
    String[] param =new String[24];
	  param[0]=request.getParameter("last_name");
	  param[1]=request.getParameter("first_name");
	  param[2]=request.getParameter("address");
	  param[3]=request.getParameter("city");
	  param[4]=request.getParameter("province");
	  param[5]=request.getParameter("postal");
	  param[6]=request.getParameter("phone");
	  param[7]=request.getParameter("ext");
	  param[8]=request.getParameter("year_of_birth");
	  param[9]=request.getParameter("month_of_birth");
	  param[10]=request.getParameter("date_of_birth");
	  param[11]=request.getParameter("hin");
	  param[12]=request.getParameter("ver");
	  param[13]=request.getParameter("roster_status");
	  param[14]=request.getParameter("patient_status");
	  param[15]=request.getParameter("date_joined");
	  param[16]=request.getParameter("chart_no");
	  param[17]=request.getParameter("staff");
	  param[18]=request.getParameter("sex");
	  param[19]=request.getParameter("end_date");
	  param[20]=request.getParameter("eff_date");
	  param[21]=request.getParameter("pcn_indicator");
	  param[22]=request.getParameter("hc_type");
	  param[23]=request.getParameter("hc_renew_date");
	  
	  //int []intparam=new int [] {Integer.parseInt(request.getParameter("cpp_id"))};
	  
	  // should confirm the new cpp does not exist in database
	  // check dob and hin and lastname, if someone's there, alarm and provide the choice (go or stop).
    //int rowsAffected = apptMainBean.queryExecuteUpdate(intparam, param, request.getParameter("dboperation"));
    String[] paramName =new String[5];
	  paramName[0]=param[0].trim();
	  paramName[1]=param[1].trim();
	  paramName[2]=param[8].trim();
	  paramName[3]=param[9].trim();
	  paramName[4]=param[10].trim();
    ResultSet rs = apptMainBean.queryResults(paramName, "search_lastfirstnamedob");
    if(rs.next()) { 
      out.println("!!!!!!!!!!!<br><font color='red'>You may have the CPP record already!!! <BR>Please search it first or add it at 'Search/Add Patients'.</font><br>******************");
      return;
    }
   
  //now add the new record
  int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation"));
  
  if (rowsAffected >=1) {
    String name = param[0]+","+param[1];
    String cpp_id=null;
 		rs = apptMainBean.queryResults(paramName, "search_lastfirstnamedob");
  	if(rs.next()) cpp_id=apptMainBean.getString(rs,"cpp_id");
%>
<p>
<h2>Successful Addition of a cpp Record.</h2>
</p>
<%  
  if(true) {
    out.clear();
    pageContext.forward("../appointment/editappointmentfromcpp.jsp?name="+name+"&cpp_id="+cpp_id); //forward request&response to the target page
    return;
  }
  } else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
  }
%>
