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
    String attrib_name = request.getParameter("atbname");
    if (attrib_name==null) attrib_name="";
    
    String demo = request.getParameter("demo");
    String display = request.getParameter("display");
    boolean saved = Boolean.valueOf(request.getParameter("saved"));
    
    String tid = request.getParameter("table_id");
    Long tableId = 0L;
    if (filled(tid)) tableId = Long.valueOf(tid);
    else tid = "";
    
    HttpSession se = request.getSession();
    String user_no = (String) se.getAttribute("user");
    
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
    
    Integer tableName = cmm.getTableNameByDisplay(display);
    String note = "";
    
    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(tableName, tableId);
    CaseManagementNote p_cmn = null;
    if (cml!=null) p_cmn = cmm.getNote(cml.getNoteId().toString());
    String uuid = "";
    if (p_cmn!=null) uuid = p_cmn.getUuid();
    
    //get note from attribute
    CaseManagementNote a_cmn = (CaseManagementNote)se.getAttribute(attrib_name);
    if (a_cmn!=null) {
	note = a_cmn.getNote();
    } else { 
	//get note from database
	if (p_cmn!=null) { //previous annotation exists
	    note = p_cmn.getNote();
	}
    }
    if (saved) {
	String prog_no = new EctProgram(se).getProgram(user_no);
	CaseManagementNote cmn = createCMNote(request.getParameter("note"), demo, user_no, prog_no);
	if (cmn!=null) {
	    if (p_cmn!=null) {  //previous annotation exists
		cmn.setUuid(uuid); //assign same UUID to new annotation
	    }
	    if (tableName.equals(cml.CASEMGMTNOTE) || tableId.equals(0L)) {
		if (!attrib_name.equals("")) se.setAttribute(attrib_name, cmn);
	    } else { //annotated subject exists
		cmm.saveNoteSimple(cmn);
		cml = new CaseManagementNoteLink();
		cml.setTableName(tableName);
		cml.setTableId(tableId);
		cml.setNoteId(cmn.getId());
		cmm.saveNoteLink(cml);
	    }
	}
	response.sendRedirect("../close.html");
    }
    
    //Display revision history
    Integer rev = 0;
    if (p_cmn!=null) {
	List l = cmm.getNotesByUUID(uuid);
	rev = l.size();
    }
%>


<html:html locale="true">
<head>
    <title>Annotation</title>
    
    <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
    <script type="text/javascript">
	function showHistory(uuid,display) {
	    if (uuid=="") {
		alert("Annotation History Not Found!");
		return false;
	    } else {
		var href = "showHistory.jsp?uuid="+uuid+"&display="+display;
		window.open(href,"histwin","width=600,height=600");
	    }
	    return true;
	}
    </script>
</head>

<body bgcolor="#EEEEFF" onload="document.forms[0].note.focus();">
    <form action="annotation.jsp" method="post">
	<input type="hidden" name="atbname" value="<%=attrib_name%>" />
	<input type="hidden" name="demo" value="<%=demo%>" />
	<input type="hidden" name="display" value="<%=display%>" />
	<input type="hidden" name="table_id" value="<%=tid%>" />
	<input type="hidden" name="saved" />
	<table>
	    <tr><td colspan="2"><%=display%> Annotation:</td></tr>
	    <tr><td colspan="2">
		    <textarea name="note" rows="10" cols="50"><%=note%></textarea>
		</td>
	    </tr>
	    <tr>
		<td>
		    <input type="submit" value="Save" onclick="this.form.saved.value='true';"/> &nbsp;
		    <input type="button" value="Cancel" onclick="window.close();"/>
		</td>
		<td align="right">
	<% if (rev>0) { %>
		    rev<a href="#" onclick="showHistory('<%=uuid%>','<%=display%>');"><%=rev%></a>
	<% } %>
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
