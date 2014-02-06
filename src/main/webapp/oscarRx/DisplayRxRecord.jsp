<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@page import="org.oscarehr.common.dao.DrugDao,org.oscarehr.common.model.Drug,org.oscarehr.util.MiscUtils,org.oscarehr.util.SpringUtils,org.oscarehr.PMmodule.dao.ProviderDao,org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String id = request.getParameter("id");

DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

Integer drugId = Integer.parseInt(id);

Drug drug = drugDao.find(drugId);


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.oscarehr.util.MiscUtils"%><html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <html:base />
        <title><bean:message key="oscarRx.DisplayRxRecord.title" /></title>
        <link rel="stylesheet" type="text/css" href="../../../share/css/OscarStandardLayout.css">
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
    </head>
    
        
    <body>
            <table width="100%" height="100%" border="0" cellspacing="0"
                   cellpadding="0">
                <tr>
                    <td valign="top">
                        
                        <table width="100%" border="0" cellspacing="0" cellpadding="3"
                               bgcolor="black" >
                            <tr>
                                <td width="66%" align="left" class="Cell">
                                    <div style="color:white;margin-left:5px;" class="Field2"><bean:message  key="oscarMDS.segmentDisplay.formDetailResults" /></div>
                                </td>
                                <td align="right">
                                    <input type="button" value="<bean:message key="global.btnClose"/>"  onClick="window.close()" />
                                    <input type="button" value="<bean:message key="global.btnPrint"/>"  onClick="window.print()" />         
                                </td>
                            </tr>
                        </table>    
                       				Provider: <%= providerDao.getProviderName(drug.getProviderNo()) %><br>
									Demographic: <%= demographicDao.getDemographic(""+drug.getDemographicId()).getDisplayName() %><br>
									
									
									Drug Name: <%= drug.getDrugName() %>     <br> 
									<% if(drug.getBrandName() != null && !drug.getBrandName().equalsIgnoreCase("null") ){ %>
									Brand Name: <%= drug.getBrandName()%><br>
									<%}%>
									<% if(drug.getCustomName() != null && !drug.getCustomName().equalsIgnoreCase("null") ){ %>
									Drug Description: <%= drug.getCustomName()%><br>
									<%}%>
									<br>   
									Rx Date: <%= drug.getRxDate() %><br>
									Rx End Date: <%= drug.getEndDate() %><br>
									Written Date: <%= drug.getWrittenDate()%><br>
									Create Date: <%= drug.getCreateDate()%><br>
									<br>
									ATC: <%= StringUtils.trimToEmpty(drug.getAtc())%><br>
									DIN: <%= StringUtils.trimToEmpty(drug.getRegionalIdentifier())%><br>
									<br>
									
									Rx Text: <%= StringUtils.trimToEmpty(drug.getSpecial())%><br>
									<br>
									
									Dosage: <%= StringUtils.trimToEmpty(drug.getDosageDisplay())%><br>
									
									
									Frequency: <%= StringUtils.trimToEmpty(drug.getFreqCode())%><br>
									Duration: <%= StringUtils.trimToEmpty(drug.getDuration())%> &nbsp;<%= StringUtils.trimToEmpty(drug.getDurUnit())%><br>
									Quantity: <%= StringUtils.trimToEmpty(drug.getQuantity())%><br>
									Repeats: <%= (drug.getRepeat() == null)?0:drug.getRepeat()%><br>
									
									
									
									Refill Duration: <%= (drug.getRefillDuration() == null)?"":drug.getRefillDuration()%><br>
									Refill Quantity: <%= (drug.getRefillQuantity() == null)?"":drug.getRefillQuantity()%><br>
									Dispense Interval: <%= (drug.getDispenseInterval() == null)?"":drug.getDispenseInterval()%><br>
									Pickup Date: <%= (drug.getPickUpDateTime() == null)?"":drug.getPickUpDateTime() %><br>
									
									Unit: <%= StringUtils.trimToEmpty(drug.getUnit())%><br>
									Method: <%= StringUtils.trimToEmpty(drug.getMethod())%><br>
									Route: <%= StringUtils.trimToEmpty(drug.getRoute())%><br>
									Form: <%= StringUtils.trimToEmpty(drug.getDrugForm())%><br>
									
									Strength: <%= StringUtils.trimToEmpty(drug.getDosage())%> 
									<% if(drug.getUnitName() != null && !drug.getUnitName().equalsIgnoreCase("null") ){ %>
									<%= drug.getUnitName()%>
									<%}%>
									<br>
									Long Term: <%= drug.getLongTerm()%><br>
									Past Med: <%= drug.getPastMed()%><br>
									Patient Compliance: <%= (drug.getPatientCompliance()==null)?"":drug.getPatientCompliance()%><br>
									Last Refill: <%= (drug.getLastRefillDate() == null)?"":drug.getLastRefillDate()%><br>
									eTreatment: <%= StringUtils.trimToEmpty(drug.getETreatmentType())%><br>
									Status: <%= StringUtils.trimToEmpty(drug.getRxStatus())%><br>
									
									<br>
									Outside Provider<br>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Name: <%= StringUtils.trimToEmpty(drug.getOutsideProviderName())%><br>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ohip: <%= StringUtils.trimToEmpty(drug.getOutsideProviderOhip())%><br>
									
									<br>
									Archived Reason: <%= StringUtils.trimToEmpty(drug.getArchivedReason())%><br>
									Archived Date: <%= (drug.getArchivedDate() == null)?"":drug.getArchivedDate()%><br>
									
									Comment: <%= StringUtils.trimToEmpty(drug.getComment())%><br>
									
									<%--
									Unused Items
									
									ID: <%= drug.getId()%><br>
									Audit: <%= drug.getAuditString()%><br>
									Full: <%= drug.getFullOutLine()%><br>
									Remote Facility Name: <%= drug.getRemoteFacilityName()%><br>
									Facility Id: <%= drug.getRemoteFacilityId()%><br>
									Position: <%= drug.getPosition()%><br>
									Start Date Unknown: <%= drug.getStartDateUnknown()%><br>
									Script No: <%= drug.getScriptNo()%><br>
									hide for cpp: <%= drug.getHideFromCpp() %><br>
									GCN: <%= drug.getGcnSeqNo()%><br>
									Special Instr: <%= drug.getSpecialInstruction()%><br>
									Gen Name: <%= drug.getGenericName()%><br>
									Min: <%= drug.getTakeMin()%><br>
									Max: <%= drug.getTakeMax()%><br>
									 --%>
                              
            		</td>
                </tr>
            </table> 
    </body>
</html>
