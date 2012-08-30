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

<%@ page import="org.oscarehr.common.service.FieldNoteManager"%>
<%@ page import="org.oscarehr.common.model.EForm, org.oscarehr.common.dao.EFormDao" %>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="java.util.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
	String[] selectedEforms = request.getParameterValues("selected_eform");
	String unselectEform = request.getParameter("unselect_eform");
	String customName = request.getParameter("custom_name");
	
	FieldNoteManager.selectFieldNoteEforms(selectedEforms);
	FieldNoteManager.unSelectFieldNoteEform(unselectEform);
	
	TreeSet<Integer> fieldNoteEforms = FieldNoteManager.getFieldNoteEforms();
	TreeSet<Integer> fieldNoteNameEforms = FieldNoteManager.getFieldNoteNameEforms(customName);
	
	EFormDao eformDao = (EFormDao)SpringUtils.getBean("EFormDao");
%>
<html:html locale="true">
<head>
<title><bean:message key="admin.fieldNote.selectEforms" /></title>
<link rel="stylesheet" href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../../share/css/eformStyle.css">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script language="javascript">
	function remove_select(fid)
	{
		document.selectFieldNoteForm.unselect_eform.value = fid;
		document.selectFieldNoteForm.submit();
	}
</script>
</head>
<body>

<div class="eformInputHeading" align="center">
	<bean:message key="admin.fieldNote.selectEforms" />
</div>
<input type="button" value="<bean:message key="admin.fieldNote.back" />" onclick="window.location.href='fieldnotereport.jsp'" />

<form name="selectFieldNoteForm" action="fieldnoteselect.jsp">

<input type="hidden" name="unselect_eform" />
<table class="elements" width="100%">
    <tr bgcolor="#CCCCFF">
        <th><bean:message key="eform.uploadhtml.btnFormName" /></a></th>
        <th><bean:message key="eform.uploadhtml.btnSubject" /></a></th>
        <th><bean:message key="eform.uploadhtml.btnFile" /></a></th>
        <th><bean:message key="eform.uploadhtml.btnDate" /></a></th>
        <th><bean:message key="eform.uploadhtml.btnTime" /></th>
        <th><bean:message key="eform.uploadhtml.btnRoleType"/></th>
        <th><bean:message key="eform.uploadhtml.msgAction" /></th>
    </tr>
    <tr class="eformInputHeadingActive" style="color: #707070;">
    	<td colspan="7">
<%
	if (fieldNoteEforms.isEmpty()) {
%>
			<bean:message key="admin.fieldNote.noFieldNote" />
<%
	} else {
%>
    		<bean:message key="admin.fieldNote.eformSelected" />
<%
	}
%>
    	</td>
    </tr>
<%
	for (Integer fid : fieldNoteEforms)
	{
		EForm fieldNoteEform = eformDao.find(fid); 
%>
    <tr>
        <td width="25%" style="padding-left: 4px;">
        	<a href="../efmshowform_data.jsp?fid=<%=fieldNoteEform.getId()%>" target="_blank"><%=fieldNoteEform.getFormName()%></a>
       	</td>
        <td width="30%" style="padding-left: 4px"><%=fieldNoteEform.getSubject()%></td>
        <td width="25%" style="padding-left: 4px"><%=fieldNoteEform.getFileName()%></td>
        <td nowrap align='center' width="10%"><%=fieldNoteEform.getFormDate()%></td>
        <td nowrap align='center' width="10%"><%=fieldNoteEform.getFormTime()%></td>
        <td nowrap align='center' width="10%"><%=fieldNoteEform.getRoleType()%></td>
        <td nowrap align='center'>
        	<a href="#" title="<bean:message key="admin.fieldNote.unselectEform" />" onclick="remove_select(<%=fieldNoteEform.getId()%>);"><bean:message key="admin.fieldNote.unselect" /></a>
       	</td>
    </tr>
<%
	}
%>
	<tr>
		<td colspan="7">
			&nbsp;<br/>&nbsp;
		</td>
	</tr>
<%
	for (Integer fid : fieldNoteNameEforms)
	{
		EForm fieldNoteEform = eformDao.find(fid); 
%>
    <tr style="background-color: #F2F2F2;">
        <td width="25%" style="padding-left: 4px;">
        	<a href="../efmshowform_data.jsp?fid=<%=fieldNoteEform.getId()%>" target="_blank"><%=fieldNoteEform.getFormName()%></a>
       	</td>
        <td width="30%" style="padding-left: 4px"><%=fieldNoteEform.getSubject()%></td>
        <td width="25%" style="padding-left: 4px"><%=fieldNoteEform.getFileName()%></td>
        <td nowrap align='center' width="10%"><%=fieldNoteEform.getFormDate()%></td>
        <td nowrap align='center' width="10%"><%=fieldNoteEform.getFormTime()%></td>
        <td nowrap align='center' width="10%"><%=fieldNoteEform.getRoleType()%></td>
        <td nowrap align='center'>
        	<input type="checkbox" value="<%=fieldNoteEform.getId()%>" title="<bean:message key="admin.fieldNote.addEform" />" name="selected_eform" />
       	</td>
    </tr>
<%
	}
%>
	<tr>
		<td colspan="7" align="right">
			<bean:message key="admin.fieldNote.enterCustomName" />:
			<input type="text" name="custom_name" size="60" />
		</td>
	</tr>
	<tr>
		<td colspan="7" align="right">
			<input type="submit" value="<bean:message key="admin.fieldNote.submit" />" />
		</td>
	</tr>
</table>

</form>
</body>
</html:html>
