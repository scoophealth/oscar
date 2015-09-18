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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

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
<%@page import="oscar.util.CBIUtil"%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	CBIUtil cbiUtil = new CBIUtil();
	List<OcanStaffForm> unsentForms = cbiUtil.getUnsubmittedCbiForms();	
	
%>

<h1>CBI Manual Submission</h1>

<script src="<%=request.getContextPath()%>/js/jquery-1.3.2.min.js"></script>
<script>
$(function () {

    $("#checkAll").bind("click", function () {
        $("[name = cbiFormIdsSelected]:checkbox").attr("checked", this.checked);
	
    });


    $("[name = cbiFormIdsSelected]:checkbox").bind("click", function () {
        var $chk = $("[name = cbiFormIdsSelected]:checkbox");	
        $("#checkAll").attr("checked", $chk.length == $chk.filter(":checked").length);
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

	function popup(url) {
		var vheight=500;
		var vwidth=600;
		var windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
		var popup=window.open(url, "cbi_submit", windowprops);
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
				
	    jQuery.ajax({url:ctx+"/CbiSubmit.do?method=submit",dataType:"html",success: function(data) {
               location.href=ctx+'/oscarReport/cbi_submit_form.jsp';
		},error: function() {
			alert('An error occurred');
			$("#subPending").val("Submit Pending Records");
			$("#SubPending").attr('disabled','false');
			

		}});

	}

</script>
				
<form method="post" id="cbiForm" action="cbi_submit_action.jsp">
	
	<table class="borderedTableAndCells">
		<tr>
			<td colspan="5" align="center">Pending CBI Forms</td>
		</tr>
		<tr>
			<td></td>
			<td>Form Id</td>
			<td>Date Created</td>			
			<td>Client</td>
			<td>Provider</td>			
		</tr>
		<%for(OcanStaffForm form:unsentForms) { %>
			<tr>
				<td><input type="checkbox" name="cbiFormIdsSelected" id="cbiFormIdsSelected" value="<%=form.getId()%>"></td>
				<td><%=form.getId()%></td>
				<td><%=formatter.format(form.getCreated())%></td>				
				<td><%=form.getClientId() %></td>
				<td><%=form.getProviderName()%></td>
			</tr>
		<%} %>
		<tr><td> <input type="checkbox" name="checkAll" id="checkAll" />All</td>
	</table>	
	<br/>
	<input type="submit" value="Submit Pending Records" id="subPending" />
</form>


<%@include file="/layouts/caisi_html_bottom.jspf"%>
