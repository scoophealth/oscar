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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html:html locale="true">
<head>
<title><bean:message key="dms.zadddocument.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#486ebd"><th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message key="dms.zadddocument.msgUpload"/></font></th></tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7">
	<FORM NAME="aForm" ENCTYPE="multipart/form-data" ACTION="../servlet/oscar.DocumentMgtUploadServlet" METHOD="POST">
		<tr valign="top"><td rowspan="2" ALIGN="right" valign="middle"> <font face="Verdana" color="#0000FF"><b><i><bean:message key="dms.zadddocument.msgAddDocument"/>
        </i></b></font></td>


      <td valign="middle" rowspan="2" ALIGN="left">		<INPUT TYPE="file" SIZE="30" NAME="file1"><br>


				<INPUT TYPE="SUBMIT" VALUE="<bean:message key="dms.zadddocument.btnUpload"/>"></td>
		</tr>
	</form>
</table>
<br>
<br>
<form>
  <input type="button" name="Button" value="<bean:message key="global.btnCancel"/>" onclick=self.close();>
</form>
</body>
</html:html>