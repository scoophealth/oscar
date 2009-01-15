<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%-- Updated by Eugene Petruhin on 16 dec 2008 while fixing #2434234 --%>

<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%@page
	import="java.util.Arrays, java.util.Properties, java.util.List, java.util.Set, java.util.ArrayList, java.util.HashSet, java.util.Iterator, java.text.SimpleDateFormat, java.util.Calendar, java.util.Date, java.text.ParseException"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page
	import="org.oscarehr.common.model.UserProperty, org.oscarehr.casemgmt.model.*"%>
<%@page import="org.oscarehr.casemgmt.web.formbeans.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.model.Role"%>


<%@page import="org.oscarehr.util.EncounterUtil"%><jsp:useBean
	id="oscarVariables" class="java.util.Properties" scope="session" />

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%
    String demographicNo = request.getParameter("demographicNo");
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    String strBeanName = "casemgmt_oscar_bean" + demographicNo;
    if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute(strBeanName))==null) {
        response.sendRedirect("error.jsp");
        return;
    }
    long start = System.currentTimeMillis();
    long beginning = start;
    long current = 0;    
    String provNo = bean.providerNo;    
    //Properties windowSizes = oscar.oscarEncounter.pageUtil.EctWindowSizes.getWindowSizes(provNo); 
    
    String pId=(String)session.getAttribute("case_program_id");
    if (pId==null) pId="";
    
    String dateFormat = "dd-MMM-yyyy H:mm";    
    long savedId = 0;
    boolean found = false;
    String bgColour = "color:#000000;background-color:#CCCCFF;";
    ArrayList lockedNotes = new ArrayList();
    ArrayList unLockedNotes = new ArrayList();
    ArrayList unEditableNotes = new ArrayList();
    
    java.util.List noteList=(java.util.List)request.getAttribute("Notes");
    int noteSize = noteList != null ? noteList.size() : 0;
    
    SimpleDateFormat jsfmt = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
    Date dToday = new Date();
    String strToday = jsfmt.format(dToday);
    
    String frmName = "caseManagementEntryForm" + demographicNo;
    CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean)session.getAttribute(frmName);    
    pageContext.setAttribute("caseManagementEntryForm", cform);
%>
<script type="text/javascript">  
    numNotes = <%=noteSize%>; //How many saved notes do we have?
    ctx = "<c:out value="${ctx}"/>";
    imgPrintgreen.src = ctx + "/oscarEncounter/graphics/printerGreen.png"; //preload green print image so firefox will update properly
    providerNo = "<%=provNo%>";
    demographicNo = "<%=demographicNo%>";
    case_program_id = "<%=pId%>";
    
    <caisi:isModuleLoad moduleName="caisi">
        caisiEnabled = true;
    </caisi:isModuleLoad>
        
    <c:if test="${sessionScope.passwordEnabled=='true'}">
        passwordEnabled = true;
    </c:if>
</script>
<%
    current = System.currentTimeMillis();
    System.out.println("NEW CASEMANAGEMENT VIEW jscode loaded " + String.valueOf(current-start));
    start = current;
%>
<%-- <div class="tabs" id="tabs">

<%
	String selectedTab = request.getParameter("tab");
	if(selectedTab==null || selectedTab.trim().equals("")) {
		selectedTab=CaseManagementViewFormBean.tabs[0];
	}
	pageContext.setAttribute("selectedTab",selectedTab);
		
	java.util.List aList=(java.util.List)request.getAttribute("Allergies"); 
	boolean allergies=false;
	if (aList!=null){
		allergies = aList.size() > 0;
	}
	
	boolean reminders = false;
	CaseManagementCPP cpp = (CaseManagementCPP)request.getAttribute("cpp");
	if(cpp!=null){
		reminders = cpp.getReminders().length() > 0;
	}
	//get programId
	String pId=(String)session.getAttribute("case_program_id");
	if (pId==null) pId="";
        System.out.println("case_program_id " + pId);
        System.out.println("Demo No " + request.getParameter("demographicNo"));
        System.out.println("Provider " + request.getParameter("providerNo"));
%>
<table>
<tr>
<th width="8%"></th><th style="font-size: 20" colspan="2" width="80%"><b>Case Management Encounter</b></th>
<%
WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
%>
<th width="12%" align="right" nowrap></th>
</tr>

