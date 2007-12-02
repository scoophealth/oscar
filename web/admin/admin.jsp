<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%
if(session.getAttribute("user") == null ) //|| !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
	response.sendRedirect("../logout.jsp");
String curProvider_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
%>

<%@ page  errorPage="errorpage.jsp" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<% String country = request.getLocale().getCountry();
   oscar.oscarSecurity.CookieSecurity cs = new oscar.oscarSecurity.CookieSecurity();
   response.addCookie(cs.GiveMeACookie(cs.adminCookie)); %>

 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->
<html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="admin.admin.title"/> Start Time : <%=oscar.OscarProperties.getInstance().getStartTime() %></title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript" language="JavaScript" src="../share/javascript/Oscar.js"></script>
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
    a:link{
        text-decoration: none;
        color:#003399;
    }

    a:active{
        text-decoration: none;
        color:#003399;
    }

    a:visited{
        text-decoration: none;
        color:#003399;
    }

    a:hover{
        text-decoration: none;
        color:#003399;
    }

    BODY {
        font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
        background-color: #A9A9A9;
    }
    .title{
        font-size: 15pt;
        font-weight: bold;
        text-align: center;
        background-color: #000000;
        color:#FFFFFF;
    }

    div.adminBox {

       width:90%;
       background-color: #eeeeff;
       margin-top: 2px;
       margin-left:auto;
       margin-right:auto;
       margin-bottom:0px;
       padding-bottom:0px;

    }

    div.adminBox h3 {
       color : #ffffff;
       font-size:14pt;
       font-weight: bold;
       text-align: left;
       background-color:#486ebd;
       margin-top:0px;
       padding-top:0px;
       margin-bottom:0px;
       padding-bottom:0px;
    }

    div.adminBox ul{
      text-align: left;
      list-style:none;
      list-style-type:none;
      list-style-position:outside;
      padding-left:1px;
      margin-left:1px;
      margin-top:0px;
      padding-top:1px;
      margin-bottom:0px;
      padding-bottom:0px;

    }

    div.logoutBox {
       text-align: right;
    }


</style>

</head>

<body class="BodyStyle" >

  <div class="title">
  <bean:message key="admin.admin.description"/>
  </div>

  <div class="logoutBox">
     <% if(roleName$.equals("admin"+ "," +curProvider_no)) {%><a href="../logout.jsp"><bean:message key="global.btnLogout"/></a>&nbsp;<% }%>
  </div>

  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=false%>" >
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.provider"/></h3>
      <ul>
         <li><a href="provideraddarecordhtm.jsp"><bean:message key="admin.admin.btnAddProvider"/></a></li>
         <li><a href="providersearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchProvider"/></a></li>
      </ul>
  </div>

  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.groupNo"/></h3>
      <ul>
         <li><a href="#" onclick ="popupPage(360,600,'admincontrol.jsp?displaymode=newgroup&amp;submit=blank')"><bean:message key="admin.admin.btnAddGroupNoRecord"/></a></li>
         <li><a href="#" onclick ="popupPage(360,600,'admincontrol.jsp?displaymode=displaymygroup&amp;dboperation=searchmygroupall')"><bean:message key="admin.admin.btnSearchGroupNoRecords"/></a></li>
      </ul>
  </div>

  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.preference"/></h3>
      <ul>
         <li><a href="admincontrol.jsp?displaymode=Preference_Add_Record_Pre"><bean:message key="admin.admin.btnAddPreference"/></a></li>
         <li><a href="preferencesearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchPreference"/></a></li>
      </ul>
  </div>


  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.security"/></h3>
      <ul>
         <li><a href="securityaddarecord.jsp"><bean:message key="admin.admin.btnAddLogin"/></a></li>
         <li><a href="securitysearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchLogin"/></a></li>
         <li><a href="#" onclick ="popupPage(300,600,'providerAddRole.jsp');return false;">Add A Role</a></li>
         <li><a href="#" onclick ="popupPage(500,700,'providerRole.jsp');return false;">Assign Role to Provider</a></li>
         <li><a href="#" onclick ="popupPage(500,800,'providerPrivilege.jsp');return false;">Assign Role/Rights to Object</a></li>
         <li><a href="#" onclick ="popupPage(500,800,'logReport.jsp?keyword=admin');return false;">Security Log Report</a></li>
         <li><a href="#" onclick ="popupPage(500,800,'unLock.jsp');return false;">Unlock Account</a></li>
      </ul>
  </div>
  </security:oscarSec>

