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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ taglib uri="../WEB-INF/msg-tag.tld" prefix="oscarmessage" %>

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

<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ADMIN PAGE</title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--
		function setfocus() {
		}

    function onsub() {
      if(document.searchprovider.keyword.value=="") {
        alert("You forgot to input a keyword!");
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
    <tr><th align="CENTER" bgcolor="#CCCCFF">ADMINISTRATIVE PAGE</th></tr>
  </table>
  <table border="0" cellspacing="0" cellpadding="0" width="90%">
  <tr>
      <td></td>
      <td align="right"><a href="../logout.jsp">Log Out </a></td>
  </tr>
  </table>
  
  <table cellspacing="0" cellpadding="2" width="90%" border="0">
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"> 
        <p>Provider</p>
      </td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="provideraddarecord.htm">Add a Provider Record</a><br>
          <a href="providersearchrecords.htm">Search/Edit/Delete Provider Records</a></p>
        </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2">Group No</td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href=# onClick ="popupPage(360,600,'admincontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall')">Add a Group No Record</a><br>
          <a href=# onClick ="popupPage(360,600,'admincontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall')">Search/Edit/Delete Group No Records</a></p>
      </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"> Preference</td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="preferenceaddarecord.jsp">Add a Preference Record for a User</a><br>
          <a href="preferencesearchrecords.htm">Search/Edit/Delete Preference 
          Records</a></p>
      </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2">Security</td>
    </tr>
    <tr bgcolor="#EEEEFF"> 
      <td> 
        <p><a href="securityaddarecord.jsp">Add a Login Record</a><br>
          <a href="securitysearchrecords.htm">Search/Edit/Delete Security Records</a></p>
      </td>
    </tr>
      
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"> 
        <p>Schedule</p>
      </td>
    </tr>
    <tr> 
      <td> 
	      <a href="#" ONCLICK ="popupPage(550,800,'../schedule/scheduletemplatesetting.jsp');return false;" title="Holiday and Schedule Setting" > Schedule Setting</a> 
         
        </td>
    </tr>
      
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"> 
        <p>Billing</p>
      </td>
    </tr>
    <tr> 
      <td> 
        <p>
                 <a href=# onClick ="popupPage(700,1000,'../billing/manageBillingform.jsp');return false;">Manage Billing Form</a><br>
             <a href=# onClick ="popupPage(800,700,'../billing/billingOHIPsimulation.jsp?html=');return false;">Simulation OHIP Diskette</a><br>
        	
	  <a href=# onClick ="popupPage(800,600,'../billing/billingOHIPreport.jsp');return false;">Generate OHIP Diskette</a><br>
           <a href=# onClick ="popupPage(800,640,'../billing/billingCorrection.jsp?billing_no=');return false;">Billing Correction</a><br>
         <a href=# onClick ="popupPage(800,640,'../billing/inr/reportINR.jsp?provider_no=all');return false;">INR Batch Billing</a><br>
         	         <a href=# onClick ="popupPage(600,800,'../billing/billingRA.jsp');return false;">Billing Reconcilliation</a><br>
         	         <a href=# onClick ="popupPage(600,1000,'../billing/billingEA.jsp');return false;">EDT Billing Report Generator</a><br>
         	         
        </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"> Demographic </td>
    </tr>
    <tr>
      <td> <a href="demographicaddarecord.htm">Add a Demographic Record</a><br>
        <!--a href="demographicsearch.htm">Search/Edit/Delete Demographic Records 
        </a--> </td>
    </tr>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"> 
        <p>Resource</p>
      </td>
    </tr>
    <tr> 
      <td> <a href="#" ONCLICK ="popupPage(200,300,'resourcebaseurl.jsp');return false;" title="Holiday and Schedule Setting" > 
        Base URL Setting</a> </td>
    </tr>
  </table>
  <!--oscarReport Code block -->
    <table cellspacing="0" cellpadding="2" width="90%" border="0">
      <tr>
        <td bgcolor="#CCCCFF" colspan="2"> oscarReport </td>
      </tr>
      <tr>
          <td colspan="2" nowrap>
               <a HREF="#" ONCLICK ="popupPage(600,900,'../oscarReport/dbReportAgeSex.jsp');return false;">
              Age-Sex Report</a><br>
                  <a HREF="#" ONCLICK ="popupPage(600,900,'../oscarReport/oscarReportVisitControl.jsp');return false;">
              Visit Report</a>
          </td>
      </tr>
    </table>
<!--/oscarReport Code block -->
  <!--backup download Code block -->
    <table cellspacing="0" cellpadding="2" width="90%" border="0">
      <tr>
        <td bgcolor="#CCCCFF" colspan="2"> oscarBackup </td>
      </tr>
      <tr>
          <td colspan="2" nowrap>
              <a HREF="#" ONCLICK ="popupPage(500,600,'adminbackupdownload.jsp'); return false;">
              oscarDatabase/Document Download</a>
          </td>
      </tr>
    </table>
<!--/backup download Code block -->
<!--oscarMessenger Code block -->
  <table cellspacing="0" cellpadding="2" width="90%" border="0">
    <tr>
      <td bgcolor="#CCCCFF" colspan="2"> oscarMessenger </td>
    </tr>
    <tr>
        <td colspan="2" nowrap>
             <a HREF="#" ONCLICK ="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curProvider_no%>&userName=<%=userfirstname%>%20<%=userlastname%>');return false;">
             <oscarmessage:newMessage providerNo="<%=curProvider_no%>"/></a>
        </td>
    </tr>
       <% /*ADDED THIS FOR THE NEW OSCAR MESSENGER AUG 27 O2*/%>
    <tr>
       <td>
            <a href="#" onclick="popupOscarRx(600,900,'../oscarMessenger/config/MessengerAdmin.jsp');return false;">Messenger Group Admin</a>
       </td>
    </tr>
  </table>
<!--/oscarMessenger Code block -->

<!--e forms block -->
  <table cellspacing="0" cellpadding="2" width="90%" border="0">
    <tr bgcolor="#CCCCFF"> 
      <td colspan="2"> E-forms</td>
    </tr>
    <tr> 
      <td> <a href="../e_form/UploadHtml.jsp">Upload a form</a><br>
        </td>
    </tr>
    <tr> 
      <td> <a href="../e_form/UploadImages.jsp">Upload an image</a><br>
        </td>
    </tr>
  </table>
<!--// end e forms block -->
  <table cellspacing="0" cellpadding="2" width="90%" border="0">
    <tr>
      <td bgcolor="#CCCCFF"><a href="#" ONCLICK ="popupPage(550,800,'updatedemographicprovider.jsp');return false;" > Update Resident </a></td>
    </tr>
  </table>

  <hr color='orange'>
  <table border="0" cellspacing="0" cellpadding="0" width="90%">
  <tr>
      <td></td>
      <td align="right"><a href="../logout.jsp">Log Out <img src="../images/rightarrow.gif"  border="0" width="25" height="20" align="absmiddle"></a></td>
  </tr>
  </table>
</center>

</body> 
</html>
