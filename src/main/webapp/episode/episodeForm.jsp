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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_eChart" rights="w" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="org.oscarehr.common.model.Episode" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>

<%
	Episode episode = (Episode)request.getAttribute("episode");
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Episode Form</title>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/share/javascript/Oscar.js"></script>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script>
	var ctx = '<%=request.getContextPath()%>';

	$(document).ready(function(){
				
		$("#startDate").datepicker({ dateFormat: "yy-mm-dd" });
		$("#endDate").datepicker({ dateFormat: "yy-mm-dd" });
        $("#description").autocomplete({
            source:ctx+'/CodeSearch.do?codingSystem='+ $("#search_coding_system").val(),
            select: function(event, ui) {
                    $( "#description" ).val( ui.item.label );
                    $( "#code" ).val( ui.item.value );
                    $('input[name="episode.codingSystem"]').val($("#search_coding_system").val());
                    return false;
            }
    	});
               
        $("#search_coding_system").bind('change',function(){
    		$("#description").autocomplete("option","source",ctx+'/CodeSearch.do?codingSystem='+ $("#search_coding_system").val());
    		$('input[name="episode.codingSystem"]').val($("#search_coding_system").val());            
    	});
        
        $('input[name="episode.codingSystem"]').val($("#search_coding_system").val());            
        
        <%if(episode != null) { %>
        	$( "#code" ).val('<%=episode.getCode()%>');
        	$('input[name="episode.status"]').val('<%=episode.getStatus()%>');
        	$('input[name="episode.id"]').val('<%=episode.getId()%>');
        	$( "#description" ).val('<%=episode.getDescription()%>');
        	$('select[name="episode.codingSystem"]').val('<%=episode.getCodingSystem()%>');
        	$('#search_coding_system').val('<%=episode.getCodingSystem()%>');
        	$('input[name="episode.demographicNo"]').val('<%=episode.getDemographicNo()%>');
        	$( "#startDate" ).val('<%=episode.getStartDateStr()%>');
        	$( "#endDate" ).val('<%=episode.getEndDateStr()%>');
        <% } %>
               
	});


</script>
<script>
	function validate() {
		if($("#description").val().length == 0) {
			alert("Description Required");
			return false;
		}
		if($("#startDate").val().length == 0) {
			alert("Start Date Required");
			return false;
		}
		
		if($("#endDate").val().length == 0 && $('select[name="episode.status"]').val() == 'Completed') {
			alert("End Date Required");
			return false;
		}
		return true;
	}
</script>
<style type="text/css">
table.outline {
	margin-top: 50px;
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

table.grid {
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

td.gridTitles {
	border-bottom: 2pt solid #888888;
	font-weight: bold;
	text-align: center;
}

td.gridTitlesWOBottom {
	font-weight: bold;
	text-align: center;
}

td.middleGrid {
	border-left: 1pt solid #888888;
	border-right: 1pt solid #888888;
	text-align: center;
}

label {
	float: left;
	width: 120px;
	font-weight: bold;
}

label.checkbox {
	float: left;
	width: 116px;
	font-weight: bold;
}

label.fields {
	float: left;
	width: 80px;
	font-weight: bold;
}

span.labelLook {
	font-weight: bold;
}

input,textarea,select { //
	margin-bottom: 5px;
}

textarea {
	width: 450px;
	height: 100px;
}

.boxes {
	width: 1em;
}

#submitbutton {
	margin-left: 120px;
	margin-top: 5px;
	width: 90px;
}

br {
	clear: left;
}
</style>

	<style>
	.ui-autocomplete {
		max-height: 100px;
		overflow-y: auto;
		/* prevent horizontal scrollbar */
		overflow-x: hidden;
		/* add padding to account for vertical scrollbar */
		padding-right: 20px;
	}
	/* IE 6 doesn't support max-height
	 * we use height instead, but this forces the menu to always be this tall
	 */
	* html .ui-autocomplete {
		height: 100px;
	}
	</style>

</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Episode Editor</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
			<html:form action="/Episode">
				<input type="hidden" name="method" value="save"/>
				<input type="hidden" id="episode.demographicNo" name="episode.demographicNo" value="<%=request.getAttribute("demographicNo")%>"/>
				<input type="hidden" id="episode.id" name="episode.id" value=""/>

					<table>
						<tr>
							<td>Coding System:</td>
							<td>
								<select id="search_coding_system" name="search_coding_system">
									<%
										List<String> codingSystems = (List<String>)request.getAttribute("codingSystems");
										for(String cs:codingSystems) {
									%>
											<option value="<%=cs%>"><%=cs%></option>
									<% } %>
								</select>
							</td>
						</tr>
						<tr>
							<td>Description:</td>
							<td><input id="description" name="episode.description" type="text" onkeypress="$('#code').val('')"/></td>
						</tr>
						<tr>
							<td>Start Date:</td>
							<td><input id="startDate" name="episode.startDateStr" type="text"/></td>
						</tr>
						<tr>
							<td>End Date:</td>
							<td><input id="endDate" name="episode.endDateStr" type="text"/></td>
						</tr>
						<tr>
							<td>Code:</td>
							<td>
								<input type="hidden" id="episode.codingSystem" name="episode.codingSystem" value=""/>
								<input id="code" name="episode.code" type="text" readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td>Status:</td>
							<td>
								<%
								    
									String status = episode == null ? "Current" : episode.getStatus();
									if(status == null || status.length()==0) {
										status="Current";
									}
									String selected=" selected=\"selected\" ";									
								%>
								<select id="episode.status" name="episode.status">
									<option value="Current" <%=("Current".equals(status))?selected:"" %>>Current</option>									
									<option value="Complete" <%=("Complete".equals(status))?selected:"" %>>Completed</option>
									<option value="Deleted" <%=("Deleted".equals(status))?selected:"" %>>Deleted</option>
								</select>
							</td>
						</tr>
					</table>
				<html:submit onclick="return validate();"/>
			</html:form>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