</table>
<table cellpadding="0" cellspacing="0" border="0">

	<tr>
		<% for(int x=0;x<CaseManagementViewFormBean.tabs.length;x++) {%>
			<%
				String extra = "";
				if((allergies && CaseManagementViewFormBean.tabs[x].equals("Allergies"))||(reminders && CaseManagementViewFormBean.tabs[x].equals("Reminders")) ) {
					extra="color:red;";
				}
				
			%>
			<%if (CaseManagementViewFormBean.tabs[x].equals("Allergies") || CaseManagementViewFormBean.tabs[x].equals("Prescriptions")){%>
			<caisirole:SecurityAccess accessName="prescription Read" accessType="access" providerNo='<%=request.getParameter("providerNo")%>' demoNo='<%=request.getParameter("demographicNo")%>' programId="<%=pId%>">
			<%if(CaseManagementViewFormBean.tabs[x].equals(selectedTab)) { %>
				<td style="background-color: #555;<%=extra%>"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>'); return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<%} else { %>
				<td><a style="<%=extra %>" href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>');return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<% } %>
			</caisirole:SecurityAccess>
			<%}else{ %>
			<%if(CaseManagementViewFormBean.tabs[x].equals(selectedTab)) { %>
				<td style="background-color: #555;<%=extra%>"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>'); return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<%} else { %>
				<td><a style="<%=extra %>" href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>');return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<% } %>
			<%} %>
		<% } %>
	</tr>
</table>
</div>
<br/> 

<table width="100%">
<tr>
<td width="75%">
<table cellspacing="1" cellpadding="1">
<tr>
	<td align="right" valign="top" nowrap><b>Client Name:</b></td><td><c:out value="${requestScope.casemgmt_demoName}" /></td>
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>Age:</b></td><td><c:out value="${requestScope.casemgmt_demoAge}" /></td>
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>DOB:</b></td><td><c:out value="${requestScope.casemgmt_demoDOB}" /></td>
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>Team:</b></td><td><c:out value="${requestScope.teamName}" /></td>
</tr>
<tr>	
	<td align="right"  valign="top" nowrap></td>
	<td><c:forEach var="tm" items="${teamMembers}">
		<c:out value="${tm}" />&nbsp;&nbsp;&nbsp;
	</c:forEach></td>	
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>Primary Health Care Provider:</b></td><td><c:out value="${requestScope.cpp.primaryPhysician}" /></td>
</tr>
<tr>
	<td align="right" valign="top" nowrap><b>Primary Counsellor/Caseworker:</b></td><td><c:out value="${requestScope.cpp.primaryCounsellor}" /></td>	
</tr>
</table>
</td>
<td>

	<%String demo=request.getParameter("demographicNo");%>
	<c:choose>
		<c:when test="${not empty requestScope.image_filename}">
			<img style="cursor: pointer;" id="ci" src="<c:out value="${ctx}"/>/images/default_img.jpg" alt="id_photo"  height="100" title="Click to upload new photo." OnMouseOver="document.getElementById('ci').src='<c:out value="${ctx}"/>/images/<c:out value="${requestScope.image_filename}"/>'" OnMouseOut="delay(5000)" window.status='Click to upload new photo'; return true;" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>	
		</c:when>
		<c:otherwise>
			<img style="cursor: pointer;" src="<c:out value="${ctx}"/>/images/defaultR_img.jpg" alt="No_Id_Photo" height="100" title="Click to upload new photo." OnMouseOver="window.status='Click to upload new photo';return true" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>
		</c:otherwise>
	</c:choose>
	
</td>

</tr>
</table>
 
<jsp:include page='<%="/casemgmt/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>'/>
--%>

<html:form action="/CaseManagementView" method="post">
	<html:hidden property="demographicNo" value="<%=demographicNo%>" />
	<html:hidden property="providerNo" value="<%=provNo%>" />
	<html:hidden property="tab" value="Current Issues" />
	<html:hidden property="hideActiveIssue" />
	<html:hidden property="ectWin.rowOneSize" styleId="rowOneSize" />
	<html:hidden property="ectWin.rowTwoSize" styleId="rowTwoSize" />
	<input type="hidden" name="chain" value="list" />
	<input type="hidden" name="method" value="view" />
	<input type="hidden" id="check_issue" name="check_issue">
	<input type="hidden" id="serverDate" value="<%=strToday%>">
	<input type="hidden" id="resetFilter" name="resetFilter" value="false">
	<!--Row One Headers -->

	<%-- <div style="float:left; width:34%; border-width:0px; background-color:#CCCCFF;" class="RowTop" >&nbsp;<bean:message key="oscarEncounter.Index.socialFamHist"/>:</div><input type="hidden" name="shInput"/>
                <div style="float:left; width:33%; border-width:0px; background-color:#CCCCFF;" class="RowTop" >
                    <% if(oscarVariables.getProperty("otherMedications", "").length() > 1) {
                    out.print(oscarVariables.getProperty("otherMedications", ""));
                    %>
                    <% } else { %>
                    <bean:message key="oscarEncounter.Index.otherMed"/>:
                    <% } %>
                </div>
                <div style="float:left; width:15%; margin-right:-4px; border-width:0px; background-color:#CCCCFF;" class="RowTop" >
                    <% if(oscarVariables.getProperty("medicalHistory", "").length() > 1) {
                    out.print(oscarVariables.getProperty("medicalHistory", ""));
                    %>
                    <% } else { %>
                    <bean:message key="oscarEncounter.Index.medHist"/>:
                    <% } %>
                </div>   
                
                <div class="RowTop" style="clear:right; float:right; width:18%; text-align:right;vertical-align:bottom; background-color:#CCCCFF;">
                    <a onMouseOver="javascript:window.status='Minimize'; return true;" href="javascript:rowOneX();" title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
                    <bean:message key="oscarEncounter.Index.x"/></a> |
                    <a onMouseOver="javascript:window.status='Small Size'; return true;" href="javascript:rowOneSmall();" title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
                    <bean:message key="oscarEncounter.Index.s"/></a> |
                    <a onMouseOver="javascript:window.status='Medium Size'; return true;" href="javascript:rowOneNormal();" title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
                    <bean:message key="oscarEncounter.Index.n"/></a> |
                    <a onMouseOver="javascript:window.status='Large Size'; return true;" href="javascript:rowOneLarge();" title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
                    <bean:message key="oscarEncounter.Index.l"/></a> |
                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:rowOneFull();" title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
                    <bean:message key="oscarEncounter.Index.f"/></a> |
                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:reset();" title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
                    <bean:message key="oscarEncounter.Index.r"/></a>
                </div>                                  

            <!-- Creating the table tag within the script allows you to adjust all table sizes at once, by changing the value of leftCol -->--%>
	<div
		style="width: 100%; height: 75px; margin: 0px; background-color: #FFFFFF;">
	<div id="divR1I1" class="topBox"
		style="float: left; width: 49%; margin-left: 3px;"><%--&nbsp;<html:textarea styleId="cpp.socialHistory" property="cpp.socialHistory" tabindex="1" styleClass="rowOne" rows="4" cols="28"/>--%>
	</div>

	<!-- This is the Medical History cell ...mh...-->
	<div id="divR1I2" class="topBox"
		style="float: right; width: 49%; margin-right: 3px;"><%--<html:textarea styleId="cpp.medicalHistory" property="cpp.medicalHistory" tabindex="3" styleClass="rowOne"  rows="4" cols="28"/>--%>
	</div>
	</div>
	<!--2nd row headers -->
	<%--
            <div style="float:left; width:50%; background-color:#CCCCFF;" class="RowTop" >
                &nbsp;
                <% if(oscarVariables.getProperty("ongoingConcerns", "").length() > 1) {
                out.print(oscarVariables.getProperty("ongoingConcerns", ""));
                %>
                <% } else { %>
                <bean:message key="oscarEncounter.Index.msgConcerns"/>:
                <% } %>
            </div><input type="hidden" name="ocInput"/>
            
            <div style="float:left; width:32%; margin-right:-4px; background-color:#CCCCFF;" class="RowTop" ><bean:message key="oscarEncounter.Index.msgReminders"/>:</div>   
            
            <div class="RowTop" style="clear:right; float:right; width:18%; text-align:right; vertical-align:bottom; background-color:#CCCCFF;">
                <a onMouseOver="javascript:window.status='Minimize'; return true;" href="javascript:rowTwoX();" title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
                <bean:message key="oscarEncounter.Index.x"/></a> |
                <a onMouseOver="javascript:window.status='Small Size'; return true;" href="javascript:rowTwoSmall();" title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
                <bean:message key="oscarEncounter.Index.s"/></a> |
                <a onMouseOver="javascript:window.status='Medium Size'; return true;" href="javascript:rowTwoNormal();" title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
                <bean:message key="oscarEncounter.Index.n"/></a> |
                <a onMouseOver="javascript:window.status='Large Size'; return true;" href="javascript:rowTwoLarge();" title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
                <bean:message key="oscarEncounter.Index.l"/></a> |
                <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:rowTwoFull();" title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
                <bean:message key="oscarEncounter.Index.f"/></a> |
                <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:reset();" title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
                <bean:message key="oscarEncounter.Index.r"/></a>
            </div>
            --%>
	<div
		style="width: 100%; height: 75px; margin-top: 0px; background-color: #FFFFFF;">
	<!--Ongoing Concerns cell -->
	<div id="divR2I1" class="topBox"
		style="clear: left; float: left; width: 49%; margin-left: 3px;">

	</div>

	<!--Reminders cell -->
	<div id="divR2I2" class="topBox"
		style="clear: right; float: right; width: 49%; margin-right: 3px;">

	</div>
	</div>

	<div id="notCPP"
		style="height: 70%; margin-left: 2px; background-color: #FFFFFF;">
	<%--<div id="rightNavBar" style="width:25%; height:100%; display:inline; float:right; background-color:white;"><jsp:include page="rightColumn.jsp" /></div>--%>
	<div id="topContent"
		style="float: left; width: 100%; margin-right: -2px; padding-bottom: 10px; background-color: #CCCCFF; font-size: 10px;">
	<nested:notEmpty name="caseManagementViewForm"
		property="filter_providers">
		<div style="float: left; margin-left: 30px; margin-top: 0px;"><u>Providers:</u><br>
		<nested:iterate type="String" id="filter_provider"
			property="filter_providers">
			<c:choose>
				<c:when test="${filter_provider == 'a'}">
                            All
                        </c:when>
				<c:otherwise>
					<nested:iterate id="provider" name="providers">
						<c:if test="${filter_provider==provider.providerNo}">
							<nested:write name="provider" property="formattedName" />
							<br>
						</c:if>
					</nested:iterate>
				</c:otherwise>
			</c:choose>
		</nested:iterate></div>
	</nested:notEmpty> <nested:notEmpty name="caseManagementViewForm" property="filter_roles">
		<div style="float: left; margin-left: 30px; margin-top: 0px;"><u>Roles:</u><br>
		<nested:iterate type="String" id="filter_role" property="filter_roles">
			<c:choose>
				<c:when test="${filter_role == 'a'}">
                            All
                        </c:when>
				<c:otherwise>
					<nested:iterate id="role" name="roles">
						<c:if test="${filter_role==role.id}">
							<nested:write name="role" property="name" />
							<br>
						</c:if>
					</nested:iterate>
				</c:otherwise>
			</c:choose>
		</nested:iterate></div>
	</nested:notEmpty> <nested:notEmpty name="caseManagementViewForm" property="note_sort">
		<div style="float: left; margin-left: 30px; margin-top: 0px;"><u>Sort:</u><br>
		<nested:write property="note_sort" /><br>
		</div>
	</nested:notEmpty>

	<div id="filter" style="display: none;">
	<div
		style="clear: both; height: 150px; width: auto; overflow: auto; float: left; position: relative; left: 10%;">
	Provider:
	<ul
		style="margin-left: 0px; padding-left: 0px; margin-top: 1px; list-style: none inside none;">
		<li><html:multibox property="filter_providers" value="a"
			onclick="filterCheckBox(this)"></html:multibox>All</li>
		<%
                        Set<Provider> providers = (Set<Provider>)request.getAttribute("providers");                                                
                        
                        String providerNo;
                        Provider prov;
                        Iterator<Provider>iter = providers.iterator();
                        while(iter.hasNext()) {
                        prov = iter.next();
                        providerNo = prov.getProviderNo();
                        %>
		<li><html:multibox property="filter_providers"
			value="<%=providerNo%>" onclick="filterCheckBox(this)"></html:multibox><%=prov.getFormattedName()%></li>
		<%
                        }
                        %>
	</ul>
	</div>

	<div
		style="height: 150px; width: auto; overflow: auto; float: left; position: relative; left: 20%;">
	Role:
	<ul
		style="margin-left: 0px; padding-left: 0px; margin-top: 1px; list-style: none inside none;">
		<li><html:multibox property="filter_roles" value="a"
			onclick="filterCheckBox(this)"></html:multibox>All</li>
		<%
                        List roles = (List)request.getAttribute("roles");
                        for( int num = 0; num < roles.size(); ++num ) {
                        Role role = (Role)roles.get(num);
                        %>
		<li><html:multibox property="filter_roles"
			value="<%=String.valueOf(role.getId())%>"
			onclick="filterCheckBox(this)"></html:multibox><%=role.getName()%></li>
		<%
                        }
                        %>
	</ul>
	</div>

	<div style="float: left; position: relative; left: 25%;">Sort:
	<ul
		style="margin-left: 0px; padding-left: 0px; margin-top: 1px; list-style: none inside none;">
		<li><html:radio property="note_sort" value="observation_date_asc">Observation Date Asc</html:radio></li>
		<li><html:radio property="note_sort"
			value="observation_date_desc">Observation Date Desc</html:radio></li>
		<li><html:radio property="note_sort" value="providerName">Provider</html:radio></li>
		<li><html:radio property="note_sort" value="programName">Program</html:radio></li>
		<li><html:radio property="note_sort" value="roleName">Role</html:radio></li>
	</ul>
	</div>

	<div
		style="text-align: right; cursor: pointer; text-decoration: underline; margin-right: 10px;"
		onclick="return filter(false);">Show View</div>
	<div
		style="text-align: right; cursor: pointer; text-decoration: underline; margin-right: 10px;"
		onclick="return filter(true);">Reset Filter</div>
	</div>
	<div
		style="float: left; clear: both; margin-top: 5px; margin-bottom: 5px; width: 100%; text-align: center;">
	<img
		src="<c:out value="${ctx}/oscarEncounter/graphics/edit-find.png"/>">
	<input id="enTemplate" tabindex="6" size="16" type="text" value=""
		onkeypress="return grabEnterGetTemplate(event)" />
	<div class="enTemplate_name_auto_complete" id="enTemplate_list"
		style="z-index: 1; display: none">&nbsp</div>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select id="channel">
		<option
			value="http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title="><bean:message
			key="oscarEncounter.Index.oscarSearch" /></option>
		<option value="http://www.google.com/search?q="><bean:message
			key="global.google" /></option>
		<option
			value="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?SUBMIT=y&amp;CDM=Search&amp;DB=PubMed&amp;term="><bean:message
			key="global.pubmed" /></option>
		<option
			value="http://search.nlm.nih.gov/medlineplus/query?DISAMBIGUATION=true&amp;FUNCTION=search&amp;SERVER2=server2&amp;SERVER1=server1&amp;PARAMETER="><bean:message
			key="global.medlineplus" /></option>
		<option
			value="http://www.bnf.org/bnf/bnf/current/noframes/search.htm?n=50&amp;searchButton=Search&amp;q="><bean:message
			key="global.BNF" /></option>
	</select> <input type="text" id="keyword" name="keyword" value=""
		onkeypress="return grabEnter('searchButton',event)" /> <input
		type="button" id="searchButton" name="button" value="Search"
		onClick="popupPage(600,800,'<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>',$('channel').options[$('channel').selectedIndex].value+urlencode($F('keyword')) ); return false;">

	</div>
	<div style="clear: both; text-align: right"><img
		style="cursor: pointer;" title="View Filter" alte="View Filter"
		onclick="showFilter();"
		src="<c:out value="${ctx}/oscarEncounter/graphics/folder-saved-search.png"/>">&nbsp;Filter
	&nbsp;&nbsp; <img style="cursor: pointer;" title="Print"
		id='imgPrintCPP' alt="Toggle Print CPP"
		onclick="return printInfo(this,'printCPP');"
		src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png' />&nbsp;CPP
	&nbsp;&nbsp; <img style="cursor: pointer;" title="Print"
		id='imgPrintRx' alt="Toggle Print Rx"
		onclick="return printInfo(this, 'printRx');"
		src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png' />&nbsp;Rx
	&nbsp;&nbsp;</div>
	</div>
</html:form>
<%
    current = System.currentTimeMillis();
    System.out.println("NEW CASEMANAGEMENT VIEW page filters loaded " + String.valueOf(current-start));
    start = current;
%>
<nested:form action="/CaseManagementEntry"
	style="display:inline; margin-top:0; margin-bottom:0;">
	<html:hidden property="demographicNo" value="<%=demographicNo%>" />
	<html:hidden property="includeIssue" value="off" />
	<input type="hidden" name="deleteId" value="0">
	<input type="hidden" name="lineId" value="0">
	<input type="hidden" name="from" value="casemgmt">
	<input type="hidden" name="method" value="save">
	<input type="hidden" name="change_diagnosis"
		value="<c:out value="${change_diagnosis}"/>" />
	<input type="hidden" name="change_diagnosis_id"
		value="<c:out value="${change_diagnosis_id}"/>" />
	<input type="hidden" name="newIssueId" id="newIssueId">
	<input type="hidden" name="newIssueName" id="newIssueName">
	<input type="hidden" name="ajax" value="false">
	<input type="hidden" name="chain" value="">
	<input type="hidden" name="caseNote.program_no" value="<%=pId%>">
	<input type="hidden" name="noteId" value="0">
	<input type="hidden" name="note_edit" value="new">
	<input type="hidden" name="sign" value="off">
	<input type="hidden" name="verify" value="off">
	<input type="hidden" name="forceNote" value="false">
	<input type="hidden" name="newNoteIdx" value="">
	<input type="hidden" name="notes2print" id="notes2print" value="">
	<input type="hidden" name="printCPP" id="printCPP" value="false">
	<input type="hidden" name="printRx" id="printRx" value="false">
	<input type="hidden" name="encType" id="encType" value="">
	<div id="mainContent"
		style="background-color: #FFFFFF; width: 100%; margin-right: -2px; display: inline; float: left;">
	<span id="issueList"
		style="background-color: #FFFFFF; height: 440px; width: 350px; position: absolute; z-index: 1; display: none; overflow: auto;">
	<table id="issueTable" class="enTemplate_name_auto_complete"
		style="position: relative; left: 0px; display: none;">
		<tr>
			<td style="height: 430px; vertical-align: bottom;">
			<div class="enTemplate_name_auto_complete" id="issueAutocompleteList"
				name="issueAutocompleteList"
				style="position: relative; left: 0px; display: none;"></div>
			</td>
		</tr>
	</table>
	</span>
	<div id="encMainDiv"
		style="width: 99%; border-top: thin groove #000000; border-right: thin groove #000000; border-left: thin groove #000000; background-color: #FFFFFF; height: 410px; overflow: auto; margin-left: 2px;">

	<%
               
                System.out.println("Notes Size " + noteList.size());
                %> <c:if test="${not empty Notes}">
		<%
                    //java.util.List noteList=(java.util.List)request.getAttribute("Notes");
                    int idx = 0;
                    
                    //Notes list will contain all notes including most recently saved
                    //we need to skip this one when displaying                    
                    
                    //if we're editing a note, display it
                    //else check for last unsigned note and use it if present
                    if( cform.getCaseNote().getId() != null ) {
                    savedId = cform.getCaseNote().getId();                
                    }  
                    System.out.println("savedId " + savedId);
                    
                    //Check user property for stale date and show appropriately
                    UserProperty uProp = (UserProperty)request.getAttribute(UserProperty.STALE_NOTEDATE);
                    
                    Date dStaleDate = null;
                    int numToDisplay = 5;
                    int numDisplayed = 0;
                    Calendar cal = Calendar.getInstance();
                    if( uProp != null ) {
                        String strStaleDate = uProp.getValue();                                            
                        if( strStaleDate.equalsIgnoreCase("A") ) {                            
                            cal.set(0,1,1);
                        }
                        else {
                            int pastMths = Integer.parseInt(strStaleDate);
                            cal.add(Calendar.MONTH, pastMths);
                        }
                                                
                    }
                    else {
                        cal.add(Calendar.YEAR,-1);                        
                    }
                    
                    dStaleDate = cal.getTime();
                    System.out.println("STALE DATE " + dStaleDate);
                    long time1,time2;
		    String noteStr;
		    int length;
                    String cppCodes[] = {"OMeds", "SocHistory", "MedHistory", "Concerns", "Reminders"};                    
                    
                    /*
                     *  Cycle through notes starting from the most recent and marking them for full inclusion or one line display
                     *  Need to do this now as we only count face to face encounters against limit of how many to fully display
                     *  If no user preference, show at most five face to face encounter notes 
                     *  Else show all notes withing the user preference
                    */
                    ArrayList<Boolean>fullTxtFormat = new ArrayList<Boolean>(noteSize);                    
                    ArrayList<String>colourStyle = new ArrayList<String>(noteSize);
                    int pos;
                    idx = 0;                    
                    boolean isCPP;
                    for(pos = noteSize-1; pos >= 0; --pos) {
                        CaseManagementNote cmNote = (CaseManagementNote)noteList.get(pos);
                        
                        isCPP = false;
                        bgColour = "color:#000000;background-color:#CCCCFF;";
                        Set nIssues = cmNote.getIssues();
                        Iterator iterator = nIssues.iterator();
                        while( iterator.hasNext() ) {
                            CaseManagementIssue issue = (CaseManagementIssue)iterator.next();
                            for( int cppIdx = 0; cppIdx < cppCodes.length; ++cppIdx ) {
                                if(issue.getIssue().getCode().equals(cppCodes[cppIdx])) {
                                    bgColour = "color:#FFFFFF;background-color:#996633;";
                                    isCPP = true;
                                    break;
                                }                    
                            }
                            if( isCPP )
                                break;
                        }
                        
                        colourStyle.add(bgColour);
                        
                        if( isCPP ) {
                            fullTxtFormat.add(Boolean.FALSE);
                            continue;
                        }
                        
                        if( noteSize > numToDisplay ) {
                            if( uProp == null ) {
                                if( numDisplayed < numToDisplay && cmNote.getObservation_date().compareTo(dStaleDate) >= 0 ) {
                                    fullTxtFormat.add(Boolean.TRUE);

                                    if( cmNote.getEncounter_type().equalsIgnoreCase(EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue()) ) {
                                        ++numDisplayed;
                                    }
                                }
                                else {
                                    fullTxtFormat.add(Boolean.FALSE);
                                }
                            }
                            else {
                                if( cmNote.getObservation_date().compareTo(dStaleDate) >= 0 ) {
                                    fullTxtFormat.add(Boolean.TRUE);
                                }
                                else {
                                    fullTxtFormat.add(Boolean.FALSE);
                                }
                            }
                        }
                        else {
                            fullTxtFormat.add(Boolean.TRUE);
                        }                        
                    }

                    boolean fulltxt;
                    pos = noteSize - 1;
		    for( idx = 0; idx < noteSize; ++idx ) {
                    
                    CaseManagementNote note = (CaseManagementNote)noteList.get(idx);
                    noteStr = note.getNote();                    
                    
                    noteStr = StringEscapeUtils.escapeHtml(noteStr);
                    fulltxt = fullTxtFormat.get(pos);
                    bgColour = colourStyle.get(pos--);
                    if( fulltxt ) { 
                        noteStr = noteStr.replaceAll("\n","<br>");                        
                    }
                    else {
                        length = noteStr.length() > 50 ? 50 : noteStr.length();
                        noteStr = noteStr.substring(0,length);                                                
                    }                                        
                    
                    //System.out.println("Starting " + note.getId());
		    time1 = System.currentTimeMillis();
                    %>
		<div id="nc<%=idx%>" class="note"><input type="hidden"
			id="signed<%=note.getId()%>" value="<%=note.isSigned()%>"> <input
			type="hidden" id="full<%=note.getId()%>"
			value="<%=fulltxt || note.getId() == savedId%>"> <input
			type="hidden" id="bgColour<%=note.getId()%>" value="<%=bgColour%>">
		<input type="hidden" name="caseNote.id" value="<%=note.getId()%>">
		<input type="hidden" name="caseNote.demographic_no"
			value="<%=note.getDemographic_no()%>">
		<div id="n<%=note.getId()%>">
		<%
                            //display last saved note for editing
                            if( note.getId() == savedId ) {    
                            found = true;
                            %> <img title="Print"
			id='print<%=note.getId()%>' alt="Toggle Print Note"
			onclick="togglePrint(<%=note.getId()%>, event)"
			style='float: right; margin-right: 5px;'
			src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png' />
		<textarea tabindex="7" cols="84" rows="10" wrap='soft' class="txtArea"
			style="line-height: 1.1em;" name="caseNote_note"
			id="caseNote_note<%=savedId%>"><nested:write
			property="caseNote.note" /></textarea>
		<div class="sig" style="display:inline;<%=bgColour%>"
			id="sig<%=note.getId()%>"><%@ include file="noteIssueList.jsp"%>
		</div>

		<c:if test="${sessionScope.passwordEnabled=='true'}">
			<p style='background-color: #CCCCFF; display: none; margin: 0px;'
				id='notePasswd'>Password:&nbsp;<html:password
				property="caseNote.password" /></p>
		</c:if> <%    
                            }
                            //else display contents of note for viewing
                            else {
                            
				if( note.isLocked() ) {
                            %> <span id="txt<%=note.getId()%>"><bean:message
			key="oscarEncounter.Index.msgLocked" /> <%=DateUtils.getDate(note.getUpdate_date(),dateFormat) + " " + note.getProviderName()%></span>
		<%
                            }
                            else {                                      
                            
                            String rev = note.getRevision();
                            if( fulltxt ) {
                            %> <img title="Minimize Display"
			id='quitImg<%=note.getId()%>' alt="Minimize Display"
			onclick="minView(event)"
			style='float: right; margin-right: 5px; margin-bottom: 3px;'
			src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif' />
		<% }else {
                            %> <img title="Maximize Display"
			id='quitImg<%=note.getId()%>' alt="Maximize Display"
			onclick="fullView(event)" style='float: right; margin-right: 5px;'
			src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_down.gif' />
		<% } 
                            %> <img title="Print"
			id='print<%=note.getId()%>' alt="Toggle Print Note"
			onclick="togglePrint(<%=note.getId()%>, event)"
			style='float: right; margin-right: 5px;'
			src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png' />
                        <a title="Edit" id="edit<%=note.getId()%>" href="#" onclick="editNote(event); return false;" style='float: right; margin-right: 5px; font-size:8px;'>E</a>

		<span id="txt<%=note.getId()%>"><%=noteStr%></span> <%--
			    time2 = System.currentTimeMillis();
			    System.out.println("Format note " + String.valueOf(time2 - time1));
			    time1 = time2;
                            --%> <% if( largeNote(noteStr) ) {
                            %> <img title="Minimize Display"
			id='bottomQuitImg<%=note.getId()%>' alt="Minimize Display"
			onclick="minView(event)"
			style='float: right; margin-right: 5px; margin-bottom: 3px;'
			src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif' />
		<%
                            }

			    //time2 = System.currentTimeMillis();
			    //System.out.println("largeNote() " + String.valueOf(time2 - time1));
			    //time1 = time2;
                            %>
		<div id="sig<%=note.getId()%>" class="sig"
			style="clear:both;<%=bgColour%>">
		<div id="sumary<%=note.getId()%>">
		<div id="observation<%=note.getId()%>"
			style="float: right; margin-right: 3px;"><i>Date:&nbsp;<span
			id="obs<%=note.getId()%>"><%=DateUtils.getDate(note.getObservation_date(),dateFormat)%></span>&nbsp;rev<a
			href="#" onclick="return showHistory('<%=note.getId()%>', event);"><%=rev%></a></i></div>
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
			    //time2 = System.currentTimeMillis();
			    //System.out.println("List Editors " + String.valueOf(time2 - time1));
			    //time1 = time2;
                                    Set issSet = note.getIssues();
                                    if( issSet.isEmpty() ) {
                                    %>
		<div>&nbsp;</div>
		<%
                                    }
                                    if( issSet.size() > 0 ) {
                                    %>
		<div style="display: block;"><span style="float: left;">Assigned
		Issues</span>
		<ul style="float: left; list-style: circle inside none; margin: 0px;">
			<% 
                                        Iterator i = issSet.iterator();
                                        while( i.hasNext() ) {
                                        CaseManagementIssue iss = (CaseManagementIssue)i.next();
                                        %>
			<li><%=iss.getIssue().getDescription().trim()%></li>
			<%
                                        }
                                    %>
		</ul>
		<br style="clear: both;">
		</div>
		<%
                                    }
			    //time2 = System.currentTimeMillis();
			    //System.out.println("List Issues " + String.valueOf(time2 - time1));
			    //time1 = time2;
                                    %>
		</div>
		</div>
		<%
                            }
                            }
                            //System.out.println("READONLY SESSION " + session.getAttribute("readonly").equals(false));                                  
                            %>
		</div>
		</div>
		<%
                    //if we are not editing note, remember note ids for setting event listeners
                    //Internet Explorer does not play nice with inserting javascript between divs
                    //so we store the ids here and list the event listeners at the end of this script
                    if( note.getId() != savedId ) {
                        
                        if( note.isSigned() || (!note.isSigned() && note.getProviderNo().equals(provNo))) {
                            if( note.isLocked() ) { 
                                lockedNotes.add(note.getId());
                            }
                            else if( !fulltxt) {
                                unLockedNotes.add(note.getId());
                            }
                        }
                        else {
                            unEditableNotes.add(note.getId());
                        }
                    }    
                    
    //current = System.currentTimeMillis();
    //System.out.println("NEW CASEMANAGEMENT VIEW display note " + note.getId() + " :  " + String.valueOf(current-start));
    //start = current;
                    } //end for */
                    %>
		<%
    current = System.currentTimeMillis();
    System.out.println("NEW CASEMANAGEMENT VIEW display notes " + String.valueOf(current-start));
    start = current;
%>
	</c:if> <%
                if( !found ) {
                savedId = 0;
                %>
	<div id="nc<%=savedId%>" class="note"><input type="hidden"
		id="signed<%=savedId%>" value="false"> <input type="hidden"
		id="full<%=savedId%>" value="true"> <input type="hidden"
		id="bgColour<%=savedId%>"
		value="color:#000000;background-color:#CCCCFF;">
	<div id="n<%=savedId%>" style="line-height: 1.1em;"><textarea
		tabindex="7" cols="84" rows="10" wrap='soft' class="txtArea"
		style="line-height: 1.1em;" name="caseNote_note"
		id="caseNote_note<%=savedId%>"><nested:write
		property="caseNote_note" /></textarea>
	<div class="sig" id="sig0"><%@ include file="noteIssueList.jsp"%>
	</div>

	<c:if test="${sessionScope.passwordEnabled=='true'}">
		<p style='display: none;' id='notePasswd'>Password:&nbsp;<html:password
			property="caseNote.password" /></p>
	</c:if></div>
	</div>
	<%--
    current = System.currentTimeMillis();
    System.out.println("NEW CASEMANAGEMENT VIEW new note set up " + String.valueOf(current-start));
    start = current;
--%> <%
                }
                %>
	</div>
	<div id='save'
		style="width: 99%; background-color: #CCCCFF; padding-top: 5px; margin-left: 2px; border-left: thin solid #000000; border-right: thin solid #000000; border-bottom: thin solid #000000;">
	<span style="float: right; margin-right: 5px;"> <input
		tabindex="10" type='image'
		src="<c:out value="${ctx}/oscarEncounter/graphics/media-floppy.png"/>"
		id="saveImg"
		onclick="Event.stop(event);return savePage('save', 'list');"
		title='<bean:message key="oscarEncounter.Index.btnSave"/>'>&nbsp;
	<input tabindex="11" type='image'
		src="<c:out value="${ctx}/oscarEncounter/graphics/document-new.png"/>"
		id="newNoteImg" onclick="newNote(event); return false;"
		title='<bean:message key="oscarEncounter.Index.btnNew"/>'>&nbsp;
	<input tabindex="12" type='image'
		src="<c:out value="${ctx}/oscarEncounter/graphics/note-save.png"/>"
		onclick="document.forms['caseManagementEntryForm'].sign.value='on';Event.stop(event);return savePage('saveAndExit', '');"
		title='<bean:message key="oscarEncounter.Index.btnSignSave"/>'>&nbsp;
	<input tabindex="13" type='image'
		src="<c:out value="${ctx}/oscarEncounter/graphics/verify-sign.png"/>"
		onclick="document.forms['caseManagementEntryForm'].sign.value='on';document.forms['caseManagementEntryForm'].verify.value='on';Event.stop(event);return savePage('saveAndExit', '');"
		title='<bean:message key="oscarEncounter.Index.btnSign"/>'>&nbsp;
	<input tabindex="14" type='image'
		src="<c:out value="${ctx}/oscarEncounter/graphics/lock-note.png"/>"
		onclick="return toggleNotePasswd();"
		title='<bean:message key="oscarEncounter.Index.btnLock"/>'>&nbsp;
	<input tabindex="15" type='image'
		src="<c:out value="${ctx}/oscarEncounter/graphics/system-log-out.png"/>"
		onclick='closeEnc(event);return false;'
		title='<bean:message key="global.btnExit"/>'>&nbsp; <input
		tabindex="16" type='image'
		src="<c:out value="${ctx}/oscarEncounter/graphics/document-print.png"/>"
		onclick="return printSetup(event);"
		title='<bean:message key="oscarEncounter.Index.btnPrint"/>'> </span>
	<input type='image' id='toggleIssue'
		onclick="return showIssues(event);"
		src="<c:out value="${ctx}/oscarEncounter/graphics/issues.png"/>"
		title='Display Issues'>&nbsp; <input tabindex="8" type="text"
		id="issueAutocomplete" name="issueSearch" style="z-index: 2;"
		onkeypress="return submitIssue(event);" size="25">&nbsp; <input
		tabindex="9" type="button" id="asgnIssues" value="Assign Issues">
	<span id="busy" style="display: none"><img
		style="position: absolute;"
		src="<c:out value="${ctx}/oscarEncounter/graphics/busy.gif"/>"
		alt="Working..." /></span></div>
	</div>
</nested:form>
</div>

<script type="text/javascript">                         
    document.forms["caseManagementEntryForm"].noteId.value = "<%=savedId%>";
    
        
    caseNote = "caseNote_note" + "<%=savedId%>"; 
    //are we editing existing note?  if not init newNoteIdx as we are dealing with a new note
   //save initial note to determine whether save is necessary
   origCaseNote = $F(caseNote);
   <%    
   if(!bean.oscarMsg.equals("")){
   %>
        $(caseNote).value +="\n\n<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(bean.oscarMsg)%>";
   <%
        bean.reason = "";
        bean.oscarMsg = "";
   } 
    
   if( request.getParameter("noteBody") != null ) {
        String noteBody = request.getParameter("noteBody");
        noteBody = noteBody.replaceAll("<br>|<BR>", "\n");
   %>
        $(caseNote).value +="\n\n<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(noteBody)%>";
   <%
   }
   
    if( found != true ) { %>
        document.forms["caseManagementEntryForm"].newNoteIdx.value = <%=savedId%>;
   <%}%>
   
    setupNotes();
    Element.observe(caseNote, "keyup", monitorCaseNote); 
    Element.observe(caseNote, 'click', getActiveText);
    <%
        Long num;
        Iterator iterator = lockedNotes.iterator();
        while( iterator.hasNext() ) {
            num = (Long)iterator.next();
    %>
            Element.observe('n<%=num%>', 'click', unlockNote);
    <%
        }
        
        iterator = unLockedNotes.iterator();
        while( iterator.hasNext() ) {
            num = (Long)iterator.next();
    %>
            Element.observe('n<%=num%>', 'click', fullView);
    <%
        }      
    
        iterator = unEditableNotes.iterator();
        while( iterator.hasNext() ) {
            num = (Long)iterator.next();
    %>
            Element.observe('n<%=num%>', 'click', noPrivs);
    <%
        }
    %>   
    
    //flag for determining if we want to submit case management entry form with enter key pressed in auto completer text box
    var submitIssues = false;
   //AutoCompleter for Issues
    <c:url value="/CaseManagementEntry.do?method=issueList&demographicNo=${demographicNo}&providerNo=${param.providerNo}" var="issueURL" />
    var issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", "<c:out value="${issueURL}"/>", {minChars: 4, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});

    <%
    int MaxLen = 20;
    int TruncLen = 17;
    String ellipses = "...";
    for(int j=0; j<bean.templateNames.size(); j++) {
     String encounterTmp = (String)bean.templateNames.get(j);
     encounterTmp = oscar.util.StringUtils.maxLenString(encounterTmp, MaxLen, TruncLen, ellipses);
     encounterTmp = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterTmp);
   %>
     autoCompleted["<%=encounterTmp%>"] = "ajaxInsertTemplate('<%=encounterTmp%>')";
     autoCompList.push("<%=encounterTmp%>");
     itemColours["<%=encounterTmp%>"] = "99CCCC";
   <%
    }
   %>      
   //set default event for assigning issues
   //we do this here so we can change event listener when changing diagnosis
   var obj = { };
   var makeIssue = "makeIssue";
   var defaultDiv = "sig<%=savedId%>";
   var changeIssueFunc;  //set in changeDiagnosis function above
   var addIssueFunc = updateIssues.bindAsEventListener(obj, makeIssue, defaultDiv);
   Element.observe('asgnIssues', 'click', addIssueFunc);
   new Autocompleter.Local('enTemplate', 'enTemplate_list', autoCompList, { colours: itemColours, afterUpdateElement: menuAction }  );      
   
   //start timer for autosave
   setTimer();        
                           
    //$("encMainDiv").scrollTop = $("n<%=savedId%>").offsetTop - $("encMainDiv").offsetTop;
    reason = "<%=insertReason(request, bean)%>";    //function defined bottom of file
   </script>

<%
    current = System.currentTimeMillis();
    System.out.println("NEW CASEMANAGEMENT VIEW total " + String.valueOf(current-beginning));
%>
<%!
  
    /*
     *Insert encounter reason for new note
     */
    protected String insertReason(HttpServletRequest request, oscar.oscarEncounter.pageUtil.EctSessionBean bean) {
        String encounterText = "";
        if( bean != null ) {            
            String apptDate = convertDateFmt(bean.appointmentDate);
            if(bean.eChartTimeStamp==null){
                  encounterText ="\n["+oscar.util.UtilDateUtilities.DateToString(bean.currentDate, "dd-MMM-yyyy",request.getLocale())+" .: "+bean.reason+"] \n";
                  //encounterText +="\n["+bean.appointmentDate+" .: "+bean.reason+"] \n";
            }else { //if(bean.currentDate.compareTo(bean.eChartTimeStamp)>0){
                   //System.out.println("2curr Date "+ oscar.util.UtilDateUtilities.DateToString(oscar.util.UtilDateUtilities.now(),"yyyy",java.util.Locale.CANADA) );
                  //encounterText +="\n__________________________________________________\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n";
                   encounterText ="\n["+("".equals(bean.appointmentDate)?oscar.util.UtilDateUtilities.getToday("dd-MMM-yyyy"):apptDate)+" .: "+bean.reason+"]\n";
            }/*else {//if((bean.currentDate.compareTo(bean.eChartTimeStamp) == 0) && (bean.reason != null || bean.subject != null ) && !bean.reason.equals(bean.subject) ){
                   //encounterText +="\n__________________________________________________\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n";
                   encounterText ="\n["+apptDate+" .: "+bean.reason+"]\n";
            }*/
           //System.out.println("eChartTimeStamp" + bean.eChartTimeStamp+"  bean.currentDate " + dateConvert.DateToString(bean.currentDate));//" diff "+bean.currentDate.compareTo(bean.eChartTimeStamp));           

        }
        encounterText = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterText);
        return encounterText;
    }
    
    protected String convertDateFmt(String strOldDate) {
        String strNewDate = "";
        if( strOldDate != null && strOldDate.length() > 0 ) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            try {
                
                Date tempDate = fmt.parse(strOldDate);
                strNewDate = new SimpleDateFormat("dd-MMM-yyyy").format(tempDate);
                
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        
        return strNewDate;
    }
    
    protected boolean largeNote(String note) {
        final int THRESHOLD = 10;
        boolean isLarge = false;       
        int pos = -1;
        
        for( int count = 0; (pos = note.indexOf("\n",pos+1)) != -1; ++count ) {            
            if( count == THRESHOLD ) {
                isLarge = true;
                break;
            }
        }
        
        return isLarge;
    }

%>
