
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.util.*"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@page import="org.oscarehr.common.model.ReportProvider" %>
<%@page import="org.oscarehr.common.model.Provider" %>

<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
%>

<%
  String user_no = (String) session.getAttribute("user");
  int nItems = 0;
  String strLimit1 = "0";
  String strLimit2 = "5";
  if (request.getParameter("limit1") != null) strLimit1 = request.getParameter("limit1");
  if (request.getParameter("limit2") != null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview") == null ? "ALL" : request.getParameter("providerview");
  BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  BigDecimal paidTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  MSPReconcile msp = new MSPReconcile();
  GregorianCalendar now = new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH) + 1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int flag = 0, rowCount = 0;
  //String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
  String xml_vdate = request.getParameter("xml_vdate") == null ? "" : request.getParameter("xml_vdate");
  String xml_appointment_date = request.getParameter("xml_appointment_date") == null ? "" : request.getParameter("xml_appointment_date");
  String xml_demoNo = request.getParameter("demographicNo") == null ? "" : request.getParameter("demographicNo");
  boolean defaultShow = request.getParameter("submitted") == null ? true : false;
  boolean showMSP = request.getParameter("showMSP") == null ? defaultShow : !defaultShow; //request.getParameter("showMSP");
  boolean showWCB = request.getParameter("showWCB") == null ? defaultShow : !defaultShow; //request.getParameter("showWCB");
  boolean showPRIV = request.getParameter("showPRIV") == null ? defaultShow : !defaultShow; //request.getParameter("showPRIV");
  boolean showICBC = request.getParameter("showICBC") == null ? defaultShow : !defaultShow; //request.getParameter("showPRIV");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>Billing Report</title>
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript"
	src="../../../share/calendar/calendar-setup.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
   popup.focus();
  }
}

function popupPage2(vheight,vwidth,varpage,pagename) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, pagename, windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
   popup.focus();
  }
}
function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function refresh() {
      history.go(0);

}

function fillEndDate(d){
    document.serviceform.xml_appointment_date.value= d;

}
function setDemographic(demoNo){
//alert(demoNo);
    document.serviceform.demographicNo.value = demoNo;
}


//-->

function setUIState(){
	//alert(document.forms[0].repType.value)
	frm = document.forms[0]
	reptype = frm.repType.value
	frm.selProv.disabled = false
	frm.selPayee.disabled = false
	frm.selAccount.disabled = false
	if(reptype == "REP_INVOICE"||reptype == "REP_ACCOUNT_REC"||reptype == "REP_REJ"){
		frm.selProv.disabled = true
		frm.selPayee.disabled = true
	}
}

function clearField(field){
  if(field == "xml_appointment_date"){
    document.forms[0].xml_appointment_date.value = ""
  }
  else if(field == "xml_vdate"){
    document.forms[0].xml_vdate.value = ""
  }
}

