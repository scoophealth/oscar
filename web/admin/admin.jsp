<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
	response.sendRedirect("../logout.jsp");
String curProvider_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
%>

<%@ page import="java.util.*,oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<% String country = request.getLocale().getCountry(); 
   oscar.oscarSecurity.CookieSecurity cs = new oscar.oscarSecurity.CookieSecurity();
   response.addCookie(cs.GiveMeACookie(cs.adminCookie)); %>



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
<title><bean:message key="admin.admin.title"/></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
<!--
function setfocus() {
}

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

//-->
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

        TABLE {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
        }
        
        TD{
            font-size:14pt;
        }

        TH{
            font-size:14pt;   
            font-weight: bold;
            text-align: left;
            background-color:#486ebd;
            color:#FFFFFF;
        }
        .title{
            font-size: 15pt;
            font-weight: bold;
            text-align: center;
            background-color: #000000;
            color:#FFFFFF;
        }
        
    </style>
</head>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
  <table cellspacing="0" cellpadding="2" width="100%" border="0">
    <tr><th  class="title"><bean:message key="admin.admin.description"/></th></tr>
  </table>
  <table border="0" cellspacing="0" cellpadding="2" width="90%">
  <tr>      
      <td align="right"><a href="../logout.jsp"><bean:message key="global.btnLogout"/></a></td>
  </tr>
    <tr bgcolor="#CCCCFF"> 
      <th> 
        <p><bean:message key="admin.admin.provider"/></p>
      </th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="provideraddarecordhtm.jsp"><bean:message key="admin.admin.btnAddProvider"/></a><br>
          <a href="providersearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchProvider"/></a></p>
        </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <th><bean:message key="admin.admin.groupNo"/></th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href=# onClick ="popupPage(360,600,'admincontrol.jsp?displaymode=newgroup&submit=blank')"><bean:message key="admin.admin.btnAddGroupNoRecord"/></a><br>
          <a href=# onClick ="popupPage(360,600,'admincontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall')"><bean:message key="admin.admin.btnSearchGroupNoRecords"/></a></p>
      </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <th colspan="2"><bean:message key="admin.admin.preference"/></th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="preferenceaddarecord.jsp"><bean:message key="admin.admin.btnAddPreference"/></a><br>
          <a href="preferencesearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchPreference"/></a></p>
      </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <th><bean:message key="admin.admin.security"/></th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="securityaddarecord.jsp"><bean:message key="admin.admin.btnAddLogin"/></a><br>
          <a href="securitysearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchLogin"/></a></p>
      </td>
    </tr>
      
    <tr bgcolor="#CCCCFF"> 
      <th> 
        <p><bean:message key="admin.admin.schedule"/></p>
      </th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
	      <a href="#" ONCLICK ="popupPage(550,800,'../schedule/scheduletemplatesetting.jsp');return false;" title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><bean:message key="admin.admin.scheduleSetting"/></a> 
         
        </td>
    </tr>
      
           <%-- This links doesnt make sense on Brazil. There are other billing engines that we must use for billing --%>
           <% if (!country.equals("BR")) { %>
    <tr bgcolor="#CCCCFF"> 
      <th> 
        <p><bean:message key="admin.admin.billing"/></p>
      </th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p>
        <% if (oscarVariables.getProperty("billregion","").equals("BC")){ %>
          <a href=# onClick ="popupPage(700,1000,'../billing/manageBillingform.jsp');return false;">Manage Billing Form</a><br>
          <a href=# onClick ="popupPage(600,600,'../billing/CA/BC/billingPrivateCodeAdjust.jsp');return false;">Manage Private Bill</a><br/>
          <a href=# onClick ="popupPage(700,1000,'../billing/CA/BC/billingSim.jsp');return false;">Simulate Submission File</a><br>
          <a href=# onClick ="popupPage(800,720,'../billing/CA/BC/billingTeleplanGroupReport.jsp');return false;">Generate Teleplan File</a><br>          
          <a href=# onClick ="popupPage(600,800,'../billing/CA/BC/billingTA.jsp');return false;">Upload Remittance Files</a><br>     
          <a href=# onClick ="popupPage(600,800,'../billing/CA/BC/viewReconcileReports.jsp');return false;">Billing Reconcilliation Reports</a><br>          
          <a href=# onClick ="popupPage(800,1000,'../billing/CA/BC/billStatus.jsp');return false;">Bill Status</a><br>  
          

       <% }else if (oscarVariables.getProperty("billregion","").equals("ON")){ %>
          <a href=# onClick ="popupPage(700,1000,'../billing/CA/ON/manageBillingLocation.jsp');return false;"><bean:message key="admin.admin.btnAddBillingLocation"/></a><br>
          <a href=# onClick ="popupPage(700,1000,'../billing/CA/ON/manageBillingform.jsp');return false;"><bean:message key="admin.admin.btnManageBillingForm"/></a><br>
          <a href=# onClick ="popupPage(700,700,'../billing/CA/ON/billingOHIPsimulation.jsp?html=');return false;"><bean:message key="admin.admin.btnSimulationOHIPDiskette"/></a><br>
        	
	  <a href=# onClick ="popupPage(700,720,'../billing/CA/ON/billingOHIPreport.jsp');return false;"><bean:message key="admin.admin.btnGenerateOHIPDiskette"/></a><br>
          <a href=# onClick ="popupPage(700,640,'../billing/CA/ON/billingCorrection.jsp?billing_no=');return false;"><bean:message key="admin.admin.btnBillingCorrection"/></a><br>
          <a href=# onClick ="popupPage(700,640,'../billing/CA/ON/inr/reportINR.jsp?provider_no=all');return false;"><bean:message key="admin.admin.btnINRBatchBilling"/></a><br>
          <a href=# onClick ="popupPage(600,900,'../billing/CA/ON/billingRA.jsp');return false;"><bean:message key="admin.admin.btnBillingReconcilliation"/></a><br>
          <a href=# onClick ="popupPage(600,1000,'../billing/CA/ON/billingOBECEA.jsp');return false;"><bean:message key="admin.admin.btnEDTBillingReportGenerator"/></a><br>         	         
       <%}%>
                   	         
        </td>
    </tr>
           <% } %>
    <%-- removed the add demographic button because it was never used, it was out of sync with the 
         doctor/receptionist version, and it was too much of a pain to keep it up to date --%>
    <%-- <tr bgcolor="#CCCCFF"> 
      <td colspan="2"><bean:message key="admin.admin.demographic"/></td>
    </tr>
    <tr bgcolor="#EEEEFF">
      <td> <a href="demographicaddarecordhtm.jsp"><bean:message key="admin.admin.btnAddDemographicRecord"/></a></td>
    </tr>--%>
    <tr bgcolor="#CCCCFF"> 
      <th> 
        <p><bean:message key="admin.admin.resource"/></p>
      </th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> <a href="#" ONCLICK ="popupPage(200,300,'resourcebaseurl.jsp');return false;" title="<bean:message key="admin.admin.baseURLSettingTitle"/>"><bean:message key="admin.admin.btnBaseURLSetting"/></a> </td>
    </tr>

  <!--oscarReport Code block -->

      <tr bgcolor="#EEEEFF">
        <th bgcolor="#CCCCFF" colspan="2"><bean:message key="admin.admin.oscarReport"/></th>
      </tr>
      <tr bgcolor="#EEEEFF">
          <td colspan="2" nowrap>
          <%      session.setAttribute("reportdownload", "/usr/local/tomcat/webapps/oscar_sfhc/oscarReport/download/"); 
          %>
            <% if (!country.equals("BR")) { %>
           <a HREF="#" ONCLICK ="popupPage(600,900,'../oscarReport/RptByExample.do');return false;"><bean:message key="admin.admin.btnQueryByExample"/></a><br>
            <% } %>
           <a HREF="#" ONCLICK ="popupPage(600,900,'../oscarReport/dbReportAgeSex.jsp');return false;"><bean:message key="admin.admin.btnAgeSexReport"/></a><br>
           <a HREF="#" ONCLICK ="popupPage(600,900,'../oscarReport/oscarReportVisitControl.jsp');return false;"><bean:message key="admin.admin.btnVisitReport"/></a><br>
           <%-- This links doesnt make sense on Brazil. Hide then --%>
           <% if (!country.equals("BR")) { %>
              <a HREF="#" ONCLICK ="popupPage(600,900,'../oscarReport/oscarReportCatchment.jsp');return false;"><bean:message key="admin.admin.btnPCNCatchmentReport"/></a><br>
              <a HREF="#" ONCLICK ="popupPage(600,900,'../oscarReport/FluBilling.do?orderby=');return false;"><bean:message key="admin.admin.btnFluBillingReport"/></a><br>
              <a href=# onClick ="popupPage(600,1000,'../oscarReport/obec.jsp');return false;"><bean:message key="admin.admin.btnOvernightChecking"/></a><br/>              
           <% } else {%>
              <a HREF="#" ONCLICK ="popupPage(600,750,'../report/reportactivepatientlist.jsp')" ><bean:message key="report.reportindex.btnActivePList"/></a><br/>
           <% } %>
              <a href="#" onclick="popupPage(600,900,'../oscarSurveillance/ReportSurveillance.jsp')"><bean:message key="admin.admin.report.SurveillanceReport"/></a>
          </td>
      </tr>

           <%-- This links doesnt make sense on Brazil. Hide then --%>
           <% if (!country.equals("BR")) { %>
<!--/oscarReport Code block -->
  <!--backup download Code block -->
  
      <tr  bgcolor="#CCCCFF">
        <th><bean:message key="admin.admin.oscarBackup"/></th>
      </tr>
      <tr bgcolor="#EEEEFF">
          <td nowrap>
              <a HREF="#" ONCLICK ="popupPage(500,600,'adminbackupdownload.jsp'); return false;"><bean:message key="admin.admin.btnAdminBackupDownload"/></a>
          </td>
      </tr>
  
<!--/backup download Code block -->
    <% } %>
