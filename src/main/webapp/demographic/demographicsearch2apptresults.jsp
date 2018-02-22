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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

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

<%@ page import="java.util.*, java.sql.*,java.net.*, oscar.*" errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="oscar.oscarDemographic.data.DemographicMerged" %>
<%@page import="org.oscarehr.common.dao.OscarLogDao"%>


<%@page import="org.oscarehr.managers.ProgramManager2" %>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@page import="org.oscarehr.PMmodule.model.Program" %>




<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<% 
	Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;
	
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	
	String curProvider_no = request.getParameter("provider_no");
	    
	String keyword = request.getParameter("keyword");
	String searchMode = request.getParameter("search_mode");
	
	String strLimit1="0";
	String strLimit2="10";
	if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
	if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
	
	int offset = Integer.parseInt(strLimit1);
	int limit = Integer.parseInt(strLimit2);
	boolean caisi = Boolean.valueOf(request.getParameter("caisi")).booleanValue();

	OscarProperties props = OscarProperties.getInstance();

	List<Demographic> demoList = null;  
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	OscarLogDao oscarLogDao = (OscarLogDao)SpringUtils.getBean("oscarLogDao");
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
	
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="demographic.demographicsearch2apptresults.title" />(demographicsearch2apptresults)</title>

<% 
	if (isMobileOptimized) { 
%>
   <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
   <link rel="stylesheet" type="text/css" href="../mobile/searchdemographicstyle.css">
<% 
	} else { 
%>
   <link rel="stylesheet" type="text/css" href="../share/css/searchBox.css" />
   <link rel="stylesheet" type="text/css" media="all" href="../demographic/searchdemographicstyle.css"  />
<% 
	} 
%>
<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script language="JavaScript">
function setfocus() {
  this.focus();
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}
function showHideItem(id){
    if(document.getElementById(id).style.display == 'inline')
        document.getElementById(id).style.display = 'none';
    else
        document.getElementById(id).style.display = 'inline';
}
function checkTypeIn() {  
  var dob = document.titlesearch.keyword;
  
  if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18){                  
     document.titlesearch.keyword.value = dob.value.substring(8,18);
     document.titlesearch.search_mode[4].checked = true;                  
  }
  
  if(document.titlesearch.search_mode[2].checked) {
    if(dob.value.length==8) {
      dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
    }
    if(dob.value.length != 10) {
      alert("<bean:message key="demographic.demographicsearch2apptresults.msgWrongDOB"/>");
      return false;
    } else {
      return true;
    }
  } else {
    return true;
  }
}

function searchInactive() {
    document.titlesearch.ptstatus.value="inactive"
    if (checkTypeIn()) document.forms[0].submit()
}

function searchAll() {
    document.titlesearch.ptstatus.value=""
    if (checkTypeIn()) document.forms[0].submit()
}



function getNextExtraClauseId() {
	for(var x=2;x<=12;x++) {
		var el = document.getElementById("search_"+x);
		if(el == null) {
			return x;
		}
	}
	return null;
}

