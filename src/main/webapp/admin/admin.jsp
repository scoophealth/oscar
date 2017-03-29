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
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.invoices,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc,_admin.torontoRfq"
	rights="r" reverse="<%=true%>">
		<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.*");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>

<%
	String curProvider_no = (String)session.getAttribute("user");
	String userfirstname = (String)session.getAttribute("userfirstname");
	String userlastname = (String)session.getAttribute("userlastname");
%>

<%@ page errorPage="errorpage.jsp"%>
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<%
	String country = request.getLocale().getCountry();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="oscar.OscarProperties"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title><bean:message key="admin.admin.page.title" /> Start Time : <%=oscar.OscarProperties.getInstance().getStartTime()%></title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="admin"/>

<script type="text/JavaScript">
function onsub() {
	if(document.searchprovider.keyword.value=="") {
		alert("<bean:message key="global.msgInputKeyword"/>");
		return false;
	} else return true;
	// do nothing at the moment
	// check input data in the future
}

function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarRx", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
function popUpBillStatus(vheight,vwidth,varpage) {
 var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
</script>
<style type="text/css">
a:link {
	text-decoration: none;
	color: #003399;
}

a:active {
	text-decoration: none;
	color: #003399;
}

a:visited {
	text-decoration: none;
	color: #003399;
}

a:hover {
	text-decoration: none;
	color: #003399;
}

BODY {
	font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
	background-color: #A9A9A9;
}

.title {
	font-size: 15pt;
	font-weight: bold;
	text-align: center;
	background-color: #000000;
	color: #FFFFFF;
}

div.adminBox {
	width: 90%;
	background-color: #eeeeff;
	margin-top: 2px;
	margin-left: auto;
	margin-right: auto;
	margin-bottom: 0px;
	padding-bottom: 0px;
}

div.adminBox h3 {
	color: #ffffff;
	font-size: 14pt;
	font-weight: bold;
	text-align: left;
	background-color: #486ebd;
	margin-top: 0px;
	padding-top: 0px;
	margin-bottom: 0px;
	padding-bottom: 0px;
}

div.adminBox ul {
	text-align: left;
	list-style: none;
	list-style-type: none;
	list-style-position: outside;
	padding-left: 1px;
	margin-left: 1px;
	margin-top: 0px;
	padding-top: 1px;
	margin-bottom: 0px;
	padding-bottom: 0px;
}

div.logoutBox {
	text-align: right;
}
</style>

</head>

<body class="BodyStyle">

<div class="title"><bean:message key="admin.admin.page.title" /></div>

<div class="logoutBox">
<%
	if (roleName$.equals("admin" + "," + curProvider_no))
		{
%><html:link
	page="/logout.jsp">
	<bean:message key="global.btnLogout" />
</html:link>&nbsp;<%
	}
%>
</div>

<!-- #USER MANAGEMENT -->
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.torontoRfq,_admin.provider" rights="r" reverse="<%=false%>">
	
	<div class="adminBox">
	<h3>&nbsp;<bean:message key="admin.admin.UserManagement" /></h3>
	<ul>
		<li><html:link page="/admin/provideraddarecordhtm.jsp">
			<bean:message key="admin.admin.btnAddProvider" />
		</html:link></li>
		<li><html:link page="/admin/providersearchrecordshtm.jsp">
			<bean:message key="admin.admin.btnSearchProvider" />
		</html:link></li>
		<li><html:link page="/admin/securityaddarecord.jsp">
			<bean:message key="admin.admin.btnAddLogin" />
		</html:link></li>
		<li><html:link page="/admin/securitysearchrecordshtm.jsp">
			<bean:message key="admin.admin.btnSearchLogin" />
		</html:link></li>

		<li><a href="#"
			onclick='popupPage(500,700,&quot;<html:rewrite page="/admin/providerRole.jsp"/>&quot;);return false;'>
		<bean:message key="admin.admin.assignRole"/></a></li>
			
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_admin,_admin.unlockAccount" rights="r">
			<li><a href="#"
				onclick='popupPage(500,800,&quot;<html:rewrite page="/admin/unLock.jsp" />&quot;);return false;'>
			<bean:message key="admin.admin.unlockAcct"/></a></li>
		</security:oscarSec>
	</ul>
	</div>
</security:oscarSec>

<!-- #USER MANAGEMENT END -->

<!-- #BILLING -->
        <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.invoices,_admin,_admin.billing" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.billing" /></h3>
		<ul>
            <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.billing" rights="r" reverse="<%=false%>">
			<%
				// Only show link to Clinicaid admin if Clinicaid Billing is enabled
				if (oscarVariables.getProperty("billregion", "").equals("CLINICAID"))
				{ 
			%>
					<li>
					<a href="../billing.do?billRegion=CLINICAID&action=invoice_reports" target="_blank">
							<bean:message key="admin.admin.invoiceRpts"/>
						</a>
					</li>
			<%
				}
				else if (oscarVariables.getProperty("billregion", "").equals("BC"))
				{
			%>
			<li><a href="#"
				onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/manageBillingform.jsp"/>&quot;);return false;'><bean:message key="admin.admin.ManageBillFrm"/></a></li>
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/billing/CA/BC/billingPrivateCodeAdjust.jsp"/>&quot;);return false;'><bean:message key="admin.admin.ManagePrivFrm"/></a></li>
			<oscar:oscarPropertiesCheck property="BC_BILLING_CODE_MANAGEMENT"
				value="yes">
				<li><a href="#"
					onclick='popupPage(600,900,&quot;<html:rewrite page="/billing/CA/BC/billingCodeAdjust.jsp"/>&quot;);return false;'><bean:message key="admin.admin.ManageBillCodes"/></a></li>
			</oscar:oscarPropertiesCheck>
			<li><a href="#"
				onclick='popupPage(600,600,&quot;<html:rewrite page="/billing/CA/BC/showServiceCodeAssocs.do"/>&quot;);return false;'><bean:message key="admin.admin.ManageServiceDiagnosticCodeAssoc"/></a></li>
			<li><a href="#"
				onclick='popupPage(600,500,&quot;<html:rewrite page="/billing/CA/BC/supServiceCodeAssocAction.do"/>&quot;);return false;'><bean:message key="admin.admin.ManageProcedureFeeCodeAssoc"/></a></li>
			<li><a href="#"
				onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/BC/billingManageReferralDoc.jsp"/>&quot;);return false;'><bean:message key="admin.admin.ManageReferralDoc"/></a></li>
			<oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="no"
				defaultVal="true">
				<li><a href="#"
					onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/BC/billingSim.jsp"/>&quot;);return false;'><bean:message key="admin.admin.SimulateSubFile"/></a></li>
				<li><a href="#"
					onclick='popupPage(800,720,&quot;<html:rewrite page="/billing/CA/BC/billingTeleplanGroupReport.jsp"/>&quot;);return false;'><bean:message key="admin.admin.genTeleplanFile"/></a></li>
			</oscar:oscarPropertiesCheck>
			<oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="yes">
				<li><a href="#"
					onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/BC/TeleplanSimulation.jsp"/>&quot;);return false;'><bean:message key="admin.admin.simulateSubFile2"/></a></li>
				<li><a href="#"
					onclick='popupPage(800,720,&quot;<html:rewrite page="/billing/CA/BC/TeleplanSubmission.jsp"/>&quot;);return false;'><bean:message key="admin.admin.genTeleplanFile2"/></a></li>
				<li><a href="#"
					onclick='popupPage(800,1000,&quot;<html:rewrite page="/billing/CA/BC/teleplan/ManageTeleplan.jsp"/>&quot;);return false;'><bean:message key="admin.admin.manageTeleplan"/></a></li>
			</oscar:oscarPropertiesCheck>
			<oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="no"
				defaultVal="true">
				<li><a href="#"
					onclick='popupPage(600,800,&quot;<html:rewrite page="/billing/CA/BC/billingTA.jsp"/>&quot;);return false;'><bean:message key="admin.admin.uploadRemittance"/></a></li>
			</oscar:oscarPropertiesCheck>
			<li><a href="#"
				onclick='popupPage(600,800,&quot;<html:rewrite page="/billing/CA/BC/viewReconcileReports.jsp"/>&quot;);return false;'><bean:message key="admin.admin.reconciliationReports"/></a></li>
			<li><a href="#"
				onclick='popUpBillStatus(375,425,&quot;<html:rewrite page="/billing/CA/BC/billingAccountReports.jsp"/>&quot;);return false;'><bean:message key="admin.admin.AccountingRpts"/></a></li>
			<li><a href="#"
				onclick='popupPage(800,1000,&quot;<html:rewrite page="/billing/CA/BC/billStatus.jsp"/>&quot;);return false;'><bean:message key="admin.admin.editInvoices"/></a></li>
			<li><a href="#"
				onclick='popupPage(200,300,&quot;<html:rewrite page="/billing/CA/BC/settleBG.jsp"/>&quot;);return false;'><bean:message key="admin.admin.settlePaidClaims"/></a></li>
			
			<%-- Addition of BC MSP Quick Billing by Dennis Warren - December 2011 --%>
			<li>
				<a href="javascript: popupPage( 500, 900,&quot;<html:rewrite page="/quickBillingBC.do" />&quot );" >
					BC MSP Quick Billing
				</a>
			</li>
			
			<%
				}
								else if (oscarVariables.getProperty("billregion", "").equals("ON"))
								{
			%>
			<li><a href="#"
				onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/ON/ScheduleOfBenefitsUpload.jsp"/>&quot;);return false;'><bean:message key="admin.admin.scheduleOfBenefits"/></a></li>
			<li><a href="#"
				onclick='popupPage(300,600,&quot;<html:rewrite page="/billing/CA/ON/addEditServiceCode.jsp"/>&quot;);return false;'><bean:message key="admin.admin.manageBillingServiceCode"/></a></li>
			<li><a href="#"
				onclick='popupPage(300,600,&quot;<html:rewrite page="/billing/CA/ON/billingONEditPrivateCode.jsp"/>&quot;);return false;'><bean:message key="admin.admin.managePrivBillingCode"/></a></li>
			<li><a href="#"
				onclick='popupPage(700,1000,&quot;<html:rewrite page="/admin/manageCSSStyles.do"/>&quot;);return false;'><bean:message key="admin.admin.manageCodeStyles"/></a></li>
			<li><html:link page="/admin/../admin/gstControl.jsp"><bean:message key="admin.admin.manageGSTControl"/></html:link></li>
			<li><html:link page="/admin/../admin/gstreport.jsp"><bean:message key="admin.admin.gstReport"/></html:link></li>
			<li><a href="#"
				onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/ON/manageBillingLocation.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnAddBillingLocation" /></a></li>
			<li><a href="#"
				onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/ON/manageBillingform.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnManageBillingForm" /></a></li>
			<li><a href="#"
				onclick='popupPage(700,700,&quot;<html:rewrite page="/billing/CA/ON/billingOHIPsimulation.jsp"/>?html=&quot;);return false;'><bean:message
				key="admin.admin.btnSimulationOHIPDiskette" /></a></li>
			<li><a href="#"
				onclick='popupPage(700,720,&quot;<html:rewrite page="/billing/CA/ON/billingOHIPreport.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnGenerateOHIPDiskette" /></a></li>
			<li><a href="#"
				onclick='popupPage(700,640,&quot;<html:rewrite page="/billing/CA/ON/billingCorrection.jsp"/>?billing_no=&quot;);return false;'><bean:message
				key="admin.admin.btnBillingCorrection" /></a></li>
			<li><a href="#"
				onclick='popupPage(700,820,&quot;<html:rewrite page="/billing/CA/ON/batchBilling.jsp"/>?service_code=all&quot;);return false;'><bean:message
				key="admin.admin.btnBatchBilling" /></a></li>
			<li><a href="#"
				onclick='popupPage(700,640,&quot;<html:rewrite page="/billing/CA/ON/inr/reportINR.jsp"/>?provider_no=all&quot;);return false;'><bean:message
				key="admin.admin.btnINRBatchBilling" /></a></li>
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/billing/CA/ON/billingONUpload.jsp"/>&quot;);return false;'><bean:message key="admin.admin.uploadMOHFile"/></a></li>
			<% if (OscarProperties.getInstance().isPropertyActive("moh_file_management_enabled")) { %>
			<li><a href="#" onclick='popupPage(600,900,&quot;<html:rewrite page="/billing/CA/ON/viewMOHFiles.jsp"/>&quot;);return false;'><bean:message key="admin.admin.viewMOHFiles"/></a></li>
			<% } %>
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/servlet/oscar.DocumentUploadServlet"/>&quot;);return false;'><bean:message
				key="admin.admin.btnBillingReconciliation" /></a></li>                       
                        <!--  li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/billing/CA/ON/billingRA.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnBillingReconciliation"/></a></li-->
			<!--  li><a href="#" onclick ='popupPage(600,1000,&quot;<html:rewrite page="/billing/CA/ON/billingOBECEA.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnEDTBillingReportGenerator"/></a></li-->
			<li>
				<a href="#" onclick='popupPage(800,1000,&quot;<html:rewrite page="/mcedt/mcedt.do"/>&quot;);return false;'><bean:message key="admin.admin.mcedt"/></a>
			</li>
			</li>
			<li><a href="#"
				onclick='popupPage(800,1000,&quot;<html:rewrite page="/billing/CA/ON/billStatus.jsp"/>&quot;);return false;'><bean:message key="admin.admin.invoiceRpts"/></a></li>
                        <li><a href="#"
                                onclick='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/ON/endYearStatement.do"/>&quot;);return false;'><bean:message key="admin.admin.endYearStatement"/></a></li>
			<%if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %>
				<li>
					<a href='#'	onclick='popupPage(300,750,&quot;<html:rewrite page="/admin/clinicNbrManage.jsp"/>&quot;);return false;'>Manage Clinic NBR Codes</a>
				</li>
			<%}%>
				<li>
					<a href='#'	onclick='popupPage(300,750,&quot;<html:rewrite page="/billing/CA/ON/managePaymentType.do"/>&quot;);return false;'><bean:message key="admin.admin.managePaymentType"/></a>
				</li>
				<% if (org.oscarehr.common.IsPropertiesOn.propertiesOn("ENABLE_PREVENTION_BILLING")) {%>
				<li>
						<a href='#'	onclick='popupPage(300,750,&quot;<html:rewrite page="/billing/CA/ON/preventionBillingSetting.jsp"/>&quot;);return false;'>
						<bean:message key="admin.admin.preventionBilling"/></a>
				</li>
				<%} %>

			<%
				}
			%>
            </security:oscarSec>
                        
                <% if (oscarVariables.getProperty("billregion","").equals("ON")) { %>                	
                        <li><a href="#" onclick="popupPage(800,1000,&quot;<html:rewrite page='/billing/CA/ON/billingONPayment.jsp'/>&quot;);return false;"><bean:message key="admin.admin.paymentReceived"/></a></li>
                <% } %>
		</ul>
		</div>
	</security:oscarSec>