<% if(oscar.oscarSecurity.CRHelper.isCRFrameworkEnabled()){%>
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
  <div class="adminBox">
      <h3>&nbsp;2 Factor Authentication</h3>
      <ul>
		  <li><a href=# onClick ="popupPage(500,700,'../gatekeeper/cert/?act=super');return false;">Set super certificate</a></li>
          <li><a href=# onClick ="popupPage(500,700,'../gatekeeper/supercert');return false;">Generate super certificate</a></li>
          <li><a href=# onClick ="popupPage(500,700,'../gatekeeper/clear');return false;">Clear user cookie and super-cert cookie</a></li>
          <li><a href=# onClick ="popupPage(500,700,'../gatekeeper/quest/adminQuestions');return false;">Administrate security questions</a></li>
          <li><a href=# onClick ="popupPage(500,700,'../gatekeeper/policyadmin/select');return false;">Administrate security policies</a></li>
          <li><a href=# onClick ="popupPage(500,700,'../gatekeeper/banremover/show');return false;">Remove bans</a></li>
          <li><a href=# onClick ="popupPage(500,700,'../gatekeeper/matrixadmin/show');return false;">Generate matrix cards</a></li>
      </ul>
   </div>
</caisi:isModuleLoad>
<% } %>

<%-- -add by caisi--%>
<caisi:isModuleLoad moduleName="caisi">
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.caisi"/></h3>
      <ul>
         <li><a href="../SystemMessage.do"><bean:message key="admin.admin.systemMessage"/></a></li>
         <li><a href="../CaisiEditor.do?method=list">Caisi Editor</a></li>
         <li><a href="../issueAdmin.do?method=list"><bean:message key="admin.admin.issueEditor"/></a></li>
         <li><a href="../CaisiRole.do"><bean:message key="admin.admin.caisiRole"/></a></li>         
         <li><a href="../SurveyManager.do"><bean:message key="admin.admin.surveyManager"/></a></li>         
      </ul>
  </div>
 </caisi:isModuleLoad>
 <%-- -add by caisi end--%>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.schedule" rights="r" reverse="<%=false%>" >
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.schedule"/></h3>
      <ul>
         <li><a href="#" onclick ="popupPage(550,800,'../schedule/scheduletemplatesetting.jsp');return false;" title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><bean:message key="admin.admin.scheduleSetting"/></a></li>
      </ul>
  </div>
  </security:oscarSec>

  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.billing" rights="r" reverse="<%=false%>" >
  <%-- This links doesnt make sense on Brazil. There are other billing engines that we must use for billing --%>
  <% if (!country.equals("BR")) { %>
   <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.billing"/></h3>
      <ul>

         <% if (oscarVariables.getProperty("billregion","").equals("BC")){ %>
          <li><a href="#" onclick ="popupPage(700,1000,'../billing/manageBillingform.jsp');return false;">Manage Billing Form</a></li>
          <li><a href="#" onclick ="popupPage(600,600,'../billing/CA/BC/billingPrivateCodeAdjust.jsp');return false;">Manage Private Bill</a></li>
          <oscar:oscarPropertiesCheck property="BC_BILLING_CODE_MANAGEMENT" value="yes">
          <li><a href="#" onclick ="popupPage(600,600,'../billing/CA/BC/billingCodeAdjust.jsp');return false;">Manage Billing Codes</a></li>
          </oscar:oscarPropertiesCheck>
          <li><a href="#" onClick="popupPage(600,600,'../billing/CA/BC/showServiceCodeAssocs.do');return false;">Manage Service/Diagnostic Code Associations</a></li>
		  <li><a href="#" onClick="popupPage(600,500,'../billing/CA/BC/supServiceCodeAssocAction.do');return false;">Manage Procedure/Fee Code Associations</a></li>
          <li><a href="#" onclick ="popupPage(700,1000,'../billing/CA/BC/billingManageReferralDoc.jsp');return false;">Manage Referral Doc</a></li>
          <oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="no" defaultVal="true">
          <li><a href="#" onclick ="popupPage(700,1000,'../billing/CA/BC/billingSim.jsp');return false;">Simulate Submission File</a></li>
          <li><a href="#" onclick ="popupPage(800,720,'../billing/CA/BC/billingTeleplanGroupReport.jsp');return false;">Generate Teleplan File</a></li>
          </oscar:oscarPropertiesCheck>
          <oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="yes">
          <li><a href="#" onclick ="popupPage(700,1000,'../billing/CA/BC/TeleplanSimulation.jsp');return false;">Simulate Submission File2</a></li>
          <li><a href="#" onclick ="popupPage(800,720,'../billing/CA/BC/TeleplanSubmission.jsp');return false;">Generate Teleplan File2</a></li>
          <li><a href="#" onclick ="popupPage(800,1000,'../billing/CA/BC/teleplan/ManageTeleplan.jsp');return false;">Manage Teleplan</a></li>
          </oscar:oscarPropertiesCheck>
          <li><a href="#" onclick ="popupPage(600,800,'../billing/CA/BC/billingTA.jsp');return false;">Upload Remittance Files</a></li>
          <li><a href="#" onclick ="popupPage(600,800,'../billing/CA/BC/viewReconcileReports.jsp');return false;">MSP Reconcilliation Reports</a></li>
          <li><a href="#" onClick="popUpBillStatus(375,425,'../billing/CA/BC/billingAccountReports.jsp');return false;">Accounting Reports</a></li>
          <li><a href="#" onclick ="popupPage(800,1000,'../billing/CA/BC/billStatus.jsp');return false;">Edit Invoices</a></li>
          <li><a href="#" onclick ="popupPage(200,300,'../billing/CA/BC/settleBG.jsp');return false;">Settle Over/Under paid Claims</a></li>
         <% }else if (oscarVariables.getProperty("billregion","").equals("ON")){ %>
          <li><a href="#" onclick ="popupPage(700,1000,'../billing/CA/ON/ScheduleOfBenefitsUpload.jsp');return false;">Upload Schedule Of Benefits</a></li>
          <li><a href="#" onclick ="popupPage(300,600,'../billing/CA/ON/addEditServiceCode.jsp');return false;">Manage Billing Service Code</a></li>
          <li><a href="#" onclick ="popupPage(300,600,'../billing/CA/ON/billingONEditPrivateCode.jsp');return false;">Manage Private Billing Code</a></li>
          <li><a href="../admin/gstControl.jsp">Manage GST Control</a></li>
          <li><a href="../admin/gstreport.jsp">GST Report</a></li> 
          <li><a href="#" onclick ="popupPage(700,1000,'../billing/CA/ON/manageBillingLocation.jsp');return false;"><bean:message key="admin.admin.btnAddBillingLocation"/></a></li>
          <li><a href="#" onclick ="popupPage(700,1000,'../billing/CA/ON/manageBillingform.jsp');return false;"><bean:message key="admin.admin.btnManageBillingForm"/></a></li>
          <li><a href="#" onclick ="popupPage(700,700,'../billing/CA/ON/billingOHIPsimulation.jsp?html=');return false;"><bean:message key="admin.admin.btnSimulationOHIPDiskette"/></a></li>
          <li><a href="#" onclick ="popupPage(700,720,'../billing/CA/ON/billingOHIPreport.jsp');return false;"><bean:message key="admin.admin.btnGenerateOHIPDiskette"/></a></li>
          <li><a href="#" onclick ="popupPage(700,640,'../billing/CA/ON/billingCorrection.jsp?billing_no=');return false;"><bean:message key="admin.admin.btnBillingCorrection"/></a></li>
          <li><a href="#" onclick ="popupPage(700,640,'../billing/CA/ON/inr/reportINR.jsp?provider_no=all');return false;"><bean:message key="admin.admin.btnINRBatchBilling"/></a></li>
          <li><a href="#" onclick ="popupPage(600,900,'../billing/CA/ON/billingONUpload.jsp');return false;">Upload MOH files</a></li>
          <li><a href="#" onclick ="popupPage(600,900,'../servlet/oscar.DocumentUploadServlet');return false;"><bean:message key="admin.admin.btnBillingReconcilliation"/></a></li>
          <!--  li><a href="#" onclick ="popupPage(600,900,'../billing/CA/ON/billingRA.jsp');return false;"><bean:message key="admin.admin.btnBillingReconcilliation"/></a></li-->
          <!--  li><a href="#" onclick ="popupPage(600,1000,'../billing/CA/ON/billingOBECEA.jsp');return false;"><bean:message key="admin.admin.btnEDTBillingReportGenerator"/></a></li-->
          <oscar:oscarPropertiesCheck property="TESTING" value="yes">
             <li><a href="#" onclick ="popupPage(800,1000,'../billing/CA/ON/billStatus.jsp');return false;">Invoice Reports</a></li>
          </oscar:oscarPropertiesCheck>
         <%}%>
      </ul>
  </div>
  <% } %>
  </security:oscarSec>


  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.resource" rights="r" reverse="<%=false%>" >
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.resource"/></h3>
      <ul>
         <li><a href="#" onclick ="popupPage(200,300,'resourcebaseurl.jsp');return false;" title="<bean:message key="admin.admin.baseURLSettingTitle"/>"><bean:message key="admin.admin.btnBaseURLSetting"/></a></li>
      </ul>
  </div>
  </security:oscarSec>

  <!--oscarReport Code block -->
  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=false%>" >
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.oscarReport"/></h3>
      <ul>
         <%session.setAttribute("reportdownload", "/usr/local/tomcat/webapps/oscar_sfhc/oscarReport/download/");%>
         <% if (!country.equals("BR")) { %>
         <li><a href="#" onclick ="popupPage(600,900,'../oscarReport/RptByExample.do');return false;"><bean:message key="admin.admin.btnQueryByExample"/></a></li>
         <%}%>
         <li><a href="#" onclick="popup(600, 900, '../oscarReport/reportByTemplate/homePage.jsp', 'reportbytemplate')">Report by Template</a>
         <li><a href="#" onclick ="popupPage(600,900,'../oscarReport/dbReportAgeSex.jsp');return false;"><bean:message key="admin.admin.btnAgeSexReport"/></a></li>
         <li><a href="#" onclick ="popupPage(600,900,'../oscarReport/oscarReportVisitControl.jsp');return false;"><bean:message key="admin.admin.btnVisitReport"/></a></li>
           <%-- This links doesnt make sense on Brazil. Hide then --%>
         <% if (!country.equals("BR")) { %>
         <li><a href="#" onclick ="popupPage(600,900,'../oscarReport/oscarReportCatchment.jsp');return false;"><bean:message key="admin.admin.btnPCNCatchmentReport"/></a></li>
         <li><a href="#" onclick ="popupPage(600,900,'../oscarReport/FluBilling.do?orderby=');return false;"><bean:message key="admin.admin.btnFluBillingReport"/></a></li>
         <li><a href="#" onclick ="popupPage(600,1000,'../oscarReport/obec.jsp');return false;"><bean:message key="admin.admin.btnOvernightChecking"/></a></li>
         <% } else {%>
         <li><a href="#" onclick ="popupPage(600,750,'../report/reportactivepatientlist.jsp')" ><bean:message key="report.reportindex.btnActivePList"/></a></li>
         <% } %>
         <li><a href="#" onClick="popupPage(600,900,'../oscarSurveillance/ReportSurveillance.jsp')"><bean:message key="admin.admin.report.SurveillanceReport"/></a></li>
         <li><a href="#" onClick="popupPage(600,900,'../oscarReport/oscarReportRehabStudy.jsp')">Rehab Study</a></li>
         <li><a href="../oscarReport/TrackedLinks.jsp">Link Tracking Reports</a></li>
         <oscar:oscarPropertiesCheck property="SERVERLOGGING" value="yes" >
         <li><a href="#" onClick="popupPage(600,900, 'oscarLogging.jsp')">Server Logging</a></li>
         </oscar:oscarPropertiesCheck>
      </ul>
  </div>
  </security:oscarSec>
