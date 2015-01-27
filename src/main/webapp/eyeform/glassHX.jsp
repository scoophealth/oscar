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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page	import="org.springframework.web.context.WebApplicationContext,
		org.springframework.web.context.support.WebApplicationContextUtils,
		org.oscarehr.casemgmt.model.CaseManagementNote,
		org.oscarehr.casemgmt.model.CaseManagementNoteLink,
		org.oscarehr.casemgmt.service.CaseManagementManager,
		org.oscarehr.common.dao.SecRoleDao,
		org.oscarehr.common.model.SecRole,
		org.oscarehr.util.SpringUtils,
		oscar.oscarEncounter.data.EctProgram,
		java.util.Date, java.util.List"%>
<%@page import="oscar.log.LogAction, oscar.log.LogConst"%>
<%@page import="oscar.dms.EDocUtil"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
  <%@ page import="org.oscarehr.util.*"%>
<%@ page import="org.oscarehr.eyeform.dao.*"%>
<%@page import="org.oscarehr.eyeform.model.EyeformSpecsHistory"%>

<%
    EyeformSpecsHistoryDao dao = (EyeformSpecsHistoryDao) SpringUtils.getBean("eyeformSpecsHistoryDao");
   String demographicNo = request.getParameter("demographic_no");
   String appointmentNo = request.getParameter("appointment_no");
   String type1 = request.getParameter("type");
   String note = "";
   String specs = dao.getByNote(Integer.parseInt(demographicNo), Integer.parseInt(appointmentNo), type1);
   if(specs == null){
	specs = "";
   }
    Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;
%>


<html:html locale="true">
<head>
    <title>Edit note for glass history</title>
    <% if (isMobileOptimized) { %>
        <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardMobileLayout.css" />
    <% } else { %>
    <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
        <style type="text/css">
            body { font-size: x-small; }
            textarea { width: 100%; margin: 5px 0; }
            div.label { float: left; }
        </style>
    <% } %>
   
</head>

<body bgcolor="#EEEEFF" onload="document.forms[0].note.focus();">
   <form action="<%=request.getContextPath()%>/eyeform/hxHistory.do" method="post" name="specsHistoryForm">
       <div class="panel">
            Notes
            <textarea name="specs.note" rows="10" maxlength="254"><%=specs%></textarea>
            <input type="submit" value="Save" onclick="this.form.method.value='save'; return validate(this);"/> &nbsp;
            <input type="button" class="leftButton top" value="Cancel" onclick="window.close();"/>          
        </div>
		<input type="hidden" value="save" name="method">
       <input type="hidden" value="<%=request.getParameter("demographic_no")%>" name="specs.demographicNo">
       <input type="hidden" value="<%=request.getParameter("appointment_no")%>" name="specs.appointmentNo">
       <input type="hidden" value="<%=request.getParameter("specs.id")%>" name="specs.id">
	   
	   <input type="hidden" value="<%=request.getParameter("type")%>" name="type">
	   <input type="hidden" value="<%=request.getParameter("dateStr")%>" name="dateStr">
	   
	   <input type="hidden" value="<%=request.getParameter("odSph")%>" name="odSph">
	   <input type="hidden" value="<%=request.getParameter("odCyl")%>" name="odCyl">
	   <input type="hidden" value="<%=request.getParameter("odAxis")%>" name="odAxis">
	   <input type="hidden" value="<%=request.getParameter("odAdd")%>" name="odAdd">
	   <input type="hidden" value="<%=request.getParameter("odPrism")%>" name="odPrism">
	   
	   <input type="hidden" value="<%=request.getParameter("osSph")%>" name="osSph">
	   <input type="hidden" value="<%=request.getParameter("osCyl")%>" name="osCyl">
	   <input type="hidden" value="<%=request.getParameter("osAxis")%>" name="osAxis">
	   <input type="hidden" value="<%=request.getParameter("osAdd")%>" name="osAdd">
	   <input type="hidden" value="<%=request.getParameter("osPrism")%>" name="osPrism">
    </form>
</body>
 
</html:html>


<%!
    CaseManagementNote createCMNote(CaseManagementNote historyNote,String note, String demo_no, String provider, String program_no) {
	if (!filled(note)) return null;

	CaseManagementNote cmNote = new CaseManagementNote();
	    cmNote.setUpdate_date(new Date());
	    cmNote.setObservation_date(new Date());
	    cmNote.setDemographic_no(demo_no);
	    cmNote.setProviderNo(provider);
	    cmNote.setSigning_provider_no(provider);
	    cmNote.setSigned(true);
	    cmNote.setProgram_no(program_no);
	    
	    SecRoleDao secRoleDao = (SecRoleDao) SpringUtils.getBean("secRoleDao");
		SecRole doctorRole = secRoleDao.findByName("doctor");		
		cmNote.setReporter_caisi_role(doctorRole.getId().toString());
	    	    
	    cmNote.setReporter_program_team("0");
	    cmNote.setNote(note);
            String historyStr;

            if(historyNote==null || historyNote.getHistory()==null || historyNote.getHistory().trim().length()==0 ){
                historyStr=note;
            }
            else{
                historyStr=note + "\n" + "   ----------------History Record----------------   \n" + historyNote.getHistory().trim() + "\n";
            }
            cmNote.setHistory(historyStr);
	return cmNote;
    }

    boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
%>
