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
<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.web.*"%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	List<OcanSubmissionLog> submissions = OcanReportUIBean.getAllSubmissions();
	List<OcanStaffForm> unsentForms = OcanReportUIBean.getAllUnsubmittedOcanForms();
	
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
%>

<h1>OCAN IAR Report - v2.0.6</h1>

<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
<script>

$(function () {

    $("#checkA").bind("click", function () {
        $("[name = test1]:checkbox").attr("checked", this.checked);
	
    });


    $("[name = test1]:checkbox").bind("click", function () {
        var $chk = $("[name = test1]:checkbox");	
        $("#checkA").attr("checked", $chk.length == $chk.filter(":checked").length);
		  if($(this).attr("checked"))
   		  {
			$(this).attr("checked",true);
		  }
		  else
		  {
		    $(this).attr("checked",false);
		  }
			
		
    })
});
	function submitIAR() {
		document.getElementById("ocanForm").action="ocan_report_export_iar.jsp";
	}

	function popup(url) {
		var vheight=500;
		var vwidth=600;
		var windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
		var popup=window.open(url, "ocan_iar", windowprops);
		if (popup != null) {
			if (popup.opener == null) {
				popup.opener = self;
			}
			popup.focus();
		}
	}

	function submitPending() {
		var ctx = '<%=request.getContextPath()%>';
		$("#subPending").val("Please Wait");
		$("#SubPending").attr('disabled','true');
				

	    var arrChk1=$("input[name=test1][checked]");
	   
		var fieldList = [];
	    for (var i=0;i<arrChk1.length;i++)
	    {
			
			 fieldList.push(arrChk1[i].value);
			 
	    }		
	    jQuery.ajax({url:ctx+"/OcanIarSubmit.do?method=submit&account="+fieldList+"",dataType:"html",success: function(data) {
            location.href=ctx+'/oscarReport/ocan_iar.jsp?assessmentIds='+fieldList;		    
		},error: function() {
			alert('An error occurred');
			$("#subPending").val("Submit Pending Records");
			$("#SubPending").attr('disabled','false');
			

		}});

	}


	function submitManual() {
		var ctx = '<%=request.getContextPath()%>';
		
	    var arrChk1=$("input[name=test1][checked]");
	    //checkbox value1
		var fieldList = [];
	    for (var i=0;i<arrChk1.length;i++)
	    {
			
			 fieldList.push(arrChk1[i].value);
			 
	    }		
		document.getElementById('ocanForm').action='ocan_report_export_iar_manual.jsp?assessmentIds='+fieldList;
		document.getElementById('ocanForm').submit();
		
	}
</script>
				
<form method="post" id="ocanForm" action="ocan_report_export_iar.jsp">
	<table class="borderedTableAndCells">
		<tr>
			<td colspan="4" align="center">IAR Submissions</td>
		</tr>
		<tr>
			<td>Submission Id</td>
			<td>Submission Date</td>
			<td># of Records</td>			
			<td>Result</td>
		</tr>
		<%for(OcanSubmissionLog submission:submissions) { %>
			<tr>
				<td><a href="#" onclick="popup('ocan_iar_detail.jsp?submissionId=<%=submission.getId()%>');return false;"><%=submission.getId()%></a></td>
				<td><%=formatter.format(submission.getSubmitDateTime())%></td>
				<td>
					<%=submission.getRecords().size() %>
				</td>				
				<td><%=submission.getResult()%></td>
			</tr>
		<% } %>		
	</table>	
	
	<br/><br/>
	
	<table class="borderedTableAndCells">
		<tr>
			<td colspan="6" align="center">Pending OCAN Forms</td>
		</tr>
		<tr><td> <input type="checkbox" name="checkAll2" id="checkA" />All</td>
			<td>Form Id</td>
			<td>Date Started</td>
			<td>Date Completed</td>
			<td>Client</td>
			<td>Provider</td>			
		</tr>
		<%for(OcanStaffForm form:unsentForms) { %>
			<tr><td><input type="checkbox" name="test1" value="<%=form.getAssessmentId()%>"></td>
				<td><%=form.getId()%></td>
				<td><%=formatter2.format(form.getStartDate()) %></td>
				<td><%=formatter2.format(form.getCompletionDate()) %></td>
				<td><%=form.getClientId() %></td>
				<td><%=form.getProviderName()%></td>
			</tr>
		<%} %>
	</table>	
	<br/>
	<input type="button" value="Submit Pending Records" id="subPending" onclick="submitPending();return false;"/>
	&nbsp;&nbsp;
	<input type="button" value="Generate Manual File" id="subManual" onclick="submitManual();return false;"/>
</form>


<%@include file="/layouts/caisi_html_bottom.jspf"%>
