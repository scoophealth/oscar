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
  if(session.getValue("user") == null) 
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
//  mygroupno = (String) session.getAttribute("groupno");  
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");

%> 
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDMS.jsp" %>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
  String function = request.getParameter("function");
  String functionid = request.getParameter("functionid");
  String doctype = request.getParameter("doctype");
%>
<html> 
<head>
<title>DOCUMENT MANAGEMENT SYSTEM</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--

var remote=null;
function refresh() {
  history.go(0);
}
function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}


var awnd=null;
function popPage(url) {
  
  awnd=rs('',url ,400,200,1);
  awnd.focus();
}

function checkDelete(url, c, u,n){
if (c == u || n == '<%=oscarVariables.getProperty("SUPERUSER")%>'){
 if(confirm("You are about to delete the document, are you sure?")) {
    popPage(url);
  }
}else{
alert("You're not allowed to delete this document!");
}


}

function setfocus() {
  this.focus();
}
//-->
</script>
<link rel="stylesheet" href="dms.css" />
</head>
<body  onLoad="setfocus()" background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr bgcolor="#486ebd">
     <th align='LEFT'>
		<input type='button' name='print' value='Print' onClick='window.print()'> </th> 
    <th align='RIGHT'  ><font face="Arial, Helvetica, sans-serif" color="#FFFFFF">DOCUMENT REPORT &nbsp;&nbsp;&nbsp;&nbsp;</font></th>
      <th align='RIGHT'><input type="button" name="Button" value="Add HTML Code" onclick="window.open('../dms/addhtmldocument.jsp?function=<%=function%>&functionid=<%=functionid%>&creator=<%=request.getParameter("curUser")%>','', 'scrollbars=yes,resizable=yes,width=600,height=600')";><input type="button" name="Button" value="Add Document" onclick="window.open('../dms/adddocument.jsp?function=<%=function%>&functionid=<%=functionid%>&creator=<%=request.getParameter("curUser")%>','', 'scrollbars=yes,resizable=yes,width=600,height=300')";><input type='button' name='close' value='Done' onClick='window.close()'></th>
  </tr>
</table>


<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7">
<tr valign="top"> 
      
    <td rowspan="2" ALIGN="center" valign="middle"> 
 
          <table width="100%" border="1" cellspacing="0" cellpadding="0">
          <tr><td colspan='5'><b><%=function.substring(0,1).toUpperCase()%><%=function.substring(1)%>'s folder</b></td></td>
<tr>
          <td width="34%"><b>Document Description</b></td>
          <td width="15%"><b>Document Type</b></td>
          <td width="17%"><b><%=function.substring(0,1).toUpperCase()%><%=function.substring(1)%></b></td>
          <td width="21%"><b>Updatedate</b></td> 
          <td width="13%"><b>Action</b></td>
  </tr>
  
  <%String creator="%", docfilename="", docdesc="", dispDesc="", dispType="", dispDocNo="", dispFunction="", dispCreator="", dispUpdatedate="", dispFilename="", dispStatus="";
  int count0 = 0;
 if(request.getParameter("creator") != null) creator= request.getParameter("creator") ;
  if(request.getParameter("docfilename") != null) docfilename= request.getParameter("docfilename") ;
   if(request.getParameter("doctype") != null) doctype= request.getParameter("doctype") ;
   if(request.getParameter("docdesc") != null) docdesc= request.getParameter("docdesc") ;
    String proFirst="", proLast="", proOHIP="", proNo="";

           String[] param0 = new String[3];
                 param0[0] = function;
                 param0[1] = functionid;
            param0[2] = "%"; // doctype;
    
          
        ResultSet rslocal2 = null;
 	rslocal2 = apptMainBean.queryResults(param0, "match_document");
 	  while(rslocal2.next()){
 	  dispDesc = rslocal2.getString("docdesc");
 	   dispFilename = rslocal2.getString("docfilename");
 	    dispType = rslocal2.getString("doctype");
 	    dispCreator = rslocal2.getString("doccreator");
 	dispDocNo = rslocal2.getString("document_no");
       dispUpdatedate = rslocal2.getString("updatedatetime");
       dispStatus = rslocal2.getString("status");
  count0 = count0 + 1;
  
  if (dispStatus.compareTo("A") == 0) dispStatus="active";
     if (dispStatus.compareTo("H") == 0) dispStatus="html";    
   //   if (function.compareTo("demographic") == 0){
       
             ResultSet rslocal=null;
             rslocal = null;
             rslocal = apptMainBean.queryResults(functionid, "search_"+function+"_details");  
   	    while(rslocal.next()){
       
       proFirst = rslocal.getString("first_name");
            proLast = rslocal.getString("last_name");
  //   proOHIP = rslocal.getString("provider_no");
  }
//  }
%>
  <tr>
          <td width="34%"><a href=# onClick="javascript:rs('new','documentGetFile.jsp?document=<%=dispFilename%>&type=<%=dispStatus%>&doc_no=<%=dispDocNo%>', 480,480,1)"><%=dispDesc%></td>
          <td width="15%"><%=dispType%></td>
          <td width="17%"><%=proLast%>, <%=proFirst%></td>
          <td width="21%"><%=dispUpdatedate%></td>
          <td width="13%"><a href=# onClick="checkDelete('documentDelete.jsp?document_no=<%=dispDocNo%>&function=<%=function%>&functionid=<%=functionid%>','<%=dispCreator%>','<%=curUser_no%>','<%=userlastname%>')">Delete</td>
  </tr> 

<%

}

