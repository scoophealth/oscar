<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
    response.sendRedirect("../logout.jsp");
  String curProvider_no,userfirstname,userlastname;
  curProvider_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<%@ page import="java.util.*,oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<% String country = request.getLocale().getCountry(); %>
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

//<!--oscarMessenger code block-->
function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarRx", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
//<!--/oscarMessenger code block -->



function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}

    //-->
    </script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
  <table cellspacing="0" cellpadding="2" width="100%" border="0">
    <tr><th align="CENTER" bgcolor="#CCCCFF"><bean:message key="admin.admin.description"/></th></tr>
  </table>
  <table border="0" cellspacing="0" cellpadding="2" width="90%">
  <tr>      
      <td align="right"><a href="../logout.jsp"><bean:message key="global.btnLogout"/></a></td>
  </tr>
    <tr bgcolor="#CCCCFF"> 
      <td> 
        <p><bean:message key="admin.admin.provider"/></p>
      </td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="provideraddarecordhtm.jsp"><bean:message key="admin.admin.btnAddProvider"/></a><br>
          <a href="providersearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchProvider"/></a></p>
        </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td><bean:message key="admin.admin.groupNo"/></td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href=# onClick ="popupPage(360,600,'admincontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall')"><bean:message key="admin.admin.btnAddGroupNoRecord"/></a><br>
          <a href=# onClick ="popupPage(360,600,'admincontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall')"><bean:message key="admin.admin.btnSearchGroupNoRecords"/></a></p>
      </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"><bean:message key="admin.admin.preference"/></td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="preferenceaddarecord.jsp"><bean:message key="admin.admin.btnAddPreference"/></a><br>
          <a href="preferencesearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchPreference"/></a></p>
      </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td><bean:message key="admin.admin.security"/></td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="securityaddarecord.jsp"><bean:message key="admin.admin.btnAddLogin"/></a><br>
          <a href="securitysearchrecordshtm.jsp"><bean:message key="admin.admin.btnSearchLogin"/></a></p>
      </td>
    </tr>
      
    <tr bgcolor="#CCCCFF"> 
      <td> 
        <p><bean:message key="admin.admin.schedule"/></p>
      </td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
	      <a href="#" ONCLICK ="popupPage(550,800,'../schedule/scheduletemplatesetting.jsp');return false;" title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><bean:message key="admin.admin.scheduleSetting"/></a> 
         
        </td>
    </tr>
      
    <tr bgcolor="#CCCCFF"> 
      <td> 
        <p><bean:message key="admin.admin.billing"/></p>
      </td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p>
        <% if (oscarVariables.getProperty("billregion","").equals("BC")){ %>
          <a href=# onClick ="popupPage(700,1000,'../billing/manageBillingform.jsp');return false;">Manage Billing Form</a><br>
          <a href=# onClick ="popupPage(800,720,'../billing/CA/BC/billingTeleplanGroupReport.jsp');return false;">Generate Teleplan Diskette</a><br>
          <a href=# onClick ="popupPage(800,720,'../billing/CA/BC/billingTeleplanCorrection.jsp?billing_no=');return false;">Billing Correction</a><br>
          <a href=# onClick ="popupPage(600,800,'../billing/CA/BC/billingTA.jsp');return false;">Billing Reconcilliation</a><br>          
       <% }else{ %>
          <a href=# onClick ="popupPage(700,1000,'../billing/manageBillingLocation.jsp');return false;"><bean:message key="admin.admin.btnAddBillingLocation"/></a><br>
          <a href=# onClick ="popupPage(700,1000,'../billing/manageBillingform.jsp');return false;"><bean:message key="admin.admin.btnManageBillingForm"/></a><br>
          <a href=# onClick ="popupPage(800,700,'../billing/billingOHIPsimulation.jsp?html=');return false;"><bean:message key="admin.admin.btnSimulationOHIPDiskette"/></a><br>
        	
	  <a href=# onClick ="popupPage(800,720,'../billing/billingOHIPreport.jsp');return false;"><bean:message key="admin.admin.btnGenerateOHIPDiskette"/></a><br>
          <a href=# onClick ="popupPage(800,640,'../billing/billingCorrection.jsp?billing_no=');return false;"><bean:message key="admin.admin.btnBillingCorrection"/></a><br>
          <a href=# onClick ="popupPage(800,640,'../billing/inr/reportINR.jsp?provider_no=all');return false;"><bean:message key="admin.admin.btnINRBatchBilling"/></a><br>
          <a href=# onClick ="popupPage(600,800,'../billing/billingRA.jsp');return false;"><bean:message key="admin.admin.btnBillingReconcilliation"/></a><br>
          <a href=# onClick ="popupPage(600,1000,'../billing/billingEA.jsp');return false;"><bean:message key="admin.admin.btnEDTBillingReportGenerator"/></a><br>         	         
       <%}%>
                   	         
        </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"><bean:message key="admin.admin.demographic"/></td>
    </tr>
    <tr bgcolor="#EEEEFF">
      <td> <a href="demographicaddarecordhtm.jsp"><bean:message key="admin.admin.btnAddDemographicRecord"/></a><br>
        <!--a href="demographicsearch.htm"><bean:message key="admin.admin.btnSearchDemographicRecord"/></a--> </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td> 
        <p><bean:message key="admin.admin.resource"/></p>
      </td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> <a href="#" ONCLICK ="popupPage(200,300,'resourcebaseurl.jsp');return false;" title="<bean:message key="admin.admin.baseURLSettingTitle"/>"><bean:message key="admin.admin.btnBaseURLSetting"/></a> </td>
    </tr>

  <!--oscarReport Code block -->

      <tr bgcolor="#EEEEFF">
        <td bgcolor="#CCCCFF" colspan="2"><bean:message key="admin.admin.oscarReport"/></td>
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
              <a href=# onClick ="popupPage(600,1000,'../oscarReport/obec.do');return false;"><bean:message key="admin.admin.btnOvernightChecking"/></a><br>
              <a href=# onClick ="popupPage(600,1000,'../billing/billingOBECEA.jsp');return false;"><bean:message key="admin.admin.btnOBECGenerator"/></a><br>
           <% } %>
          </td>
      </tr>

           <%-- This links doesnt make sense on Brazil. Hide then --%>
           <% if (!country.equals("BR")) { %>
<!--/oscarReport Code block -->
  <!--backup download Code block -->
  
      <tr  bgcolor="#CCCCFF">
        <td><bean:message key="admin.admin.oscarBackup"/></td>
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
      <td bgcolor="#CCCCFF"><bean:message key="admin.admin.oscarMessenger"/></td>
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
      <td><bean:message key="admin.admin.eForms"/></td>
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
  
    <tr bgcolor="#CCCCFF"> 
      <td><bean:message key="admin.admin.misc"/></td>
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
  <% } %>

  </table>

  <hr color='orange'>
  <table border="0" cellspacing="0" cellpadding="0" width="90%">
  <tr>
      <td></td>
      <td align="right"><a href="../logout.jsp"><bean:message key="global.btnLogout"/> <img src="../images/rightarrow.gif"  border="0" width="25" height="20" align="absmiddle"></a></td>
  </tr>
  </table>
</center>

</body> 
</html:html>
