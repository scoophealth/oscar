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
<%@page import="org.oscarehr.common.IsPropertiesOn"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_search" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_search");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

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
<%@page import="org.oscarehr.common.dao.OscarLogDao"%>
<%@page import="org.oscarehr.managers.DemographicManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicTransfer"%>
<%@page import="org.oscarehr.caisi_integrator.ws.MatchingDemographicTransferScore"%>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.managers.ProgramManager2"%>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<%
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

	AdmissionManager admissionManager = SpringUtils.getBean(AdmissionManager.class);
	ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
	List<ProgramProvider> ppList1 = programManager2.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
	ProgramProvider currentProgramInDomain = programManager2.getCurrentProgramInDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
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
	
	
	/* OSCAREMR-6243 */
	function popup9(vheight, vwidth, varpage,flag) {
		alert(flag);
		var page = varpage;
		windowprops = "height="
				+ vheight
				+ ",width="
				+ vwidth
				+ ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
		
		if(flag) {
			var confirmed = confirm('The demographic does not match the program you are currently set to (<%=currentProgramInDomain.getProgram().getName()%>). Are you sure you want to access this file?')
			if(!confirmed) {
				return false;
			}
		}
		
	
		
		var popup = window.open(varpage, "<bean:message key="global.oscarRx"/>_________________$tag________________________________demosearch",	windowprops);
		if (popup != null) {
			if (popup.opener == null) {
				popup.opener = self;
			}
			popup.focus();
		}
	}

	function popupEChart9(vheight,vwidth,varpage,flag) { //open a new popup window
		  var page = "" + varpage;
		  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
		  
		  if(flag) {
				var confirmed = confirm('The demographic does not match the program you are currently set to (<%=currentProgramInDomain.getProgram().getName()%>). Are you sure you want to access this file?')
				if(!confirmed) {
					return false;
				}
			}
		  
		  var popup=window.open(page, "encounter", windowprops);
		  if (popup != null) {
		    if (popup.opener == null) {
		      popup.opener = self;
		    }
		    popup.focus();
		  }
		}
</SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/nhpup_1.1.js"></script>
<style>

abbr
{
        border-bottom: 1px dotted #666;
	cursor: help;
}

.tooltip
{
	position:absolute;
	background-color:#eeeefe;
	border: 1px solid #aaaaca;
	font-size: smaller;
	padding:4px;
	width: 160px;
	box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);
	-webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);	
}

</style>

<script>
jQuery(document).ready(function() {	
	
	/**
	 * store the value of and then remove the title attributes from the
	 * abbreviations (thus removing the default tooltip functionality of
         * the abbreviations)
	 */
	jQuery('abbr').each(function(){		
		
		jQuery(this).data('title',jQuery(this).attr('title'));
		jQuery(this).removeAttr('title');
	
	});

	
        /**
	 * when abbreviations are mouseover-ed show a tooltip with the data from the title attribute
	 */	
	jQuery('abbr').mouseover(function() {		
		
		// first remove all existing abbreviation tooltips
		jQuery('abbr').next('.tooltip').remove();
		
		var demographic_no = jQuery(this).data('title');
		
		var data = "<img src='../images/ui-anim_basic_16x16.gif' border='0'/>";
		// create the tooltip
		jQuery(this).after('<span class="tooltip" id="temp_tooltip">' + data + '</span>');
		
		// position the tooltip 4 pixels above and 4 pixels to the left of the abbreviation
		var left = jQuery(this).position().left + jQuery(this).width() + 4;
		var top = jQuery(this).position().top - 4;
		jQuery(this).next().css('left',left);
		jQuery(this).next().css('top',top);				
		
		//now that we've setup the window, let's call the ajax that will populate the span
        jQuery.getJSON("../Tickler.do?method=getTicklerSummaryForSearchPageTooltip&demographic_no="+demographic_no, {},
        	function(xml) {
        		if(xml instanceof Array) {
        			var msg = '';
        			for(var i=0;i<xml.length;i++) {
        				if(i>0) {
        					msg += "<hr/>";
        				}
        			//	console.log(JSON.stringify(xml[i]));
        				var t = xml[i].message;
        				if(t.length > 120) {
        					t = t.substring(0,120) + "...";
        				}
        				
        				if(xml[i].latestComment != null) {
        					t += "[<i>" + xml[i].latestComment + "</i>]";
        				}
        			
        				msg += "<b>"+xml[i].serviceDate + "</b> : " +t;
        			}
        			if(msg == '') {
        				msg = "<i>No ticklers found</i>";
        			}
        			jQuery("#temp_tooltip").html(msg);
        		}
        });
	});

	
	/**
	 * when abbreviations are clicked trigger their mouseover event then fade the tooltip
	 * (this is friendly to touch interfaces)
	 */
	jQuery('abbr').click(function(){
		
		jQuery(this).mouseover();
		
		// after a slight 2 second fade, fade out the tooltip for 1 second
		jQuery(this).next().animate({opacity: 0.9},{duration: 2000, complete: function(){
			jQuery(this).fadeOut(1000);
		}});
		
	});
	
	/**
	 * Remove the tooltip on abbreviation mouseout
	 */
	jQuery('abbr').mouseout(function(){
			
		jQuery(this).next('.tooltip').remove();				

	});	
	
});
</script>
</head>
	

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">