function getExtraClauseData(x) {
	var data = 
		"<ul id=\"extraSearchClause"+x+"\" >" + 
	    "<li>" +
	    "    <div class=\"label\">" +
	    "</div>" +
	    " 	<input type=\"hidden\" name=\"search_"+x+"\" id=\"search_"+x+"\" value=\"true\"/>" +
	    "" 	+
	    " 	<img src=\"../images/icons/101.png\" border=\"0\" onClick=\"removeThisSearchClause("+x+")\"/>" +
	    "     <select class=\"wideInput\" name=\"search_mode_"+x+"\" id=\"search_mode_"+x+"\" onChange=\"updateKeywordField("+x+")\">"+
	    "        <option value=\"search_name\">"+
	    "            Name"+
	    "        </option>"+
	    "        <option value=\"search_phone\">"+
	    "            Phone"+
	    "        </option>"+
	    "        <option value=\"search_dob\">"+
	    "            DOB yyyy-mm-dd"+
	    "        </option>"+
	    "        <option value=\"search_address\" >"+
	    "            Address"+
	    "        </option>"+
	    "        <option value=\"search_hin\">"+
	    "            Health Ins #"+
	    "        </option>"+
	    "        <option value=\"search_chart_no\">"+
	    "            Chart No"+
	    "        </option>"+
	    "        <option value=\"search_demographic_no\">"+
	    "            Demographic No"+
	    "       </option>"+
	    "		<option value=\"search_program_no\">"+
	    "            Program"+
	    "       </option>"+
	    "     </select>"+
	    "</li>"+
	    "<li>"+
	    "    <div class=\"label\">"+
	    "    </div>"+
	    "    <span id=\"keyword_span_"+x+"\"><input class=\"wideInput\" type=\"text\" NAME=\"keyword_"+x+"\" id=\"keyword_"+x+"\" VALUE=\"\" SIZE=\"17\" MAXLENGTH=\"100\"></span>"+
	    "	<span id=\"program_select_span_"+x+"\" style=\"display:none\">" +
	    "	<select name=\"programKeyword_"+x+"\" id=\"programKeyword_"+x+"\"></select>" +
	    "	</span>" +
	    "</li>"+
	"</ul>";
	
	return data;
}
function updateKeywordField(idx) {
	//get the search_type for the {idx}
	//if it's search_program_no, then change to the select, otherwise change to textbox
	
	var blah = $("#search_mode_" + idx).val();
	
	if("search_program_no" == blah) {
		$("#program_select_span_" + idx).show();
		$("#keyword_span_" + idx).hide();
	} else {
		$("#program_select_span_" + idx).hide();
		$("#keyword_span_" + idx).show();
	}
}

function addExtraSearchClause() {
	
	var iterID = getNextExtraClauseId();
	
	if(iterID != null) {
		var data = getExtraClauseData(iterID);
		//console.log(data);
		$("#extraSearchClauses").append(data);
		
		//copy the options for the program list
		var $options = $("#programKeyword_0 > option").clone();
		$('#programKeyword_' + iterID).append($options);
		
		var max = parseInt($("#max_search_clause").val());
		if(parseInt(iterID) > max) {
			$("#max_search_clause").val(iterID);
		}
	}
	
	return iterID;
}

function removeThisSearchClause(v) {
	$("#extraSearchClause" + v).remove();
	console.log($("#f").serialize());
}