<!--oscarMessenger Code block -->
  
    <tr>
      <th bgcolor="#CCCCFF"><bean:message key="admin.admin.oscarMessenger"/></th>
    </tr>
    <tr  bgcolor="#EEEEFF">
        <td nowrap>
             <a HREF="#" ONCLICK ="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curProvider_no%>&userName=<%=userfirstname%>%20<%=userlastname%>');return false;">
             <bean:message key="admin.admin.messages"/></a>
        </td>
    </tr>
       <% /*ADDED THIS FOR THE NEW OSCAR MESSENGER AUG 27 O2*/%>
    <tr  bgcolor="#EEEEFF">
       <td>
            <a href="#" onclick="popupOscarRx(600,900,'../oscarMessenger/config/MessengerAdmin.jsp');return false;"><bean:message key="admin.admin.btnMessengerAdmin"/></a>
       </td>
    </tr>
  
<!--/oscarMessenger Code block -->

<!--e forms block -->
  
    <tr bgcolor="#CCCCFF"> 
      <th><bean:message key="admin.admin.eForms"/></th>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> <a href="../eform/uploadhtml.jsp"><bean:message key="admin.admin.btnUploadForm"/></a><br>
        </td>
    </tr>
    <tr  bgcolor="#EEEEFF"> 
      <td> <a href="../eform/uploadimages.jsp"><bean:message key="admin.admin.btnUploadImage"/></a><br>
        </td>
    </tr>    
  
