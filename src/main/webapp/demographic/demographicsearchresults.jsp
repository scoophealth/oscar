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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.jpedal.fonts.tt.FirstPoint"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedProvider"%>
<%@page import="org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicTransfer"%>
<%@page import="org.oscarehr.caisi_integrator.ws.MatchingDemographicTransferScore"%>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

	if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
     Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;

     LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
     
 	GregorianCalendar now=new GregorianCalendar();
 	int curYear = now.get(Calendar.YEAR);
 	int curMonth = (now.get(Calendar.MONTH)+1);
 	int curDay = now.get(Calendar.DAY_OF_MONTH);
 	String curProvider_no = (String) session.getAttribute("user");
 	

%>


<%@ page import="java.util.*, java.sql.*, java.net.URLEncoder, oscar.*, oscar.util.*" errorPage="errorpage.jsp" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="oscar.oscarDemographic.data.DemographicMerged" %>

<jsp:useBean id="providerBean" class="java.util.Properties"	scope="session" />

<%
	String strOffset = "0";
	String strLimit = "18";
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";
	if (request.getParameter("limit1") != null)
		strOffset = request.getParameter("limit1");
	if (request.getParameter("limit2") != null)
		strLimit = request.getParameter("limit2");

	int offset = Integer.parseInt(strOffset);
	int limit = Integer.parseInt(strLimit);

	String displayMode = request.getParameter("displaymode");
	String dboperation = request.getParameter("dboperation");
	String keyword = request.getParameter("keyword");
	String orderBy = request.getParameter("orderby");
	String ptStatus = request.getParameter("ptstatus");

	java.util.ResourceBundle oscarResources = ResourceBundle.getBundle("oscarResources", request.getLocale());
    String noteReason = oscarResources.getString("oscarEncounter.noteReason.TelProgress");

	if (OscarProperties.getInstance().getProperty("disableTelProgressNoteTitleInEncouterNotes") != null 
			&& OscarProperties.getInstance().getProperty("disableTelProgressNoteTitleInEncouterNotes").equals("yes")) {
		noteReason = "";
	}

%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
<title><bean:message key="demographic.demographicsearchresults.title" /></title>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="demoSearch"/>

<% if (isMobileOptimized) { %>
   <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
   <link rel="stylesheet" type="text/css" href="../mobile/searchdemographicstyle.css">
<% } else { %>
   <link rel="stylesheet" type="text/css" media="all" href="../demographic/searchdemographicstyle.css"  />
   <link rel="stylesheet" type="text/css" media="all" href="../share/css/searchBox.css"  />
   <style type="text/css"> .deep { background-color: <%= deepColor %>; } .weak { background-color: <%= weakColor %>; } </style>
<% } %>

<%
	String ptstatus = request.getParameter("ptstatus") == null ? "active" : request.getParameter("ptstatus");

	org.oscarehr.util.MiscUtils.getLogger().info("PSTATUS " + ptstatus);
	OscarProperties props = OscarProperties.getInstance();
%>

<script language="JavaScript">

	function showHideItem(id) {
		if (document.getElementById(id).style.display == 'inline')
			document.getElementById(id).style.display = 'none';
		else
			document.getElementById(id).style.display = 'inline';
	}
	function setfocus() {
		document.titlesearch.keyword.focus();
		document.titlesearch.keyword.select();
	}

	function checkTypeIn() {
		var dob = document.titlesearch.keyword;
		typeInOK = true;

		if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18) {
			document.titlesearch.keyword.value = dob.value.substring(8, 18);
			document.titlesearch.search_mode[4].checked = true;
		}
		if (document.titlesearch.search_mode[0].checked) {
			var keyword = document.titlesearch.keyword.value;
			var keywordLowerCase = keyword.toLowerCase();
			document.titlesearch.keyword.value = keywordLowerCase;
		}
		if (document.titlesearch.search_mode[2].checked) {
			if (dob.value.length == 8) {
				dob.value = dob.value.substring(0, 4) + "-"
						+ dob.value.substring(4, 6) + "-"
						+ dob.value.substring(6, 8);
			}
			if (dob.value.length != 10) {
				alert("<bean:message key="demographic.search.msgWrongDOB"/>");
				typeInOK = false;
			}

			return typeInOK;
		} else {
			return true;
		}
	}

	function popup(vheight, vwidth, varpage) {
		var page = varpage;
		windowprops = "height="
				+ vheight
				+ ",width="
				+ vwidth
				+ ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
		var popup = window.open(varpage, "<bean:message key="global.oscarRx"/>_________________$tag________________________________demosearch",	windowprops);
		if (popup != null) {
			if (popup.opener == null) {
				popup.opener = self;
			}
			popup.focus();
		}
	}

	function popupEChart(vheight,vwidth,varpage) { //open a new popup window
		  var page = "" + varpage;
		  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
		  var popup=window.open(page, "encounter", windowprops);
		  if (popup != null) {
		    if (popup.opener == null) {
		      popup.opener = self;
		    }
		    popup.focus();
		  }
		}
