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
EctSessionBean bean = (EctSessionBean) request.getSession().getAttribute("EctSessionBean");

oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil consultUtil;
consultUtil = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil();
consultUtil.estPatient(bean.getDemographicNo());

oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil theRequests;
theRequests = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil();
theRequests.estConsultationVecByDemographic(bean.getDemographicNo());
%>

<html:html locale="true">
<head>
<title>
<bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.title"/>
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

background-color: #bbbbff;
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
function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgConsReq"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

</script>



<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors/>
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
    <tr>
        <td width="100%" style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2" height="0%" colspan="2">
        <p class="HelpAboutLogout"><span class="FakeLink"><a href="Help.htm"><bean:message key="global.help"/></a></span> |
        <span class="FakeLink"><a href="About.htm"><bean:message key="global.about"/></a></span> | <span class="FakeLink">
        <a href="Disclaimer.htm"><bean:message key="global.disclaimer"/></a></span></p>
        </td>
    </tr>
    <tr>
        <td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
        <td width="100%" bgcolor="#000000" style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
        <p class="ScreenTitle"><bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.title"/></p>
        </td>
    </tr>
    <tr>
        <td></td>
        <td width="100%" style="border-left: 2px solid #A9A9A9; " height="100%" valign="top">
            <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">

            <!----Start new rows here-->
                <tr>
                    <td>
 		                <div class="DivContentTitle"><bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgConsReqFor"/>  <%=consultUtil.patientName%> </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="DivContentSectionHead">
                        <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgClickLink"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>

                        <table border="0" width="80%" cellspacing="1">
                            <tr>
                                <th class="VCRheads" width="75">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgStatus"/>
                                </th>
                                <th align="left" class="VCRheads">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgPat"/>
                                </th>
                                <th align="left" class="VCRheads">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgProvider"/>
                                </th>
                                <th align="left" class="VCRheads">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgService"/>
                                </th>
                                <th align="left" class="VCRheads">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgRefDate"/>
                                </th>
                            </tr>
                        <%  
                            for (int i = 0; i < theRequests.ids.size(); i++){
                            String id      = (String) theRequests.ids.elementAt(i);
                            String status  = (String) theRequests.status.elementAt(i);
                            String patient = (String) theRequests.patient.elementAt(i);
                            String provide = (String) theRequests.provider.elementAt(i);
                            String service = (String) theRequests.service.elementAt(i);
                            String date    = (String) theRequests.date.elementAt(i);
                        %>
                            <tr>
                                <td class="stat<%=status%>" width="75">
				<% if (status.equals("1")){ %>
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgNothingDone"/>
				<% }else if(status.equals("2")) { %>
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgSpecialistCall"/>
				<% }else if(status.equals("3")) { %>
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgPatCall"/>
				<% }else if(status.equals("4")) { %>
                                <bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgAppMade"/>
                               <% } %>


                                </td>
                                <td class="stat<%=status%>">
                                <%=patient%>
                                </td>
                                <td class="stat<%=status%>">
                                <%=provide%>
                                </td>
                                <td class="stat<%=status%>">
                                <a href="javascript:popupOscarRx(700,960,'../../oscarEncounter/ViewRequest.do?requestId=<%=id%>')">
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
            <!----End new rows here-->

		        <tr height="100%">
                    <td>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

	<tr>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
  	</tr>
  	<tr>
    	<td width="100%" height="0%" colspan="2">&nbsp;</td>
  	</tr>
  	<tr>
    	<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
  	</tr>
</table>
</body>
</html:html>

