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
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy 
%>
<%@ page language="java" %>
<%@ page import="java.util.*,oscar.util.*, oscar.oscarWaitingList.bean.*" %>
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

function popupPage(ctr, patientName, demographicNo, startDate, vheight,vwidth,varpage) { 
  var nbPatients = "<bean:write name="nbPatients"/>";  
  if(nbPatients>1){    
    var selected = document.forms[0].selectedProvider[ctr].options[document.forms[0].selectedProvider[ctr].selectedIndex].value;
  }
  else{
    var selected = document.forms[0].selectedProvider.options[document.forms[0].selectedProvider.selectedIndex].value;
  }
  var page = varpage + '&provider_no=' + selected + '&startDate=' + startDate + '&demographic_no=' + demographicNo + '&demographic_name=' + patientName;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "<bean:message key="provider.appointmentProviderAdminDay.apptProvider"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
    popup.focus();
  }
}

function removePatient(demographicNo, waitingList){
    var agree=confirm("Are you sure you want to remove this patient from the waiting list?");
    if (agree){
        windowprops = "height=50,width=50,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
        var page = 'RemoveFromWaitingList.jsp?listId='+waitingList+'&demographicNo='+demographicNo; 
        var popup = window.open(page, "removeWaitingList", windowprops); 
    }
    else{
        return false ;
    }
        
    
}
</script>
<body class="BodyStyle" vlink="#0000FF" onload='window.resizeTo(900,400)' >
<!--  -->    
    <html:form action="/oscarWaitingList/WLWaitingList.do">    
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="20%">
                Waiting List
            </td>
            <td class="MainTableTopRowRightColumn" width="80%">
                <table class="TopStatusBar" >                 
                    <tr>    
                        <td width="250">Current List: <logic:present name="waitingListName"><bean:write name="waitingListName"/></logic:present></td>
                        <td align="left">Please Select a Waiting List:                            
                            <html:select property="selectedWL">
                                <option value=""> </option>
                                <%
                                    WLWaitingListNameBeanHandler wlNameHd = new WLWaitingListNameBeanHandler();
                                    Vector allWaitingListName = wlNameHd.getWaitingListNameVector();
                                    for(int i=0; i<allWaitingListName.size(); i++){
                                        WLWaitingListNameBean wLBean = (WLWaitingListNameBean) allWaitingListName.elementAt(i);
                                        String id = wLBean.getId();
                                        String name = wLBean.getWaitingListName();                                       
                                        String selected = id.compareTo((String) request.getAttribute("WLId")==null?"0":(String) request.getAttribute("WLId"))==0?"SELECTED":"";                                        
                                %>
                                 <option value="<%=id%>" <%=selected%>><%=name%></option>
                                <%}%>
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
               <table border=0 cellspacing=4 width="100%">
                <tr>
                    <td>
                        <table>                            
                            <tr>
                                <td>               
                                    <tr>
                                        <td align="left" class="Header" width="20">                                            
                                        </td>                                        
                                        <td align="left" class="Header" width="100">
                                            Patient Name
                                        </td>
                                        <td align="left" class="Header" width="100">
                                            Phone
                                        </td>
                                        <td align="left" class="Header" width="150">
                                            Note
                                        </td>
                                        <td align="left" class="Header" width="130">
                                            On Waiting List Since
                                        </td> 
                                        <td align="left" class="Header" width="300">
                                            Provider
                                        </td> 
                                        <td align="left" class="Header" width="50">                                        
                                        </td>
                                     </tr>
                                    <logic:iterate id="waitingListBean" name="waitingList" property="waitingListVector" indexId = "ctr">
                                    <tr class="data">
                                        <td ><bean:write name="waitingListBean" property="position" /></td>
                                        <td><bean:write name="waitingListBean" property="patientName" /></td>
                                        <td><bean:write name="waitingListBean" property="phoneNumber" /></td>
                                        <td><bean:write name="waitingListBean" property="note" /></td>
                                        <td><bean:write name="waitingListBean" property="onListSince" /></td>
                                        <td>
                                            <html:select property="selectedProvider">                                                
                                                <html:options collection="allProviders" property="providerID" labelProperty="providerName"/>
                                            </html:select> 
                                            <a href=# onClick ="popupPage(<%=ctr%>,'<bean:write name="waitingListBean" property="patientName" />','<bean:write name="waitingListBean" property="demographicNo"/>','<bean:write name="today"/>',400,780,'../schedule/scheduleflipview.jsp?originalpage=../oscarWaitingList/DisplayWaitingList.jsp');return false;">Make Appointment</a>
                                        </td>
                                        <td>
                                            <a href=# onClick ="removePatient('<bean:write name="waitingListBean" property="demographicNo"/>', '<bean:write name="WLId"/>');">Remove</a>
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