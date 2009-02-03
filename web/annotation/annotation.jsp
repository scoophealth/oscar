<%@page contentType="text/html"%>
<%@page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
 
-->
<%@page	import="org.springframework.web.context.WebApplicationContext"%>
<%@page	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page	import="org.oscarehr.casemgmt.model.CaseManagementNote"%>
<%@page	import="org.oscarehr.casemgmt.model.CaseManagementNoteLink"%>
<%@page	import="org.oscarehr.casemgmt.service.CaseManagementManager"%>
<%@page import="java.util.Date, java.util.List"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>

<%
    String display = request.getParameter("display");
    String tid = request.getParameter("table_id");
    Long tableId = filled(tid) ? Long.valueOf(tid) : null;
    
    String note = request.getParameter("note");
    if (note==null) note = "";
    boolean saved = Boolean.valueOf(request.getParameter("saved"));
    
    HttpSession se = request.getSession();
    String user_no = (String) se.getAttribute("user");
    
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
    
    Integer tableName = cmm.getTableNameByDisplay(display);
    List <CaseManagementNoteLink> cmll = cmm.getLinkByTableId(tableName, tableId);
    
    for (CaseManagementNoteLink cml : cmll) {
	String nid = cml.getNoteId().toString();
	note = cmm.getNote(nid).getNote();
    }
    
    if (saved) {
	Long nwNoteId = saveCMNote(note, user_no, cmm);
	if (cmll.size()>0) {
	    CaseManagementNoteLink cml = cmll.get(0);
	    if (nwNoteId<0) cml.setTableName(-1);
	    else cml.setNoteId(nwNoteId);
	    cmm.updateNoteLink(cml);
	} else {
	    if (nwNoteId>0) {
		CaseManagementNoteLink cml = new CaseManagementNoteLink();
		cml.setNoteId(nwNoteId);
		cml.setTableName(tableName);
		cml.setTableId(tableId);
		if (tableId!=null) cmm.saveNoteLink(cml);
		else se.setAttribute("annotationlink", cml);
	    }
	}
	response.sendRedirect("close.jsp");
    }
%>


<html:html locale="true">
<head>
    <title>Annotation</title>
    
    <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
    <script type="text/javascript">
	
    </script>
</head>

<body bgcolor="#EEEEFF" onload="document.forms[0].note.focus();">
    <form action="annotation.jsp" method="post">
	<input type="hidden" name="display" value="<%=display%>" />
	<input type="hidden" name="table_id" value="<%=tid%>" />
	<input type="hidden" name="saved" />
	<table>
	    <tr><td><%=display%> Annotation:</td></tr>
	    <tr>
		<td><textarea name="note" rows="10" cols="50"><%=note%></textarea></td>
	    </tr>
	    <tr>
		<td align="center">
		    <input type="submit" value="Save" onclick="this.form.saved.value='true';"/> &nbsp;
		    <input type="button" value="Cancel" onclick="window.close();"/>
		</td>
	    </tr>
	</table>
    </form>
</body>

</html:html>


<%!
    Long saveCMNote(String note, String provider, CaseManagementManager cmm) {
	if (!filled(note)) return -1L;
	
	CaseManagementNote cmNote = new CaseManagementNote();
	    cmNote.setUpdate_date(new Date());
	    cmNote.setObservation_date(new Date());
	    cmNote.setProviderNo(provider);
	    cmNote.setSigning_provider_no(provider);
	    cmNote.setSigned(true);
	    cmNote.setHistory("");
	    cmNote.setProgram_no("10015");  //dummy program no for program "OSCAR"
	    cmNote.setReporter_caisi_role("1");  //caisi_role for "doctor"
	    cmNote.setReporter_program_team("0");
	    cmNote.setNote(note);
	cmm.saveNoteSimple(cmNote);
	
	return cmNote.getId();
    }
    
    boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
%>