</caisi:isModuleLoad>

  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.backup" rights="r" reverse="<%=false%>" >
  <%-- This links doesnt make sense on Brazil. Hide then --%>
  <% if (!country.equals("BR")) { %>
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.oscarBackup"/></h3>
      <ul>
         <li><a href="#" onclick ="popupPage(500,600,'adminbackupdownload.jsp'); return false;"><bean:message key="admin.admin.btnAdminBackupDownload"/></a></li>
      </ul>
  </div>
  <% } %>
  </security:oscarSec>

  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.messenger" rights="r" reverse="<%=false%>" >
  <!--oscarMessenger Code block -->
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.oscarMessenger"/></h3>
      <ul>
         <li><a href="#" onclick ="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curProvider_no%>&amp;userName=<%=userfirstname%>%20<%=userlastname%>');return false;"><bean:message key="admin.admin.messages"/></a></li>
         <li><a href="#" onclick="popupOscarRx(600,900,'../oscarMessenger/config/MessengerAdmin.jsp');return false;"><bean:message key="admin.admin.btnMessengerAdmin"/></a></li>
      </ul>
  </div>
  </security:oscarSec>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>" >
  <!--e forms block -->
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.eForms"/></h3>
      <ul>
         <li><a href="../eform/efmformmanager.jsp"><bean:message key="admin.admin.btnUploadForm"/></a></li>
         <li><a href="../eform/efmimagemanager.jsp"><bean:message key="admin.admin.btnUploadImage"/></a></li>
         <li><a href="../eform/efmmanageformgroups.jsp">Form Groups</a></li>
      </ul>
  </div>
  </security:oscarSec>
