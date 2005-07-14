<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%
  //if (session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
  //    response.sendRedirect("../logout.jsp");
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>
<%@ page import = "java.io.*, oscar.eform.*"  errorPage="../errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
<html:html locale="true">
<head>
<link rel="stylesheet" href="../web.css">

</head>
<script language="javascript">
<!--
  function checkHtml(){
    if (document.myForm.FileName.value==""){
      alert("<bean:message key="eform.uploadimages.msgChooseFile"/>");
    } else {
      document.myForm.submit();
    }
  }
  function BackHtml(){
    top.location.href = "../admin/admin.jsp";
  }
  function newWindow(file,window) {
    msgWindow=open(escape(file),window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
    if (msgWindow.opener == null) msgWindow.opener = self;
  }
//-->
</script>

<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica"><bean:message key="eform.uploadimages.msgTitle"/></font></th></tr>
</table>
<table border="0" cellspacing="2" cellpadding="2" width="100%" >
  <tr><td align='right'><a href=# onclick="javascript:BackHtml()"><bean:message key="eform.uploadimages.btnBack"/></a></td></tr>
</table>

<table cellspacing="2" cellpadding="2" width="80%" border="0" BGCOLOR="<%=weakColor%>">
<FORM NAME="myForm" ENCTYPE="multipart/form-data" ACTION="uploadimagesproc.jsp" METHOD="post" onSubmit="return checkHtml()">
  <tr><td align='right'><b><bean:message key="eform.uploadimages.msgFileName"/> </b></td><td><input type="file" name="FileName" size="80"></td></tr>
  <tr><td></td><td><input type="button" value="<bean:message key="eform.uploadimages.btnUplaod"/>" onclick="javascript:checkHtml()"></td></tr>
</form>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="80%">
  <tr><td><bean:message key="eform.uploadimages.msgImageLibrary"/> </td>
  <td align='right'></td></tr>
</table>

<table border="0" cellspacing="2" cellpadding="2" width="80%">
  <tr bgcolor=<%=deepColor%> >
    <th><bean:message key="eform.uploadimages.msgimgName"/></th>
    <th><bean:message key="eform.uploadimages.msgImgAction"/></th>
  </tr>
<%
  boolean bOdd = false;
  // String imgDir = "../../OscarDocument/" + oscarVariables.getProperty("project_home") + "/eform/images/";
  String imgDir = oscarVariables.getProperty("eform_image");

//  EfmImagePath.setEfmImagePath(oscarVariables.getProperty("eform_image"));
  File dir=new File(imgDir);

  String temp[]=dir.list();
  //System.out.println(imgDir);
  if(temp !=null) {
    for(int i=0;i<temp.length;i++){
      bOdd = bOdd?false:true ;
	  String fileURL="/OscarDocument/" + oscarVariables.getProperty("project_home") + "/eform/images/"+temp[i];
//    File ft=new File(temp[i]);
	//File ppp=new File(imgDir1);
%>
  <tr bgcolor="<%=bOdd?weakColor:"white"%>">
    <td><a href="JavaScript:newWindow('<%=fileURL%>','_blank')"><%=temp[i]%></a></td>
    <td width=100 align='center'><input type="button" value="<bean:message key="eform.uploadimages.btnDelete"/>" onclick="DoEmpty('<%=temp[i]%>')"></td>
  </tr>
<%
    }
  }
%>

</table>
</center>

</body>

<script language="javascript">
<!--
  function DoEmpty(str){
    if (confirm("<bean:message key="eform.uploadimages.msgDelAll"/>"))
    window.location = "deleteimage.jsp?filename="+str;
  }
//-->
</script>

</html:html>