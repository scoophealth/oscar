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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="java.lang.*"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="org.oscarehr.managers.ProgramManager2" %>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@page import="org.oscarehr.PMmodule.model.Program" %>
<%@page import="java.util.List"%>


<%
        boolean fromMessenger = request.getParameter("fromMessenger") == null ? false : (request.getParameter("fromMessenger")).equalsIgnoreCase("true")?true:false;
		String roleName = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");		
		LoggedInInfo loggedInInfo2 = LoggedInInfo.getLoggedInInfoFromSession(request);
		
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>

<script src="../js/jquery-1.9.1.min.js"></script>

<script type="text/javascript">

function checkdbstatus() {
	if( document.titlesearch.search_mode.value == 'search_band_number' ) { 
		document.titlesearch.dboperation.value = 'search_status_id_mysql'; 
	} else {
		 document.titlesearch.dboperation.value = 'search_titlename';
	}
}

function encodeInput() {
	checkdbstatus();
	document.titlesearch.keyword.value = document.titlesearch.keyword.value.replace(/\"/g, "");
	document.titlesearch.keyword.value = encodeURI(document.titlesearch.keyword.value);
}

function search() {
	encodeInput();
	if (checkTypeIn()) document.titlesearch.submit();
}

function searchInactive() {
    document.titlesearch.ptstatus.value="inactive";
    if (checkTypeIn()) document.titlesearch.submit();
}

function searchAll() {
    document.titlesearch.ptstatus.value="";
    if (checkTypeIn()) document.titlesearch.submit();
}

function searchOutOfDomain() {
    document.titlesearch.outofdomain.value="true";
    if (checkTypeIn()) document.titlesearch.submit();
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
	    "		<option value=\"search_band_number\">"+
	    "            Band #"+
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

<form method="get" name="titlesearch" action="<%=request.getContextPath()%>/demographic/demographiccontrol.jsp"
	onsubmit="return checkTypeIn()">
<div class="searchBox">
<div class="RowTop header">
	<div class="title">
		<bean:message key="demographic.search.msgSearchPatient" />
	</div>
	<div class="createNew">
		<span class="HelpAboutLogout" style="font-size:12px; font-style:normal;">
			<oscar:help keywords="&Title=Patient+Search&portal_type%3Alist=Document" key="app.top1" style="color:black; font-size:10px;font-style:normal;"/> |
        		<a style="color:black; font-size:10px;font-style:normal;" href="<%=request.getContextPath()%>/oscarEncounter/About.jsp" target="_new"><bean:message key="global.about" /></a>
		</span> 
	</div>
</div> 
<ul>
    <li>
        <div class="label">
        </div>
	<% String searchMode = request.getParameter("search_mode");
         String keyWord = request.getParameter("keyword");
        
         if (searchMode == null || searchMode.equals("")) {
             searchMode = OscarProperties.getInstance().getProperty("default_search_mode","search_name");             
         }  
         if (keyWord == null) {
             keyWord = "";
         }
     %>

     	<input type="hidden" name="max_search_clause" id="max_search_clause" value="1"/>
     	
     	<img id="addBtn" src="../images/icons/103.png" border="0" onClick="addExtraSearchClause()"/>
         <select class="wideInput" name="search_mode" id="search_mode_0" onChange="updateKeywordField(0)">

            <option value="search_name" <%=searchMode.equals("search_name")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formName" />
            </option>
            <option value="search_phone" <%=searchMode.equals("search_phone")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formPhone" />
            </option>
            <option value="search_dob" <%=searchMode.equals("search_dob")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formDOB" />
            </option>
            <option value="search_address" <%=searchMode.equals("search_address")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formAddr" />
            </option>
            <option value="search_hin" <%=searchMode.equals("search_hin")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formHIN" />
            </option>
            <option value="search_chart_no" <%=searchMode.equals("search_chart_no")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formChart" />
            </option>
       
            <option value="search_demographic_no" <%=searchMode.equals("search_demographic_no")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formDemographicNo" />
            </option>

            <option value="search_program_no" <%=searchMode.equals("search_program_no")?"selected":""%>>
                <bean:message key="demographic.zdemographicfulltitlesearch.formProgram" />
            </option>

			 <oscar:oscarPropertiesCheck value="true" defaultVal="false" property="FIRST_NATIONS_MODULE">          
		            <option value="search_band_number" <%=searchMode.equals("search_band_number")?"selected":""%> >
		                <bean:message key="demographic.zdemographicfulltitlesearch.formBandNumber" />
		            </option>
			 </oscar:oscarPropertiesCheck>

         </select>
    </li>
    <li>
        <div class="label">
        </div>
        <span id="keyword_span_0">
        <input class="wideInput" type="text" NAME="keyword" VALUE="<%=StringEscapeUtils.escapeHtml(keyWord)%>" SIZE="17" MAXLENGTH="100">
        </span>
        <span id="program_select_span_0" style="display:none">
        <select name="programKeyword_0" id="programKeyword_0">
        	<%
        		//get list of programs
        		ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
        		List<ProgramProvider> ppList = programManager.getProgramDomain(loggedInInfo2, loggedInInfo2.getLoggedInProviderNo());
        		for(ProgramProvider pp:ppList) {
        			Program p = programManager.getProgram(loggedInInfo2, pp.getProgramId().intValue());
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
    <INPUT TYPE="hidden" NAME="limit2" VALUE="10">
    <INPUT TYPE="hidden" NAME="displaymode" VALUE="Search">
    <INPUT TYPE="hidden" NAME="ptstatus" VALUE="active">
    <INPUT TYPE="hidden" NAME="fromMessenger" VALUE="<%=fromMessenger%>">
					<INPUT TYPE="hidden" NAME="outofdomain" VALUE="">
    <INPUT TYPE="SUBMIT" class="rightButton blueButton top" VALUE="<bean:message key="demographic.zdemographicfulltitlesearch.msgSearch" />" SIZE="17"
					TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchActive"/>" 
					onclick="checkdbstatus();" >
				&nbsp;&nbsp;&nbsp; <INPUT TYPE="button" onclick="searchInactive();"
					TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchInactive"/>"
					VALUE="<bean:message key="demographic.search.Inactive"/>">
				<INPUT TYPE="button" onclick="searchAll();"
					TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchAll"/>"
					VALUE="<bean:message key="demographic.search.All"/>">
					
				<%
					if(loggedInInfo2.getCurrentFacility().isIntegratorEnabled()) {
				%>
						<input type="checkbox" name="includeIntegratedResults" value="true"/>Include Integrator
				<%}%>
				
					<security:oscarSec roleName="<%=roleName%>" objectName="_search.outofdomain" rights="r">  
						<INPUT TYPE="button" onclick="searchOutOfDomain();"
							TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchOutOfDomain"/>"
							VALUE="<bean:message key="demographic.search.OutOfDomain"/>">
					</security:oscarSec>
					
				<caisi:isModuleLoad moduleName="caisi">
				<input type="button" value="cancel" onclick="location.href='<html:rewrite page="/PMmodule/ProviderInfo.do"/>'" >
				</caisi:isModuleLoad>			
    </li>
</ul>

<div id="extraSearchClauses"></div>

</div>
</form>
