
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%-- Updated by Eugene Petruhin on 09 jan 2009 while fixing #2482832 & #2494061 --%>
<%-- Updated by Eugene Petruhin on 15 jan 2009 while fixing #2510692 --%>
<%-- Updated by Eugene Petruhin on 27 jan 2009 while fixing #2510693 --%>

<%@ include file="/casemgmt/taglibs.jsp"%>

<%@ page import="org.oscarehr.casemgmt.model.*"%>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="oscar.OscarProperties"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@page import="org.oscarehr.casemgmt.web.CaseManagementViewAction"%>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplay"%>
<%
	Logger logger=Logger.getLogger("CaseManagementView.jsp");
	
	@SuppressWarnings("unchecked")
	java.util.List<NoteDisplay> noteList=(java.util.List<NoteDisplay>)request.getAttribute("Notes");

    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>


<html:form action="/CaseManagementView" method="get">
	<html:hidden property="demographicNo" />
	<html:hidden property="providerNo" />
	<html:hidden property="tab" />
	<html:hidden property="cpp.id" />
	<html:hidden property="hideActiveIssue" />
	<input type="hidden" name="issue_code" value="" />
	<input type="hidden" name="method" value="view" />

	<script>

	function trimAll(sString)
	{
		while (sString.substring(0,1) == ' ')
		{
			sString = sString.substring(1, sString.length);
		}
		while (sString.substring(sString.length-1, sString.length) == ' ')
		{
			sString = sString.substring(0,sString.length-1);
		}
		return sString;
	}

	// filter for provider - will work for other dropdowns as well