if (count0 == 0) {
  %>
    <tr><td colspan='5'>no match found</td></tr>
    <%
    }
  %>
  </table>
  </td>
</tr>
</table>

  <%
     if (function.compareTo("provider") == 0){
     %>
     <table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4E9E7">
     <tr valign="top"> 
           
         <td rowspan="2" ALIGN="center" valign="middle"> 
      
          <table width="100%" border="1" cellspacing="0" cellpadding="0" >  
<tr><td colspan='5'><b>Share folder</b></td></td>
<tr>
          <td width="34%"><b>Document Description</b></td>
          <td width="15%"><b>Document Type</b></td>
          <td width="17%"><b><%=function.substring(0,1).toUpperCase()%><%=function.substring(1)%></b></td>
          <td width="21%"><b>Updatedate</b></td> 
          <td width="13%"><b>Active</b></td>
  </tr>
   <% creator="%"; docfilename="";docdesc="";dispDesc=""; dispType=""; dispFunction=""; dispCreator=""; dispUpdatedate=""; dispFilename=""; dispStatus="";



       int count = 0;
             String[] param1 = new String[1];
                   param1[0] = function;
                  
            
         rslocal2 = null;
   	rslocal2 = apptMainBean.queryResults(param1, "share_document");
   	  while(rslocal2.next()){
   	  dispDesc = rslocal2.getString("docdesc");
   	   dispFilename = rslocal2.getString("docfilename");
   	    dispType = rslocal2.getString("doctype");
          dispCreator = rslocal2.getString("doccreator");
         dispUpdatedate = rslocal2.getString("updatedatetime");
         dispDocNo = rslocal2.getString("document_no");
         dispStatus = rslocal2.getString("status");
    count = count + 1;
    
    if (dispStatus.compareTo("A") == 0) dispStatus="active";
         if (dispStatus.compareTo("H") == 0) dispStatus="html";    
     
	           ResultSet    rslocal = null;
	               rslocal = apptMainBean.queryResults(dispCreator, "search_"+function+"_details");  
	     	    while(rslocal.next()){
	         
	         proFirst = rslocal.getString("first_name");
	              proLast = rslocal.getString("last_name");
	    //   proOHIP = rslocal.getString("provider_no");
  }
  
  %>
    <tr>
            <td width="34%"><a href=# onClick="javascript:rs('new','documentGetFile.jsp?document=<%=dispFilename%>&type=<%=dispStatus%>&doc_no=<%=dispDocNo%>', 480,480,1)"><%=dispDesc%></td>
            <td width="15%"><%=dispType%></td>
            <td width="17%"><%=proLast%>, <%=proFirst%></td>
            <td width="21%"><%=dispUpdatedate%></td>
            <td width="13%"><a href=# onClick="checkDelete('documentDelete.jsp?document_no=<%=dispDocNo%>&function=<%=function%>&functionid=<%=functionid%>','<%=dispCreator%>','<%=curUser_no%>', '<%=userlastname%>')">Delete</td>
    </tr> 
  
  <%
  
}
if (count == 0) {
  %>
    <tr><td colspan='5'>no match found</td></td>
    <%
    }
    %> 
</table>
</td>
</tr></table>
<% } %>

<br>
<form>
  <input type="button" name="Button" value="Done - Close Window" onclick=self.close();>
</form>
</body>
</html>