<!-- #BILLING END-->

<!-- #LABS/INBOX -->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin," rights="r" reverse="<%=false%>">

		<div class="adminBox">
			<h3>&nbsp;<bean:message key="admin.admin.LabsInbox" /></h3>
			<ul>
				<li><a href="#" onclick='popupPage(800,1000,&quot;<html:rewrite page="/lab/CA/ALL/testUploader.jsp"/>&quot;);return false;'><bean:message key="admin.admin.hl7LabUpload" /></a></li>
				<oscar:oscarPropertiesCheck property="OLD_LAB_UPLOAD" value="yes"
					defaultVal="false">
					<li><a href="#"
						onclick='popupPage(800,1000,&quot;<html:rewrite page="/lab/CA/BC/LabUpload.jsp"/>&quot;);return false;'><bean:message key="admin.admin.oldLabUpload"/></a></li>
				</oscar:oscarPropertiesCheck>
				<li><a href="#" onclick='popupPage(800,1000,&quot;<html:rewrite page="/admin/labforwardingrules.jsp"/>&quot;);return false;'><bean:message key="admin.admin.labFwdRules" /></a></li>
				<li><a href="javascript:void(0);" onclick="popupPage(550,800,&quot;<html:rewrite page="/admin/addQueue.jsp"/>&quot;);return false;" ><bean:message key="admin.admin.AddNewQueue"/></a></li>
			</ul>
		</div>	
		
	</security:oscarSec>