<%if (!"detailed".equals(request.getParameter("note_view")))
				{%>
	function filter(term, _id, cellNr) {
		
		var suche = trimAll(term.toLowerCase());
		//alert(suche.length + suche  + _id);
		var table = document.getElementById(_id);

		if ("all" == suche) {
			for (var r = 1; r < table.rows.length; r++)
				table.rows[r].style.display = '';
			return;
		}

		for (var r = 1; r < table.rows.length; r++) {
			var ele = table.rows[r].cells[cellNr].innerHTML.replace(/<[^>]+>/g,"");
			
			if (ele.toLowerCase().indexOf(suche) >= 0 ) {
				table.rows[r].style.display = '';
			} else {
				table.rows[r].style.display = 'none';
			}
		}
	}
<%}
				else if (noteList != null)
				{%>
	function filter(term, _id, cellNr) {
		
		var suche = trimAll(term.toLowerCase());
		var noteCount = <%=noteList.size()%>;
				table.style.display = '';
			
	}
<%}%>

	function clickTab(name) {
		document.caseManagementViewForm.tab.value=name;
		document.caseManagementViewForm.submit();
	}
	function popupNotePage(varpage) {
        var page = "" + varpage;
        windowprops = "height=800,width=800,location=no,"
          + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
       var popup = window.open(page, "editNote", windowprops);
       if (popup != null) {
    		if (popup.opener == null) {
      		popup.opener = self;
    		}
    		popup.focus();
  		}
    }
    
    function popupHistoryPage(varpage) {
        var page = "" + varpage;
        windowprops = "location=no,"
          + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, "", windowprops);
    }
    
	function popupUploadPage(varpage,dn) {
        var page = "" + varpage+"?demographicNo="+dn;
        windowprops = "height=500,width=500,location=no,"
          + "scrollbars=no,menubars=no,toolbars=no,resizable=yes,top=50,left=50";
         var popup=window.open(page, "", windowprops);
         popup.focus();
        
    }

	function delay(time){
		string="document.getElementById('ci').src='<%=request.getContextPath()+ClientImage.imagePresentPlaceholderUrl%>'";
		setTimeout(string,time);
	}

	</script>

	<div class="tabs" id="tabs">
	<%
		String selectedTab=request.getParameter("tab");
			if (selectedTab == null || selectedTab.trim().equals(""))
			{
				selectedTab=CaseManagementViewFormBean.tabs[0];
			}
			pageContext.setAttribute("selectedTab", selectedTab);

			java.util.List aList=(java.util.List)request.getAttribute("Allergies");
			boolean allergies=false;
			if (aList != null)
			{
				allergies=aList.size() > 0;
			}

			boolean reminders=false;
			CaseManagementCPP cpp=(CaseManagementCPP)request.getAttribute("cpp");
			if (cpp != null && cpp.getReminders() != null)
			{
				reminders=cpp.getReminders().length() > 0;
			}
			//get programId
			String pId=(String)session.getAttribute("case_program_id");
			if (pId == null) pId="";
	%>
	<table>
		<tr>
			<th width="8%"></th>
			<th style="font-size: 20" colspan="2" width="80%"><b>Case
			Management Encounter</b></th>
			<%
				WebApplicationContext ctx=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			%>
			<th width="12%" align="right" nowrap></th>
		</tr>

	</table>
	<table cellpadding="0" cellspacing="0" border="0">

		<tr>
			<%
				for (int x=0; x < CaseManagementViewFormBean.tabs.length; x++)
					{
			%>
			<%
				if (OscarProperties.getInstance().isTorontoRFQ())
						{
							if (CaseManagementViewFormBean.tabs[x].equals("Prescriptions") || CaseManagementViewFormBean.tabs[x].equals("Allergies"))
							{
								continue;
							}
						}
						String extra="";
						if ((allergies && CaseManagementViewFormBean.tabs[x].equals("Allergies"))
								|| (reminders && CaseManagementViewFormBean.tabs[x].equals("Reminders")))
						{
							extra="color:red;";
						}
			%>
			<%
				if (CaseManagementViewFormBean.tabs[x].equals("Allergies") || CaseManagementViewFormBean.tabs[x].equals("Prescriptions"))
						{
			%>
			<caisirole:SecurityAccess accessName="prescription Read"
				accessType="access"
				providerNo='<%=request.getParameter("providerNo")%>'
				demoNo='<%=request.getParameter("demographicNo")%>'
				programId="<%=pId%>">
				<%
					if (CaseManagementViewFormBean.tabs[x].equals(selectedTab))
									{
				%>
				<td style="background-color: #555;<%=extra%>"><a
					href="javascript:void(0)"
					onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x]%>'); return false;"><%=CaseManagementViewFormBean.tabs[x]%></a></td>
				<%
					}
									else
									{
				%>
				<td><a style="<%=extra%>" href="javascript:void(0)"
					onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x]%>');return false;"><%=CaseManagementViewFormBean.tabs[x]%></a></td>
				<%
					}
				%>
			</caisirole:SecurityAccess>
			<%
				}
						else
						{
			%>
			<%
				if (CaseManagementViewFormBean.tabs[x].equals(selectedTab))
							{
			%>
			<td style="background-color: #555;<%=extra%>"><a
				href="javascript:void(0)"
				onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x]%>'); return false;"><%=CaseManagementViewFormBean.tabs[x]%></a></td>
			<%
				}
							else
							{
			%>
			<td><a style="<%=extra%>" href="javascript:void(0)"
				onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x]%>');return false;"><%=CaseManagementViewFormBean.tabs[x]%></a></td>
			<%
				}
			%>
			<%
				}
			%>
			<%
				}
			%>
		</tr>
	</table>
	</div>
	<br />

	<table width="100%">
		<tr>
			<td width="75%">
			<table cellspacing="1" cellpadding="1">
				<tr>
					<td align="right" valign="top" nowrap><b>Client Name:</b></td>
					<td><c:out value="${requestScope.casemgmt_demoName}" /></td>
				</tr>
				<tr>
					<td align="right" valign="top" nowrap><b>Age:</b></td>
					<td><c:out value="${requestScope.casemgmt_demoAge}" /></td>
				</tr>
				<tr>
					<td align="right" valign="top" nowrap><b>DOB:</b></td>
					<td><c:out value="${requestScope.casemgmt_demoDOB}" /></td>
				</tr>
				<tr>
					<td align="right" valign="top" nowrap><b>Team:</b></td>
					<td><c:out value="${requestScope.teamName}" /></td>
				</tr>
				<tr>
					<td align="right" valign="top" nowrap></td>
					<td><c:forEach var="tm" items="${teamMembers}">
						<c:out value="${tm}" />&nbsp;&nbsp;&nbsp;
					</c:forEach></td>
				</tr>
				<%
					if (!OscarProperties.getInstance().isTorontoRFQ())
						{
				%>

				<tr>
					<td align="right" valign="top" nowrap><b>Primary Health
					Care Provider:</b></td>
					<td><c:out value="${requestScope.cpp.primaryPhysician}" /></td>
				</tr>
				<%
					}
				%>
				<tr>
					<td align="right" valign="top" nowrap><b>Primary
					Counsellor/Caseworker:</b></td>
					<td><c:out value="${requestScope.cpp.primaryCounsellor}" /></td>
				</tr>
			</table>
			</td>
			<td>
			<%
				String demo=request.getParameter("demographicNo");
			%> <c:choose>
				<c:when test="${not empty requestScope.image_exists}">
					<c:set var="clientId" value="${demographicNo}"></c:set>
					<img style="cursor: pointer;" id="ci" src="<c:out value="${ctx}"/><%=ClientImage.imagePresentPlaceholderUrl%>" alt="id_photo" height="100" title="Click to upload new photo."
						OnMouseOver="document.getElementById('ci').src='../imageRenderingServlet?source=local_client&clientId=<c:out value="${clientId}" />'"
						OnMouseOut="delay(5000)" window.status='Click to upload new photo' ; return	true;"
						onClick="popupUploadPage('<html:rewrite page="/casemgmt/uploadimage.jsp"/>',<%=demo%>);return false;" />
				</c:when>
				<c:otherwise>
					<img style="cursor: pointer;" src="<c:out value="${ctx}"/><%=ClientImage.imageMissingPlaceholderUrl%>" alt="No_Id_Photo" height="100" title="Click to upload new photo." OnMouseOver="window.status='Click to upload new photo';return true"
						onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;" />
				</c:otherwise>
			</c:choose></td>

		</tr>
		<tr>
			<td colspan="2" style="border-top: 1px solid #666666;">&nbsp;</td>
		</tr>
	</table>
	<jsp:include
		page='<%="/casemgmt/" + selectedTab.toLowerCase().replaceAll(" ", "_") + ".jsp"%>' />

	

	<c:if
		test="${sessionScope.caseManagementViewForm.note_view!='detailed'}">
		<html:hidden property="note_view" value="summary" />
	</c:if>
	<c:if
		test="${sessionScope.caseManagementViewForm.note_view=='detailed'}">
		<html:hidden property="note_view" value="detailed" />
	</c:if>
	<c:if
		test="${sessionScope.caseManagementViewForm.prescipt_view!='all'}">
		<html:hidden property="prescipt_view" value="current" />
	</c:if>
	<c:if
		test="${sessionScope.caseManagementViewForm.prescipt_view=='all'}">
		<html:hidden property="prescipt_view" value="all" />
	</c:if>

	<br />
	<br />
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="r">
	<c:if test="${not empty Notes}">
