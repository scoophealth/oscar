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
<%@page	import="org.springframework.web.context.WebApplicationContext,
		org.springframework.web.context.support.WebApplicationContextUtils,
		org.oscarehr.casemgmt.model.CaseManagementNote,
		org.oscarehr.casemgmt.model.CaseManagementNoteLink,
		org.oscarehr.casemgmt.service.CaseManagementManager,
		oscar.oscarEncounter.data.EctProgram,
		java.util.Date, java.util.List"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>

<%
    String demo = request.getParameter("demo");
    String display = request.getParameter("display");
    boolean saved = Boolean.valueOf(request.getParameter("saved"));
    
    String tid = request.getParameter("table_id");
    Long tableId = filled(tid) ? Long.valueOf(tid) : 0L;
    
    HttpSession se = request.getSession();
    String user_no = (String) se.getAttribute("user");
    
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
    
    Integer tableName = cmm.getTableNameByDisplay(display);
    String note = "";
    
    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(tableName, tableId);
    String last_display = (String)se.getAttribute("anno_display");
    Long last_id = (Long)se.getAttribute("anno_last_id");
    if (display.equals(last_display) && tableId.equals(last_id)) {
	//get note from attribute
	CaseManagementNote cmn = (CaseManagementNote)se.getAttribute(demo+"annoNote"+tableName);
	if (cmn!=null) note = cmn.getNote();
    } else { 
	//attribute outdated, get note from database
	se.removeAttribute("anno_display");
	se.removeAttribute("anno_last_id");
	if (cml!=null) { //annotation exists
	    String nid = cml.getNoteId().toString();
	    note = cmm.getNote(nid).getNote();
	}
    }
    
    if (saved) {
	String prog_no = new EctProgram(se).getProgram(user_no);
	CaseManagementNote cmn = createCMNote(request.getParameter("note"), demo, user_no, prog_no);
	if (tableName.equals(cml.CASEMGMTNOTE) || tableId.equals(0L)) {
	    se.setAttribute(demo+"annoNote"+tableName, cmn);
	    se.setAttribute("anno_display", display);
	    se.setAttribute("anno_last_id", tableId);
	} else { //annotated subject exists
	    cmm.saveNoteSimple(cmn);
	    if (cml!=null) { //previous annotation exists
		cml.setNoteId(cmn.getId());
		cmm.updateNoteLink(cml);
	    } else { //new annotation
		cml = new CaseManagementNoteLink();
		cml.setTableName(tableName);
		cml.setTableId(tableId);
		cml.setNoteId(cmn.getId());
		cmm.saveNoteLink(cml);
	    }
	}
	response.sendRedirect("../close.html");
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
	<input type="hidden" name="demo" value="<%=demo%>" />
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
    CaseManagementNote createCMNote(String note, String demo_no, String provider, String program_no) {
	if (!filled(note)) return null;
	
	CaseManagementNote cmNote = new CaseManagementNote();
	    cmNote.setUpdate_date(new Date());
	    cmNote.setObservation_date(new Date());
	    cmNote.setDemographic_no(demo_no);
	    cmNote.setProviderNo(provider);
	    cmNote.setSigning_provider_no(provider);
	    cmNote.setSigned(true);
	    cmNote.setHistory("");
	    cmNote.setProgram_no(program_no);
	    cmNote.setReporter_caisi_role("1");  //caisi_role for "doctor"
	    cmNote.setReporter_program_team("0");
	    cmNote.setNote(note);
	
	return cmNote;
    }
    
    boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
%>