</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="10" onLoad="setUIState()">
<table width="100%" border="1" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="1" cellspacing="0" cellpadding="0">
			<tr bgcolor="#FFFFFF">
				<div align="right"><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')">
				<font face="Arial, Helvetica, sans-serif" size="1">Manage
				Provider List</font> </a></div>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr bgcolor="#000000">
				<td height="40" width="10%"></td>
				<td width="90%" align="left">
				<p><font face="Verdana, Arial, Helvetica, sans-serif"
					color="#FFFFFF"> <b> <font
					face="Arial, Helvetica, sans-serif" size="4">Billing Reports</font>
				</b> </font></p>
				</td>
				<td nowrap valign="bottom"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF">
				<b><%=DateUtils.sumDate("yyyy-M-d","0")%> </b> </font></td>
			</tr>
		</table>
		<table width="100%" border="0" bgcolor="#EEEEFF">
			<form action="createBillingReportAction.do">
			<tr>
				<td width="34%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <b>Select Payee</b> </font></td>
				<td width="66%" class="bCellData"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <b> <select name="selPayee" size="1"
					id="selPayee">
					<option value="ALL">All Payees</option>
					<%
                  {
                    String proFirst = "";
                    String proLast = "";
                    String proOHIP = "";
                    String specialty_code;
                    String billinggroup_no;
                    int Count = 0;
                    for(Object[] result:reportProviderDao.search_reportprovider("billingreport")) {
                    	ReportProvider rp = (ReportProvider)result[0];
                    	Provider p = (Provider)result[1];
                    	proFirst = p.getFirstName();
                    	proLast = p.getLastName();
                    	proOHIP = p.getOhipNo();
                 
                %>
					<option value="<%=proOHIP%>"><%=proLast%> , <%=proFirst%>
					</option>
					<%}}                %>
				</select> </b> </font></td>
			</tr>
			<tr>
				<td><font face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <b>Select Provider</b> </font></td>
				<td class="bCellData"><select name="selProv" id="select">
					<option value="ALL">All Providers</option>
					<%
              {
                String proFirst = "";
                String proLast = "";
                String proOHIP = "";
                String specialty_code;
                String billinggroup_no;
                int Count = 0;
                for(Object[] result:reportProviderDao.search_reportprovider("billingreport")) {
                	ReportProvider rp = (ReportProvider)result[0];
                	Provider p = (Provider)result[1];
                	proFirst = p.getFirstName();
                	proLast = p.getLastName();
                	proOHIP = p.getOhipNo();
            %>
					<option value="<%=proOHIP%>"><%=proLast%> , <%=proFirst%>
					</option>
					<%}}            %>
				</select></td>
			</tr>
			<tr>
				<td><font color="#333333" size="2"
					face="Verdana, Arial, Helvetica, sans-serif"> <b>Select
				Account</b> </font></td>
				<td class="bCellData"><select name="selAccount" id="select2">
					<option value="ALL">All Accounts</option>
					<%
              String proFirst = "";
              String proLast = "";
              String proOHIP = "";
              String specialty_code;
              String billinggroup_no;
              int Count = 0;
              for(Object[] result:reportProviderDao.search_reportprovider("billingreport")) {
              	ReportProvider rp = (ReportProvider)result[0];
              	Provider p = (Provider)result[1];
              	proFirst = p.getFirstName();
              	proLast = p.getLastName();
              	proOHIP = p.getOhipNo();
            %>
					<option value="<%=proOHIP%>"><%=proLast%> , <%=proFirst%>
					</option>
					<%}            %>
				</select></td>

			</tr>
			<tr>
				<td><font face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <b>Report Type</b> </font></td>
				<td class="bCellData"><select name="repType"
					onChange="setUIState()">
					<option value="REP_INVOICE">Invoice</option>
					<option value="REP_REJ">Rejection</option>
					<option value="REP_ACCOUNT_REC">Accounts Receivable</option>
					<option value="REP_WO">Write-Off</option>
					<option value="REP_PAYREF">Payments and Refunds(Cash)</option>
					<option value="REP_PAYREF_SUM">Payments and Refunds
					Summary</option>
				</select></td>
			</tr>
			<tr>
				<td><font face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <strong>Date Range</strong> </font></td>
				<td class="bCellData">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><font size="1" face="Arial, Helvetica, sans-serif">
						<a href="javascript: function myFunction() {return false; }"
							id="hlSDate">Start Date :</a> </font></td>
						<td><input type="text" name="xml_vdate" id="xml_vdate"
							value="<%=xml_vdate%>" readonly="true"> <a
							href="javascript: clearField('xml_vdate')">clear</a></td>
					</tr>
					<tr>
						<td><font size="1" face="Arial, Helvetica, sans-serif">
						<a href="javascript: function myFunction() {return false; }"
							id="hlADate">End Date :</a> </font></td>
						<td><input type="text" name="xml_appointment_date"
							id="xml_appointment_date" value="<%=xml_appointment_date%>"
							readonly="true"> <a
							href="javascript: clearField('xml_appointment_date')">clear</a></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td><font face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <strong>Document Format</strong> </font></td>
				<td class="bCellData"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <strong> <select name="docFormat"
					id="select3">
					<option value="pdf">PDF</option>
					<option value="csv">Spread Sheet</option>
				</select> </strong> </font></td>
			</tr>
			<tr>
				<td><font face="Verdana, Arial, Helvetica, sans-serif" size="2"
					color="#333333"> <strong>Insurer</strong> </font></td>
				<td class="bCellData"><font color="#333333" size="2"
					face="Verdana, Arial, Helvetica, sans-serif">&nbsp;</font>
				<table width="100%">
					<tr>
						<td class="bCellData"><input name="showMSP" type="checkbox"
							value="true" checked /> MSP</td>
						<td class="bCellData"><input name="showWCB" type="checkbox"
							value="true" checked /> WCB</td>
						<td class="bCellData"><input name="showPRIV" type="checkbox"
							value="true" checked /> Private</td>
						<td class="bCellData"><input name="showICBC" type="checkbox"
							value="true" checked /> ICBC</td>
					</tr>
				</table>
				<font color="#333333" size="2"
					face="Verdana, Arial, Helvetica, sans-serif">&nbsp;</font> <font
					color="#333333" size="2"
					face="Verdana, Arial, Helvetica, sans-serif">&nbsp;</font></td>
			</tr>
			<tr>
				<td colspan="2">
				<div align="center"><font color="#333333" size="2"
					face="Verdana, Arial, Helvetica, sans-serif"> <span
					class="bCellData"> <font color="#333333" size="2"
					face="Verdana, Arial, Helvetica, sans-serif"> <input
					type="hidden" name="verCode" value="V03" /> </font> </span> <input type="submit"
					name="Submit" value="Create Report"> </font></div>
				</td>
			</tr>
			</form>
		</table>
		<%
      String billTypes = request.getParameter("billTypes");
      if (billTypes == null) {
        billTypes = MSPReconcile.REJECTED;
      }
    %> <script language='javascript'>
       Calendar.setup({inputField:"xml_vdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
       Calendar.setup({inputField:"xml_appointment_date",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlADate",singleClick:true,step:1});
      </script></td>
	</tr>
</table>
</body>
</html>