<b>Progress Note Report View:</b>

<table border="0" width="100%">
			<tr>
				<td align="left"><span
					style="text-decoration: underline; cursor: pointer; color: blue"
					onclick="document.caseManagementViewForm.note_view.value='summary';document.caseManagementViewForm.method.value='setViewType';document.caseManagementViewForm.submit(); return false;">Summary</span>
				&nbsp;|&nbsp; <span
					style="text-decoration: underline; cursor: pointer; color: blue"
					onclick="document.caseManagementViewForm.note_view.value='detailed';document.caseManagementViewForm.method.value='setViewType';document.caseManagementViewForm.submit();return false;">Detailed</span>
				<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="w">
				<c:if test="${sessionScope.readonly=='false'}">
					<c:url
						value="/CaseManagementEntry.do?method=edit&note_edit=new&from=casemgmt&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
						var="noteURL" />
	&nbsp;|&nbsp;
	<span style="text-decoration: underline; cursor: pointer; color: blue"
						onclick="popupNotePage('<c:out value="${noteURL}" escapeXml="false"/>')">New
					Note</span>
				</c:if> 
				</security:oscarSec>
				&nbsp;|&nbsp; <span
					style="text-decoration: underline; cursor: pointer; color: blue"
					onclick="window.print();">Print</span> <c:if test="${can_restore}">
					<c:url
						value="/CaseManagementEntry.do?method=restore&from=casemgmt&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
						var="noteURL" />
	&nbsp;|&nbsp;
	<span style="text-decoration: underline; cursor: pointer; color: blue"
						onclick="popupNotePage('<c:out value="${noteURL}" escapeXml="false"/>')">Restore
					Lost Note</span>
				</c:if></td>
				<td align="right">Provider: <html:select
					property="filter_provider"
					onchange="filter(this.options[this.selectedIndex].text, 'test', 2);">
					<html:option value="">All</html:option>
					<html:options collection="providers" property="value" labelProperty="label" />
				</html:select> &nbsp; &nbsp; &nbsp; Sort: <html-el:select property="note_sort"
					onchange="document.caseManagementViewForm.method.value='view';document.caseManagementViewForm.note_view.value='${param.note_view}';document.caseManagementViewForm.submit()">
					<html:option value="observation_date_desc">Observation Date - Desc</html:option>
					<html:option value="observation_date_asc">Observation Date - Asc</html:option>
					<html:option value="providerName">Provider</html:option>
					<html:option value="programName">Program</html:option>
					<html:option value="roleName">Role</html:option>
				</html-el:select></td>
			</tr>
		</table>
		<c:if
			test="${param.note_view!='detailed'}">
			<table id="test" width="100%" border="0" cellpadding="0"
				cellspacing="1" bgcolor="#C0C0C0">
				<tr class="title">
					<td></td>
					<td>Date</td>
					<td>Provider</td>
					<td>Status</td>
					<td>Program</td>
					<td>Location</td>
					<td>Role</td>
				</tr>
				<%
					int index=0;
								String bgcolor="white";
				%>
				<c:forEach var="note" items="${Notes}">
					<%
						if (index++ % 2 != 0)
										{
											bgcolor="white";
										}
										else
										{
											bgcolor="#EEEEFF";
										}
					%>
					<tr bgcolor="<%=bgcolor%>" align="center">
						<td>
						<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="u">
						<c:choose>
							<c:when test="${(note.editable) and (sessionScope.readonly=='false')}">
								<c:url value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${note.noteId}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}&forceNote=true" var="notesURL" />
								<img src="<c:out value="${ctx}"/>/images/edit_white.png" title="Edit/Sign Note" style="cursor: pointer" onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')" />
							</c:when>
							<c:otherwise>
								<img src="<c:out value="${ctx}"/>/images/transparent_icon.gif" title="" />
							</c:otherwise>
						</c:choose> 
						</security:oscarSec>
						<c:choose>
							<c:when test="${note.hasHistory == true and note.locked != true}">
								<c:url value="/CaseManagementEntry.do?method=history&from=casemgmt&noteId=${note.noteId}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}" var="historyURL" />
								<img src="<c:out value="${ctx}"/>/images/history.gif" title="Note History" style="cursor: pointer" onclick="popupHistoryPage('<c:out value="${historyURL}" escapeXml="false"/>')">
							</c:when>
							<c:otherwise>
								<img src="<c:out value="${ctx}"/>/images/transparent_icon.gif" title="" />
							</c:otherwise>
						</c:choose> 
						<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="u">
						<c:choose>
							<c:when test="${note.locked}">
								<c:url
									value="/CaseManagementView.do?method=unlock&noteId=${note.noteId}"
									var="lockedURL" />
								<img src="<c:out value="${ctx}"/>/images/ulock.gif"
									title="Unlock" style="cursor: pointer"
									onclick="popupPage('<c:out value="${lockedURL}" escapeXml="false"/>')" />
							</c:when>
							<c:otherwise>
								<img src="<c:out value="${ctx}"/>/images/transparent_icon.gif"
									title="" />
							</c:otherwise>
						</c:choose>
						</security:oscarSec>
						</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd hh:mm a" value="${note.observationDate}" /></td>
						<td><c:out value="${note.providerName}" /></td>
						<td><c:out value="${note.status}" /></td>
						<td><c:out value="${note.programName}" /></td>
						<td><c:out value="${note.location}" /></td>
						<td><c:out value="${note.roleName}" /></td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		<c:if
			test="${param.note_view=='detailed'}">
			<table id="test" width="100%" border="0" cellpadding="0"
				cellspacing="1" bgcolor="#C0C0C0">
				<%
					int index1=0;
								String bgcolor1="white";
				%>
				<c:forEach var="note" items="${Notes}">
					<%
						if (index1++ % 2 != 0)
						{
							bgcolor1="white";
						}
						else
						{
							bgcolor1="#EEEEFF";
						}
						Integer noteId;
						noteId=noteList.get(index1 - 1).getNoteId();
						request.setAttribute("noteId", noteId);
					%>
					<tr>
						<td>
						<table id="test<%=index1%>" width="100%" border="0" style="margin-bottom: 5px">
							<tr bgcolor="<%=bgcolor1%>">
								<td width="7%">Facility</td>
								<td width="93%"><c:out value="${note.location}" /></td>
							</tr>
							<tr bgcolor="<%=bgcolor1%>">
								<td width="7%">Program</td>
								<td width="93%">
									<c:out value="${note.programName}" />
								</td>
							</tr>
							<tr bgcolor="<%=bgcolor1%>">
								<td width="7%">Provider</td>
								<td width="93%">
									<c:out value="${note.providerName}" />
								</td>
							</tr>
							<tr bgcolor="<%=bgcolor1%>">
								<td width="7%">Date</td>
								<td width="93%"><fmt:formatDate pattern="yyyy-MM-dd hh:mm a" value="${note.observationDate}" /></td>
							</tr>
							<tr bgcolor="<%=bgcolor1%>">
								<td width="7%">Status</td>
								<td width="93%"><c:out value="${note.status}" /></td>
							</tr>
						<tr bgcolor="<%=bgcolor1%>">
								<td width="7%">Action</td>
								<td width="93%">
									<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="u">
										<c:if test="${(note.editable) and (sessionScope.readonly=='false')}">
											<c:url value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${requestScope.noteId}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}" var="notesURL" />
											<input type="button" value="Edit and Sign" onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')">
										</c:if> 
									</security:oscarSec>
									<c:if test="${note.hasHistory == true}">
										<c:url value="/CaseManagementEntry.do?method=history&from=casemgmt&noteId=${requestScope.noteId}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}" var="historyURL" />
										<input type="button" value="Note History" onclick="popupHistoryPage('<c:out value="${historyURL}" escapeXml="false"/>')">
									</c:if> 
									<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="u">
										<c:if test="${note.locked}">
											<c:url value="/CaseManagementView.do?method=unlock&noteId=${requestScope.noteId}" var="lockedURL" />
											<input type="button" value="Unlock" onclick="popupPage('<c:out value="${lockedURL}" escapeXml="false"/>')">
										</c:if>
									</security:oscarSec>
								</td>
							</tr>
						<tr bgcolor="<%=bgcolor1%>">
								<td width="7%">Note</td>
								<td width="93%">
									<c:choose>
										<c:when test="${note.locked}">
											<span style="color: red"><i>Contents Hidden</i></span>
										</c:when>
										<c:otherwise>
											<pre><c:out value="${note.note}" /></pre>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</table>
						
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		<%
			String filter=request.getParameter("filter_provider");
					if (filter != null && filter.length() > 0)
					{
		%>
			<script>
				var els = document.getElementsByName("filter_provider");
				var el = els[0];
				filter(el.options[el.selectedIndex].text, 'test', 2);
			</script>
		<%
			}
		%>
		<span style="text-decoration: underline; cursor: pointer; color: blue"
			onclick="document.caseManagementViewForm.note_view.value='summary';document.caseManagementViewForm.method.value='setViewType';document.caseManagementViewForm.submit(); return false;">Summary</span>