$(document).ready(function(){
	updateKeywordField(0);
	
	<%
	String strMax1 = request.getParameter("max_search_clause");
	int max1 = 1;
	try {
		max1 = Integer.parseInt(strMax1);
	}catch(NumberFormatException e) {
		
	}
	
	
	for(int x=2;x<=max1;x++) {
		String isActive = request.getParameter("search_" + x);
		if(isActive != null && "true".equals(isActive)) {
			String searchModeX = request.getParameter("search_mode_" + x);
			String keywordX = request.getParameter("keyword_"+x);
			String programKeywordX = request.getParameter("programKeyword_" + x);
			
			if((keywordX != null && !keywordX.equals("")) || ("search_program_no".equals(searchModeX) && programKeywordX != null && !programKeywordX.equals(""))) {
				%>
					//console.log('adding search clause ' + x);
					var iterID = addExtraSearchClause();
					$("#search_mode_" + iterID).val('<%=searchModeX%>');
					$("#keyword_" + iterID).val('<%=keywordX%>');
					$("#programKeyword_" + iterID).val('<%=programKeywordX%>');
					updateKeywordField(iterID);
					//console.log('done adding search clause ' + x);
					
				<%
			}
		}
	}
%>
	

});
</script>
</head>
<body bgcolor="white" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
<div id="demographicSearch" class="searchBox">
	<form id="f" method="post" name="titlesearch" action="../demographic/demographiccontrol.jsp" onSubmit="return checkTypeIn()">
	<%--@ include file="zdemographictitlesearch.htm"--%>
    <div class="header deep">
        <div class="title"></div>  
    </div>
    <ul>
        <li>
            <div class="label">
            </div>
            <input type="hidden" name="max_search_clause" id="max_search_clause" value="1"/>
     	
     		<img id="addBtn" src="../images/icons/103.png" border="0" onClick="addExtraSearchClause()"/>
            <select class="wideInput" name="search_mode" id="search_mode_0" onChange="updateKeywordField(0)">
                <option value="search_name" <%=request.getParameter("search_mode").equals("search_name")?"selected":""%>>
					<bean:message key="demographic.demographicsearch2apptresults.optName" />
				</option>
				<option value="search_phone" <%=request.getParameter("search_mode").equals("search_phone")?"selected":""%>>
                    <bean:message key="demographic.demographicsearch2apptresults.optPhone" />
                </option>
				<option value="search_dob" <%=request.getParameter("search_mode").equals("search_dob")?"selected":""%>>
                    <bean:message key="demographic.demographicsearch2apptresults.optDOB" />
                </option>
                <option value="search_address" <%=request.getParameter("search_mode").equals("search_address")?"selected":""%>>
                    <bean:message key="demographic.demographicsearch2apptresults.optAddress" />
                </option>
				<option value="search_hin" <%=request.getParameter("search_mode").equals("search_hin")?"selected":""%>>
                    <bean:message key="demographic.demographicsearch2apptresults.optHIN" />
                </option>
                <option value="search_chart_no" <%=request.getParameter("search_mode").equals("search_chart_no")?"selected":""%>>
                    <bean:message key="demographic.demographicsearch2apptresults.optChart"/>
                </option>
                <option value="search_program_no" <%=searchMode.equals("search_program_no")?"selected":""%>>
                	<bean:message key="demographic.zdemographicfulltitlesearch.formProgram" />
            	</option>
            </select>
        </li>
        <li>
            <div class="label"> </div>
            <span id="keyword_span_0">
            <input type="text" class="wideInput" NAME="keyword" VALUE="<%=request.getParameter("keyword")%>" SIZE="17" MAXLENGTH="100"/>
            </span>
			<span id="program_select_span_0" style="display:none">
        <select name="programKeyword_0" id="programKeyword_0">
        	<%
        		//get list of programs
        		ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
        		List<ProgramProvider> ppList = programManager.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
        		for(ProgramProvider pp:ppList) {
        			Program p = programManager.getProgram(loggedInInfo, pp.getProgramId().intValue());
        			//do we have one set?
        			String strProgKeyword = request.getParameter("programKeyword_0" );
        			int var = -1;
        			if(strProgKeyword != null) {
        				var = Integer.parseInt(strProgKeyword);
        			}
        			String selected = (var >= 0 && var == p.getId().intValue())?" selected=\"selected\" ":"";
        	%> <option value="<%=p.getId()%>" <%=selected%>><%=p.getName() %></option>  <%
        		}
        	%>
        </select>
        </span>            
        </li>
        <li>
	<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name, first_name">
        <INPUT TYPE="hidden" NAME="dboperation" VALUE="search_titlename">
        <INPUT TYPE="hidden" NAME="limit1" VALUE="0">
        <INPUT TYPE="hidden" NAME="limit2" VALUE="5">
        <input type="hidden" name="displaymode" value="Search ">
        <INPUT TYPE="hidden" NAME="ptstatus" VALUE="active">
        
        <input type="hidden" name="fromAppt" value="<%=request.getParameter("fromAppt")%>">
		<input type="hidden" name="originalPage" value="<%=request.getParameter("originalPage")%>">
		<input type="hidden" name="bFirstDisp" value="<%=request.getParameter("bFirstDisp")%>">
		<input type="hidden" name="provider_no" value="<%=request.getParameter("provider_no")%>">
		<input type="hidden" name="start_time" value="<%=request.getParameter("start_time")%>">
		<input type="hidden" name="end_time" value="<%=request.getParameter("end_time")%>">
		<input type="hidden" name="year" value="<%=request.getParameter("year")%>">
		<input type="hidden" name="month" value="<%=request.getParameter("month")%>">
		<input type="hidden" name="day" value="<%=request.getParameter("day")%>">
		<input type="hidden" name="appointment_date" value="<%=request.getParameter("appointment_date")%>">
		<input type="hidden" name="notes" value="<%=request.getParameter("notes")%>">
		<input type="hidden" name="reason" value="<%=request.getParameter("reason")%>">
		<input type="hidden" name="location" value="<%=request.getParameter("location")%>">
		<input type="hidden" name="resources" value="<%=request.getParameter("resources")%>">
		<input type="hidden" name="type" value="<%=request.getParameter("type")%>">
		<input type="hidden" name="style" value="<%=request.getParameter("style")%>">
		<input type="hidden" name="billing" value="<%=request.getParameter("billing")%>">
		<input type="hidden" name="status" value="<%=request.getParameter("status")%>">
		<input type="hidden" name="createdatetime" value="<%=request.getParameter("createdatetime")%>">
		<input type="hidden" name="creator" value="<%=request.getParameter("creator")%>">
		<input type="hidden" name="remarks" value="<%=request.getParameter("remarks")%>">
		    
		        
