<!--
/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/
 -->
 <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 <%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
 <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
 <%@ page import="oscar.oscarEncounter.data.*, oscar.oscarProvider.data.*, oscar.util.UtilDateUtilities" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
        response.sendRedirect("error.jsp");
        return;
    }

    String demoNo = bean.demographicNo;
    EctPatientData.Patient pd = new EctPatientData().getPatient(demoNo);
    String famDocName, famDocSurname, famDocColour, inverseUserColour, userColour;
    String user = (String) session.getAttribute("user");
    ProviderColourUpdater colourUpdater = new ProviderColourUpdater(user);
    userColour = colourUpdater.getColour();
    //we calculate inverse of provider colour for text
    int base = 16;
    if( userColour.length() == 0 )
        userColour = "#CCCCFF";   //default blue if no preference set

    int num = Integer.parseInt(userColour.substring(1), base);      //strip leading # sign and convert
    int inv = ~num;                                                 //get inverse
    inverseUserColour = Integer.toHexString(inv).substring(2);    //strip 2 leading digits as html colour codes are 24bits

    if(bean.familyDoctorNo.equals("")) {
        famDocName = "";
        famDocSurname = "";
        famDocColour = "";
    }
    else {
        EctProviderData.Provider prov = new EctProviderData().getProvider(bean.familyDoctorNo);
        famDocName = prov.getFirstName();
        famDocSurname = prov.getSurname();
        colourUpdater = new ProviderColourUpdater(bean.familyDoctorNo);
        famDocColour = colourUpdater.getColour();
        if( famDocColour.length() == 0 )
            famDocColour = "#CCCCFF";
    }

    String patientName = pd.getFirstName()+" "+pd.getSurname();
    String patientAge = pd.getAge();
    String patientSex = pd.getSex();
    String pAge = Integer.toString(UtilDateUtilities.calcAge(bean.yearOfBirth,bean.monthOfBirth,bean.dateOfBirth));

    java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
    %>

    <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<div style="padding-left:2px; text-align:left; font-size: 12px; color:<%=inverseUserColour%>; background-color:<%=userColour%>">
    <span style="border-bottom: medium solid <%=famDocColour%>"><bean:message key="oscarEncounter.Index.msgMRP"/>&nbsp;&nbsp;
    <%=famDocName%> <%=famDocSurname%>  </span>

    <span class="Header" style="color:<%=inverseUserColour%>; background-color:<%=userColour%>">
        <%
            String winName = "Master" + bean.demographicNo;
            String url;
            if (vLocale.getCountry().equals("BR"))
                url = "/demographic/demographiccontrol.jsp?demographic_no=" + bean.demographicNo + "&amp;displaymode=edit&amp;dboperation=search_detail_ptbr";
            else
                url = "/demographic/demographiccontrol.jsp?demographic_no=" + bean.demographicNo + "&amp;displaymode=edit&amp;dboperation=search_detail";
        %>
        <a href="#" onClick="popupPage(700,1000,'<%=winName%>','<c:out value="${ctx}"/><%=url%>'); return false;" title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><%=bean.patientLastName %>, <%=bean.patientFirstName%></a> <%=bean.patientSex%> <%=bean.patientAge%>  <%=bean.phone%>

		<a href="javascript:popupPage(400,850,'ApptHist','<c:out value="${ctx}"/>/demographic/demographiccontrol.jsp?demographic_no=<%=bean.demographicNo%>&amp;last_name=<%=bean.patientLastName%>&amp;first_name=<%=bean.patientFirstName%>&amp;orderby=appointment_date&amp;displaymode=appt_history&amp;dboperation=appt_history&amp;limit1=0&amp;limit2=25')" style="font-size: 11px;text-decoration:none;" title="<bean:message key="oscarEncounter.Header.nextApptMsg"/>"><span style="margin-left:20px;"><bean:message key="oscarEncounter.Header.nextAppt"/>: <oscar:nextAppt demographicNo="<%=bean.demographicNo%>"/></span></a>

        &nbsp;&nbsp;        

        <% if(oscar.OscarProperties.getInstance().hasProperty("ONTARIO_MD_INCOMINGREQUESTOR")){%>
           <a href="javascript:void(0)" onClick="popupPage(600,175,'Calculators','<c:out value="${ctx}"/>/common/omdDiseaseList.jsp?sex=<%=bean.patientSex%>&age=<%=pAge%>'); return false;" ><bean:message key="oscarEncounter.Header.OntMD"/></a>
        <%}%>
   </span>
</div>


