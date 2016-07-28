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

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplay"%>
<%  long start = System.currentTimeMillis(); %>
<%@include file="/casemgmt/taglibs.jsp"%>
<%@page
	import="java.util.List, java.util.Set, java.util.Iterator, org.oscarehr.casemgmt.model.CaseManagementIssue, org.oscarehr.casemgmt.model.CaseManagementNoteExt"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.provider.web.CppPreferencesUIBean"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.casemgmt.web.CaseManagementViewAction"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%@page import="org.oscarehr.common.model.PartialDate"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
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

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<nested:size id="num" name="Notes" />

<div style="width: 10%; float: right; text-align: center;">
<h3 style="padding:0px; background-color:#<c:out value="${param.hc}"/>">
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	com.quatro.service.security.SecurityManager securityManager = new com.quatro.service.security.SecurityManager();
	if(securityManager.hasWriteAccess("_" + request.getParameter("issue_code"),roleName$)) {
%>
<a href="#" title='Add Item' onclick="return showEdit(event,'<bean-el:message key="${param.title}" />','',0,'','','','<%=request.getAttribute("addUrl")%>0', '<c:out value="${param.cmd}"/>','<%=request.getAttribute("identUrl")%>','<%=request.getAttribute("cppIssue")%>','','<c:out value="${param.demographicNo}"/>');">+</a>
<% } else { %>
	&nbsp;
<% } %>
</h3>
</div>
<div style="clear: left; float: left; width: 90%;">
<h3 style="width:100%; background-color:#<c:out value="${param.hc}"/>"><a
	href="#"
        onclick="return showIssueHistory('<c:out value="${param.demographicNo}"/>','<%=request.getAttribute("issueIds")%>');"><bean-el:message key="${param.title}" /></a></h3>
</div>
        
        <c:choose>
            <c:when test='${param.title == "oscarEncounter.oMeds.title" || param.title == "oscarEncounter.riskFactors.title" || param.title == "oscarEncounter.famHistory.title"|| param.noheight == "true"}'>
                <div style="clear: both; overflow: auto;">
            </c:when>
            <c:otherwise>
                <div style="clear: both; height: inherit; overflow: auto;">
            </c:otherwise>
        </c:choose>