<div id="demographicSearch">
    <a href="#" onclick="showHideItem('demographicSearch');" id="cancelButton" class="leftButton top"> <bean:message key="global.btnCancel" /> </a>
	<%@ include file="zdemographicfulltitlesearch.jsp"%>
</div>


<div id="searchResults">
<a href="#" onclick="showHideItem('demographicSearch');" id="searchPopUpButton" class="rightButton top">Search</a>
<br>
<%if(request.getParameter("keyword")!=null && request.getParameter("keyword").length()==0) { %>
<i><bean:message key="demographic.demographicsearchresults.msgMostRecentPatients" /></i> :
<% } else {%> 
<i><bean:message key="demographic.demographicsearchresults.msgSearchKeys" /></i> : <%=request.getParameter("keyword")%>
<%}%>
    <table>
        <tr class="tableHeadings deep">
        
		<% if ( fromMessenger ) {%>
		<!-- leave blank -->
		                <td class="demoIdSearch">
                    <a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnDemoNo" /></a>
                </td>
		<%} else {%>
                <td class="demoIdSearch">
                    <a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnDemoNo" /></a>
                </td>
		<td class="links"><bean:message key="demographic.demographicsearchresults.module" /> <!-- b><a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit%>">Links<sup>*</sup></a></b --></td>

		<%}%>
		<td class="name"><a
                    href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=last_name&limit1=0&limit2=<%=strLimit%>"><bean:message
                    key="demographic.demographicsearchresults.btnDemoName"/></a>
                </td>
                
        <oscar:oscarPropertiesCheck property="searchresults.showAddress" value="true" defaultVal="false">
	        <td class="address">
	        	<bean:message key="oscarEncounter.search.demographicSearch.formAddr"/>
	        </td>
        </oscar:oscarPropertiesCheck>
                
		<td class="chartNo"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=chart_no&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnChart" /></a>
                </td>
		<td class="sex"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=sex&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnSex" /></a>
                </td>
		<td class="dob"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=dob&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnDOB" /> <span class="dateFormat"><bean:message key="demographic.demographicsearchresults.btnDOBFormat" /></span></a>
                </td>
		<td class="doctor"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=provider_no&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnDoctor" /></a>
                </td>
                <td class="rosterStatus"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=roster_status&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnRosSta" /></a>
                </td>
		<td class="patientStatus"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=patient_status&limit1=0&limit2=<%=strLimit%>"><bean:message
                        key="demographic.demographicsearchresults.btnPatSta" /></a>
                </td>
                <td class="phone"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=StringEscapeUtils.escapeHtml(request.getParameter("keyword"))%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=phone&limit1=0&limit2=<%=strLimit%>"><bean:message
			key="demographic.demographicsearchresults.btnPhone" /></a>
                </td>
                <td class="program"><bean:message key="demographic.demographicsearchresults.btnProgram" />
                </td>
	</tr>
	<%
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
        OscarLogDao oscarLogDao = (OscarLogDao)SpringUtils.getBean("oscarLogDao");
	CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");
	String providerNo = loggedInInfo.getLoggedInProviderNo();
	boolean outOfDomain = true;
	if(OscarProperties.getInstance().getProperty("ModuleNames","").indexOf("Caisi") != -1) {
		if(!"true".equals(OscarProperties.getInstance().getProperty("pmm.client.search.outside.of.domain.enabled","true"))) {
			outOfDomain=false;
		}
		if(request.getParameter("outofdomain")!=null && request.getParameter("outofdomain").equals("true")) {
			outOfDomain=true;
		}
	}
	
	

	if (searchMode == null)
		searchMode = "search_name";
	if (orderBy == null)
		orderBy = "last_name";
	
	
	List<Demographic> demoList = null;
      if(request.getParameter("keyword")!=null && request.getParameter("keyword").length()==0) {
            int mostRecentPatientListSize=Integer.parseInt(OscarProperties.getInstance().getProperty("MOST_RECENT_PATIENT_LIST_SIZE","3"));
            List<Integer> results = oscarLogDao.getRecentDemographicsAccessedByProvider(providerNo,  0, mostRecentPatientListSize);
            demoList = new ArrayList<Demographic>();
            for(Integer r : results) {
                demoList.add(demographicDao.getDemographicById(r));
            }
        } else {

        	//there's a list of searchMode/keyword doubles
        	List<String> searchModes = new ArrayList<String>();
        	List<String> keywords = new ArrayList<String>();
        	searchModes.add(searchMode);
			keywords.add(keyword);
			
			String strMax = request.getParameter("max_search_clause");
			int max = 1;
			try {
				max = Integer.parseInt(strMax);
			}catch(NumberFormatException e) {
				
			}
			
			
        	for(int x=2;x<=max;x++) {
        		String isActive = request.getParameter("search_" + x);
            	if(isActive != null && "true".equals(isActive)) {
            		String searchModeX = request.getParameter("search_mode_" + x);
    				String keywordX = request.getParameter("keyword_"+x);
    				String programKeywordX = request.getParameter("programKeyword_"+x);
    				
    				if("search_program_no".equals(searchModeX)) {
    					if(programKeywordX != null && !programKeywordX.equals("")) {
        					searchModes.add(searchModeX);
        					keywords.add(programKeywordX);
        				}
    				} else {
	    				if(keywordX != null && !keywordX.equals("")) {
	    					searchModes.add(searchModeX);
	    					keywords.add(keywordX);
	    				}
    				}
            	}
        	}
            demoList = doSearch(loggedInInfo, demographicDao,searchModes,ptstatus,keywords,limit,offset,orderBy,providerNo,outOfDomain);	
        }	
	
        
       

        
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
				   <oscar:oscarPropertiesCheck property="searchresults.showAddress" value="true" defaultVal="false">				   
				   	<td class="address"></td>
				   </oscar:oscarPropertiesCheck>
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
					<td></td>
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
			
			
			//OSCAREMR-6243
			//we have the variable ppList1 which is the program domain for the user.
			//currentProgramInDomain
			List<Admission> currentAdmissions = admissionManager.getCurrentAdmissions(demo.getDemographicNo());
			
			//the rule is that if my current program is not a program this patient is in, then give a warning
			boolean showProgramWarning = true;
			for(Admission a:currentAdmissions) {
				if(a.getProgramId().equals(currentProgramInDomain.getProgramId().intValue())) {
					showProgramWarning = false;
					break;
				}
			}
			

