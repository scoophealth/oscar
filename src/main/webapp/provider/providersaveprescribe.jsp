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

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
%>
<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicAccessoryDao" %>
<%@page import="org.oscarehr.common.model.DemographicAccessory" %>
<%
	DemographicAccessoryDao demographicAccessoryDao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A PRESCRIBE RECORD</font></th>
	</tr>
</table>
<%
  //if action is good, then give me the result
  String temp=null, content="";//default is not null
  for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	temp=e.nextElement().toString();
	if( temp.indexOf("xml_")==-1) continue;
  	content+="<" +temp+ ">" +request.getParameter(temp)+ "</" +temp+ ">";
  }

    String[] param =new String[5];
	  param[0]=request.getParameter("demographic_no");
	  param[1]=request.getParameter("user_no");
	  param[2]=request.getParameter("prescribe_date");
	  param[3]=request.getParameter("prescribe_time"); //MyDateFormat.getTimeXX_XX_XX(request.getParameter("prescribe_time"));
	  param[4]=request.getParameter("xml_Medication");

    //to update demographic acce record
    String[] param1 =new String[2]; //update demographic acce record
	  param1[0]=request.getParameter("demographic_no");
	  param1[1]=content;
    String[] param2 =new String[2];
	  param2[0]=param1[1];
	  param2[1]=param1[0];
    long numRecord=1, rowsAffected=0;
    DemographicAccessory da = demographicAccessoryDao.find(Integer.parseInt(request.getParameter("demographic_no")));
    if(da != null) {
    	da.setContent(content);
    	demographicAccessoryDao.merge(da);
    } else {
    	da = new DemographicAccessory();
    	da.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
    	da.setContent(content);
    	demographicAccessoryDao.persist(da);
    }

  //add the prescribe record
  rowsAffected = oscarSuperManager.update("providerDao", request.getParameter("dboperation"), param);
  if (rowsAffected == 1) {
	  List<Map<String, Object>> resultList = oscarSuperManager.find("providerDao", "search_prescribe_no", new Object[] {request.getParameter("demographic_no")});
    for (Map pre : resultList) {
%>
<p>
<h1>Successful Addition of a Prescribe Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.document.encounter.encounterattachment.value +="<prescribe>providercontrol.jsp?prescribe_no=<%=pre.get("prescribe_no")%>^displaymode=vary^displaymodevariable=providerprescribesingle.jsp^dboperation=search_prescribe^bNewForm=0</prescribe>";
      self.opener.document.encounter.attachmentdisplay.value +="Prescribe ";
     	//self.opener.refresh();
</script>
<%
      break; //get only one prescribe_no
    }//end of while
  }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%
  }
%>
<p></p>
<hr width="90%"/>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