<!-- #LABS/INBOX END -->	

<!--  #FORMS/EFORMS -->	
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>">
	
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.FormsEforms" /></h3>
		<ul>
			<li><a href="#"
				onclick='popupPage(500,1000,&quot;<html:rewrite page="/form/setupSelect.do"/>&quot;);return false;'><bean:message
				key="admin.admin.btnSelectForm" /></a></li>
			<li><a href="#"
				onclick='popupPage(500,1000,&quot;<html:rewrite page="/form/formXmlUpload.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnImportFormData" /></a></li>
			<li><html:link page="/admin/../eform/efmformmanager.jsp">
				<bean:message key="admin.admin.btnUploadForm" />
			</html:link></li>
			<li><html:link page="/admin/../eform/efmimagemanager.jsp">
				<bean:message key="admin.admin.btnUploadImage" />
			</html:link></li>
			<li><html:link page="/admin/../eform/efmmanageformgroups.jsp">
				<bean:message key="admin.admin.frmGroups"/>
			</html:link></li>

			<% if (org.oscarehr.common.IsPropertiesOn.isIndivicaRichTextLetterEnable()) { %>
			<li><html:link page="/admin/../eform/efmformrtl_config.jsp"><bean:message key="admin.admin.richTextLetter"/></html:link></li>
			<% } %>

			<li><html:link page="/admin/../eform/efmmanageindependent.jsp">
				<bean:message key="admin.admin.frmIndependent"/>
			</html:link></li>

			<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.fieldnote" rights="r" reverse="<%=false%>">
			<li><a href="#" 
				onclick='popupPage(600,900,&quot;<html:rewrite page="/admin/../eform/fieldNoteReport/fieldnotereport.jsp"/>&quot;);return false;'>
				<bean:message key="admin.admin.fieldNoteReport" /></a>
			</li>
			</security:oscarSec>
		</ul>
		</div>
	</security:oscarSec>