</SCRIPT>
</head>
	

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">

<div id="demographicSearch">
    <a href="#" onclick="showHideItem('demographicSearch');" id="cancelButton" class="leftButton top"> <bean:message key="global.btnCancel" /> </a>
	<%@ include file="zdemographicfulltitlesearch.jsp"%>
</div>


<div id="searchResults">
<a href="#" onclick="showHideItem('demographicSearch');" id="searchPopUpButton" class="rightButton top">Search</a>
<br>
<i><bean:message key="demographic.demographicsearchresults.msgSearchKeys" /></i> : <%=request.getParameter("keyword")%>

    <table>
        <tr class="tableHeadings deep">
        
		<% if ( fromMessenger ) {%>
		<!-- leave blank -->
		                <td class="demoIdSearch">
                    <a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnDemoNo" /></a>
                </td>
		<%} else {%>
                <td class="demoIdSearch">
                    <a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnDemoNo" /></a>
                </td>
		<td class="links"><bean:message key="demographic.demographicsearchresults.module" /> <!-- b><a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit%>">Links<sup>*</sup></a></b --></td>

		<%}%>
		<td class="name"><a
                    href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=last_name&limit1=0&limit2=<%=strLimit%>"><bean:message
                    key="demographic.demographicsearchresults.btnDemoName"/></a>
                </td>
		<td class="chartNo"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=chart_no&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnChart" /></a>
                </td>
		<td class="sex"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=sex&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnSex" /></a>
                </td>
		<td class="dob"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=dob&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnDOB" /> <span class="dateFormat"><bean:message key="demographic.demographicsearchresults.btnDOBFormat" /></span></a>
                </td>
		<td class="doctor"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=provider_no&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnDoctor" /></a>
                </td>
                <td class="rosterStatus"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=roster_status&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnRosSta" /></a>
                </td>
		<td class="patientStatus"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=patient_status&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnPatSta" /></a>
                </td>
                <td class="phone"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=phone&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnPhone" /></a>
                </td>
	</tr>
	<%
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");
	String providerNo = loggedInInfo.getLoggedInProviderNo();
	boolean outOfDomain = true;
	if(OscarProperties.getInstance().getProperty("ModuleNames","").indexOf("Caisi") != -1) {
		outOfDomain=false;
		if(request.getParameter("outofdomain")!=null && request.getParameter("outofdomain").equals("true")) {
			outOfDomain=true;
		}
	}
	
	

	if (searchMode == null)
		searchMode = "search_name";
	if (orderBy == null)
		orderBy = "last_name";
	
	
	List<Demographic> demoList = null;
	
	demoList = doSearch(demographicDao,searchMode,ptstatus,keyword,limit,offset,orderBy,providerNo,outOfDomain);	
	
	
	boolean toggleLine = false;
	boolean firstPageShowIntegratedResults = request.getParameter("firstPageShowIntegratedResults") != null && "true".equals(request.getParameter("firstPageShowIntegratedResults"));
	int nItems=0;

	if(demoList==null) {
		out.println("Your Search Returned No Results!!!");
	} 
	else {
		
		if(orderBy.equals("last_name")) {
			Collections.sort(demoList, Demographic.LastNameComparator);
		}
		else if(orderBy.equals("demographic_no")) {
			Collections.sort(demoList, Demographic.DemographicNoComparator);
		}
		else if(orderBy.equals("chart_no")) {
			Collections.sort(demoList, Demographic.ChartNoComparator);
		}
		else if(orderBy.equals("sex")) {
			Collections.sort(demoList, Demographic.SexComparator);
		}
		else if(orderBy.equals("dob")) {
			Collections.sort(demoList, Demographic.DateOfBirthComparator);
		}
		else if(orderBy.equals("provider_no")) {
			Collections.sort(demoList, Demographic.ProviderNoComparator);
		}
		else if(orderBy.equals("roster_status")) {
			Collections.sort(demoList, Demographic.RosterStatusComparator);
		}
		else if(orderBy.equals("patient_status")) {
			Collections.sort(demoList, Demographic.PatientStatusComparator);
		}
		else if(orderBy.equals("phone")) {
			Collections.sort(demoList, Demographic.PhoneComparator);
		}
		
		
		@SuppressWarnings("unchecked")
		  List<MatchingDemographicTransferScore> integratorSearchResults=(List<MatchingDemographicTransferScore>)request.getAttribute("integratorSearchResults");
		  
		  
		  if (integratorSearchResults!=null) {
		      firstPageShowIntegratedResults = true;
			  for (MatchingDemographicTransferScore matchingDemographicTransferScore : integratorSearchResults) {
			      if( isLocal(matchingDemographicTransferScore, demoList)) {
				  	continue;
			      }
				  
				  DemographicTransfer demographicTransfer=matchingDemographicTransferScore.getDemographicTransfer();
		%>
				   <tr class="<%=toggleLine?"even":"odd"%>">
				   <td class="demoIdSearch">
				   	<a title="Import" href="#"  onclick="popup(700,1027,'../appointment/copyRemoteDemographic.jsp?remoteFacilityId=<%=demographicTransfer.getIntegratorFacilityId()%>&demographic_no=<%=String.valueOf(demographicTransfer.getCaisiDemographicId())%>&originalPage=../demographic/demographiceditdemographic.jsp&provider_no=<%=curProvider_no%>')" >Import</a></td>
				   <td class="links">Remote</td>
				   <td class="name"><%=Misc.toUpperLowerCase(demographicTransfer.getLastName())%>, <%=Misc.toUpperLowerCase(demographicTransfer.getFirstName())%></td>
				   <td class="chartNo"></td>
				   <td class="sex"><%=demographicTransfer.getGender()%></td>
				   <td class="dob"><%=demographicTransfer.getBirthDate() != null ?  DateFormatUtils.ISO_DATE_FORMAT.format(demographicTransfer.getBirthDate()) : ""%></td>
				   <td class="doctor">
				   
		<% 
		   		FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
		   		providerPk.setIntegratorFacilityId(demographicTransfer.getIntegratorFacilityId());
		   		providerPk.setCaisiItemId(demographicTransfer.getCaisiProviderId());
		   		CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(), providerPk);
		   		MiscUtils.getLogger().debug("Cached provider, pk="+providerPk.getIntegratorFacilityId()+","+providerPk.getCaisiItemId()+", cachedProvider="+cachedProvider);
		   		
		   		String providerName="";
		   		
		   		if (cachedProvider!=null)
		   		{
		   			providerName=cachedProvider.getLastName()+", "+cachedProvider.getFirstName();
		   		}
		%>
		        	<%=providerName%>
					</td>
					<td class="rosterStatus"></td>
					<td class="patientStatus"></td>
					<td class="phone"><%=demographicTransfer.getPhone1()%></td>
				</tr>
		<%	  
					toggleLine = !toggleLine;
					nItems++;
				}
		 	}
		
		

		DemographicMerged dmDAO = new DemographicMerged();

		for(Demographic demo : demoList) {


			
			String dem_no = demo.getDemographicNo().toString();
			String head = dmDAO.getHead(dem_no);

			if (head != null && !head.equals(dem_no)) {
				//skip non head records
				continue;
			}

%>
	<tr class="<%=toggleLine?"even":"odd"%>">
	<td class="demoIdSearch">

	<%

		if (fromMessenger) {
	%>
		<a href="demographiccontrol.jsp?keyword=<%=StringEscapeUtils.escapeJavaScript(Misc.toUpperLowerCase(demo.getLastName()+", "+demo.getFirstName()))%>&demographic_no=<%= dem_no %>&displaymode=linkMsg2Demo&dboperation=search_detail" ><%=demo.getDemographicNo()%></a></td>
	<%	
		} else { 
	%>
		<a title="Master Demographic File" href="#"  onclick="popup(700,1027,'demographiccontrol.jsp?demographic_no=<%=head%>&displaymode=edit&dboperation=search_detail')" ><%=dem_no%></a></td>
	
		<!-- Rights -->
		<td class="links"><security:oscarSec roleName="<%=roleName$%>"
			objectName="_eChart" rights="r">
			<a class="encounterBtn" title="Encounter" href="#"
				onclick="popupEChart(710,1024,'<c:out value="${ctx}"/>/oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=dem_no%>&curProviderNo=&reason=<%=URLEncoder.encode(noteReason)%>&encType=&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=');return false;">E</a>
		</security:oscarSec> <!-- Rights --> <security:oscarSec roleName="<%=roleName$%>"
			objectName="_rx" rights="r">
			<a class="rxBtn" title="Prescriptions" href="#" onclick="popup(700,1027,'../oscarRx/choosePatient.do?providerNo=<%=demo.getProviderNo()%>&demographicNo=<%=dem_no%>')">Rx</a>
		</security:oscarSec></td>

	<%	
		}
		if (OscarProperties.getInstance().isPropertyActive("new_eyeform_enabled")) { 
	%>
		<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r">
			<a title="Eyeform" href="#" onclick="popup(800, 1280, '../eyeform/eyeform.jsp?demographic_no=<%=dem_no %>&reason=')">EF</a>
		</security:oscarSec>
	<% 
		} 
	%>
		<caisi:isModuleLoad moduleName="caisi">
		<td class="name"><a href="#" onclick="location.href='<%= request.getContextPath() %>/PMmodule/ClientManager.do?id=<%=dem_no%>'"><%=Misc.toUpperLowerCase(demo.getLastName())%>, <%=Misc.toUpperLowerCase(demo.getFirstName())%></a></td>
		</caisi:isModuleLoad>
		<caisi:isModuleLoad moduleName="caisi" reverse="true">
		<td class="name"><%=Misc.toUpperLowerCase(demo.getLastName())%>, <%=Misc.toUpperLowerCase(demo.getFirstName())%></td>
		</caisi:isModuleLoad>
		<td class="chartNo"><%=demo.getChartNo()==null||demo.getChartNo().equals("")?"&nbsp;":demo.getChartNo()%></td>
		<td class="sex"><%=demo.getSex()%></td>
		<td class="dob"><%=demo.getFormattedDob()%></td>
		<td class="doctor"><%=Misc.getShortStr(providerBean.getProperty(demo.getProviderNo() == null ? "" : demo.getProviderNo()),"_",12 )%></td>
		<td class="rosterStatus"><%=demo.getRosterStatus()==null||demo.getRosterStatus().equals("")?"&nbsp;":demo.getRosterStatus()%></td>
		<td class="patientStatus"><%=demo.getPatientStatus()==null||demo.getPatientStatus().equals("")?"&nbsp;":demo.getPatientStatus()%></td>
		<td class="phone"><%=demo.getPhone()==null||demo.getPhone().equals("")?"&nbsp;":(demo.getPhone().length()==10?(demo.getPhone().substring(0,3)+"-"+demo.getPhone().substring(3)):demo.getPhone())%></td>
	</tr>
	<%
		
	toggleLine = !toggleLine;
	nItems++; //to calculate if it is the end of records
		}
	}
