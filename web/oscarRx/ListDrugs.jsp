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

--%><%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="oscar.oscarRx.data.*,oscar.oscarProvider.data.ProviderMyOscarIdData,oscar.oscarDemographic.data.DemographicData,oscar.OscarProperties,oscar.log.*"%>
<%@ page import="org.oscarehr.common.model.OscarAnnotation" %>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.casemgmt.web.PrescriptDrug"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.ArrayList,oscar.util.*,java.util.*,org.oscarehr.common.model.*,org.oscarehr.common.dao.*"%>
<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />
<logic:notPresent name="RxSessionBean" scope="session">
    <logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
                 name="RxSessionBean" scope="session" />
    <logic:equal name="bean" property="valid" value="false">
        <logic:redirect href="error.html" />
    </logic:equal>
</logic:present>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) pageContext.findAttribute("bean");
        boolean showall = false;
        if (request.getParameter("show") != null) {
            if (request.getParameter("show").equals("all")) {
                showall = true;
            }
        }

        boolean integratorEnabled = LoggedInInfo.loggedInInfo.get().currentFacility.isIntegratorEnabled();
        String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_PRESCRIP;
%>

<div class="drugProfileText" style="width: 100%;">
    <table width="100%" cellpadding="3">
        <tr>
            <th align="left"><b>Rx Date</b></th>
            <th align="left"><b>Days to Exp</b></th>
            <th align="left"><b>Long Term Med</b></th>
            <th align="left"><b><bean:message key="SearchDrug.msgPrescription"/></b></th>
            <th align="center" width="75px"><b><bean:message key="SearchDrug.msgReprescribe"/></b></th>
            <th align="center" width="75px"><b><bean:message key="SearchDrug.msgDelete"/></b></th>
            <th align="center" width="75px"><b>Discontinue</b></th>
            <th align="center" width="20px">&nbsp;</th>
            <th align="center"><bean:message key="SearchDrug.msgLocationPrescribed"/></th>
        </tr>

        <%
            CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
            List<Drug> prescriptDrugs = caseManagementManager.getPrescriptions(patient.getDemographicNo(), showall);
            //DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
            //List<Drug> prescriptDrugs = drugDao.getPrescriptions(""+patient.getDemographicNo(), showall);



            long now = System.currentTimeMillis();
            long month = 1000L * 60L * 60L * 24L * 30L;
            
            for (Drug prescriptDrug : prescriptDrugs) {
                String styleColor = "";

                if (request.getParameter("status") != null) { //TODO: Redo this in a better way
                    String stat = request.getParameter("status");
                    if (stat != null && stat.equals("active") && prescriptDrug.isExpired()) {
                        continue;
                    } else if (stat != null && stat.equals("inactive") && !prescriptDrug.isExpired()) {
                        continue;
                    }
                }
                if (request.getParameter("longTermOnly") != null && request.getParameter("longTermOnly").equals("true")){
                    if (!prescriptDrug.isLongTerm()){
                      continue;
                    }
                }

                if (request.getParameter("longTermOnly") != null && request.getParameter("longTermOnly").equals("acute")){
                    if (prescriptDrug.isLongTerm()){
                      continue;
                    }
                }
//add all long term med drugIds to an array.
                styleColor = getClassColour( prescriptDrug, now, month);
        %>
        <tr>
            <td valign="top"><a id="rxDate_<%=prescriptDrug.getId()%>"   <%=styleColor%> href="StaticScript.jsp?regionalIdentifier=<%=prescriptDrug.getRegionalIdentifier()%>&amp;cn=<%=response.encodeURL(prescriptDrug.getCustomName())%>"><%=oscar.util.UtilDateUtilities.DateToString(prescriptDrug.getRxDate())%></a></td>
            <td><%=prescriptDrug.daysToExpire()%></td>
            <td><%=prescriptDrug.isLongTerm()%> </td>
            
            <td><a id="prescrip_<%=prescriptDrug.getId()%>" <%=styleColor%> href="StaticScript.jsp?regionalIdentifier=<%=prescriptDrug.getRegionalIdentifier()%>&amp;cn=<%=response.encodeURL(prescriptDrug.getCustomName())%>"> <%=RxPrescriptionData.getFullOutLine(prescriptDrug.getSpecial()).replaceAll(";", " ")%></a></td>
            <td width="75px" align="center">
                <%if (prescriptDrug.getRemoteFacilityName() == null) {%>
                <a name="rePrescribe" id="reRx_<%=prescriptDrug.getId()%>" <%=styleColor%> href="javascript:void(0)" onclick="represcribe(this)"> <%="Represcribe"%></a>
                <%} else {%>
                <form action="<%=request.getContextPath()%>/oscarRx/searchDrug.do" method="post">
                    <input type="hidden" name="demographicNo" value="<%=patient.getDemographicNo()%>" />
                    <input type="hidden" name="searchString" value="<%=getName(prescriptDrug)%>" />
                    <input type="submit" class="ControlPushButton" value="Search to Re-prescribe" />
                </form>
                <%}%>
            </td>
            <td width="75px" align="center">
                <%if (prescriptDrug.getRemoteFacilityName() == null) {%>
                   <a id="del_<%=prescriptDrug.getId()%>" name="delete" <%=styleColor%> href="javascript:void(0);" onclick="Delete2(this);"><%="Delete"%></a>
                <%}%>
            </td>
            <td width="75px" align="center">
                <a id="discont_<%=prescriptDrug.getId()%>" href="javascript:void(0);" onclick="Discontinue(event,this);" <%=styleColor%> >Discontinue</a>
            </td>

            <td width="20px" align="center">
                <a href="#" title="Annotation" onclick="window.open('../annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=prescriptDrug.getId()%>&amp;demo=<%=bean.getDemographicNo()%>','anwin','width=400,height=250');"> <img src="../images/notes.gif" border="0"></a>
            </td>
            
            <td align="center">
                <%
                if (prescriptDrug.getRemoteFacilityName() != null){ %>
                    <%=prescriptDrug.getRemoteFacilityName()%>
                <%}else if(  prescriptDrug.getOutsideProviderName() !=null && !prescriptDrug.getOutsideProviderName().equals("")  ){%>
                    <%=prescriptDrug.getOutsideProviderName()%>
                <%}else{%>
                    local
                <%}%>


            </td>
            
        </tr>
        <%}%>
    </table>