<!--  #FORMS/EFORMS END-->	

<!-- #REPORTS-->
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.oscarReport" /></h3>
		<ul>
			<%
				session.setAttribute("reportdownload", "/usr/local/tomcat/webapps/oscar_sfhc/oscarReport/download/");
			%>
			
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/RptByExample.do"/>&quot;);return false;'><bean:message
				key="admin.admin.btnQueryByExample" /></a></li>
			
			<li><a href="#"
				onclick='popup(600,900,&quot;<html:rewrite page="/oscarReport/reportByTemplate/homePage.jsp"/>&quot;, "reportbytemplate")'><bean:message key="admin.admin.rptbyTemplate"/></a></li>
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/dbReportAgeSex.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnAgeSexReport" /></a></li>
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/oscarReportVisitControl.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnVisitReport" /></a></li>
			<%-- This links doesnt make sense on Brazil. Hide then --%>
			
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/oscarReportCatchment.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnPCNCatchmentReport" /></a></li>
			<li><a href="#"
				onclick='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/FluBilling.do"/>?orderby=&quot;);return false;'><bean:message
				key="admin.admin.btnFluBillingReport" /></a></li>
			<li><a href="#"
				onclick='popupPage(600,1000,&quot;<html:rewrite page="/oscarReport/obec.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnOvernightChecking" /></a></li>

			<li><a href="#"
				onclick="popupPage(600,900,&quot;<html:rewrite page="/oscarSurveillance/ReportSurveillance.jsp"/>&quot;)"><bean:message
				key="admin.admin.report.SurveillanceReport" /></a></li>
			<li><a href="#"
				onclick="popupPage(600,900,&quot;<html:rewrite page="/oscarReport/oscarReportRehabStudy.jsp"/>&quot;)"><bean:message key="admin.admin.rehabStudy"/></a></li>
			<li><a href="#"
				onclick="popupPage(600,900,&quot;<html:rewrite page="/oscarReport/patientlist.jsp"/>&quot;)"><bean:message key="admin.admin.exportPatientbyAppt"/></a></li>
			<caisi:isModuleLoad moduleName="caisi">
				<li><html:link page="/PMmodule/reports/activity_report_form.jsp"><bean:message key="admin.admin.activityRpt"/></html:link></li>
			</caisi:isModuleLoad>
			<li><html:link
				page="/oscarReport/provider_service_report_form.jsp"><bean:message key="admin.admin.providerServiceRpt"/></html:link></li>
			<caisi:isModuleLoad moduleName="caisi">
				<li><html:link page="/PopulationReport.do"><bean:message key="admin.admin.popRpt"/></html:link></li>
			</caisi:isModuleLoad>
			<li><html:link page="/oscarReport/cds_4_report_form.jsp"><bean:message key="admin.admin.cdsRpt"/></html:link></li>
			<li><html:link page="/oscarReport/mis_report_form.jsp"><bean:message key="admin.admin.misRpt"/></html:link></li>
			<li><html:link page="/oscarReport/ocan_report_form.jsp"><bean:message key="admin.admin.ocanRpt"/></html:link></li>
			<li><html:link page="/oscarReport/ocan_iar.jsp"><bean:message key="admin.admin.ocanIarRpt"/></html:link></li>
			<li><html:link page="/oscarReport/ocan_reporting.jsp"><bean:message key="admin.admin.ocanReporting"/></html:link></li>
			<li><html:link page="/oscarReport/cbi_submit_form.jsp"><bean:message key="admin.admin.cbiSubmit"/></html:link></li>
			<li><html:link page="/admin/cbiAdmin.jsp"><bean:message key="admin.admin.cbi.reportlink"/></html:link></li>
			<li><html:link page="/oscarReport/cbi_report_form.jsp"><bean:message key="admin.admin.cbiRpt"/></html:link></li>
			<li><html:link page="/admin/UsageReport.jsp"><bean:message key="admin.admin.usageRpt"/></html:link></li>
			<oscar:oscarPropertiesCheck property="SERVERLOGGING" value="yes">
				<li><a href="#"
					onclick="popupPage(600,900, &quot;<html:rewrite page="/admin/oscarLogging.jsp"/>&quot;)"><bean:message key="admin.admin.serverLog"/></a></li>
			</oscar:oscarPropertiesCheck>
                        <li><a href="#"
                               onclick="popupPage(600,900,&quot;<html:rewrite page="/report/DxresearchReport.do"/>&quot;)"><bean:message key="admin.admin.diseaseRegister"/></a></li>

			<li><a href="#"
				onclick='popupPage(550,810,&quot;<html:rewrite page="/admin/demographicstudy.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnStudy" /></a></li>
			<caisi:isModuleLoad moduleName="eaaps.enabled">
	                                <li><a href="#"
	                                        onclick='popupPage(550,810,&quot;<html:rewrite page="/eaaps/index.jsp"/>&quot;);return false;'>
						<bean:message key="admin.admin.btnEaaps" /></a>
					</li>
                        </caisi:isModuleLoad>
			<%
				if (oscarVariables.getProperty("billregion", "").equals("ON"))
								{
			%>
			<li><a href="#"
				onclick='popupPage(660,1000,&quot;<html:rewrite page="/report/reportonbilledphcp.jsp"/>&quot;);return false;'><bean:message key="admin.admin.PHCP"/></a>
			<span style="font-size: x-small;"> (Setting: <a href="#"
				onclick='popupPage(660,1000,&quot;<html:rewrite page="/report/reportonbilledvisitprovider.jsp"/>&quot;);return false;'><bean:message key="admin.admin.provider"/></a>,
			<a href="#"
				onclick='popupPage(660,1000,&quot;<html:rewrite page="/report/reportonbilleddxgrp.jsp"/>&quot;);return false;'><bean:message key="admin.admin.dx"/>
			category</a>) </span></li>
			<%
				}
							
			%>		
			<li>
				<a href="#" onclick='popupPage(550,800,&quot;<html:rewrite page="/renal/ckd_screening_report.jsp"/>&quot;);return false;'>
					CKD Screening Report
				</a>
			</li>	
			<li>
				<a href="#" onclick='popupPage(550,800,&quot;<html:rewrite page="/renal/preImplementationSubmit.jsp"/>&quot;);return false;'>
					Pre-Implementation Report
				</a>
			</li>
			<li>
				<a href="#" onclick='popupPage(550,800,&quot;<html:rewrite page="/renal/patientLetterManager.jsp"/>&quot;);return false;'>
					Manage Patient Letter
				</a>
			</li>			
		</ul>
		</div>
	</security:oscarSec>
