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

<jsp:useBean id="documentBean" class="oscar.DocumentBean" scope="request" /> 


<% 
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");

filename = documentBean.getFilename();
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html:html locale="true">
<head>
<title><bean:message key="dms.complete.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File2) {
      
      self.close();
      self.opener.document.aDoc.docfilename.value = File2;
}
-->
</script>
<link rel="stylesheet" href="../web.css" />
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onLoad="CodeAttach('<%=filename%>')" >
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#486ebd"><th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message key="dms.complete.msgTitle"/></font></th></tr>
</table>
<bean:message key="dms.complete.msgCompleted"/>
<br>
<br>
<form>
  <input type="button" name="Button" value="<bean:message key="dms.complete.btnDone"/>" onclick= "CodeAttach('<%=filename%>')"> 
</form>
<script LANGUAGE="JavaScript">
      self.close();
         self.opener.document.aDoc.docfilename.value="<%=filename%>";
</script>
</body>
</html:html> 