%>
</table>
<%

  
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit)+Integer.parseInt(strOffset);
  nLastPage=Integer.parseInt(strOffset)-Integer.parseInt(strLimit);
  if(nLastPage>=0) {
%> 
	<a href="demographiccontrol.jsp?keyword=<%=keyword%>&search_mode=<%=searchMode%>&displaymode=<%=displayMode%>&dboperation=<%=dboperation%>&orderby=<%=orderBy%>&limit1=<%=nLastPage%>&limit2=<%=strLimit%>&ptstatus=<%=ptStatus%>&firstPageShowIntegratedResults=<%=firstPageShowIntegratedResults%><%=nLastPage==0 && firstPageShowIntegratedResults?"&includeIntegratedResults=true":""%>">
	<bean:message key="demographic.demographicsearchresults.btnLastPage" /></a> <%
  }
  if(nItems>=Integer.parseInt(strLimit)) {
      if (nLastPage>=0) {
	%> | <%    } %> 
	<a href="demographiccontrol.jsp?keyword=<%=keyword%>&search_mode=<%=searchMode%>&displaymode=<%=displayMode%>&dboperation=<%=dboperation%>&orderby=<%=orderBy%>&limit1=<%=nNextPage%>&limit2=<%=strLimit%>&ptstatus=<%=ptStatus%>&firstPageShowIntegratedResults=<%=firstPageShowIntegratedResults%>">
	<bean:message key="demographic.demographicsearchresults.btnNextPage" /></a>
<%
}
%>
<br> 
<div class="createNew">
<a href="demographicaddarecordhtm.jsp?search_mode=<%=searchMode%>&keyword=<%=keyWord%>" title="<bean:message key="demographic.search.btnCreateNewTitle" />">
<bean:message key="demographic.search.btnCreateNew" />
</a>