</caisi:isModuleLoad>
<!-- #REPORTS END -->	

<!-- #ECHART -->
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.encounter" rights="r" reverse="<%=false%>">
		
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.eChart" /></h3>
		<ul>

			<security:oscarSec roleName="<%=roleName$%>" objectName="_newCasemgmt.templates" rights="w" reverse="<%=false%>">
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/providertemplate.jsp"/>&quot;);return false;'>
				<bean:message key="admin.admin.btnInsertTemplate" /></a>
			</li>
			</security:oscarSec>
		</ul>
		</div>
	</security:oscarSec>
</caisi:isModuleLoad>
<!-- #ECHART END-->	


<%-- -add by caisi  TODO: move these under integration or system management?--%>
<caisi:isModuleLoad moduleName="caisi">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.caisi" 	rights="r" reverse="<%=false%>">
	
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.caisi" /></h3>
		<ul>
			<li><html:link action="/SystemMessage.do">
				<bean:message key="admin.admin.systemMessage" />
			</html:link></li>
			<li><html:link action="/FacilityMessage.do?"><bean:message key="admin.admin.FacilitiesMsgs"/></html:link></li>
			<li><html:link action="/issueAdmin.do?method=list">
				<bean:message key="admin.admin.issueEditor" />
			</html:link></li>
			<li><html:link action="/SurveyManager.do">
				<bean:message key="admin.admin.surveyManager" />
			</html:link></li>
			<li><html:link action="/DefaultEncounterIssue.do">
				<bean:message key="admin.admin.defaultEncounterIssue" />
			</html:link></li>
		</ul>
		</div>
	</security:oscarSec>

	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.caisi" rights="r" reverse="<%=true%>">

		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.caisi" /></h3>
		<ul>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin.systemMessage" rights="r" reverse="<%=false%>">
				<li><html:link action="/SystemMessage.do">
					<bean:message key="admin.admin.systemMessage" />
				</html:link></li>
			</security:oscarSec>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin.facilityMessage" rights="r" reverse="<%=false%>">
				<li><html:link action="/FacilityMessage.do?"><bean:message key="admin.admin.FacilitiesMsgs"/></html:link></li>
			</security:oscarSec>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin.lookupFieldEditor" rights="r">
				<li><html:link action="/Lookup/LookupTableList.do"> <bean:message key="admin.admin.LookupFieldEditor"/></html:link></li>
			</security:oscarSec>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin.issueEditor" rights="r">
				<li><html:link action="/issueAdmin.do?method=list">
					<bean:message key="admin.admin.issueEditor" />
				</html:link></li>
			</security:oscarSec>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin.userCreatedForms" rights="r">
				<li><html:link action="/SurveyManager.do">
					<bean:message key="admin.admin.surveyManager" />
				</html:link></li>
			</security:oscarSec>
		</ul>
		</div>
	</security:oscarSec>

</caisi:isModuleLoad>
<%-- -add by caisi end--%>



<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">

