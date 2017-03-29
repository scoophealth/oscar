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

<%@page import="org.oscarehr.eyeform.model.EyeformSpecsHistory"%>
<%@page import="org.oscarehr.eyeform.model.EyeformFollowUp"%>
<%@page import="org.oscarehr.eyeform.model.EyeformProcedureBook"%>
<%@page import="org.oscarehr.eyeform.model.EyeformTestBook"%>
<%@page import="java.util.List"%>

<%@ include file="/taglibs.jsp"%>


<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />

		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
		<script src="<c:out value="../js/jquery.js"/>"></script>
<script>
	jQuery.noConflict();
</script>

<script>
function addFollowUp() {
	var total = jQuery("#followup_num").val();
	total++;
	jQuery("#followup_num").val(total);
	jQuery.ajax({url:'plan_followup.jsp?id='+total,async:false, success:function(data) {
		  jQuery("#followup_container").append(data);
	}});
}

function deleteFollowUp(id) {
	//var deleteList = jQuery("input[name='followup.delete']").val();
	var followUpId = jQuery("input[name='followup_"+id+".id']").val();
	//jQuery("input[name='followup.delete']").val(deleteList += ','+followUpId);
	jQuery("form[name='eyeformPlanForm']").append("<input type=\"hidden\" name=\"followup.delete\" value=\""+followUpId+"\"/>");
	jQuery("#followup_"+id).remove();

}

function addProcedure() {
	var total = jQuery("#procedure_num").val();
	total++;
	jQuery("#procedure_num").val(total);
	jQuery.ajax({url:'plan_procedure.jsp?id='+total,async:false, success:function(data) {
		  jQuery("#procedure_container").append(data);
	}});
}

function deleteProcedure(id) {
	var procedureId = jQuery("input[name='procedure_"+id+".id']").val();
	jQuery("form[name='eyeformPlanForm']").append("<input type=\"hidden\" name=\"procedure.delete\" value=\""+procedureId+"\"/>");
	jQuery("#procedure_"+id).remove();
}

function addTest() {
	var total = jQuery("#test_num").val();
	total++;
	jQuery("#test_num").val(total);
	jQuery.ajax({url:'plan_test.jsp?id='+total,async:false, success:function(data) {
		  jQuery("#test_container").append(data);
	}});
}

function deleteTest(id) {
	var testId = jQuery("input[name='test_"+id+".id']").val();
	jQuery("form[name='eyeformPlanForm']").append("<input type=\"hidden\" name=\"test.delete\" value=\""+testId+"\"/>");
	jQuery("#test_"+id).remove();
}

function setSelect(id,type,name,val) {
	jQuery("select[name='"+type+"_"+id+"."+name+"']").each(function() {
		jQuery(this).val(val);
	});
}

function setInput(id,type,name,val) {
	jQuery("input[name='"+type+"_"+id+"."+name+"']").each(function() {
		jQuery(this).val(val);
	});
}

jQuery(document).ready(function() {
<%
	@SuppressWarnings("unchecked")
	List<EyeformFollowUp> followUps = (List<EyeformFollowUp>) request.getAttribute("followUps");
	if(followUps != null) {
		for(EyeformFollowUp fu:followUps) {%>
				addFollowUp();
				var num = jQuery("#followup_num").val();
				setInput(num,'followup','id','<%=fu.getId()%>');
				setSelect(num,'followup','type','<%=fu.getType()%>');
				setSelect(num,'followup','followupProvider','<%=fu.getFollowupProvider()%>');
				setInput(num,'followup','timespan','<%=fu.getTimespan()%>');
				setSelect(num,'followup','timeframe','<%=fu.getTimeframe()%>');
				setSelect(num,'followup','urgency','<%=fu.getUrgency()%>');
				setInput(num,'followup','comment','<%=fu.getComment()==null?"":fu.getComment().replaceAll("'", "\\\\'")%>');
			<%}
	}

	@SuppressWarnings("unchecked")
	List<EyeformProcedureBook> procedures = (List<EyeformProcedureBook>) request.getAttribute("procedures");
	if(procedures != null) {
		for(EyeformProcedureBook proc:procedures) {%>
				addProcedure();
				var num = jQuery("#procedure_num").val();
				setInput(num,'procedure','id','<%=proc.getId()%>');
				setSelect(num,'procedure','urgency','<%=proc.getUrgency()%>');
				setSelect(num,'procedure','eye','<%=proc.getEye()%>');
				setInput(num,'procedure','procedureName','<%=proc.getProcedureName()%>');
				setInput(num,'procedure','location','<%=proc.getLocation()==null?"":proc.getLocation().replaceAll("'", "\\\\'")%>');
				setInput(num,'procedure','comment','<%=proc.getComment()==null?"":proc.getComment().replaceAll("'", "\\\\'")%>');
			<%}
	}

	@SuppressWarnings("unchecked")
	List<EyeformTestBook> tests = (List<EyeformTestBook>) request.getAttribute("tests");
	if(tests != null) {
		for(EyeformTestBook test:tests) {%>
				addTest();
				var num = jQuery("#test_num").val();
				setInput(num,'test','id','<%=test.getId()%>');
				setSelect(num,'test','eye','<%=test.getEye()%>');
				setInput(num,'test','testname','<%=test.getTestname()%>');
				setSelect(num,'test','urgency','<%=test.getUrgency()%>');
				setInput(num,'test','comment','<%=test.getComment()==null?"":test.getComment().replaceAll("'", "\\\\'")%>');
			<%
		}
	}
%>
});
</script>
</head>
<body>
<center><b>Arrange Plan</b></center>
<br/>
<b>Follow Up/Consult:</b>
<br />

<html:form action="/eyeform/EyeformPlan.do">

		<input type="hidden" name="method" value="save"/>

		<input type="hidden" name="followup.demographicNo" value="<%=request.getParameter("followup.demographicNo")%>"/>
		<input type="hidden" name="followup.appointmentNo" value="<%=request.getParameter("followup.appointmentNo")%>"/>

<!-- follow up section -->
<div id="followup_container"></div>
<input type="hidden" id="followup_num" name="followup_num" value="0"/>
<a href="#" onclick="addFollowUp();">[Add]</a>

<br/><br/>
<b>Book Procedure:</b>
<br />
<!-- procedure section -->
<div id="procedure_container"></div>
<input type="hidden" id="procedure_num" name="procedure_num" value="0"/>
<a href="#" onclick="addProcedure();">[Add]</a>
<br/><br/>
<b>Book Diagnostics:</b>
<br />
<!-- test/diag section -->
<div id="test_container"></div>
<input type="hidden" id="test_num" name="test_num" value="0"/>
<a href="#" onclick="addTest();">[Add]</a>
<br/>
<br/>
	<html:submit value="Submit" />
	&nbsp;&nbsp;
	<input type="button" name="cancel" value="Cancel" onclick="window.close()" />

			</td>
		</tr>
</html:form>


</body>
</html>
