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
 * Date         Implemented By  Company                 Comments
 * 29-09-2004   Ivy Chan        iConcept Technologies   initial version
 *
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 *
 *  
 */
-->
 <%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ page language="java" %>
<%@ page import="java.util.*,oscar.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<html:html locale="true">
<head>
<title>
Waiting List
</title>

</head>

<script language="JavaScript">
function goToPage(){
    window.location = '../oscarWaitingList/SetupDisplayWaitingList.do?waitingListId=' + document.forms[0].selectedWL.options[document.forms[0].selectedWL.selectedIndex].value
}
function popupPage(ctr, patientName, startDate, vheight,vwidth,varpage) { 
  var nbPatients = "<bean:write name="nbPatients"/>";  
  if(nbPatients>1){    
    var selected = document.forms[0].selectedProvider[ctr].options[document.forms[0].selectedProvider[ctr].selectedIndex].value;
  }
  else{
    var selected = document.forms[0].selectedProvider.options[document.forms[0].selectedProvider.selectedIndex].value;
  }
  var page = varpage + '&provider_no=' + selected + '&startDate=' + startDate;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "<bean:message key="provider.appointmentProviderAdminDay.apptProvider"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
    popup.focus();
  }
}
</script>
<body class="BodyStyle" vlink="#0000FF" onload='window.resizeTo(800,400)' >
<!--  -->    
    <html:form action="/oscarWaitingList/WLWaitingList.do">    
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                Waiting List
            </td>
            <td class="MainTableTopRowRightColumn" width="400">
                <table class="TopStatusBar" >                 
                    <tr>                         
                        <td>Please Select a Waiting List:
                            <html:select property="selectedWL">
                                <html:options collection="allWaitingListName" property="id" labelProperty="waitingListName"/>
                            </html:select>                        
                            <INPUT type="button" onClick="goToPage()" value="Generate Report">                            
                        </td>
                    </tr>                  
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">             
            </td>
            <td class="MainTableRightColumn">
               <table border=0 cellspacing=4 width=700>
                <tr>
                    <td>
                        <table>                            
                            <tr>
                                <td>               
                                    <tr>
                                        <td align="left" class="Header" width="50">
                                            Position
                                        </td>                                        
                                        <td align="left" class="Header" width="100">
                                            Patient Name
                                        </td>
                                        <td align="left" class="Header" width="100">
                                            Phone
                                        </td>
                                        <td align="left" class="Header" width="100">
                                            Note
                                        </td>
                                        <td align="left" class="Header" width="100">
                                            On Waiting List Since
                                        </td> 
                                        <td align="left" class="Header" width="250">
                                            Provider
                                        </td> 
                                     </tr>
                                    <logic:iterate id="waitingListBean" name="waitingList" property="waitingListVector" indexId = "ctr">
                                    <tr class="data">
                                        <td width="50"><bean:write name="waitingListBean" property="position" /></td>
                                        <td width="100"><bean:write name="waitingListBean" property="patientName" /></td>
                                        <td width="100"><bean:write name="waitingListBean" property="phoneNumber" /></td>
                                        <td width="100"><bean:write name="waitingListBean" property="note" /></td>
                                        <td width="100"><bean:write name="waitingListBean" property="onListSince" /></td>
                                        <td width="250" nowrap>
                                            <html:select property="selectedProvider">
                                                <html:options collection="allProviders" property="providerID" labelProperty="providerName"/>
                                            </html:select>
                                            <a href=# onClick ="popupPage(<%=ctr%>,'<bean:write name="waitingListBean" property="patientName" />','<bean:write name="today"/>',400,780,'../schedule/scheduleflipview.jsp?originalpage=../oscarWaitingList/DisplayWaitingList.jsp');return false;">Make Appointment</a>
                                        </td>
                                    </tr>                        
                                    </logic:iterate>                                    
                                </td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td><input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onClick="window.close()"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
    </html:form>
</body>
</html:html>