</div>

</div>

</body>
</html:html>
<%!

Boolean isLocal(MatchingDemographicTransferScore matchingDemographicTransferScore, List<Demographic> demoList) {
    String hin = matchingDemographicTransferScore.getDemographicTransfer().getHin(); 
    for( Demographic demo : demoList ) {
		
		if( hin != null && hin.equals(demo.getHin()) ) {
		    return true;
		}
    }
    
    return false;
    
}

List<Demographic> doSearch(DemographicDao demographicDao,String searchMode, String ptstatus, String keyword, int limit, int offset, String orderBy, String providerNo, boolean outOfDomain) {
	List<Demographic> demoList = null;  
	OscarProperties props = OscarProperties.getInstance();
	
	String pstatus = props.getProperty("inactive_statuses", "IN, DE, IC, ID, MO, FI");
	pstatus = pstatus.replaceAll("'","").replaceAll("\\s", "");
	List<String>stati = Arrays.asList(pstatus.split(","));
	
	

	if( "".equals(ptstatus) ) {
		if(searchMode.equals("search_name")) {
			demoList = demographicDao.searchDemographicByName(keyword, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_phone")) {
			demoList = demographicDao.searchDemographicByPhone(keyword, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_dob")) {
			demoList = demographicDao.searchDemographicByDOB(keyword, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_address")) {
			demoList = demographicDao.searchDemographicByAddress(keyword, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_hin")) {
			demoList = demographicDao.searchDemographicByHIN(keyword, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_chart_no")) {
			demoList = demographicDao.findDemographicByChartNo(keyword, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_demographic_no")) {
			demoList = demographicDao.findDemographicByDemographicNo(keyword, limit, offset,providerNo,outOfDomain);
		}
	}
	else if( "active".equals(ptstatus) ) {
	    if(searchMode.equals("search_name")) {
			demoList = demographicDao.searchDemographicByNameAndNotStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
	    else if(searchMode.equals("search_phone")) {
			demoList = demographicDao.searchDemographicByPhoneAndNotStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_dob")) {
			demoList = demographicDao.searchDemographicByDOBAndNotStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_address")) {
			demoList = demographicDao.searchDemographicByAddressAndNotStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_hin")) {
			demoList = demographicDao.searchDemographicByHINAndNotStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_chart_no")) {
			demoList = demographicDao.findDemographicByChartNoAndNotStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_demographic_no")) {
			demoList = demographicDao.findDemographicByDemographicNoAndNotStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
	}
	else if( "inactive".equals(ptstatus) ) {
	    if(searchMode.equals("search_name")) {
			demoList = demographicDao.searchDemographicByNameAndStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
	    else if(searchMode.equals("search_phone")) {
			demoList = demographicDao.searchDemographicByPhoneAndStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_dob")) {
			demoList = demographicDao.searchDemographicByDOBAndStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_address")) {
			demoList = demographicDao.searchDemographicByAddressAndStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_hin")) {
			demoList = demographicDao.searchDemographicByHINAndStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_chart_no")) {
			demoList = demographicDao.findDemographicByChartNoAndStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
		else if(searchMode.equals("search_demographic_no")) {
			demoList = demographicDao.findDemographicByDemographicNoAndStatus(keyword, stati, limit, offset,providerNo,outOfDomain);
		}
	}

	
	return demoList;
}
%>