&nbsp;|&nbsp;
<span style="text-decoration: underline; cursor: pointer; color: blue"
			onclick="document.caseManagementViewForm.note_view.value='detailed';document.caseManagementViewForm.method.value='setViewType';document.caseManagementViewForm.submit();return false;">Detailed</span>
		<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="w">
		<c:if test="${sessionScope.readonly=='false'}">
			<c:url
				value="/CaseManagementEntry.do?method=edit&note_edit=new&from=casemgmt&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
				var="noteURL" />
	&nbsp;|&nbsp;
	
			<span style="text-decoration: underline; cursor: pointer; color: blue"
				onclick="popupNotePage('<c:out value="${noteURL}" escapeXml="false"/>')">New
			Note</span>
		</c:if>
		</security:oscarSec>
&nbsp;|&nbsp;
<span style="text-decoration: underline; cursor: pointer; color: blue"
			onclick="window.print();">Print</span> <c:if test="${can_restore}">
					<c:url
						value="/CaseManagementEntry.do?method=restore&from=casemgmt&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
						var="noteURL" />
	&nbsp;|&nbsp;
	<span style="text-decoration: underline; cursor: pointer; color: blue"
						onclick="popupNotePage('<c:out value="${noteURL}" escapeXml="false"/>')">Restore
					Lost Note</span>
				</c:if>

		<br />
		<br />
	</c:if>
</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="w">
	<c:if test="${'Current Issues'==selectedTab and empty Notes and sessionScope.readonly=='false'}">
		<c:url
			value="/CaseManagementEntry.do?method=edit&note_edit=new&from=casemgmt&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
			var="noteURL" />
		<span style="text-decoration: underline; cursor: pointer; color: blue"
			onclick="popupNotePage('<c:out value="${noteURL}" escapeXml="false"/>')">New
		Note</span>
	</c:if>
</security:oscarSec>

</html:form>
