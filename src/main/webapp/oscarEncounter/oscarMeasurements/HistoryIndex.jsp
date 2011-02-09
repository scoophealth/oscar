
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
<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.bean.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.data.*"%>
<%@ page import="java.util.Vector"%>
<%
    EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
    MeasurementMapConfig measurementMapConfig = new MeasurementMapConfig();
%>

    <oscar:oscarPropertiesCheck property="MY_OSCAR" value="yes">
            <indivo:indivoRegistered demographic="<%=String.valueOf(bean.getDemographicNo())%>" provider="<%=String.valueOf(request.getSession().getAttribute(\"user\"))%>">
            <bean:define id="myoscar" value="true"/>
            </indivo:indivoRegistered>
    </oscar:oscarPropertiesCheck>
<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/phr/phr.js"></script>
<title><bean:message key="oscarEncounter.Index.oldMeasurements" />
</title>
<html:base />

</head>


<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<style type="text/css" media="print">
    .noprint {
        display: none;
    }
    
</style>
<script type="text/javascript" language=javascript>
    function submitSendPhr() {
        phrActionPopup('about:blank', 'sendPhrPopup');
        document.sendPhrForm.submit();
    }
</script>
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="window.focus();">
<html:errors />
<!--html:form action="/oscarEncounter/oscarMeasurements/DeleteData"-->
<!-- WHAT IS THIS FORM FOR??? THERE IS NO DELETE BUTTON???? -->

<form action="<%=request.getContextPath()%>/oscarEncounter/oscarMeasurements/SendToPhr.do" name="sendPhrForm" target="sendPhrPopup">
<input type="hidden" name="demographicNo" value="<%=String.valueOf(bean.getDemographicNo())%>">
        <table>
		<tr>
			<td>
			<table>
				<tr>
					<th colspan='3'>Old Measurements Index</th>
				</tr>
				<tr>
                    <logic:present name="myoscar">
                    <th align="left" class="Header">PHR</th>
                    </logic:present>
					<th align="left" class="Header" width="20"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingType" />
					</th>
					<th align="left" class="Header" width="200">Type Description</th>
					<th align="left" class="Header" width="50"></th>
				</tr>
				<logic:present name="measurementsData">
					<logic:iterate id="data" name="measurementsData"
                    property="measurementsDataVector" type="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean" indexId="ctr">
						<tr class="data">
                                                    <logic:present name="myoscar">
                                                        <td width="20" class="noprint">
                                                            <%if (measurementMapConfig.isTypeMappedToLoinc(data.getType()) || data.getType().equalsIgnoreCase("bp")) {%>
                                                            <input type="checkbox" name="measurementTypeList" value="<bean:write name="data" property="type"/>">
                                                            <%}%>
                                                        </td>
                                                    </logic:present>
                                                  
                                                        <td width="20"><bean:write name="data" property="type" /></td>
							<td width="200"><bean:write name="data"
								property="typeDescription" /></td>
							<td width="50"><a href="#"
								name='<bean:message key="oscarEncounter.Index.oldMeasurements"/>'
								onClick="popupPage(300,800,'SetupDisplayHistory.do?type=<bean:write name="data" property="type" />'); return false;">more...</a></td>
						</tr>
					</logic:iterate>
				</logic:present>
			</table>
			<table>
				<tr>    
                                        <logic:present name="myoscar">
                                            <td>

                                                <input type="button" onclick="submitSendPhr()" value="Send Selected to PHR">
                                            </td>
                                        </logic:present>
					<td><input type="button" name="Button"
						value="<bean:message key="global.btnPrint"/>"
						onClick="window.print()"></td>
					<td><input type="button" name="Button"
						value="<bean:message key="global.btnClose"/>"
						onClick="window.close()"></td>
					<logic:present name="type">
						<input type="hidden" name="type"
							value="<bean:write name="type" />" />
					</logic:present>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html:html>