<!-- #Schedule Management -->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.schedule" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.ScheduleManagement" /></h3>
		<ul>
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/schedule/scheduletemplatesetting.jsp"/>&quot;);return false;'
				title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><bean:message
				key="admin.admin.scheduleSetting" /></a></li>
			<oscar:oscarPropertiesCheck property="ENABLE_EDIT_APPT_STATUS"
				value="yes">
				<li><a href="#"
					onclick="popupPage(500,600,'../appointment/appointmentstatuscontrol.jsp');return false;"
					title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><bean:message
					key="admin.admin.appointmentStatusSetting" /></a></li>
			</oscar:oscarPropertiesCheck>

			<li><a href="#" onclick ="popupPage(500,screen.width,'../appointment/appointmentTypeAction.do'); return false;"><bean:message
						key="admin.admin.appointmentTypeList" /></a></li>
						
			<li><a href="#"
				onclick='popupPage(360,600,&quot;<html:rewrite page="/admin/adminnewgroup.jsp"/>?submit=blank &quot;)'><bean:message
				key="admin.admin.btnAddGroupNoRecord" /></a></li>
			<li><a href="#"
				onclick='popupPage(360,600,&quot;<html:rewrite page="/admin/admindisplaymygroup.jsp"/> &quot;)'><bean:message
				key="admin.admin.btnSearchGroupNoRecords" /></a></li>
			<li><a href="#"
				onclick='popupPage(360,600,&quot;<html:rewrite page="/admin/groupnoacl.jsp"/>&quot;)'><bean:message
				key="admin.admin.btnGroupNoAcl" /></a></li>
                        <li><a href="#"
				onclick='popupPage(360,600,&quot;<html:rewrite page="/admin/groupPreferences.jsp"/>&quot;)'><bean:message key="admin.admin.btnGroupPreference" /></a></li>
			<li><a href="#" onclick='popupPage(800, 700,&quot;<html:rewrite page="/oscarPrevention/PreventionManager.jsp"/>&quot;);return false;' title="Customize prevention notifications."><bean:message key="admin.admin.preventionNotification.title" /></a></li>
		</ul>
		</div>
	</security:oscarSec>
<!-- #Schedule Management END-->

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.document" rights="r" reverse="<%=false%>">
<div class="adminBox">
		<h3>&nbsp;Document Management</h3>
		<ul>
<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/displayDocumentCategories.jsp"/>&quot;);return false;'><bean:message key="admin.admin.DocumentCategories"/></a></li>
<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/displayDocumentDescriptionTemplate.jsp?setDefault=true"/>&quot;);return false;'><bean:message key="admin.admin.DocumentDescriptionTemplate"/></a></li>
        </ul>
</div>
</security:oscarSec>
                                      
                        
<!-- #SYSTEM Management-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.SystemManagement" /></h3>
		<ul>
		
		<li><a href="javascript:void(0);" onclick="popupPage(550,800,&quot;<html:rewrite page="/lookupListManagerAction.do?method=manageSingle&listName=consultApptInst"/>&quot;);return false;" >
			<bean:message key="admin.admin.oscarEncounter.consult.appointmentIntructions"/></a>
		</li>
		
		<li><a href="javascript:void(0);" onclick="popupPage(550,800,&quot;<html:rewrite page="/lookupListManagerAction.do?method=manage"/>&quot;);return false;" ><bean:message key="admin.admin.lookUpLists"/></a></li>
 
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=false%>">
			<li><a href="#"
				onclick='popupPage(300,600,&quot;<html:rewrite page="/admin/providerAddRole.jsp"/>&quot;);return false;'>
			<bean:message key="admin.admin.addRole"/></a></li>
		</security:oscarSec>
					
		<li><a href="#"
			onclick='popupPage(500,800,&quot;<html:rewrite page="/admin/providerPrivilege.jsp"/>&quot;);return false;'>
		<bean:message key="admin.admin.assignRightsObject"/></a></li>
		
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/displayDocumentCategories.jsp"/>&quot;);return false;'><bean:message key="admin.admin.DocumentCategories"/></a></li>
                        
                        <li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/displayDocumentDescriptionTemplate.jsp?setDefault=true"/>&quot;);return false;'><bean:message key="admin.admin.DocumentDescriptionTemplate"/></a></li>
                                
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/ManageClinic.do"/>&quot;);return false;'><bean:message key="admin.admin.clinicAdmin"/></a></li>
			<%
				if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable())
							{
			%>
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/ManageSites.do"/>&quot;);return false;'><bean:message key="admin.admin.sitesAdmin"/></a></li>
			<%
				}
			%>
            <li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/oscarEncounter/oscarConsultationRequest/config/EditSpecialists.jsp"/>&quot;);return false;'><bean:message key="admin.admin.professionalSpecialistAdmin"/></a></li>
			
			<li><a href="#"
				onclick='popupPage(400,450,&quot;<html:rewrite page="/oscarResearch/oscarDxResearch/dxResearchCustomization.jsp"/>&quot;);return false;'><bean:message
				key="oscarEncounter.Index.btnCustomize" /> <bean:message
				key="oscar.admin.diseaseRegistryQuickList" /></a></li>
			<li><a href="#"
				onclick='popupPage(250,450,&quot;<html:rewrite page="/oscarEncounter/oscarMeasurements/Customization.jsp"/>&quot;);return false;'><bean:message
				key="oscarEncounter.Index.btnCustomize" /> <bean:message
				key="admin.admin.oscarMeasurements" /></a></li>
			<li><a href="#"
				onclick='popupPage(200,300,&quot;<html:rewrite page="/admin/resourcebaseurl.jsp"/>&quot;);return false;'
				title='<bean:message key="admin.admin.baseURLSettingTitle"/>'><bean:message
				key="admin.admin.btnBaseURLSetting" /></a></li>
		
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin,_admin.messenger" rights="r" reverse="<%=false%>">
					<li><a href="#"
						onclick='popupOscarRx(600,900,&quot;<html:rewrite page="/oscarMessenger/DisplayMessages.do"/>?providerNo=<%=curProvider_no%>&amp;userName=<%=userfirstname%>%20<%=userlastname%>&quot;);return false;'><bean:message
						key="admin.admin.messages" /></a></li>
					<li><a href="#"
						onclick='popupOscarRx(600,900,&quot;<html:rewrite page="/oscarMessenger/config/MessengerAdmin.jsp"/>&quot;);return false;'><bean:message
						key="admin.admin.btnMessengerAdmin" /></a></li>
		
			</security:oscarSec>
			
			<li><a href="#" onclick='popupPage(800,1000,&quot;<html:rewrite page="/admin/keygen/keyManager.jsp"/>&quot;);return false;'><bean:message key="admin.admin.keyPairGen" /></a></li>
			<li><a href="#" onclick='popupPage(600,600,&quot;<html:rewrite page="/FacilityManager.do"/>&quot;);return false;'><bean:message key="admin.admin.manageFacilities" /></a></li>
			<li><a href="#" onclick='popupPage(800, 1000,&quot;<html:rewrite page="/oscarEncounter/oscarMeasurements/adminFlowsheet/NewFlowsheet.jsp"/>&quot;);return false;'>Create New Flowsheet</a></li>
			<li><a href="#" onclick='popupPage(800, 1000,&quot;<html:rewrite page="/admin/manageFlowsheets.jsp"/>&quot;);return false;'><bean:message key="admin.admin.flowsheetManager"/></a></li>
	      	<li><a href="#" onclick='popupPage(800, 1000,&quot;<html:rewrite page="/admin/lotnraddrecordhtm.jsp"/>&quot;);return false;'><bean:message key="admin.admin.add_lot_nr.title"/></a></li>
			<li><a href="#" onclick='popupPage(800, 1000,&quot;<html:rewrite page="/admin/lotnrsearchrecordshtm.jsp"/>&quot;);return false;'><bean:message key="admin.lotnrsearchrecordshtm.title"/></a></li>
	      	
	      	<oscar:oscarPropertiesCheck property="LOGINTEST" value="yes">
	            <li><a href="#"
	            onclick='popupPage(800,1000,&quot;<html:rewrite page="/admin/uploadEntryText.jsp"/>&quot;);return false;'><bean:message key="admin.admin.uploadEntryTxt"/></a>
	            </li>
            </oscar:oscarPropertiesCheck>	
            
		 	<%
				if (oscar.oscarSecurity.CRHelper.isCRFrameworkEnabled())
						{
			%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin.cookieRevolver" rights="r">
		
				<li>&nbsp; <bean:message key="admin.admin.titleFactorAuth"/>
				<ul>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/ip/show');return false;"><bean:message key="admin.admin.ipFilter"/></a></li>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/cert/?act=super');return false;"><bean:message key="admin.admin.setCert"/></a></li>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/supercert');return false;"><bean:message key="admin.admin.genCert"/></a></li>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/clear');return false;"><bean:message key="admin.admin.clearCookie"/></a></li>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/quest/adminQuestions');return false;"><bean:message key="admin.admin.adminSecQuestions"/></a></li>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/policyadmin/select');return false;"><bean:message key="admin.admin.adminSecPolicies"/></a></li>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/banremover/show');return false;"><bean:message key="admin.admin.removeBans"/></a></li>
					<li><a href="#"
						onclick="popupPage(500,700,'../gatekeeper/matrixadmin/show');return false;"><bean:message key="admin.admin.genMatrixCards"/></a></li>
				</ul>
				</li>
			</security:oscarSec>
			<%
				}
			%>           	
						
		</ul>
		</div>
	</security:oscarSec>
