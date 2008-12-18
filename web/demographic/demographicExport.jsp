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
 * McMaster Unviersity test2
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%@page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  
  DemographicSets  ds = new DemographicSets();
  ArrayList sets = ds.getDemographicSets();
  
  oscar.oscarReport.data.RptSearchData searchData  = new oscar.oscarReport.data.RptSearchData();    
  ArrayList queryArray = searchData.getQueryTypes();

  String userRole = (String)session.getAttribute("userrole");
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<!--I18n-->
<title>oscarPrevention</title>
<script src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<SCRIPT LANGUAGE="JavaScript">

function showHideItem(id){ 
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = ''; 
    else
        document.getElementById(id).style.display = 'none'; 
}

function showItem(id){
        document.getElementById(id).style.display = ''; 
}

function hideItem(id){
        document.getElementById(id).style.display = 'none'; 
}

function showHideNextDate(id,nextDate,nexerWarn){
    if(document.getElementById(id).style.display == 'none'){
        showItem(id);
    }else{
        hideItem(id);
        document.getElementById(nextDate).value = "";
        document.getElementById(nexerWarn).checked = false ;
        
    }        
}

function disableifchecked(ele,nextDate){        
    if(ele.checked == true){       
       document.getElementById(nextDate).disabled = true;       
    }else{                      
       document.getElementById(nextDate).disabled = false;              
    }
}

function checkSelect(slct) {
    if (slct==-1) {
	alert("Please select a Patient Set");
	return false;
    }
    else return true;
}
</SCRIPT>




<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<%
if (!userRole.toLowerCase().contains("admin")) { %>
<p>
<h2>Sorry! Only administrators can export demographics.</h2>
</p>
<%
} else {
%>

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="100">
		demographicExport</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Demographic Export</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp;</td>
		<td valign="top" class="MainTableRightColumn"><html:form
			action="/demographic/DemographicExport" method="get"
			onsubmit="return checkSelect(patientSet.value);">
			<div>
			<% if (demographic_no!= null) { %> <input type="hidden"
				name="demographicNo" value="<%=demographic_no%>" /> Exporting : <%} else {%>
			Patient Set: <html:select property="patientSet">
				<html:option value="-1">--Select Set--</html:option>
				<%for (int i =0 ; i < queryArray.size(); i++){
                        RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
                        String qId = sc.id;
                        String qName = sc.queryName;%>
				<html:option value="<%=qId%>"><%=qName%></html:option>
				<%}%>
			</html:select> <%}%> <!--
                  <input type="submit" value="Export" />
//--></div>

		</html:form> <html:form action="/demographic/DemographicExport2" method="get"
			onsubmit="patientSet.value = document.forms[0].patientSet.value;return checkSelect(patientSet.value);">
			<% if (demographic_no!= null) { %>
			<input type="hidden" name="demographicNo" value="<%=demographic_no%>" />
		    Exporting :
		    
		    <%} else {%>
			<html:hidden property="patientSet" />
			<%}%>
		   Media Type:
		   <html:select property="mediaType">
				<html:option value="Hard Disk">Hard Disk</html:option>
				<html:option value="CD/DVD">CD/DVD</html:option>
				<html:option value="Flash Disk">Flash Disk</html:option>
				<html:option value="Floppy Disk">Floppy Disk</html:option>
				<html:option value="Tape">Tape</html:option>
			</html:select>
		   &nbsp;
		   Number of Media:
		   <html:text property="noOfMedia" size="1" value="1" />
			<p><input type="submit" value="Export (CMS spec 2.0)" />
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
<script type="text/javascript">
    //Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
</script>

<%}%>
</body>
</html:html>
<%!

%>