<ul style="margin-left: 5px;">
<% List<CaseManagementNoteExt> noteExts = (List<CaseManagementNoteExt>)request.getAttribute("NoteExts"); %>
	<nested:iterate indexId="noteIdx" id="note" name="Notes"
		type="org.oscarehr.casemgmt.model.CaseManagementNote">
                <input type="hidden" id="<c:out value="${param.cmd}"/><nested:write name="note" property="id"/>" value="<nested:write name="noteIdx"/>">
		<% if( noteIdx % 2 == 0 ) { %>
		<li class="cpp"	style="clear: both; whitespace: nowrap; background-color: #F3F3F3;">
		<%}else {%>
		
		<li class="cpp" style="clear: both; whitespace: nowrap;">
		<%}
                //load up the prefs once
                CppPreferencesUIBean prefsBean = new CppPreferencesUIBean(loggedInInfo.getLoggedInProviderNo());
                prefsBean.loadValues();
				String addlData = CaseManagementViewAction.getCppAdditionalData(note.getId(),(String)request.getAttribute("cppIssue"),noteExts,prefsBean);
				
				String strNoteExts = getNoteExts(note.getId(), noteExts);
                List<Provider> listEditors = note.getEditors();
                StringBuffer editors = new StringBuffer();
                for( Provider p: listEditors) {
                	editors.append(p.getFormattedName() + ";");                     
                }             
                
                String htmlNoteTxt = note.getNote() + addlData;
               
                //single line or 'normal' view.
                boolean singleLine = Boolean.valueOf(oscar.OscarProperties.getInstance().getProperty("echart.cpp.single_line","false"));
                UserPropertyDAO userPropertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
                UserProperty prop = userPropertyDao.getProp(loggedInInfo.getLoggedInProviderNo(),UserProperty.CPP_SINGLE_LINE);
                if(prop != null) {
                	singleLine = Boolean.valueOf((prop.getValue().equals("yes")?true:false));
                }
                if(singleLine) {
                	if(htmlNoteTxt.indexOf("\n")!=-1) {
                    	htmlNoteTxt = htmlNoteTxt.substring(0,htmlNoteTxt.indexOf("\n")) + "...";
                    }	
                } else {
                	htmlNoteTxt = htmlNoteTxt.replaceAll("\n", "<br>");
                }                          
                
                String noteTxt = note.getNote();
                noteTxt = noteTxt.replaceAll("\"","");
                noteTxt = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(noteTxt);         
                
                Set<CaseManagementIssue>setIssues = note.getIssues();
                StringBuffer strNoteIssues = new StringBuffer();
                CaseManagementIssue iss;
                Iterator<CaseManagementIssue>iter = setIssues.iterator();
                while(iter.hasNext()) {
                    iss = iter.next();
                    strNoteIssues.append(iss.getIssue_id()+";"+iss.getIssue().getCode()+";"+org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(iss.getIssue().getDescription()));
                    if( iter.hasNext() ) {
                        strNoteIssues.append(";");
                    }
                }
                %> <span id="spanListNote<nested:write name="note" property="id"/>">
			
			<c:choose>
            <c:when test='${param.title == "oscarEncounter.oMeds.title" || param.title == "oscarEncounter.riskFactors.title" || param.title == "oscarEncounter.famHistory.title"|| param.noheight == "true"}'>
                <a class="links" onmouseover="this.className='linkhover'"	onmouseout="this.className='links'" title="Rev:<nested:write name="note" property="revision"/> - Last update:<nested:write name="note" property="update_date" format="dd-MMM-yyyy"/>" id="listNote<nested:write name="note" property="id"/>" href="#" onclick="showEdit(event,'<bean-el:message key="${param.title}" />','<nested:write name="note" property="id"/>','<%=StringEscapeUtils.escapeJavaScript(editors.toString())%>','<nested:write name="note" property="observation_date" format="dd-MMM-yyyy"/>','<nested:write name="note" property="revision"/>','<%=noteTxt%>', '<%=request.getAttribute("addUrl")%><nested:write name="note" property="id"/>', '<c:out value="${param.cmd}"/>','<%=request.getAttribute("identUrl")%>','<%=strNoteIssues.toString()%>','<%=strNoteExts%>','<c:out value="${param.demographicNo}"/>');return false;"  style="width:100%;overflow:scroll;" >			 			
            </c:when>
            <c:otherwise>
                <a class="topLinks" onmouseover="this.className='topLinkhover'"	onmouseout="this.className='topLinks'" title="Rev:<nested:write name="note" property="revision"/> - Last update:<nested:write name="note" property="update_date" format="dd-MMM-yyyy"/>" id="listNote<nested:write name="note" property="id"/>" href="#" onclick="showEdit(event,'<bean-el:message key="${param.title}" />','<nested:write name="note" property="id"/>','<%=StringEscapeUtils.escapeJavaScript(editors.toString())%>','<nested:write name="note" property="observation_date" format="dd-MMM-yyyy"/>','<nested:write name="note" property="revision"/>','<%=noteTxt%>', '<%=request.getAttribute("addUrl")%><nested:write name="note" property="id"/>', '<c:out value="${param.cmd}"/>','<%=request.getAttribute("identUrl")%>','<%=strNoteIssues.toString()%>','<%=strNoteExts%>','<c:out value="${param.demographicNo}"/>');return false;"  style="width:100%;overflow:scroll;" >			 			
            </c:otherwise>
        	</c:choose>                 
			
			<%=htmlNoteTxt%></a>
		</span></li>
	</nested:iterate>
	<%
		List<NoteDisplay>remoteNotes = (List<NoteDisplay>)request.getAttribute("remoteNotes");
		String htmlText;
		int noteIdx = 0;
		if( remoteNotes != null ) {
		    for( NoteDisplay remoteNote : remoteNotes) {
				htmlText = remoteNote.getNote();
				htmlText = htmlText.replaceAll("\n", "<br>");
				if( noteIdx % 2 == 0 ) {
				%>				
				<li class="cpp" style="clear: both; whitespace: nowrap; background-color: #FFCCCC;">
				<%
				}
				else {
				    %>
				    <li class="cpp" style="clear: both; whitespace: nowrap; background-color: #CCA3A3">
				    <%
				}
				%>
					<a class="links" onmouseover="this.className='linkhover'"	onmouseout="this.className='links'" title="<%=remoteNote.getLocation()%> by <%=remoteNote.getProviderName()%> on <%=ConversionUtils.toTimestampString(remoteNote.getObservationDate())%>" href="javascript:void(0)" onclick="showIntegratedNote('<bean-el:message key="${param.title}" />',<%=htmlText%>,<%=remoteNote.getLocation()%>, <%=remoteNote.getProviderName()%>, <%=ConversionUtils.toTimestampString(remoteNote.getObservationDate())%>);">					
					<%=htmlText%>
					</a>
				</li>
				<%
		    }
		    
		}
	%>
</ul>
<br>
<input type="hidden" id="<c:out value="${param.cmd}"/>num"
	value="<nested:write name="num"/>">
<input type="hidden" id="<c:out value="${param.cmd}"/>threshold"
	value="0">
<%!
    String getNoteExts(Long noteId, List<CaseManagementNoteExt> lcme) {
	StringBuffer strcme = new StringBuffer();
	for (CaseManagementNoteExt cme : lcme) {
	    if (cme.getNoteId().equals(noteId)) {
		String key = cme.getKeyVal();
		String val = null;
		if (key.contains(" Date")) {
		    val = readPartialDate(cme);
		} else {
		    val = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(cme.getValue());
		}
		if (strcme.length()>0) strcme.append(";");
		strcme.append(key + ";" + val);
	    }
	}
	return strcme.toString();
    }

        String readPartialDate(CaseManagementNoteExt cme) {
            String type = cme.getValue();
            String val = null;

            if (type!=null && !type.trim().equals("")) {
                if (type.equals(PartialDate.YEARONLY))
                    val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy");
                else if (type.equals(PartialDate.YEARMONTH))
                    val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy-MM");
                else val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy-MM-dd");
            } else {
                val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy-MM-dd");
            }
            return val;
        }
%>