<!-- #SYSTEM Management END-->

<!-- #SYSTEM REPORTS-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.SystemReports" /></h3>
		<ul>
		
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_admin,_admin.securityLogReport" rights="r">
			<li><a href="#"
				onclick='popupPage(500,800,&quot;<html:rewrite page="/admin/logReport.jsp"/>?keyword=admin&quot;);return false;'>
			<bean:message key="admin.admin.securityLogReport"/></a></li>
		</security:oscarSec>
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_admin, _admin.traceability" rights="r">
			<li><a href="#"
				onclick='popupPage(500,800,&quot;<html:rewrite page="/admin/traceReport.jsp"/>?keyword=admin&quot;);return false;'>
			<bean:message key="admin.admin.traceabilityReport"/></a></li>
		</security:oscarSec>
				
					
		</ul>
		</div>
	</security:oscarSec>
<!-- #SYSTEM REPORTS END-->


<!-- #INTEGRATION-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.Integration" /></h3>
		<ul>
			<li>API/Connections
				<ul>
				<li>&nbsp;<a href="#" onclick='popupPage(500,800,&quot;<html:rewrite page="/admin/api/clients.jsp"/>&quot;);return false;'>REST Clients</a></li>
				</ul>
			</li>
			<li><a href="#" onclick="popupPage(900, 500, '../setProviderStaleDate.do?method=viewIntegratorProperties');return false;"><bean:message key="provider.btnSetIntegratorPreferences" /></a></li>
			<li><a href="#" onClick="popupPage(800, 1000, '../admin/integratorPushStatus.jsp');return false;"><bean:message key="admin.admin.integratorPush" /></a></li>
			
			<li><a href="<%=request.getContextPath()%>/lab/CA/ALL/sendOruR01.jsp"><bean:message key="admin.admin.sendOruR01" /></a></li>
			<li><a href="#" onclick='popupPage(400, 400,&quot;<html:rewrite page="/hospitalReportManager/hospitalReportManager.jsp"/>&quot;);return false;'>Hospital Report Manager (HRM) Status</a></li>
			<li><a href="#" onclick='popupPage(400, 400,&quot;<html:rewrite page="/hospitalReportManager/hrmPreferences.jsp"/>&quot;);return false;'>Hospital Report Manager (HRM) Preferences</a></li>
			<li><a href="#" onclick='popupPage(400, 400,&quot;<html:rewrite page="/hospitalReportManager/hrmShowMapping.jsp"/>&quot;);return false;'>Hospital Report Manager (HRM) Class Mappings</a></li>
			<li><a href="#" onclick='popupPage(400, 400,&quot;<html:rewrite page="/hospitalReportManager/hrmCategories.jsp"/>&quot;);return false;'>Hospital Report Manager (HRM) Categories</a></li>

			<%
				String olisKeystore = OscarProperties.getInstance().getProperty("olis_keystore", "");
				if(olisKeystore.length()>0) {
			%>
			<li><a href="#" onclick='popupPage(400, 400,&quot;<html:rewrite page="/olis/Preferences.jsp"/>&quot;);return false;'>OLIS Preferences</a></li>
			<% } %>			
			<li><a href="#" onclick='popupPage(800, 1000,&quot;<html:rewrite page="/admin/MyoscarConfiguration.jsp"/>&quot;);return false;'><bean:message key="admin.admin.phrconfig"/></a></li>
			<%
				if (StringUtils.trimToNull(OscarProperties.getInstance().getProperty("oscar_myoscar_sync_component_url"))!=null)
				{
					MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
					if (myOscarLoggedInInfo!=null && myOscarLoggedInInfo.isLoggedIn())
					{
						%>
							<li><a href="#" onclick='popupPage(800, 1000,&quot;<html:rewrite page="/admin/oscar_myoscar_sync_config_redirect.jsp"/>&quot;);return false;'><bean:message key="admin.admin.oscar_phr_sync_config"/></a></li>
						<%
					}
					else
					{
						%>
							<li onclick="alert('<bean:message key="admin.admin.oscar_phr_sync_config_must_be_logged_in"/>');"><bean:message key="admin.admin.oscar_phr_sync_config"/></li>
						<%							
					}
				}
			%>

			<%
				if (oscarVariables.getProperty("hsfo.loginSiteCode", "") != null && !"".equalsIgnoreCase(oscarVariables.getProperty("hsfo.loginSiteCode", "")))
							{
			%>
			<li><a href="#"
				onclick='popupPage(400,600,&quot;<html:rewrite page="/admin/RecommitHSFO.do"/>?method=showSchedule&quot;);return false;'><bean:message key="admin.admin.hsfoSubmit"/></a></li>
			<%
				}
			%>

			  <%
                                if (oscarVariables.getProperty("hsfo2.loginSiteCode", "") != null && !"".equalsIgnoreCase(oscarVariables.getProperty("hsfo2.loginSiteCode", "")))
                                {
                        %>
                        <li><a href="#"
                                onclick='popupPage(400,600,&quot;<html:rewrite page="/admin/RecommitHSFO2.do"/>?method=showSchedule&quot;);return false;'>schedule HSFO2 XML resubmit</a></li>
                        <%
                                }
                        %>		
	
			<li><a href="javascript:void(0);" onclick="popupPage(550,800,&quot;<html:rewrite page="/admin/updateDrugref.jsp"/>&quot;);return false;" ><bean:message key="admin.admin.UpdateDrugref"/></a></li>		
		</ul>
		</div>
	</security:oscarSec>
