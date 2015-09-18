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
      String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed2) {
	return;
}
%>

<%
	String user_no = (String) session.getAttribute("user");
  int  nItems=0;
  String strLimit1="0";
  String strLimit2="5";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>
<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%@ include file="../admin/dbconnection.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao"%>
<%@page import="org.oscarehr.common.model.ClinicLocation"%>
<%@page import="org.oscarehr.common.dao.ReportProviderDao"%>
<%@page import="org.oscarehr.common.model.ReportProvider"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.dao.BillingONCHeader1Dao"%>
<%@page import="org.oscarehr.common.model.BillingONCHeader1"%>
<%@page import="oscar.util.ConversionUtils"%>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillingONCHeader1Dao billingOnCHeaderDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
%>
<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String clinic="";
  String clinicview = oscarVariables.getProperty("clinic_view");

  String visitLocation = clinicLocationDao.searchVisitLocation(clinicview);
  if(visitLocation!=null) {
 	 clinic = visitLocation;
  }

  int flag = 0, rowCount=0;
  String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
  String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
  String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<html>
<head>
<title>Reports</title>
<script type="text/javascript">

function popupPage(height, width, url) {
	window.open(url,"manageProviders","height=" + height + ",width=" + width + ",scrollbars=yes");
	
}
</script>
</head>
<body>
<div class="hidden-print" style="float:right;">	
	<a style="font-size:10px" href="#" onclick="popupPage(700,720,'../oscarReport/manageProvider.jsp?action=visitreport')">Manage Visit Report Providers</a>	
</div>

<div class="page-header">
	<h3>
		<bean:message key="oscarReport.oscarReportVisitControl.title" />
		<div class="pull-right">
			<button name="print" onclick="window.print()" class="btn hidden-print">
				<i class="icon-print icon-black"></i>
				<bean:message key="global.btnPrint" />
			</button>
		</div>
	</h3>
</div>

<form action="${ctx}/oscarReport/oscarReportVisitControl.jsp"
	class="well form-horizontal hidden-print" id="visitForm">
	<fieldset>
		<h4>
			<bean:message key="oscarReport.oscarReportVisitControl.title" />
			<br> <small>Please select the report type, provider and
				service begin and end dates.</small>
		</h4>
		<div class="control-group">
			<label class="control-label">Select Report</label>
			<div class="controls">
				<label class="radio inline"> <input type="radio"
					name="reportAction" onClick="toggleDivs();" value="lk"
					<%=reportAction.equals("lk")?"checked":""%>> <bean:message
						key="oscarReport.oscarReportVisitControl.msgLarryKainReport" />
				</label> <label class="radio inline"> <input type="radio"
					name="reportAction" onClick="toggleDivs();" value="vr"
					<%=reportAction.equals("vr") || reportAction.equals("")?"checked":""%>>
					<bean:message
						key="oscarReport.oscarReportVisitControl.msgVisitReport" />
				</label>
			</div>
		</div>
		<div class="control-group" id="providerDiv">
			<label class="control-label">Provider</label>

			<div class="controls">
				<select id="providerview" name="providerview"
					<%=reportAction.equals("lk")?"disabled":""%>>
					<option value="%">
						<bean:message
							key="oscarReport.oscarReportVisitControl.msgSelectProviderAll" />
					</option>
					<%
						for (ReportProvider rps : reportProviderDao.findByAction("visitreport")) {
							Provider p = providerDao.getProvider(rps.getProviderNo());
							if (p.getStatus().equals("1")) {
					%>
							<option value="<%=p.getProviderNo()%>"><%=p.getFormattedName()%></option>
					<%
							}
						}
					%>
					</select>
			</div>
		</div>

		</div>

		<div class="control-group">
			<label class="control-label">Service Date Begin</label>
			<div class="controls">
				<input type="text" id="xml_vdate" name="xml_vdate"
					value="<%=xml_vdate%>">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Service Date End</label>
			<div class="controls">

				<input type="text" id="xml_appointment_date"
					name="xml_appointment_date" value="<%=xml_appointment_date%>">
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<button type="submit" class="btn btn-primary">
					<bean:message
						key="oscarReport.oscarReportVisitControl.btnCreateReport" />
				</button>
			</div>
		</div>
	</fieldset>
</form>
<%
	if (reportAction.compareTo("") == 0 || reportAction == null) {
%>
<p>&nbsp;</p>
<%
	} else {
       if (reportAction.compareTo("lk") == 0) {
%>
<%@ include file="oscarReportVisit_lk.jspf"%>
<%
	}
       if (reportAction.compareTo("vr") == 0) {
%>
<%@ include file="oscarReportVisit_vr.jspf"%>
<%
	}

   }
%>

<script>
	var startDt = $("#xml_vdate").datepicker({
		format : "yyyy-mm-dd"
	});

	var endDt = $("#xml_appointment_date").datepicker({
		format : "yyyy-mm-dd"
	});

	$(document).ready(function() {
		$('#visitform').validate({
			rules : {
				xml_vdate : {
					required : false,
					oscarDate : true
				},
				xml_appointment_date : {
					required : false,
					oscarDate : true
				}
			}
		});
	});

	registerFormSubmit('visitForm', 'dynamic-content');

	function toggleDivs() {
		if ($('input[name=reportAction]:checked').val() == 'vr')
			$("#providerview").removeAttr('disabled');
		else
			$("#providerview").attr('disabled', 'disabled');
	}
</script>
</body>
</html>