</caisi:isModuleLoad>

  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.encounter" rights="r" reverse="<%=false%>" >
  <!--// start oscarEncounter block-->
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.oscarEncounter"/></h3>
      <ul>
        <li><a href="#" onclick ="popupPage(500,1000,'../form/setupSelect.do');return false;" ><bean:message key="admin.admin.btnSelectForm"/></a></li>
        <li><a href="#" onclick ="popupPage(500,1000,'../form/formXmlUpload.jsp');return false;" ><bean:message key="admin.admin.btnImportFormData"/></a></li>
        <li><a href="#" onclick ="popupPage(250,450,'../oscarResearch/oscarDxResearch/dxResearchCustomization.jsp');return false;" ><bean:message key="oscarEncounter.Index.btnCustomize"/> <bean:message key="oscar.admin.diseaseRegistryQuickList"/></a></li>
        <li><a href="#" onclick ="popupPage(250,450,'../oscarEncounter/oscarMeasurements/Customization.jsp');return false;" ><bean:message key="oscarEncounter.Index.btnCustomize"/> <bean:message key="admin.admin.oscarMeasurements"/></a></li>
      </ul>
  </div>
  </security:oscarSec>

  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=false%>" >
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.misc"/></h3>
      <ul>
        <li><a href="#" onClick="popupPage(550,800,'../demographic/demographicExport.jsp');return false;">Demographic Export <!-- I18N --></a></li>
                <li><a href="#" onClick="popupPage(550,800,'demographicmergerecord.jsp');return false;">Demographic Merge Records</a></li>
        <li><a href="#" onclick ="popupPage(550,800,'updatedemographicprovider.jsp');return false;" ><bean:message key="admin.admin.btnUpdatePatientProvider"/></a></li>
        <li><a href="#" onclick ="popupPage(550,800,'providertemplate.jsp');return false;" ><bean:message key="admin.admin.btnInsertTemplate"/></a></li>
        <% if (!country.equals("BR")) { %>
        <li><a href="#" onclick ="popupPage(550,810,'demographicstudysearchresults.jsp');return false;" ><bean:message key="admin.admin.btnStudy"/></a></li>
	<%   if (oscarVariables.getProperty("billregion","").equals("ON")){  %>
        <li><a href="#" onclick ="popupPage(660,1000,'../report/reportonbilledphcp.jsp');return false;">PHCP</a>
            <span style="font-size: x-small;">
               (Setting: <a href="#" onclick ="popupPage(660,1000,'../report/reportonbilledvisitprovider.jsp');return false;">provider</a>, <a href="#" onclick ="popupPage(660,1000,'../report/reportonbilleddxgrp.jsp');return false;">dx category</a>)
            </span>
        </li>
        <% } } %>
        <li><a href="#" onclick ="popupPage(800,1000,'../lab/CA/BC/LabUpload.jsp');return false;">Lab Upload</a></li>
        <li><a href="#" onclick ="popupPage(800,1000,'../lab/CA/ALL/testUploader.jsp');return false;">HL7 Lab Upload</a></li>
        <li><a href="#" onclick ="popupPage(800,1000,'../oscarKeys/keyGen.jsp');return false;">Key Pair Generator</a></li>
        <li><a href="#" onclick ="popupPage(800,1000,'labforwardingrules.jsp');return false;">Lab Forwarding Rules</a></li>
        <%if (oscarVariables.getProperty("hsfo.loginSiteCode","")!=null && 
        		!"".equalsIgnoreCase(oscarVariables.getProperty("hsfo.loginSiteCode",""))){  %>
        <li><a href="#" onclick ="popupPage(400,600,'../admin/RecommitHSFO.do?method=showSchedule');return false;">schedule HSFO XML resubmit</a></li>
        <%} %>
        
      </ul>
  </div>
  </security:oscarSec>

  <hr style=" color: black;"/>
  <div class="logoutBox">
     <% if(roleName$.equals("admin"+ "," +curProvider_no)) {%><a href="../logout.jsp"><bean:message key="global.btnLogout"/></a>&nbsp;<% }%>
  </div>


</body>
</html:html>
