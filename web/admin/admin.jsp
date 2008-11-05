<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc,_admin.torontoRfq" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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


<%--
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
--%>
<html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="admin.admin.title"/> Start Time : <%=oscar.OscarProperties.getInstance().getStartTime() %></title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript"  src="../share/javascript/Oscar.js"></script>
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
     <% if(roleName$.equals("admin"+ "," +curProvider_no)) {%><html:link page="/admin/logout.jsp"><bean:message key="global.btnLogout"/></html:link>&nbsp;<% }%>
  </div>

  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.torontoRfq,_admin.provider" rights="r" reverse="<%=false%>" >
    <div class="adminBox">
	  <h3>&nbsp;<bean:message key="admin.admin.provider"/></h3>
	  <ul>
	      <li><html:link  page="/admin/provideraddarecordhtm.jsp"><bean:message key="admin.admin.btnAddProvider"/></html:link></li>
	      <li><html:link page="/admin/providersearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchProvider"/></html:link></li>
	  </ul>
    </div>
  </security:oscarSec>
  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=false%>" >
      <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
	  <div class="adminBox">
	      <h3>&nbsp;<bean:message key="admin.admin.groupNo"/></h3>
	      <ul>
		  <li><a href="#" onclick ='popupPage(360,600,&quot;<html:rewrite page="/admin/admincontrol.jsp"/>?displaymode=newgroup&amp;submit=blank &quot;)'><bean:message key="admin.admin.btnAddGroupNoRecord"/></a></li>
		  <li><a href="#" onclick ='popupPage(360,600,&quot;<html:rewrite page="/admin/admincontrol.jsp"/>?displaymode=displaymygroup&amp;dboperation=searchmygroupall &quot;)'><bean:message key="admin.admin.btnSearchGroupNoRecords"/></a></li>
	      </ul>
	  </div>

	  <div class="adminBox">
	      <h3>&nbsp;<bean:message key="admin.admin.preference"/></h3>
	      <ul>
		  <li><html:link page="/admin/admincontrol.jsp?displaymode=Preference_Add_Record_Pre"><bean:message key="admin.admin.btnAddPreference"/></html:link></li>
		  <li><html:link page="/admin/preferencesearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchPreference"/></html:link></li>
	      </ul>
	  </div>
      </caisi:isModuleLoad>
  </security:oscarSec>      
  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.torontoRfq,_admin.security" rights="r" reverse="<%=false%>" >
      
      
      <div class="adminBox">
	  <h3>&nbsp;<bean:message key="admin.admin.security"/></h3>
	  <ul>
	      <li><html:link page="/admin/securityaddarecord.jsp"><bean:message key="admin.admin.btnAddLogin"/></html:link></li>
	      <li><html:link page="/admin/securitysearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchLogin"/></html:link></li>
	      <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=false%>" >
		  <li><a href="#" onclick ='popupPage(300,600,&quot;<html:rewrite page="/admin/providerAddRole.jsp"/>&quot;);return false;'>Add A Role</a></li>
	      </security:oscarSec>
	      <li><a href="#" onclick = 'popupPage(500,700,&quot;<html:rewrite page="/admin/providerRole.jsp"/>&quot;);return false;'>Assign Role to Provider</a></li>
	      <li><a href="#" onclick = 'popupPage(500,800,&quot;<html:rewrite page="/admin/providerPrivilege.jsp"/>&quot;);return false;'>Assign Role/Rights to Object</a></li>
	      
	      <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.securityLogReport" rights="r">
	      <li><a href="#" onclick = 'popupPage(500,800,&quot;<html:rewrite page="/admin/logReport.jsp"/>?keyword=admin&quot;);return false;'>Security Log Report</a></li>
	      </security:oscarSec>
	      <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.unlockAccount" rights="r">
	      <li><a href="#" onclick = 'popupPage(500,800,&quot;<html:rewrite page="/admin/unLock.jsp" />&quot;);return false;'>Unlock Account</a></li>
	      </security:oscarSec>
	  </ul>
      </div>
      
      
      <% if(oscar.oscarSecurity.CRHelper.isCRFrameworkEnabled()){%>
      <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.cookieRevolver" rights="r">
      <div class="adminBox">
	  <h3>&nbsp;2 Factor Authentication</h3>
	  <ul>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/ip/show');return false;">Set IP filter (no super certificate)</a></li>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/cert/?act=super');return false;">Set super certificate</a></li>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/supercert');return false;">Generate super certificate</a></li>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/clear');return false;">Clear user cookie and super-cert cookie</a></li>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/quest/adminQuestions');return false;">Administrate security questions</a></li>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/policyadmin/select');return false;">Administrate security policies</a></li>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/banremover/show');return false;">Remove bans</a></li>
	      <li><a href="#" onclick = "popupPage(500,700,'../gatekeeper/matrixadmin/show');return false;">Generate matrix cards</a></li>
	  </ul>
      </div>
      </security:oscarSec>
      <% } %>
  </security:oscarSec> 
  
      <%-- -add by caisi--%>
      
      <caisi:isModuleLoad moduleName="caisi">      
      <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.caisi" rights="r" reverse="<%=false%>" >	      
	  <div class="adminBox">
	      <h3>&nbsp;<bean:message key="admin.admin.caisi"/></h3>
	      <ul>
	      <li><html:link action="/SystemMessage.do"><bean:message key="admin.admin.systemMessage"/></html:link></li>
		  <li><html:link action="/FacilityMessage.do?">Facilities Messages</html:link></li>
          <li><html:link action="/Lookup/LookupTableList.do"> Lookup Field Editor</html:link></li>
		  <li><html:link action="/issueAdmin.do?method=list"><bean:message key="admin.admin.issueEditor"/></html:link></li>
		  <li><html:link action="/CaisiRole.do"><bean:message key="admin.admin.caisiRole"/></html:link></li>         
		  <li><html:link action="/SurveyManager.do"><bean:message key="admin.admin.surveyManager"/></html:link></li>         
	      </ul>
	  </div>
	  </security:oscarSec>	  
	  
	  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.caisi" rights="r" reverse="<%=true%>" >	      
	  
	  <div class="adminBox">
	      <h3>&nbsp;<bean:message key="admin.admin.caisi"/></h3>
	      <ul>
	      <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.systemMessage" rights="r" reverse="<%=false%>" >
		  <li><html:link action="/SystemMessage.do"><bean:message key="admin.admin.systemMessage"/></html:link></li>
		  </security:oscarSec>
		  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.facilityMessage" rights="r" reverse="<%=false%>" >
		  <li><html:link action="/FacilityMessage.do?">Facilities Messages</html:link></li>
		  </security:oscarSec>
          <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.lookupFieldEditor" rights="r">
          <li><html:link action="/Lookup/LookupTableList.do"> Lookup Field Editor</html:link></li>
		  </security:oscarSec>		  
		  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.issueEditor" rights="r">
		  <li><html:link action="/issueAdmin.do?method=list"><bean:message key="admin.admin.issueEditor"/></html:link></li>
		  </security:oscarSec>		  
		  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.caisiRoles" rights="r">
		  <li><html:link action="/CaisiRole.do"><bean:message key="admin.admin.caisiRole"/></html:link></li>         
		  </security:oscarSec>
		  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin.userCreatedForms" rights="r">
		  <li><html:link action="/SurveyManager.do"><bean:message key="admin.admin.surveyManager"/></html:link></li>         
	      </security:oscarSec>
	      </ul>
	  </div>
	  </security:oscarSec>
	  
      </caisi:isModuleLoad>     
      <%-- -add by caisi end--%>
    
  
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
  <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.schedule" rights="r" reverse="<%=false%>" >
  <div class="adminBox">
      <h3>&nbsp;<bean:message key="admin.admin.schedule"/></h3>
      <ul>
         <li><a href="#" onclick = 'popupPage(550,800,&quot;<html:rewrite page="/schedule/scheduletemplatesetting.jsp"/>&quot;);return false;' title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><bean:message key="admin.admin.scheduleSetting"/></a></li>
         <oscar:oscarPropertiesCheck property="ENABLE_EDIT_APPT_STATUS" value="yes">  
         <li><a href="#" onclick ="popupPage(500,600,'../appointment/appointmentstatuscontrol.jsp');return false;" title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><bean:message key="admin.admin.appointmentStatusSetting"/></a></li>
         </oscar:oscarPropertiesCheck> 
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
          <li><a href="#" onclick ='popupPage(700,1000,&quot;<html:rewrite page="/billing/manageBillingform.jsp"/>&quot;);return false;'>Manage Billing Form</a></li>
          <li><a href="#" onclick ='popupPage(600,600,&quot;<html:rewrite page="/billing/CA/BC/billingPrivateCodeAdjust.jsp"/>&quot;);return false;'>Manage Private Bill</a></li>
          <oscar:oscarPropertiesCheck property="BC_BILLING_CODE_MANAGEMENT" value="yes">
          <li><a href="#" onclick ='popupPage(600,600,&quot;<html:rewrite page="/billing/CA/BC/billingCodeAdjust.jsp"/>&quot;);return false;'>Manage Billing Codes</a></li>
          </oscar:oscarPropertiesCheck>
          <li><a href="#" onclick='popupPage(600,600,&quot;<html:rewrite page="/billing/CA/BC/showServiceCodeAssocs.do"/>&quot;);return false;'>Manage Service/Diagnostic Code Associations</a></li>
		  <li><a href="#" onclick='popupPage(600,500,&quot;<html:rewrite page="/billing/CA/BC/supServiceCodeAssocAction.do"/>&quot;);return false;'>Manage Procedure/Fee Code Associations</a></li>
          <li><a href="#" onclick ='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/BC/billingManageReferralDoc.jsp"/>&quot;);return false;'>Manage Referral Doc</a></li>
          <oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="no" defaultVal="true">
          <li><a href="#" onclick ='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/BC/billingSim.jsp"/>&quot;);return false;'>Simulate Submission File</a></li>
          <li><a href="#" onclick ='popupPage(800,720,&quot;<html:rewrite page="/billing/CA/BC/billingTeleplanGroupReport.jsp"/>&quot;);return false;'>Generate Teleplan File</a></li>
          </oscar:oscarPropertiesCheck>
          <oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="yes">
          <li><a href="#" onclick ='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/BC/TeleplanSimulation.jsp"/>&quot;);return false;'>Simulate Submission File2</a></li>
          <li><a href="#" onclick ='popupPage(800,720,&quot;<html:rewrite page="/billing/CA/BC/TeleplanSubmission.jsp"/>&quot;);return false;'>Generate Teleplan File2</a></li>
          <li><a href="#" onclick ='popupPage(800,1000,&quot;<html:rewrite page="/billing/CA/BC/teleplan/ManageTeleplan.jsp"/>&quot;);return false;'>Manage Teleplan</a></li>
          </oscar:oscarPropertiesCheck>
          <oscar:oscarPropertiesCheck property="NEW_BC_TELEPLAN" value="no" defaultVal="true">
          <li><a href="#" onclick ='popupPage(600,800,&quot;<html:rewrite page="/billing/CA/BC/billingTA.jsp"/>&quot;);return false;'>Upload Remittance Files</a></li>
          </oscar:oscarPropertiesCheck>
          <li><a href="#" onclick ='popupPage(600,800,&quot;<html:rewrite page="/billing/CA/BC/viewReconcileReports.jsp"/>&quot;);return false;'>MSP Reconcilliation Reports</a></li>
          <li><a href="#" onclick ='popUpBillStatus(375,425,&quot;<html:rewrite page="/billing/CA/BC/billingAccountReports.jsp"/>&quot;);return false;'>Accounting Reports</a></li>
          <li><a href="#" onclick ='popupPage(800,1000,&quot;<html:rewrite page="/billing/CA/BC/billStatus.jsp"/>&quot;);return false;'>Edit Invoices</a></li>
          <li><a href="#" onclick ='popupPage(200,300,&quot;<html:rewrite page="/billing/CA/BC/settleBG.jsp"/>&quot;);return false;'>Settle Over/Under paid Claims</a></li>
         <% }else if (oscarVariables.getProperty("billregion","").equals("ON")){ %>
          <li><a href="#" onclick ='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/ON/ScheduleOfBenefitsUpload.jsp"/>&quot;);return false;'>Upload Schedule Of Benefits</a></li>
          <li><a href="#" onclick ='popupPage(300,600,&quot;<html:rewrite page="/billing/CA/ON/addEditServiceCode.jsp"/>&quot;);return false;'>Manage Billing Service Code</a></li>
          <li><a href="#" onclick ='popupPage(300,600,&quot;<html:rewrite page="/billing/CA/ON/billingONEditPrivateCode.jsp"/>&quot;);return false;'>Manage Private Billing Code</a></li>
          <li><html:link page="/admin/../admin/gstControl.jsp">Manage GST Control</html:link></li>
          <li><html:link page="/admin/../admin/gstreport.jsp">GST Report</html:link></li> 
          <li><a href="#" onclick ='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/ON/manageBillingLocation.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnAddBillingLocation"/></a></li>
          <li><a href="#" onclick ='popupPage(700,1000,&quot;<html:rewrite page="/billing/CA/ON/manageBillingform.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnManageBillingForm"/></a></li>
          <li><a href="#" onclick ='popupPage(700,700,&quot;<html:rewrite page="/billing/CA/ON/billingOHIPsimulation.jsp"/>?html=&quot;);return false;'><bean:message key="admin.admin.btnSimulationOHIPDiskette"/></a></li>
          <li><a href="#" onclick ='popupPage(700,720,&quot;<html:rewrite page="/billing/CA/ON/billingOHIPreport.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnGenerateOHIPDiskette"/></a></li>
          <li><a href="#" onclick ='popupPage(700,640,&quot;<html:rewrite page="/billing/CA/ON/billingCorrection.jsp"/>?billing_no=&quot;);return false;'><bean:message key="admin.admin.btnBillingCorrection"/></a></li>
          <li><a href="#" onclick ='popupPage(700,640,&quot;<html:rewrite page="/billing/CA/ON/inr/reportINR.jsp"/>?provider_no=all&quot;);return false;'><bean:message key="admin.admin.btnINRBatchBilling"/></a></li>
          <li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/billing/CA/ON/billingONUpload.jsp"/>&quot;);return false;'>Upload MOH files</a></li>
          <li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/servlet/oscar.DocumentUploadServlet"/>&quot;);return false;'><bean:message key="admin.admin.btnBillingReconcilliation"/></a></li>
          <!--  li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/billing/CA/ON/billingRA.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnBillingReconcilliation"/></a></li-->
          <!--  li><a href="#" onclick ='popupPage(600,1000,&quot;<html:rewrite page="/billing/CA/ON/billingOBECEA.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnEDTBillingReportGenerator"/></a></li-->
          <oscar:oscarPropertiesCheck property="TESTING" value="yes">
             <li><a href="#" onclick ='popupPage(800,1000,&quot;<html:rewrite page="/billing/CA/ON/billStatus.jsp"/>&quot;);return false;'>Invoice Reports</a></li>
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
         <li><a href="#" onclick ='popupPage(200,300,&quot;<html:rewrite page="/admin/resourcebaseurl.jsp"/>&quot;);return false;' title='<bean:message key="admin.admin.baseURLSettingTitle"/>'><bean:message key="admin.admin.btnBaseURLSetting"/></a></li>
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
         <li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/RptByExample.do"/>&quot;);return false;'><bean:message key="admin.admin.btnQueryByExample"/></a></li>
         <%}%>
         <li><a href="#" onclick ='popup(600,900,&quot;<html:rewrite page="/oscarReport/reportByTemplate/homePage.jsp"/>&quot;, "reportbytemplate")'>Report by Template</a></li>
         <li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/dbReportAgeSex.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnAgeSexReport"/></a></li>
         <li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/oscarReportVisitControl.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnVisitReport"/></a></li>
           <%-- This links doesnt make sense on Brazil. Hide then --%>
         <% if (!country.equals("BR")) { %>
         <li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/oscarReportCatchment.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnPCNCatchmentReport"/></a></li>
         <li><a href="#" onclick ='popupPage(600,900,&quot;<html:rewrite page="/oscarReport/FluBilling.do"/>?orderby=&quot;);return false;'><bean:message key="admin.admin.btnFluBillingReport"/></a></li>
         <li><a href="#" onclick ='popupPage(600,1000,&quot;<html:rewrite page="/oscarReport/obec.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnOvernightChecking"/></a></li>
         <% } else {%>
         <li><a href="#" onclick ='popupPage(600,750,&quot;<html:rewrite page="/report/reportactivepatientlist.jsp"/>&quot;)'><bean:message key="report.reportindex.btnActivePList"/></a></li>
         <% } %>
         <li><a href="#" onclick="popupPage(600,900,&quot;<html:rewrite page="/oscarSurveillance/ReportSurveillance.jsp"/>&quot;)"><bean:message key="admin.admin.report.SurveillanceReport"/></a></li>
         <li><a href="#" onclick="popupPage(600,900,&quot;<html:rewrite page="/oscarReport/oscarReportRehabStudy.jsp"/>&quot;)">Rehab Study</a></li>
         <li><a href="#" onclick="popupPage(600,900,&quot;<html:rewrite page="/oscarReport/patientlist.jsp"/>&quot;)">Export Patient List by Appointment Time</a></li>
         <li><html:link page="/oscarReport/TrackedLinks.jsp">Link Tracking Report</html:link></li>
         <li><html:link page="/PMmodule/reports/activity_report_form.jsp">Activity Report</html:link></li>
         <li><html:link page="/oscarReport/provider_service_report_form.jsp">Provider Service Report</html:link></li>
         <li><html:link page="/PopulationReport.do">Population Report</html:link></li>
         <oscar:oscarPropertiesCheck property="SERVERLOGGING" value="yes" >
         <li><a href="#" onclick="popupPage(600,900, &quot;<html:rewrite page="/admin/oscarLogging.jsp"/>&quot;)">Server Logging</a></li>
         </oscar:oscarPropertiesCheck>
      </ul>
  </div>
  </security:oscarSec>