%>
	<tr class="<%=toggleLine?"even":"odd"%>">
	<td class="demoIdSearch">
	<%

		if (fromMessenger) {
	%>
		<a href="demographiccontrol.jsp?keyword=<%=URLEncoder.encode(Misc.toUpperLowerCase(demo.getLastName()+", "+demo.getFirstName()))%>&demographic_no=<%= dem_no %>&displaymode=linkMsg2Demo&dboperation=search_detail" ><%=demo.getDemographicNo()%></a></td>
	<%	
		} else { 
	%>
		<a title="Master Demographic File" href="#"  onclick="popup9(700,1027,'demographiccontrol.jsp?demographic_no=<%=head%>&displaymode=edit&dboperation=search_detail',<%=showProgramWarning %>)" ><%=dem_no%></a></td>
	
		<!-- Rights -->
		<td class="links"><security:oscarSec roleName="<%=roleName$%>"
			objectName="_eChart" rights="r">
			<a class="encounterBtn" title="Encounter" href="#"
				onclick="popupEChart9(710,1024,'<c:out value="${ctx}"/>/oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=dem_no%>&curProviderNo=&reason=<%=URLEncoder.encode(noteReason)%>&encType=&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=','<%=showProgramWarning %>');return false;">E</a>
		</security:oscarSec> <!-- Rights --> <security:oscarSec roleName="<%=roleName$%>"
			objectName="_rx" rights="r">
			<a class="rxBtn" title="Prescriptions" href="#" onclick="popup9(700,1027,'../oscarRx/choosePatient.do?providerNo=<%=demo.getProviderNo()%>&demographicNo=<%=dem_no%>',<%=showProgramWarning %>)">Rx</a>
		</security:oscarSec>
		
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_tickler" rights="r">
			<span>
			<abbr title="<%=dem_no%>">
				<a class="rxBtn" href="#" onclick="popup9(700,1027,'../Tickler.do?filter.demographicNo=<%=dem_no%>',<%=showProgramWarning%>)">T</a>
			</abbr>
			</span>
		</security:oscarSec>
		
		
		</td>

	<%	
		}
		if (OscarProperties.getInstance().isPropertyActive("new_eyeform_enabled")) { 
	%>
		<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r">
			<a title="Eyeform" href="#" onclick="popup(800, 1280, '../eyeform/eyeform.jsp?demographic_no=<%=dem_no %>&reason=')">EF</a>
		</security:oscarSec>
	<% 
		}
		
		String address = demo.getAddress() + "<br/>" + demo.getCity() + "," + demo.getProvince() + "<br/>" + demo.getPostal();
	%>
		<caisi:isModuleLoad moduleName="caisi">
		<td class="name"><a href="#" onclick="location.href='<%= request.getContextPath() %>/PMmodule/ClientManager.do?id=<%=dem_no%>'"><%=Misc.toUpperLowerCase(demo.getLastName())%>, <%=Misc.toUpperLowerCase(demo.getFirstName())%></a></td>
		</caisi:isModuleLoad>
		<caisi:isModuleLoad moduleName="caisi" reverse="true">
		<td class="name"><%=Misc.toUpperLowerCase(demo.getLastName())%>, <%=Misc.toUpperLowerCase(demo.getFirstName())%></td>
		</caisi:isModuleLoad>
		<oscar:oscarPropertiesCheck property="searchresults.showAddress" value="true" defaultVal="false">
			<td class="address"><%=address%></td>
		</oscar:oscarPropertiesCheck>
		<td class="chartNo"><%=demo.getChartNo()==null||demo.getChartNo().equals("")?"&nbsp;":demo.getChartNo()%></td>
		<td class="sex"><%=demo.getSex()%></td>
		<td class="dob"><%=demo.getFormattedDob()%></td>
		<td class="doctor"><%=Misc.getShortStr(providerBean.getProperty(demo.getProviderNo() == null ? "" : demo.getProviderNo()),"_",12 )%></td>
		<td class="rosterStatus"><%=demo.getRosterStatus()==null||demo.getRosterStatus().equals("")?"&nbsp;":demo.getRosterStatus()%></td>
		<td class="patientStatus"><%=demo.getPatientStatus()==null||demo.getPatientStatus().equals("")?"&nbsp;":demo.getPatientStatus()%></td>
		<td class="phone"><%=demo.getPhone()==null||demo.getPhone().equals("")?"&nbsp;":(demo.getPhone().length()==10?(demo.getPhone().substring(0,3)+"-"+demo.getPhone().substring(3)):demo.getPhone())%></td>
		<td class="program">
			<ul>
			<%
			for(Admission a: currentAdmissions) {
				if(isInList(ppList1,a.getProgramId())) {
				%><li><%=a.getProgram().getName()%></li><%
				}
			}
			%>
			</ul>
		</td>
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
	<a href="demographiccontrol.jsp?keyword=<%=URLEncoder.encode(keyword,"UTF-8")%>&search_mode=<%=searchMode%>&displaymode=<%=displayMode%>&dboperation=<%=dboperation%>&orderby=<%=orderBy%>&limit1=<%=nLastPage%>&limit2=<%=strLimit%>&ptstatus=<%=ptStatus%>&firstPageShowIntegratedResults=<%=firstPageShowIntegratedResults%><%=nLastPage==0 && firstPageShowIntegratedResults?"&includeIntegratedResults=true":""%>">
	<bean:message key="demographic.demographicsearchresults.btnLastPage" /></a> <%
  }
  if(nItems>=Integer.parseInt(strLimit)) {
      if (nLastPage>=0) {
	%> | <%    } %> 
	<a href="demographiccontrol.jsp?keyword=<%=URLEncoder.encode(keyword,"UTF-8")%>&search_mode=<%=searchMode%>&displaymode=<%=displayMode%>&dboperation=<%=dboperation%>&orderby=<%=orderBy%>&limit1=<%=nNextPage%>&limit2=<%=strLimit%>&ptstatus=<%=ptStatus%>&firstPageShowIntegratedResults=<%=firstPageShowIntegratedResults%>">
	<bean:message key="demographic.demographicsearchresults.btnNextPage" /></a>
<%
}
%>
<br> 
<div class="createNew">
<a href="demographicaddarecordhtm.jsp?search_mode=<%=searchMode%>&keyword=<%=StringEscapeUtils.escapeHtml(keyWord)%>" title="<bean:message key="demographic.search.btnCreateNewTitle" />">
<bean:message key="demographic.search.btnCreateNew" />
</a>
</div>

<div class="goBackToSchedule">
<a href="../provider/providercontrol.jsp" title="<bean:message key="demographic.search.btnReturnToSchedule" />">
<bean:message key="demographic.search.btnReturnToSchedule" />
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

List<Demographic> doSearch(LoggedInInfo loggedInInfo, DemographicDao demographicDao,List<String> searchModes, String ptstatus, List<String> keywords, int limit, int offset, String orderBy, String providerNo, boolean outOfDomain) {
	
	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	
	List<Demographic> demoList = null;  

	boolean active = ("".equals(ptstatus)) || ( "active".equals(ptstatus) );
	boolean inactive = ("".equals(ptstatus)) || ( "inactive".equals(ptstatus) );

	demoList = demographicManager.doMultiSearch(loggedInInfo, searchModes, keywords, limit, offset, providerNo, outOfDomain, active, inactive);

	return demoList;
}


boolean isInList(List<ProgramProvider> l, Integer programNo) {
	for(ProgramProvider pp:l) {
		if(pp.getProgramId().intValue() == programNo.intValue()) {
			return true;
		}
	}
	return false;
}
%>