<!--// end e forms block -->

<!--// start oscarEncounter block-->  
    <tr bgcolor="#CCCCFF"> 
      <th><bean:message key="admin.admin.oscarEncounter"/></th>
    </tr>
    <tr bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(500,1000,'../form/setupSelect.do');return false;" ><bean:message key="admin.admin.btnSelectForm"/></a></td>
    </tr>
    <tr bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(500,1000,'../form/formXmlUpload.jsp');return false;" ><bean:message key="admin.admin.btnImportFormData"/></a></td>
    </tr>
    <tr bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(250,450,'../oscarResearch/oscarDxResearch/dxResearchCustomization.jsp');return false;" ><bean:message key="oscarEncounter.Index.btnCustomize"/> <bean:message key="oscar.admin.diseaseRegistryQuickList"/></a></td>
    </tr>
    <tr bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(250,450,'../oscarEncounter/oscarMeasurements/Customization.jsp');return false;" ><bean:message key="oscarEncounter.Index.btnCustomize"/> <bean:message key="admin.admin.oscarMeasurements"/></a></td>
    </tr>
<!--// end of oscar measuremnt block-->

    <tr bgcolor="#CCCCFF"> 
      <th><bean:message key="admin.admin.misc"/></th>
    </tr>
    <tr bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(550,800,'updatedemographicprovider.jsp');return false;" ><bean:message key="admin.admin.btnUpdatePatientProvider"/></a></td>
    </tr>  
    <tr bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(550,800,'providertemplate.jsp');return false;" ><bean:message key="admin.admin.btnInsertTemplate"/></a></td>
    </tr>

  <% if (!country.equals("BR")) { %>
    <tr  bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(550,810,'demographicstudysearchresults.jsp');return false;" ><bean:message key="admin.admin.btnStudy"/></a></td>
    </tr>
	<% if (oscarVariables.getProperty("billregion","").equals("ON")){    %>
    <tr  bgcolor="#EEEEFF">
      <td><a href="#" ONCLICK ="popupPage(660,1000,'../report/reportonbilledphcp.jsp');return false;">PHCP</a>
        <font size="-2">(Setting: <a href="#" ONCLICK ="popupPage(660,1000,'../report/reportonbilledvisitprovider.jsp');return false;">provider</a>, <a href="#" ONCLICK ="popupPage(660,1000,'../report/reportonbilleddxgrp.jsp');return false;">dx category</a>)</font></td>
    </tr>
  <% } } %>

  </table>

  <hr color='black'>
  <table border="0" cellspacing="0" cellpadding="0" width="90%">
  <tr>
      <td></td>
      <td align="right"><a href="../logout.jsp"><bean:message key="global.btnLogout"/></a></td>
  </tr>
  </table>
</center>

</body> 
</html:html>