</caisi:isModuleLoad>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
    <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.backup" rights="r" reverse="<%=false%>" >
	<%-- This links doesnt make sense on Brazil. Hide then --%>
	<% if (!country.equals("BR")) { %>
	<div class="adminBox">
	    <h3>&nbsp;<bean:message key="admin.admin.oscarBackup"/></h3>
	    <ul>
		<li><a href="#" onclick ='popupPage(500,600,&quot;<html:rewrite page="/admin/adminbackupdownload.jsp"/>&quot;); return false;'><bean:message key="admin.admin.btnAdminBackupDownload"/></a></li>
	    </ul>
	</div>
	<% } %>
    </security:oscarSec>
    <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.messenger" rights="r" reverse="<%=false%>" >
	<!--oscarMessenger Code block -->
	<div class="adminBox">
	    <h3>&nbsp;<bean:message key="admin.admin.oscarMessenger"/></h3>
	    <ul>
		<li><a href="#" onclick ='popupOscarRx(600,900,&quot;<html:rewrite page="/oscarMessenger/DisplayMessages.do"/>?providerNo=<%=curProvider_no%>&amp;userName=<%=userfirstname%>%20<%=userlastname%>&quot;);return false;'><bean:message key="admin.admin.messages"/></a></li>
		<li><a href="#" onclick ='popupOscarRx(600,900,&quot;<html:rewrite page="/oscarMessenger/config/MessengerAdmin.jsp"/>&quot;);return false;'><bean:message key="admin.admin.btnMessengerAdmin"/></a></li>
	    </ul>
	</div>
    </security:oscarSec>
    <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>" >
	<!--e forms block -->
	<div class="adminBox">
	    <h3>&nbsp;<bean:message key="admin.admin.eForms"/></h3>
	    <ul>
		<li><html:link page="/admin/../eform/efmformmanager.jsp"><bean:message key="admin.admin.btnUploadForm"/></html:link></li>
		<li><html:link page="/admin/../eform/efmimagemanager.jsp"><bean:message key="admin.admin.btnUploadImage"/></html:link></li>
		<li><html:link page="/admin/../eform/efmmanageformgroups.jsp">Form Groups</html:link></li>
	    </ul>
	</div>
    </security:oscarSec>
    <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.encounter" rights="r" reverse="<%=false%>" >
	<!--// start oscarEncounter block-->
	<div class="adminBox">
	    <h3>&nbsp;<bean:message key="admin.admin.oscarEncounter"/></h3>
	    <ul>
		<li><a href="#" onclick ='popupPage(500,1000,&quot;<html:rewrite page="/form/setupSelect.do"/>&quot;);return false;' ><bean:message key="admin.admin.btnSelectForm"/></a></li>
		<li><a href="#" onclick ='popupPage(500,1000,&quot;<html:rewrite page="/form/formXmlUpload.jsp"/>&quot;);return false;' ><bean:message key="admin.admin.btnImportFormData"/></a></li>
		<li><a href="#" onclick ='popupPage(250,450,&quot;<html:rewrite page="/oscarResearch/oscarDxResearch/dxResearchCustomization.jsp"/>&quot;);return false;' ><bean:message key="oscarEncounter.Index.btnCustomize"/> <bean:message key="oscar.admin.diseaseRegistryQuickList"/></a></li>
		<li><a href="#" onclick ='popupPage(250,450,&quot;<html:rewrite page="/oscarEncounter/oscarMeasurements/Customization.jsp"/>&quot;);return false;'><bean:message key="oscarEncounter.Index.btnCustomize"/> <bean:message key="admin.admin.oscarMeasurements"/></a></li>
	    </ul>
	</div>
    </security:oscarSec>
    <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=false%>" >
	<div class="adminBox">
	    <h3>&nbsp;<bean:message key="admin.admin.misc"/></h3>
	    <ul>
                <li><a href="#" onclick ='popupPage(550,800,&quot;<html:rewrite page="/admin/ManageClinic.do"/>&quot;);return false;'>Clinic Admin</a></li>
		<li><a href="#" onclick ='popupPage(550,800,&quot;<html:rewrite page="/demographic/demographicExport.jsp"/>&quot;);return false;'>Demographic Export <!-- I18N --></a></li>
		<li><a href="#" onclick ='popupPage(550,800,&quot;<html:rewrite page="/admin/demographicmergerecord.jsp"/>&quot;);return false;'>Demographic Merge Records</a></li>
		<li><a href="#" onclick ='popupPage(550,800,&quot;<html:rewrite page="/admin/updatedemographicprovider.jsp"/>&quot;);return false;' ><bean:message key="admin.admin.btnUpdatePatientProvider"/></a></li>
		<li><a href="#" onclick ='popupPage(550,800,&quot;<html:rewrite page="/admin/providertemplate.jsp"/>&quot;);return false;' ><bean:message key="admin.admin.btnInsertTemplate"/></a></li>
		<% if (!country.equals("BR")) { %>
		<li><a href="#" onclick ='popupPage(550,810,&quot;<html:rewrite page="/admin/demographicstudysearchresults.jsp"/>&quot;);return false;' ><bean:message key="admin.admin.btnStudy"/></a></li>
		<%   if (oscarVariables.getProperty("billregion","").equals("ON")){  %>
		<li><a href="#" onclick ='popupPage(660,1000,&quot;<html:rewrite page="/report/reportonbilledphcp.jsp"/>&quot;);return false;'>PHCP</a>
		    <span style="font-size: x-small;">
			(Setting: <a href="#" onclick ='popupPage(660,1000,&quot;<html:rewrite page="/report/reportonbilledvisitprovider.jsp"/>&quot;);return false;'>provider</a>, <a href="#" onclick ='popupPage(660,1000,&quot;<html:rewrite page="/report/reportonbilleddxgrp.jsp"/>&quot;);return false;'>dx category</a>)
		    </span>
		</li>
		<% } } %>
		<oscar:oscarPropertiesCheck property="OLD_LAB_UPLOAD" value="yes" defaultVal="false">
		    <li><a href="#" onclick ='popupPage(800,1000,&quot;<html:rewrite page="/lab/CA/BC/LabUpload.jsp"/>&quot;);return false;'>Lab Upload</a></li>
		</oscar:oscarPropertiesCheck>
		<li><a href="#" onclick ='popupPage(800,1000,&quot;<html:rewrite page="/lab/CA/ALL/testUploader.jsp"/>&quot;);return false;'>HL7 Lab Upload</a></li>
		<li><a href="#" onclick ='popupPage(800,1000,&quot;<html:rewrite page="/oscarKeys/keyGen.jsp"/>&quot;);return false;'>Key Pair Generator</a></li>
		<li><a href="#" onclick ='popupPage(800,1000,&quot;<html:rewrite page="/admin/labforwardingrules.jsp"/>&quot;);return false;'>Lab Forwarding Rules</a></li>
		<%if (oscarVariables.getProperty("hsfo.loginSiteCode","")!=null && 
		!"".equalsIgnoreCase(oscarVariables.getProperty("hsfo.loginSiteCode",""))){  %>
		<li><a href="#" onclick ='popupPage(400,600,&quot;<html:rewrite page="/admin/RecommitHSFO.do"/>?method=showSchedule&quot;);return false;'>schedule HSFO XML resubmit</a></li>
		<%} %>
		
	    </ul>
	</div>
    </security:oscarSec>
</caisi:isModuleLoad>
  <hr style=" color: black;"/>
  <div class="logoutBox">
     <% if(roleName$.equals("admin"+ "," +curProvider_no)) {%><html:link page="/logout.jsp"><bean:message key="global.btnLogout"/></html:link>&nbsp;<% }%>
  </div>


</body>
</html:html>