<!-- #INTEGRATION END -->

<!-- #STATUS-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.Status" /></h3>
		<ul>
			<% if (OscarProperties.getInstance().isFaxEnabled()) { %>
            <li><a href="#" onclick='popupPage(600, 800,&quot;<html:rewrite page="/admin/faxStatus.do" />&quot;);return false;'><bean:message key="admin.faxStatus.faxStatus" /></a></li>
            <% } %>
			<li><a href="#" onclick='popupPage(800, 800,&quot;<html:rewrite page="/admin/oscarStatus.do" />&quot;);return false;'><bean:message key="admin.oscarStatus.oscarStatus" /></a></li>
			
		</ul>
		</div>
	</security:oscarSec>
<!-- #STATUS END -->
	
<!-- #Data Management -->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.backup" rights="r" reverse="<%=false%>">

		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.DataManagement" /></h3>
		<ul>
			<li><a href="#"
				onclick='popupPage(500,600,&quot;<html:rewrite page="/admin/adminbackupdownload.jsp"/>&quot;); return false;'><bean:message
				key="admin.admin.btnAdminBackupDownload" /></a></li>
				
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/demographic/demographicExport.jsp"/>&quot;);return false;'><bean:message key="admin.admin.DemoExport"/></a></li>
                        <li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/demographic/demographicImport.jsp"/>&quot;);return false;'><bean:message key="admin.admin.DemoImport"/></a></li>
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/demographicmergerecord.jsp"/>&quot;);return false;'><bean:message key="admin.admin.mergeRec"/></a></li>
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/admin/updatedemographicprovider.jsp"/>&quot;);return false;'><bean:message
				key="admin.admin.btnUpdatePatientProvider" /></a></li>
		   
		   <% if (OscarProperties.getInstance().getProperty("NEW_CONTACTS_UI","false").equals("true")) { %>
            	<li><a href="#" onclick='popupPage(800, 1000,&quot;<html:rewrite page="/demographic/migrate_demographic_contacts.jsp"/>&quot;);return false;'><bean:message key="admin.admin.migrate_contacts"/></a></li>
            <% } %>
				
		</ul>
		</div>
		
	</security:oscarSec>
<!-- #Data Management END-->



<oscar:oscarPropertiesCheck property="OSCAR_LEARNING" value="yes">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
		<div class="adminBox">
		<h3>&nbsp;<bean:message key="admin.admin.learning" /></h3>
		<ul>
		<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/oscarLearning/CourseManager.jsp"/>&quot;);return false;'><bean:message key="admin.admin.learning.manageCourses"/></a></li>
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/demographic/demographicImport.jsp"/>&quot;);return false;'><bean:message key="admin.admin.learning.importPatient"/></a></li>
			<li><a href="#"
				onclick='popupPage(550,800,&quot;<html:rewrite page="/oscarLearning/StudentImport.jsp"/>&quot;);return false;'><bean:message key="admin.admin.learning.importStudent"/></a></li>
		</ul>
		</div>
	</security:oscarSec>
</oscar:oscarPropertiesCheck>


</caisi:isModuleLoad>





<hr style="color: black;" />
<div class="logoutBox">
<%
	if (roleName$.equals("admin" + "," + curProvider_no))
		{
%><html:link
	page="/logout.jsp">
	<bean:message key="global.btnLogout" />
</html:link>&nbsp;<%
	}
%>
</div>


</body>
</html:html>
