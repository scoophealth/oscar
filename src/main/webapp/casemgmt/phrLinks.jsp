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
<%@page import="oscar.oscarProvider.data.ProviderMyOscarIdData" %>
<%@page import="org.oscarehr.phr.util.MyOscarUtils" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.dao.PHRVerificationDao" %>
<%@page import="org.oscarehr.util.SpringUtils"%>
<% 
String demographicNo = request.getParameter("demographicNo"); 
String provNo = (String) session.getAttribute("user");
if( !ProviderMyOscarIdData.idIsSet(provNo)) {	
	return;
}

DemographicDao demographicDao=(DemographicDao)SpringUtils.getBean("demographicDao");
Demographic demographic=demographicDao.getDemographic(demographicNo);
String winName = "viewPatientPHR" + demographicNo;
String winName2 = "SendMyoscarMsg" + demographicNo;
%>

	var elDiv = jQuery("div#toolbar");
	var elSelect = jQuery("select#MyOscarSelect");
	if(elSelect.length != 0){
		elSelect.empty();
	}else{
		var hap = jQuery('div#toolbar').append("<select id='MyOscarSelect' style='width:100px;' onchange='this.selectedIndex = 0;'>");
		elSelect = jQuery("select#MyOscarSelect");
	}

	elSelect.append('<option>PHR</option>');

<%if (demographic.getMyOscarUserName()==null ||demographic.getMyOscarUserName().equals("")) {		/*register link -myoscar (strikethrough) links to create account*/	%>		
		elSelect.append('<option onclick="popupPage(700,900, \'indivoRegistration\', \'../phr/indivo/RegisterIndivo.jsp?demographicNo=<%=demographicNo%>\');" >Register Patient for MyOSCAR</option>');
<%}else{%>
		elSelect.append('<option onclick="popupPage(600,900,\'<%=winName%>\',\'<%=request.getContextPath()%>/demographic/viewPhrRecord.do?demographic_no=<%=demographicNo%>\');return false;">Open Patient\'s Record</option>');
		elSelect.append('<option onclick="popupPage(700,960,\'<%=winName2%>\',\'<%=request.getContextPath() %>/phr/PhrMessage.do?method=createMessage&providerNo=<%=provNo%>&demographicNo=<%=demographicNo%>\'); return false;">Send Patient a Message</option>');		    
<%}%>