</div>
        <br>
<%!

String getName(Drug prescriptDrug){
    String searchString = prescriptDrug.getBrandName();
    if (searchString == null) {
        searchString = prescriptDrug.getCustomName();
    }
    if (searchString == null) {
        searchString = prescriptDrug.getRegionalIdentifier();
    }
    if (searchString == null) {
        searchString = prescriptDrug.getSpecial();
    }
    return searchString;
}

    String getClassColour(Drug prescriptDrug, long referenceTime, long durationToSoon){
        StringBuffer sb = new StringBuffer("class=\"");

        if (!prescriptDrug.isExpired() && (prescriptDrug.getEndDate().getTime() - referenceTime <= durationToSoon)) {  // ref = now and duration will be a month
            sb.append("expireInReference ");
        }
        
        if (!prescriptDrug.isExpired() && !prescriptDrug.isArchived()) {
            sb.append("currentDrug ");
        }

        if (prescriptDrug.isArchived()) {
            sb.append("archivedDrug ");
        }

        if(prescriptDrug.isExpired()) {
            sb.append("expiredDrug ");
        }

        if(prescriptDrug.isLongTerm()){
            sb.append("longTermMed ");
        }

        //if(prescriptDrug.isDiscontinued()){
       //     sb.append("discontinued ");
        //}

        String retval = sb.toString();

        if(retval.equals("class=\"")){
            return "";
        }



        

        return retval.substring(0,retval.length())+"\"";

    }

%>