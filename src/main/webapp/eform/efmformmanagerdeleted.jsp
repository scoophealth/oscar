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
  
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>
<%@ page import="oscar.eform.data.*, oscar.eform.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
String orderByRequest = request.getParameter("orderby");
String orderBy = "";
if (orderByRequest == null) orderBy = EFormUtil.DATE;
else if (orderByRequest.equals("form_subject")) orderBy = EFormUtil.SUBJECT;
else if (orderByRequest.equals("form_name")) orderBy = EFormUtil.NAME;
else if (orderByRequest.equals("file_name")) orderBy = EFormUtil.FILE_NAME;
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="eform.uploadhtml.title" /></title>
<link rel="stylesheet" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../share/css/eforms.css">
</head>
<script language="javascript">
  function checkFormAndDisable(){
    if(document.forms[0].formHtml.value==""){ 
      alert("<bean:message key="eform.uploadhtml.msgFileMissing"/>");
    } else {
      document.forms[0].subm.value = "<bean:message key="eform.uploadimages.processing"/>";
      document.forms[0].subm.disabled = true;
      document.forms[0].submit();
    } 
  }
  function BackHtml(){
    top.location.href = "../admin/admin.jsp";
  }
  function newWindow(url, id) {
        Popup = window.open(url,id,'toolbar=no,location=no,status=yes,menubar=no, scrollbars=yes,resizable=yes,width=700,height=600,left=200,top=0');
  }
  function confirmNRestore(url) {
    if (confirm("<bean:message key="eform.calldeletedformdata.confirmRestore"/>")) {
        document.location = url;
    }
  }
</script>
<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="98%">
	<tr bgcolor="#CCCCFF">
		<th><font face="Helvetica"><bean:message
			key="eform.uploadhtml.msgUploadEForm" /></font></th>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="5" width="98%">
	<tr>
		<td>
		<center>
		<table cellspacing="2" cellpadding="2" width="90%" border="0"
			style="margin-top: 10px" BGCOLOR="#EEEEFF">
			<html:form action="/eform/uploadHtml" method="POST"
				onsubmit="return checkFormAndDisable()"
				enctype="multipart/form-data">
				<font color="red" size="1"> <html:errors /> </font>
				<tr>
					<td align='right' nowrap><b><bean:message
						key="eform.uploadhtml.formName" /> </b></td>
					<td><input type="text" name="formName" size="50"></td>
				</tr>
				<tr>
					<td align='right' nowrap><b><bean:message
						key="eform.uploadhtml.formSubject" /> </b></td>
					<td><input type="text" name="subject" size="50"></td>
				</tr>
				<tr>
					<td align='right' nowrap><b><bean:message
						key="eform.uploadhtml.formFileName" /> </b></td>
					<td><input type="file" name="formHtml" size="80"></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" name="subm"
						value="<bean:message key="eform.uploadhtml.btnUpload"/>"></td>
				</tr>
			</html:form>
		</table>
		</center>
		</td>
		<td style="border-left: 2px solid grey">
		<table border="0" cellspacing="2" cellpadding="2"
			style="margin-left: 10px" width="100%">
			<tr>
				<td align='left'><a href=# onclick="javascript:BackHtml()"><bean:message
					key="eform.uploadhtml.btnBack" /></a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmformmanager.jsp"><bean:message
					key="admin.admin.btnUploadForm" /> </a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmformmanagerdeleted.jsp"
					class="current"><bean:message key="eform.uploadhtml.btnDeleted" />
				</a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmimagemanager.jsp"><bean:message
					key="admin.admin.btnUploadImage" /> </a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmmanageformgroups.jsp"><bean:message
					key="eform.groups.name" /> </a></td>
			</tr>
		</table>
		</td>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="98%">
	<tr>
		<td><bean:message key="eform.calldeletedformdata.title" /></td>
	</tr>
</table>

<table class="elements" width="98%">
	<tr bgcolor="#CCCCFF">
		<th><a href="efmformmanagerdeleted.jsp?orderby=form_name"><bean:message
			key="eform.uploadhtml.btnFormName" /></a></th>
		<th><a href="efmformmanagerdeleted.jsp?orderby=form_subject"><bean:message
			key="eform.uploadhtml.btnSubject" /></a></th>
		<th><a href="efmformmanagerdeleted.jsp?orderby=file_name"><bean:message
			key="eform.uploadhtml.btnFile" /></a></th>
		<th><a href="efmformmanagerdeleted.jsp?"><bean:message
			key="eform.uploadhtml.btnDate" /></a></th>
		<th><bean:message key="eform.uploadhtml.btnTime" /></th>
		<th><bean:message key="eform.uploadhtml.msgAction" /></th>
	</tr>
	<%
	ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listEForms(orderBy, EFormUtil.DELETED);
  for (int i=0; i<eForms.size(); i++) {
	  HashMap<String, ? extends Object> curForm =  eForms.get(i);
%>
	<tr style="background-color: <%= ((i%2) == 1)?"#F2F2F2":"white"%>;">
		<td width="25%" style="padding-left: 4px;"><a href="#"
			onclick="newWindow('efmshowform_data.jsp?fid=<%=curForm.get("fid")%>', '<%="FormD"+i%>'); return false;"><%=curForm.get("formName")%></a></td>
		<td width="30%" style="padding-left: 4px"><%=curForm.get("formSubject")%>&nbsp;</td>
		<td width="25%" style="padding-left: 4px"><%=curForm.get("formFileName")%></td>
		<td nowrap align='center' width="10%"><%=curForm.get("formDate")%></td>
		<td nowrap align='center' width="10%"><%=curForm.get("formTime")%></td>
		<td nowrap align='center'><a href="#"
			onclick="javascript: confirmNRestore('../eform/restoreEForm.do?fid=<%=curForm.get("fid")%>');"><bean:message
			key="eform.calldeletedformdata.btnRestore" /></a></td>
	</tr>
	<% } %>
</table>
</center>

</body>
</html:html>
