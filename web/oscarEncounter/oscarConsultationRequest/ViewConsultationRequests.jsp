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

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="oscar.oscarEncounter.pageUtil.*"%>

<%
    if(session.getAttribute("user") == null) response.sendRedirect("../../logout.jsp");
    //EctSessionBean bean = (EctSessionBean) request.getSession().getAttribute("EctSessionBean");
%>

<html:html locale="true">

<%

  String team = (String) request.getAttribute("teamVar");
  if (team == null){
    team = new String();
  }

  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil consultUtil;
  consultUtil = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil();
  consultUtil.estTeams();
  /*if (!team.equals("-1")){
    if( bean.isCurrentTeam() ){
      team = bean.getCurrentTeam();
      //System.out.println("team was set");
    }else{
      team = (String) bean.getTeam();
      //System.out.println("team wasn't set");
    }
  }*/

String protocol = "http";
if (request.isSecure()){
   protocol = "https";
}

String serverURL = protocol+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
%>


<head>
<title>
<bean:message key="ectViewConsultationRequests.title"/>
</title>
<html:base/>
<!--META HTTP-EQUIV="Refresh" CONTENT="20;"-->

<style type="text/css">
td.stat1 {

background-color: #eeeeFF;
color : black;


}

th,td.stat2 {

background-color: #ccccFF;
color : black;


}

td.stat3 {

background-color: #B8B8FF;
color : black;


}

td.stat4 {

background-color: #eeeeff;
color : black;

}

th.VCRheads {
background-color: #ddddff;
color : black;
}

</style>



</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}
///
function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgConsReq"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
//setTimeout("window.location.reload();",5000);
}

///
function popupOscarConsultationConfig(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgConsConfig"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}


//
</script>



<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                Consultation
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td class="Header" NOWRAP >
                            <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msfConsReqForTeam"/> = 
                            <%
                               if (team.equals("-1")){
                            %>
                            <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgViewAll"/>
                            <% } else { %>
                            <%= team %>
                            <% } %>               
                        </td>
                        <td  >
                        </td>                        
                    </tr>
                </table>
            </td>
        </tr>
        <tr style="vertical-align:top">
            <td class="MainTableLeftColumn">
                <table>
                    <tr>
                        <td NOWRAP>
                        <a href="javascript:popupOscarConsultationConfig(700,960,'<%=serverURL%>/oscarEncounter/oscarConsultationRequest/config/ShowAllServices.jsp')" class="consultButtonsActive">
                            <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgEditSpecialists"/></a>
                        </td>
                    </tr>
                </table>
            </td>
            <td class="MainTableRightColumn">
                <table width="100%">                                
                <tr>
                    <td>                        
                        <html:form action="/oscarEncounter/ViewConsultation" >
                            <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.formSelectTeam"/>:
                            <select name="sendTo">
				<option value="-1"><bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.formViewAll"/></option>
                                <%
                                   for (int i =0; i < consultUtil.teamVec.size();i++){
                                     String te = (String) consultUtil.teamVec.elementAt(i);
                                     if (te.equals(team)){
                                %>
                                    <option value="<%=te%>" selected><%=te%></option>
                                <%}else{%>
                                    <option value="<%=te%>"><%=te%></option>
                                <%}}%>
                            </select>                            
                            <input type="submit" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.btnConsReq"/>"/>
                            <html:hidden property="currentTeam"/>
                        </html:form>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table border="0" width="80%" cellspacing="1">
                            <tr>
                                <th align="left" class="VCRheads" width="10%">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgStatus"/>
                                </th>
                                <th align="left" class="VCRheads" width="75">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgPatient"/>
                                </th>
                                <th align="left" class="VCRheads">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgProvider"/>
                                </th>
                                <th align="left" class="VCRheads">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgService"/>
                                </th>
                                <th align="left" class="VCRheads">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgRefDate"/>
                                </th>
                            </tr>
                        <%  
                            oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil theRequests;
                            theRequests = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil();
                            theRequests.estConsultationVecByTeam(team);

                            for (int i = 0; i < theRequests.ids.size(); i++){
                            String id      = (String) theRequests.ids.elementAt(i);
                            String status  = (String) theRequests.status.elementAt(i);
                            String patient = (String) theRequests.patient.elementAt(i);
                            String provide = (String) theRequests.provider.elementAt(i);
                            String service = (String) theRequests.service.elementAt(i);
                            String date    = (String) theRequests.date.elementAt(i);
                        %>
                            <tr>
                                <td class="stat<%=status%>">
                                    <% if (status.equals("1")){ %>
                                    <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgND"/>      
                                    <% }else if(status.equals("2")) { %>
                                    <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgSR"/>      
                                    <% }else if(status.equals("3")) { %>
                                    <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgPR"/>      
                                    <% }else if(status.equals("4")) { %>
                                    <bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgDONE"/>    
                                    <% } %>
                                </td>
                                <td class="stat<%=status%>">
                                    <a href="javascript:popupOscarRx(700,960,'<%=serverURL%>/oscarEncounter/ViewRequest.do?requestId=<%=id%>')">
                                    <%=patient%>
                                    </a>
                                </td>
                                <td class="stat<%=status%>">
                                    <%=provide%>
                                </td>
                                <td class="stat<%=status%>">
                                    <a href="javascript:popupOscarRx(700,960,'<%=serverURL%>/oscarEncounter/ViewRequest.do?requestId=<%=id%>')">
                                    <%=service%>
                                    </a>

                                </td>
                                <td class="stat<%=status%>">
                                    <%=date%>
                                </td>
                            </tr>
                        <%}%>
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
</body>

</html:html>