<%
	String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("keyword") || temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("search_mode") ||temp.equals("chart_no")  ||temp.equals("ptstatus") ||temp.equals("submit") || temp.equals("includeIntegratedResults")) continue;
		if(temp.matches("search_[1-9]+") || temp.matches("search_mode_[1-9]+") ||  temp.matches("keyword_[1-9]+") ||temp.matches("programKeyword_[1-9]+") ) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
  }
%>
       <a href="#" onclick="showHideItem('demographicSearch');" id="cancelButton" class="leftButton top"><bean:message key="global.btnCancel" /></a>
       <input type="SUBMIT" class="rightButton blueButton top" name="displaymode"
           value='<bean:message key="global.search"/>' size="17"
           title='<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchActive"/>'>&nbsp;&nbsp;
       <INPUT TYPE="button" id="inactiveButton"
           onclick="searchInactive();"
           TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchInactive"/>"
           VALUE="<bean:message key="demographic.search.Inactive"/>">
       <INPUT TYPE="button" id="allButton"
           onclick="searchAll();"
           TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchAll"/>"
           VALUE="<bean:message key="demographic.search.All"/>">
<%
	if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()){
%>
    	<input type="checkbox" name="includeIntegratedResults" value="true"   <%="true".equals(request.getParameter("includeIntegratedResults"))?"checked":""%>/> <span style="font-size:small"><bean:message key="demographic.search.msgInclIntegratedResults"/></span>
<%	} %>
   </li>
<% if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()){%>
        <li>
        	<jsp:include page="../admin/IntegratorStatus.jspf"></jsp:include>
        </li>
<%	} %>
    </ul>
    <div id="extraSearchClauses">



</div>
	</form>
</div>



<div id="searchResults">
    <div class="header deep">
        <div class="title"><bean:message key="demographic.demographicsearch2apptresults.patientsRecord" />
        </div>
    </div>
<table width="95%" border="0">
	<tr>
            <td align="left">
                    <%if(request.getParameter("keyword")!=null && request.getParameter("keyword").length()==0) { %>
                    <bean:message key="demographic.demographicsearch2apptresults.msgMostRecentPatients"/>
                    <% } else { %>
                    <bean:message key="demographic.demographicsearch2apptresults.msgKeywords" /> <%=request.getParameter("keyword")%> <%}%></td>
	</tr>
</table>

<script language="JavaScript">

