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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.notes");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>

<%@ page import="java.util.Set, java.util.List, java.util.Iterator"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNote"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementIssue"%>
<%@ page import="org.oscarehr.common.model.Provider"%>
<%@ page import="oscar.util.DateUtils"%>

<% CaseManagementNote note = (CaseManagementNote)request.getAttribute("Note");
    pageContext.setAttribute("provName", note.getProviderName());    
    pageContext.setAttribute("fmtTxt", note.getNote().replaceAll("\n", "<br>"));        
    String dateFormat = "dd-MMM-yyyy H:mm";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<c:if test="${success}">
	<img title="Minimize Display" id='quitImg<c:out value="${Note.id}"/>'
		onclick='minView(event)' style='float: right; margin-right: 5px; margin-top: 2px;'
		src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif' />
	<img title="Print" id='print<c:out value="${Note.id}"/>'
		alt="Toggle Print Note"
		onclick="togglePrint(<c:out value="${Note.id}"/>, event)"
		style='float: right; margin-right: 5px; margin-top: 2px;'
		src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png' />
        <a title="Edit" id="edit<c:out value="${Note.id}"/>" href="#" onclick="editNote(event); return false;" style='float: right; margin-right: 5px; font-size:8px;'>Edit</a>
	<span id="txt<c:out value="${Note.id}"/>"><c:out
		escapeXml="false" value="${fmtTxt}" /></span>
	<div id="sig<c:out value="${Note.id}"/>">
	<div class="sig" id="sumary<c:out value="${Note.id}"/>">
	<div id="observation<c:out value="${Note.id}"/>"
		style="float: right; margin-right: 3px;"><i>Encounter Date:&nbsp;<span
		id="obs<c:out value="${Note.id}"/>"><%=DateUtils.getDate(note.getObservation_date(),dateFormat)%></span>&nbsp;rev<a
		href="#"
		onclick="return showHistory('<c:out value="${Note.id}"/>', event);"><%=note.getRevision()%></a></i></div>
	<div><span style="float: left;">Editors:</span>
	<ul style="list-style: none inside none; margin: 0px;">
		<%  
                          List editors = note.getEditors();
                          Iterator<Provider> it = editors.iterator(); 
                          int count = 0;
                          int MAXLINE = 2;
                          while( it.hasNext() ) {
                              Provider p = it.next();

                              if( count % MAXLINE == 0 ) {
                                  out.print("<li>" + p.getFormattedName() + "; ");
                              }
                              else {
                                  out.print(p.getFormattedName() + "</li>");
                              }
                              ++count;
                          }
                          if( count % MAXLINE == 0 )
                              out.print("</li>");
                          %>

	</ul>
	</div>
        <div style="clear: right; margin-right: 3px; float: right;">Enc
		Type:&nbsp;<span id="encType<%=note.getId()%>"><%=note.getEncounter_type().equals("")?"":"&quot;"+note.getEncounter_type()+"&quot;"%></span>
	</div>

	<%                        
                Set issSet = note.getIssues();
                if( issSet.size() > 0 ) {
                %> Assigned Issues
	<ul style="list-style: circle inside none; margin: 0px;">
		<% 
                    Iterator i = issSet.iterator();
                    while( i.hasNext() ) {
                    CaseManagementIssue iss = (CaseManagementIssue)i.next();
                    %>
		<li><%=iss.getIssue().getDescription()%></li>
		<%
                    }
                    %>
	</ul>
	<%
                    }
                    else {
                    %>
                    <div>&nbsp;</div>
                    <%
                    }
                    %>
	</div>
	</div>
	
</c:if>
<c:if test="${ not success}">
	<img title="Minimize Display" id='quitImg<c:out value="${Note.id}"/>'
		alt="Minimize Display" onclick='resetView(true, true, event)'
		style='float: right; margin-right: 5px;'
		src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif' />
	<span id="txt<c:out value="${Note.id}"/>"><bean:message
		key="oscarEncounter.Index.msgLocked" /> <%=DateUtils.getDate(note.getUpdate_date(),dateFormat)%>
	<c:out value="${provName}" /></span>
	<p id="passwdError" style="color: red;">Incorrect password</p>
	<p id='passwdPara' class="passwd">Password:&nbsp;<input
		onkeypress="return grabEnter('btnUnlock', event);" type='password'
		id='passwd' size='16'>&nbsp; <input id='btnUnlock'
		type='button'
		onclick="return unlock_ajax('<c:out value="n${Note.id}"/>');"
		value='<bean:message key="oscarEncounter.Index.btnUnLock"/>'>
	</p>
	<script type="text/javascript">      
            $('passwd').focus();
        </script>
</c:if>