var fullname="";
<%-- RJ 07/10/2006 Need to pass doctor of patient back to referrer --%>
function addName(demographic_no, lastname, firstname, chartno, messageID, doctorNo, remoteFacilityId) {  
  fullname=lastname+","+firstname;

   if (remoteFacilityId == '')
   {
	   document.addform.action="<%=request.getParameter("originalpage")%>?";
   }
   else
   {
	   document.addform.action="<%=request.getContextPath()%>/appointment/copyRemoteDemographic.jsp?originalPage=<%=URLEncoder.encode(request.getParameter("originalpage"))%>&";	  
   }	  
  
  document.addform.action=document.addform.action+"demographic_no="+demographic_no+"&name="+fullname+"&chart_no="+chartno+"&bFirstDisp=false"+"&messageID="+messageID+"&doctor_no="+doctorNo+"&remoteFacilityId="+remoteFacilityId; 
  
  document.addform.submit();
  return true;
}

<%if(caisi) {%>
function addNameCaisi(demographic_no,lastname,firstname,chartno,messageID) {
  	fullname=lastname+","+firstname;
  	if(opener.document.<%=request.getParameter("formName")%>!=null){
      if(opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementName")%>']!=null)
    	 opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementName")%>'].value=fullname;
	  if(opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementId")%>']!=null)
  	     opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementId")%>'].value=demographic_no;
	}
	self.close();
}
<%}%>
</script>




	<form method="post" name="addform" action="../appointment/addappointment.jsp">
        
<table>
        <tr class="tableHeadings deep">
        

		<td class="demoIdSearch">
		<bean:message key="demographic.demographicsearch2apptresults.demographicId" />
       </td>

		<td class="lastname">
		<bean:message key="demographic.demographicsearch2apptresults.lastName" />
                </td>
		<td class="firstname">
		<bean:message key="demographic.demographicsearch2apptresults.firstName" />
                </td>
		<td class="age">
		<bean:message key="demographic.demographicsearch2apptresults.age" />
                </td>
		<td class="rosterStatus">
		<bean:message key="demographic.demographicsearch2apptresults.rosterStatus" />
                </td>
		<td class="sex">
		<bean:message key="demographic.demographicsearch2apptresults.sex" />
                </td>
        <td class="dob">
        <bean:message key="demographic.demographicsearch2apptresults.DOB" />
                </td>
		<td class="doctor">
		<bean:message key="demographic.demographicsearch2apptresults.doctor" />
                </td>
	</tr>


<%
	String ptstatus = request.getParameter("ptstatus") == null ? "active" : request.getParameter("ptstatus");
	org.oscarehr.util.MiscUtils.getLogger().debug("PSTATUS " + ptstatus);

	int rowCounter=0;
	String bgColor = rowCounter%2==0?"#EEEEFF":"white";

	String pstatus = props.getProperty("inactive_statuses", "IN, DE, IC, ID, MO, FI");
	pstatus = pstatus.replaceAll("'","").replaceAll("\\s", "");
	List<String>stati = Arrays.asList(pstatus.split(","));
        
        if(request.getParameter("keyword")!=null && request.getParameter("keyword").length()==0) {
            int mostRecentPatientListSize=Integer.parseInt(OscarProperties.getInstance().getProperty("MOST_RECENT_PATIENT_LIST_SIZE","3"));
            List<Integer> results = oscarLogDao.getRecentDemographicsAccessedByProvider(providerNo,  0, mostRecentPatientListSize);
            demoList = new ArrayList<Demographic>();
            for(Integer r:results) {
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
			String orderBy = "last_name";
            demoList = doSearch(demographicDao,searchModes,ptstatus,keywords,limit,offset,orderBy,providerNo,outOfDomain);	
        }
	
	if(demoList == null) {
	    //out.println("failed!!!");
	} 
	else {
		Collections.sort(demoList, Demographic.LastNameComparator);
		
		DemographicMerged dmDAO = new DemographicMerged();
	
		for(Demographic demo : demoList) {

			String dem_no = demo.getDemographicNo().toString();
			String head = dmDAO.getHead(dem_no);
			
			if (head != null && !head.equals(dem_no)) {
				//skip non head records
				continue;
			}
			
			rowCounter++;
			bgColor = rowCounter%2==0?"#EEEEFF":"white";

%>
<tr style="background-color: <%=bgColor%>" onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';" onMouseout="this.style.backgroundColor='<%=bgColor%>';"
		onClick="document.forms[0].demographic_no.value=<%=demo.getDemographicNo()%>;<% if(caisi) { out.print("addNameCaisi");} else { out.print("addName");} %>('<%=demo.getDemographicNo()%>','<%=URLEncoder.encode(demo.getLastName())%>','<%=URLEncoder.encode(demo.getFirstName())%>','<%=URLEncoder.encode(demo.getChartNo() == null ? "" : demo.getChartNo())%>','<%=request.getParameter("messageId")%>','<%=demo.getProviderNo()%>','')">

		<td class="demoId"><input type="submit" class="mbttn" name="demographic_no" value="<%=demo.getDemographicNo()%>"
			onClick="<% if(caisi) {out.print("addNameCaisi");} else {out.print("addName");} %>('<%=demo.getDemographicNo()%>','<%=URLEncoder.encode(demo.getLastName())%>','<%=URLEncoder.encode(demo.getFirstName())%>','<%=URLEncoder.encode(demo.getChartNo() == null ? "" : demo.getChartNo())%>','<%=request.getParameter("messageId")%>','<%=demo.getProviderNo()%>','')">
        </td>
		<td class="lastName"><%=Misc.toUpperLowerCase(demo.getLastName())%></td>
		<td class="firstName"><%=Misc.toUpperLowerCase(demo.getFirstName())%></td>
		<td class="age"><%=demo.getAge()%></td>
		<td class="rosterStatus"><%=demo.getRosterStatus()==null||demo.getRosterStatus().equals("")?"&nbsp;":demo.getRosterStatus()%></td>
		<td class="sex"><%=demo.getSex()%></td>
		<td class="dob"><%=demo.getYearOfBirth() + "-" + demo.getMonthOfBirth() + "-" + demo.getDateOfBirth()%></td>
        <td class="doctor"><%=providerBean.getProperty(demo.getProviderNo()==null?"":demo.getProviderNo())==null?"":providerBean.getProperty(demo.getProviderNo())%></td>
        </tr>

<%
    }
  }

  @SuppressWarnings("unchecked")
  List<MatchingDemographicTransferScore> integratorSearchResults=(List<MatchingDemographicTransferScore>)request.getAttribute("integratorSearchResults");
  if (integratorSearchResults!=null) {
	  for (MatchingDemographicTransferScore matchingDemographicTransferScore : integratorSearchResults) {
	      if( isLocal(matchingDemographicTransferScore, demoList)) {
		  	continue;
	      }
		  rowCounter++;
		  bgColor = rowCounter%2==0?"#EEEEFF":"white";
		  DemographicTransfer demographicTransfer=matchingDemographicTransferScore.getDemographicTransfer();
%>
		   <tr style="background-color: <%=bgColor%>" onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';" onMouseout="this.style.backgroundColor='<%=bgColor%>';"
			   onClick="document.forms[0].demographic_no.value=<%=demographicTransfer.getCaisiDemographicId()%>;addName('<%=demographicTransfer.getCaisiDemographicId()%>','<%=URLEncoder.encode(demographicTransfer.getLastName())%>','<%=URLEncoder.encode(demographicTransfer.getFirstName())%>','','<%=request.getParameter("messageId")%>','<%=demographicTransfer.getCaisiProviderId()%>','<%=demographicTransfer.getIntegratorFacilityId()%>')">
			<td class="demoId" colspan="8">
				<input type="submit" class="mbttn" name="demographic_no" value="Integrator <%=CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(), demographicTransfer.getIntegratorFacilityId()).getName()%>:<%=demographicTransfer.getCaisiDemographicId()%>" />
            </td>
			<td class="lastName"><%=Misc.toUpperLowerCase(demographicTransfer.getLastName())%></td>
			<td class="firstName"><%=Misc.toUpperLowerCase(demographicTransfer.getFirstName())%></td>
<%
		String ageString="";
		String bdayString="";
	
		if (demographicTransfer.getBirthDate()!=null) {
			Integer ageX=DateUtils.getAge(demographicTransfer.getBirthDate(), new GregorianCalendar());
			ageString=ageX.toString();
			
			bdayString=DateFormatUtils.ISO_DATE_FORMAT.format(demographicTransfer.getBirthDate());
		}
%>
			<td class="age"><%=ageString%></td>
			<td class="rosterStatus"></td>
			<td class="sex"><%=demographicTransfer.getGender()%></td>
			<td class="dob"><%=bdayString%></td>
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
		</tr>
<%	  
		}
 	}
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("keyword") || temp.equals("dboperation") ||temp.equals("displaymode")||temp.equals("submit") ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
  }
  
%>
	
</table>
</form>
<%
	int nLastPage=0,nNextPage=0;
	nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
	nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
%> 
<%
	if(rowCounter==0 && nLastPage<=0) {	 
	  	     
      	java.util.HashMap<String, String> params = new java.util.HashMap<String, String>();
      	params.put("originalPage", request.getParameter("originalPage"));
      	params.put("provider_no", request.getParameter("provider_no"));
		params.put("bFirstDisp", request.getParameter("bFirstDisp"));
      	params.put("year", request.getParameter("year"));
  		params.put("month", request.getParameter("month"));
  		params.put("day", request.getParameter("day"));  		
  		params.put("start_time", request.getParameter("start_time"));
  		params.put("end_time", request.getParameter("end_time"));
  		params.put("duration", request.getParameter("duration"));
  		params.put("appointment_date", request.getParameter("appointment_date"));  		
  		params.put("notes", request.getParameter("notes"));
  		params.put("reason", request.getParameter("reason"));
  		params.put("location", request.getParameter("location"));
  		params.put("resources", request.getParameter("resources"));
  		params.put("apptType", request.getParameter("type"));
  		params.put("style", request.getParameter("style"));
  		params.put("billing", request.getParameter("billing"));
  		params.put("status", request.getParameter("status"));
  		params.put("createdatetime", request.getParameter("createdatetime"));
  		params.put("creator", request.getParameter("creator"));
  		params.put("remarks", request.getParameter("remarks"));
  		
  		pageContext.setAttribute("apptParamsName", params);
  		%><bean:message key="demographic.search.noResultsWereFound" /><%
	} else {
		java.util.HashMap<String, String> params = new java.util.HashMap<String, String>();
		pageContext.setAttribute("apptParamsName", params);
	}
  		if(OscarProperties.getInstance().getProperty("ModuleNames","").indexOf("Caisi") != -1) {
                if(OscarProperties.getInstance().getProperty("caisi.search.workflow","false").equals("true")) {

%>
                <html:link action="/PMmodule/GenericIntake/Edit.do?method=create&type=quick&fromAppt=1" name="apptParamsName">
                <bean:message key="demographic.search.btnCreateNew" /></html:link>
                
                <% } else { %>
			  	<div class="createNew">
					<a href="../demographic/demographicaddarecordhtm.jsp?fromAppt=1&originalPage=<%=request.getParameter("originalPage")%>&search_mode=<%=request.getParameter("search_mode")%>&keyword=<%=request.getParameter("keyword")%>&notes=<%=request.getParameter("notes")%>&appointment_date=<%=request.getParameter("appointment_date")%>&year=<%=request.getParameter("year")%>&month=<%=request.getParameter("month")%>&day=<%=request.getParameter("day")%>&start_time=<%=request.getParameter("start_time")%>&end_time=<%=request.getParameter("end_time")%>&duration=<%=request.getParameter("duration")%>&bFirstDisp=false&provider_no=<%=request.getParameter("provider_no")%>&notes=<%=request.getParameter("notes")%>&reason=<%=request.getParameter("reason")%>&location=<%=request.getParameter("location")%>&resources=<%=request.getParameter("resources")%>&type=<%=request.getParameter("type")%>&style=<%=request.getParameter("style")%>&billing=<%=request.getParameter("billing")%>&status=<%=request.getParameter("status")%>&createdatetime=<%=request.getParameter("createdatetime")%>&creator=<%=request.getParameter("creator")%>&remarks=<%=request.getParameter("remarks")%>">
					<bean:message key="demographic.search.btnCreateNew" /></a>
			    </div>                 
                <% } %>
 <%
        } else {
%>
  <div class="createNew">
		<a href="../demographic/demographicaddarecordhtm.jsp?fromAppt=1&originalPage=<%=request.getParameter("originalPage")%>&search_mode=<%=request.getParameter("search_mode")%>&keyword=<%=request.getParameter("keyword")%>&notes=<%=request.getParameter("notes")%>&appointment_date=<%=request.getParameter("appointment_date")%>&year=<%=request.getParameter("year")%>&month=<%=request.getParameter("month")%>&day=<%=request.getParameter("day")%>&start_time=<%=request.getParameter("start_time")%>&end_time=<%=request.getParameter("end_time")%>&duration=<%=request.getParameter("duration")%>&bFirstDisp=false&provider_no=<%=request.getParameter("provider_no")%>&notes=<%=request.getParameter("notes")%>&reason=<%=request.getParameter("reason")%>&location=<%=request.getParameter("location")%>&resources=<%=request.getParameter("resources")%>&type=<%=request.getParameter("type")%>&style=<%=request.getParameter("style")%>&billing=<%=request.getParameter("billing")%>&status=<%=request.getParameter("status")%>&createdatetime=<%=request.getParameter("createdatetime")%>&creator=<%=request.getParameter("creator")%>&remarks=<%=request.getParameter("remarks")%>">
		<bean:message key="demographic.search.btnCreateNew" /></a>
    </div>    
<%
        }
%>	

<script language="JavaScript">
	<!--
	function last() {
	  document.nextform.action="../demographic/demographiccontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ;
	  //document.nextform.submit();  
	}
	function next() {
	  document.nextform.action="../demographic/demographiccontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ;
	  //document.nextform.submit();  
	}
	//-->
	</script>
	<a href="#" onclick="showHideItem('demographicSearch');" id="searchPopUpButton" class="rightButton top">Search</a>
	<div class="bottomBar">
	<form method="post" name="nextform" action="../demographic/demographiccontrol.jsp">
<%
	if(nLastPage>=0) {
%> 
	<input type="submit" id="prevPageButton" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnPrevPage"/>"
	onClick="last()"> 
<%
	}

	if(rowCounter==limit) {
%> 
	<input type="submit" id="nextPageButton" name="submit" value="<bean:message key="demographic.demographicsearch2apptresults.btnNextPage"/>" onClick="next()"> 
<%
	}
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("submit")  ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
	}
%>

</form>
</div>
</div>
</body>
</html>
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

List<Demographic> doSearch(DemographicDao demographicDao,List<String> searchModes, String ptstatus, List<String> keywords, int limit, int offset, String orderBy, String providerNo, boolean outOfDomain) {
	List<Demographic> demoList = null;  

	boolean active = ("".equals(ptstatus)) || ( "active".equals(ptstatus) );
	boolean inactive = ("".equals(ptstatus)) || ( "inactive".equals(ptstatus) );
	

	demoList = demographicDao.doMultiSearch(searchModes, keywords, limit, offset, providerNo, outOfDomain, active, inactive, null);

	
	return demoList;
}

%>
